package com.zhuhuibao.mybatis.vip.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.VipConstant;
import com.zhuhuibao.common.constant.VipConstant.VipLevel;
import com.zhuhuibao.common.constant.VipConstant.VipPrivilegeType;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.entity.VipMemberPrivilege;
import com.zhuhuibao.mybatis.vip.entity.VipPrivilege;
import com.zhuhuibao.mybatis.vip.entity.VipRecord;
import com.zhuhuibao.mybatis.vip.mapper.VipInfoMapper;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.mapper.ZhbMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * VIP特权服务
 * 
 * @author tongxinglong
 *
 */
@Service
@Transactional
public class VipInfoService {
	private Logger log = LoggerFactory.getLogger(VipInfoService.class);
	@Autowired
	private VipInfoMapper vipInfoMapper;
	@Autowired
	private ZhbMapper zhbMapper;
	@Autowired
	private MemberService memberSV;
	@Autowired
	private ZhbService zhbSV;
	
	/**
	 * 开通尊贵会员
	 * @param contract_id
	 * @param member_account
	 * @param vip_level
	 * @param active_time
	 * @param validity
	 * @return
	 * @throws Exception
	 */
	public int addVipService(String contract_id, String member_account, int vip_level, 
			String active_time, int validity) throws Exception{
		int result = 0;
		try{
			//校验该合同是否已经存在
			if(isNotExistsVipRecord(contract_id)){
				//VIP级别对应的商品ID
				int goodsId = VipConstant.VIP_LEVEL_GOODSID.get(String.valueOf(vip_level));
				DictionaryZhbgoods zhbGoods = zhbSV.getZhbGoodsById(String.valueOf(goodsId));
				if(null == zhbGoods){
					throw new BusinessException(MsgCodeConstant.NOT_EXIST_GOODS_ERROR, "不存在该商品信息");
				}
				//VIP级别对应赠送筑慧币数量
				BigDecimal amount = new BigDecimal(VipConstant.VIP_LEVEL_ZHB.get(String.valueOf(vip_level)));
				//获取memberID
				Long member_id = getMemberId(member_account);
				//获取管理员账号
				Long createid = ShiroUtil.getOmsCreateID();
				if(createid == null) {
					throw new BusinessException(MsgCodeConstant.member_mcode_account_status_exception, "获取当前登录管理员失败");
				}
				// 筑慧币充值条件: 订单中amount大于0
				if (amount.compareTo(BigDecimal.ZERO) > 0 ) {
					// 进行筑慧币充值
					int prepaidResult = zhbSV.execPrepaid("0", member_id, createid, amount, zhbGoods.getPinyin(), zhbGoods.getId());
					if (0 == prepaidResult) {
						throw new BusinessException(MsgCodeConstant.ZHB_AUTOPAYFOR_FAILED, "充值失败");
					}
				}
				// VIP升级
				result = addVipRecord(contract_id, vip_level, active_time,
						validity, result, amount, member_id, createid);
			}else{
				throw new BusinessException(MsgCodeConstant.EXIST_CONTRACTNO_WARN, "该合同编号已经操作过");
			}
				
		}catch(Exception e){
			log.error("ZhbService::addVipService::contract_id=" + contract_id + ",member_account=" + member_account + ",vip_level="
					+vip_level+",active_time="+active_time+",validity="+validity, e);
			throw e;
		}
		
		return result;
	}
	/**
	 * VIP升级
	 * @param contract_id
	 * @param vip_level
	 * @param active_time
	 * @param validity
	 * @param result
	 * @param amount
	 * @param member_id
	 * @param createid
	 * @return
	 * @throws ParseException
	 */
	private int addVipRecord(String contract_id, int vip_level,
			String active_time, int validity, int result, BigDecimal amount,
			Long member_id, Long createid) throws ParseException {
		
		VipMemberInfo vipMemberInfo = findVipMemberInfoById(member_id);
		if (null == vipMemberInfo) {
			result = 0;
			throw new BusinessException(MsgCodeConstant.NOT_EXIST_VIP, "当前账号不是vip会员");
			
		} else if (vipMemberInfo.getVipLevel() <= vip_level) {
			/**
			 * 判断该用户是否是收费会员
			 * 若原本不是收费会员，即免费会员——》黄金会员
			 * 	1.生效时间：填的日期；
			 * 	2.过期时间：填的日期+1年；
			 * 若原本是收费会员，即黄金会员——》铂金会员
			 * 	1.生效时间：当天；
			 * 	2.过期时间：原来的失效日期+1年；
			 */
			Calendar cal = Calendar.getInstance();
			//默认有效时间是老会员套餐的有效时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//生效时间：填的日期；
			Date active_date = sdf.parse(active_time);
			cal.setTime(active_date);
			
			if (ArrayUtils.contains(VipConstant.CHARGE_VIP_LEVEL, String.valueOf(vipMemberInfo.getVipLevel()))) {
				//原来是收费会员的情况： {"30","60","130","160"}
				//过期时间：原来的失效日期+1年；
				Date oldExpireTime = vipMemberInfo.getExpireTime();
				cal.setTime(oldExpireTime);
				//生效时间：当天；
				Calendar cal2 = Calendar.getInstance();
				active_date = cal2.getTime();
				active_time = sdf.format(active_date);
			}
			//计算过期时间：+1年；
			cal.add(Calendar.YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.add(Calendar.DATE, 1);
			
			vipMemberInfo.setActiveTime(active_date);
			vipMemberInfo.setExpireTime(cal.getTime());
			//处理会员等级
			vipMemberInfo.setVipLevel(vip_level);
			
			result = updateVipMemberInfo(vipMemberInfo);
			//增加vip操作记录
			insertVipRecord(contract_id, member_id, createid, amount, vip_level, active_time, validity, cal.getTime());
			
		} else{
			result = 0;
			throw new BusinessException(MsgCodeConstant.DEGRADE_VIP_WARN, "暂不支持会员降级处理");
		}
		return result;
	}
	
	private Long getMemberId(String member_account) {
		Member member = new Member();
		if (member_account.contains("@")) {
		    member.setEmail(member_account);
		} else {
		    member.setMobile(member_account);
		}
		Member mem = memberSV.findMember(member);
		if(mem == null || StringUtils.isEmpty(mem.getId()) ){
			throw new BusinessException(MsgCodeConstant.member_mcode_username_not_exist, "该盟友账号不存在");
		}
		Long member_id = Long.valueOf(mem.getId());
		return member_id;
	}
	
	/**
	 * 添加一条vip操作记录
	 * @param contractId
	 * @param buyerId
	 * @param operaterId
	 * @param amount
	 * @param vipLevel
	 * @param activeTime
	 * @param validity
	 * @param oldActiveTime
	 * @param expireTime
	 * @throws ParseException
	 */
	private void insertVipRecord(String contractId, Long buyerId, Long operaterId, BigDecimal amount, 
			int vipLevel, String activeTime, int validity, Date expireTime) throws ParseException {
		VipRecord vipRecord = new VipRecord();
		vipRecord.setContractNo(contractId);
		vipRecord.setBuyerId(buyerId);
		vipRecord.setOperaterId(operaterId);
		vipRecord.setAmount(amount);
		vipRecord.setStatus("1");
		vipRecord.setVipLevel(vipLevel);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date activeDate = sdf.parse(activeTime);
		
		vipRecord.setActiveTime(activeDate);
		vipRecord.setExpireTime(expireTime);
		
		Calendar cal = Calendar.getInstance();
		vipRecord.setAddTime(cal.getTime());
		vipRecord.setUpdateTime(cal.getTime());
		
		vipInfoMapper.insertVipRecord(vipRecord);
	}
	
	private boolean isNotExistsVipRecord(String contractNo) {
		return null == getVipRecordByContractNo(contractNo);
	}
	
	private VipRecord getVipRecordByContractNo(String contractNo) {

		if (StringUtils.isNotBlank(contractNo) && !"0".equals(contractNo)) {
			return vipInfoMapper.selectVipRecordByContractNo(contractNo);
		}

		return null;
	}

	
	/**
	 * 根据ID获取会员VIP信息
	 * 
	 * @param memberId
	 * @return
	 */
	public VipMemberInfo findVipMemberInfoById(Long memberId) {
		return vipInfoMapper.selectVipMemberInfoById(memberId);
	}

	/**
	 * 根据会员级别获取所有会员特权list
	 * 
	 * @param vipLevel
	 * @return
	 */
	@Cacheable(value = "vipPrivilegeCache", key = "#vipLevel")
	public List<VipPrivilege> listVipPrivilegeByLevel(String vipLevel) {
		return vipInfoMapper.selectVipPrivilegeListByLevel(Integer.valueOf(vipLevel));
	}

	/**
	 * 根据会员级别获取会员特权map
	 * 
	 * @param vipLevel
	 * @return
	 */
	public Map<String, VipPrivilege> findVipPrivilegeMap(String vipLevel) {
		Map<String, VipPrivilege> privilegeMap = new HashMap<String, VipPrivilege>();
		List<VipPrivilege> list = listVipPrivilegeByLevel(vipLevel);
		if (CollectionUtils.isNotEmpty(list)) {
			for (VipPrivilege p : list) {
				privilegeMap.put(p.getPinyin(), p);
			}
		}

		return privilegeMap;
	}

	// /**
	// * 判断当前VIP级别是否具有相应特权
	// *
	// * @param vipLevel
	// * @param privilegePinyin
	// * @return
	// */
	// public boolean vipHadPrivilege(int vipLevel, String privilegePinyin) {
	// boolean hadVipPrivilege = false;
	// Map<String, VipPrivilege> privilegeMap = getVipPrivilegeMap(vipLevel);
	// if (MapUtils.isNotEmpty(privilegeMap)) {
	// VipPrivilege privilege = privilegeMap.get(privilegePinyin);
	// if (null != privilege && (VipPrivilegeType.DISCOUNT ==
	// privilege.getType() || (VipPrivilegeType.EXIST == privilege.getType() &&
	// "1".equals(privilege.getValue())))) {
	// hadVipPrivilege = true;
	// }
	// }
	//
	// return hadVipPrivilege;
	// }

	/**
	 * 获取用户自定义特权信息
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return
	 */
	public VipMemberPrivilege findVipMemberPrivilege(Long memberId, String privilegePinyin) {
		privilegePinyin = StringUtils.isNotBlank(privilegePinyin) ? privilegePinyin.toUpperCase() : "";
		Map<String, Object> param = MapUtil.convert2HashMap("memberId", memberId, "pinyin", privilegePinyin);
		return vipInfoMapper.selectVipMemberPrivilege(param);
	}

	/**
	 * 查询会员剩余额外特权数量
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return
	 */
	public long getExtraPrivilegeNum(Long memberId, String privilegePinyin) {
		if (null != memberId && StringUtils.isNotBlank(privilegePinyin)) {
			VipMemberPrivilege extraPrivilege = findVipMemberPrivilege(memberId, privilegePinyin.toUpperCase());
			if (null != extraPrivilege && VipConstant.VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType())) {
				return extraPrivilege.getValue();
			}
		}

		return 0;
	}

