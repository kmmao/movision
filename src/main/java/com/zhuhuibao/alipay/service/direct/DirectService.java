package com.zhuhuibao.alipay.service.direct;

import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;


/**
 * 即时到账接口 （create_direct_pay_by_user）
 */
@Service
public class DirectService {
    private static final Logger log = LoggerFactory.getLogger(DirectService.class);

    /**
     * 接口服务名称
     */
    public static final String SERVICE_NAME = AlipayPropertiesLoader.getPropertyValue("direct_service");
    public static final String NOTIFY_URL =  AlipayPropertiesLoader.getPropertyValue("direct_notify_url");
//    public static final String RETURN_URL =  AlipayPropertiesLoader.getPropertyValue("direct_return_url");

    @Autowired
    private AlipayService alipayService;

    /**
     * 支付宝支付
     *
     * @param paramMap 请求参数集合
     * @return
     */
    public String alipayRequst(Map<String, String> paramMap) throws ParseException {

        paramMap.put("service" ,SERVICE_NAME);
//        paramMap.put("returnUrl", RETURN_URL); //同步通知
        paramMap.put("notifyUrl", NOTIFY_URL);   //异步通知

        return  alipayService.alipay(paramMap, PayConstants.ALIPAY_METHOD_GET);

    }

    /**
     * 立即支付
     * @param response   HttpServletResponse
     * @param paramMap   请求参数集合
     */
    public void doPay(HttpServletResponse response,Map<String,String> paramMap){
        log.debug("立即支付请求参数:{}", paramMap.toString());
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String  sHtmlText = alipayRequst(paramMap);
            log.debug("支付宝请求页面:{}",sHtmlText);

            out.write(sHtmlText);
            out.flush();
            return;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取输出流异常：" + e.getMessage());
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR,e.getMessage());

        } finally {
            if (out != null)
                out.close();
        }
    }


}