	/**
	 * 存在自定义特权
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return
	 */
	public boolean hadExtraPrivilege(Long memberId, String privilegePinyin) {
		boolean hadExtraPrivilege = false;
		VipMemberPrivilege extraPrivilege = findVipMemberPrivilege(memberId, privilegePinyin);
		if (null != extraPrivilege && VipConstant.VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType()) && extraPrivilege.getValue() > 0) {
			hadExtraPrivilege = true;
		} else if (null != extraPrivilege && !VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType())) {
			hadExtraPrivilege = true;
		}

		return hadExtraPrivilege;
	}

	/**
	 * 使用自定义特权（数量类型）
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return 0:失败，1:成功
	 */
	public int useExtraPrivilege(Long memberId, String privilegePinyin) {
		int result = 0;
		Map<String, Object> param = MapUtil.convert2HashMap("memberId", memberId, "pinyin", privilegePinyin);
		VipMemberPrivilege extraPrivilege = vipInfoMapper.selectVipMemberPrivilege(param);
		if (null != extraPrivilege && VipConstant.VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType()) && extraPrivilege.getValue() > 0) {
			extraPrivilege.setValue(extraPrivilege.getValue() - 1);
			extraPrivilege.setOldUpdateTime(extraPrivilege.getUpdateTime());
			result = vipInfoMapper.updateVipMemberPrivilegeValue(extraPrivilege);
		}

		return result;
	}

	/**
	 * 注册时初始化特权特权内容
	 * 
	 * @param memberId
	 * @param identify
	 */
	public void initDefaultExtraPrivilege(Long memberId, String identify) {
		int defaultPrivilegeLevel = StringUtils.contains(identify, "2") ? VipConstant.EXTRA_PRIVILEGE_LEVEL_PERSONAL
				: VipConstant.EXTRA_PRIVILEGE_LEVEL_ENTERPRISE;
		int freeLevel = StringUtils.contains(identify, "2") ? VipConstant.VipLevel.PERSON_FREE.value : VipLevel.ENTERPRISE_FREE.value;
		VipMemberInfo vipMemberInfo = vipInfoMapper.selectVipMemberInfoById(memberId);
		if (null == vipMemberInfo) {
			insertVipMemberInfo(memberId, freeLevel, 50, null);
		}

		List<VipMemberPrivilege> memberPrivilegeList = vipInfoMapper.selectVipMemberPrivilegeList(memberId);
		if (CollectionUtils.isEmpty(memberPrivilegeList)) {
			insertExtraPrivilege(memberId, String.valueOf(defaultPrivilegeLevel));
		}
	}

	/**
	 * 自定义特权列表
	 * 
	 * @param memberId
	 * @return
	 */
	public List<VipMemberPrivilege> listVipMemberPrivilege(Long memberId) {
		return vipInfoMapper.selectVipMemberPrivilegeList(memberId);
	}

	/**
	 * 添加会员VIP信息
	 * 
	 * @param memberId
	 * @param vipLevel
	 * @param activeYears
	 *            生效年份
	 * @throws ParseException 
	 */
	public int insertVipMemberInfo(Long memberId, int vipLevel, int activeYears, String activeTime) {
		VipMemberInfo vipMemberInfo = new VipMemberInfo();
		vipMemberInfo.setMemberId(memberId);
		vipMemberInfo.setVipLevel(vipLevel);
		
		Calendar cal = Calendar.getInstance();
		//处理生效时间
		if(null == activeTime || StringUtils.isEmpty(activeTime)){
			//前台没有传生效时间的情况，使用当前时间
			vipMemberInfo.setActiveTime(cal.getTime());
			//处理失效时间
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.add(Calendar.YEAR, activeYears);
			cal.add(Calendar.DATE, 1);
			vipMemberInfo.setExpireTime(cal.getTime());
		}else{
			//前台传生效时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date activeDate = sdf.parse(activeTime);
				vipMemberInfo.setActiveTime(activeDate);
				
				cal.setTime(activeDate); 
				cal.add(Calendar.YEAR, 1);
				vipMemberInfo.setExpireTime(cal.getTime());
			} catch (ParseException e) {
				throw new BusinessException(MsgCodeConstant.DATE_CONVERT_WARN, "日期转换格式异常");
			}
			
		}
		
		try{
			vipInfoMapper.insertVipMemberInfo(vipMemberInfo);
		}catch(Exception e){
			return 0;
		}
		return 1;
	}

	/**
	 * 修改会员VIP信息
	 * 
	 * @param vipMemberInfo
	 */
	public int updateVipMemberInfo(VipMemberInfo vipMemberInfo) {
		return vipInfoMapper.updateVipMemberInfo(vipMemberInfo);
	}

	/**
	 * 添加对应级别的自定义特权
	 * 
	 * @param memberId
	 * @param vipLevel
	 */
	private void insertExtraPrivilege(Long memberId, String vipLevel) {
		List<VipPrivilege> privilegeList = listVipPrivilegeByLevel(vipLevel);
		if (CollectionUtils.isNotEmpty(privilegeList)) {
			for (VipPrivilege p : privilegeList) {
				if (VipConstant.VipPrivilegeType.NUM.toString().equals(p.getType())) {
					VipMemberPrivilege memberPrivilege = buildVipMemberPrivilege(memberId, p);
					vipInfoMapper.insertVipMemberPrivilege(memberPrivilege);
				}
			}
		}
	}

	/**
	 * 根据vipPrivilege初始化VipMemberPrivilege
	 * 
	 * @param memberId
	 * @param vipPrivilege
	 * @return
	 */
	private VipMemberPrivilege buildVipMemberPrivilege(Long memberId, VipPrivilege vipPrivilege) {
		VipMemberPrivilege memberPrivilege = new VipMemberPrivilege();

		memberPrivilege.setMemberId(memberId);
		memberPrivilege.setType(vipPrivilege.getType());
		memberPrivilege.setPinyin(vipPrivilege.getPinyin());
		memberPrivilege.setName(vipPrivilege.getName());
		memberPrivilege.setValue(vipPrivilege.getValue());
		Calendar cal = Calendar.getInstance();
		memberPrivilege.setAddTime(cal.getTime());
		memberPrivilege.setUpdateTime(cal.getTime());

		return memberPrivilege;
	}

	/**
	 * 运营管理系统-VIP会员
	 * 
	 * @param account
	 * @param name
	 * @param vipLevel
	 * @param status
	 * @param pager
	 * @return
	 */
	public List<Map<String, String>> listAllVipInfo(String account, String name, String vipLevel, String status, Paging<Map<String, String>> pager) {
		List<Map<String, String>> viplist = new ArrayList<Map<String, String>>();
		Map<String, String> param = MapUtil.convert2HashMap("account", account, "name", name, "vipLevel", vipLevel, "status", status);
		viplist = vipInfoMapper.findAllVipInfoList(pager.getRowBounds(), param);
		if (CollectionUtils.isNotEmpty(viplist)) {
			for (Map<String, String> vip : viplist) {
				String level = String.valueOf(vip.get("vip_level"));
				if (StringUtils.isNotBlank(level) && ArrayUtils.contains(VipConstant.CHARGE_VIP_LEVEL, level)) {
					vip.put("status", "1");
				} else {
					vip.put("status", "0");
				}
			}
		}

		return viplist;
	}

	/**
	 * 根据商品查询VIP信息
	 * @param goodsId
	 * @return
     */
	public List<Map<String, String>> findVipInfoByID(Long goodsId) {

		return vipInfoMapper.findVipInfoByID(goodsId);
	}
}
