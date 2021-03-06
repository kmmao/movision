package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.CircleFacade;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.boss.UserManageFacade;
import com.movision.facade.index.FacadePost;
import com.movision.mybatis.activePart.entity.ActivePartList;
import com.movision.mybatis.activityContribute.entity.ActivityContribute;
import com.movision.mybatis.activityContribute.entity.ActivityContributeVo;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.entity.PostLabelDetails;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.share.entity.SharesVo;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserLike;
import com.movision.utils.ImgSortUtil;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/7 9:05
 */
@RestController
@RequestMapping("/boss/post")
public class PostController {
    @Autowired
    PostFacade postFacade;

    @Autowired
    CircleFacade circleFacade;

    @Autowired
    UserManageFacade userManageFacade;

    @Autowired
    private MovisionOssClient movisionOssClient;

    @Autowired
    private FacadePost facadePost;

    @Autowired
    private ImgSortUtil imgSortUtil;

    /**
     * 后台管理-查询帖子列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询帖子列表", notes = "查询帖子列表", response = Response.class)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response queryPostByList(@ApiParam(value = "登录用户") @RequestParam String loginid,
                                    @RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = postFacade.queryPostByList(loginid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(list);
        return response;
    }

    /**
     * 查询圈子分类
     *
     * @return
     */
    @ApiOperation(value = "查询圈子分类", notes = "用于查询圈子分类接口", response = Response.class)
    @RequestMapping(value = "query_circle_type_list", method = RequestMethod.POST)
    public Response queryCircleTypeList(@ApiParam(value = "登录用户") @RequestParam String loginid) {
        List<CircleCategory> map = circleFacade.queryCircleTypeList(loginid);
        Response response = new Response();
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 选择圈子
     *
     * @return
     */
    @ApiOperation(value = "选择圈子", notes = "用于选择圈子接口", response = Response.class)
    @RequestMapping(value = "/query_list_circle_type", method = RequestMethod.POST)
    public Response queryListByCircleType(@ApiParam(value = "圈子类型id") @RequestParam(required = false) String categoryid,
                                          @ApiParam(value = "登录用户") @RequestParam String loginid) {
        Response response = new Response();
        List<Circle> list = circleFacade.queryListByCircleType(categoryid, loginid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(list);
        return response;
    }

/*
    */
/**
 * 后台管理-查询活动列表
 *
 * @param pageNo
 * @param pageSize
 * @return
 *//*

    @ApiOperation(value = "查询活动列表", notes = "查询活动列表", response = Response.class)
    @RequestMapping(value = "/list_active_list", method = RequestMethod.POST)
    public Response queryPostActiveToByList(@ApiParam(value = "登录用户") @RequestParam String loginid,
                                            @RequestParam(required = false, defaultValue = "1") String pageNo,
                                            @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostActiveList> pager = new Paging<PostActiveList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostActiveList> list = postFacade.queryPostActiveToByList(loginid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }
*/

    /**
     * 后台管理-查询活动报名列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询活动报名列表", notes = "查询活动报名列表", response = Response.class)
    @RequestMapping(value = "/list_call_list", method = RequestMethod.POST)
    public Response queyPostCallActive(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @RequestParam(required = false, defaultValue = "10") String pageSize,
                                       @ApiParam(value = "活动id") @RequestParam String postid) {
        Response response = new Response();
        Paging<ActivePartList> pager = new Paging<ActivePartList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ActivePartList> list = postFacade.queyPostCallActive(pager, postid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 查询发帖人信息
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "查询发帖人信息", notes = "查询发帖人信息", response = Response.class)
    @RequestMapping(value = "/query_posted_man", method = RequestMethod.POST)
    public Response queryPostByPosted(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        User user = postFacade.queryPostByPosted(postid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(user);
        return response;
    }

    /**
     * 删除帖子
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "逻辑删除帖子", notes = "逻辑删除帖子", response = Response.class)
    @RequestMapping(value = "/delete_post", method = RequestMethod.POST)
    public Response deletePost(@ApiParam(value = "帖子id") @RequestParam String postid,
                               @ApiParam(value = "登录用户") @RequestParam String loginid) {
        Response response = new Response();
        Map map = postFacade.deletePost(postid, loginid);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看评论
     *
     * @param postid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看帖子评论", notes = "查看帖子评论", response = Response.class)
    @RequestMapping(value = "/query_post_appraise", method = RequestMethod.POST)
    public Response queryPostAppraise(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @ApiParam(value = "排序方式 默认降序  0按时间升序（非必传字段）") @RequestParam(required = false) String type,
                                      @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<CommentVo> list = postFacade.queryPostAppraise(postid, type, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看评论-评论详情列表
     *
     * @param commentid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询帖子评论详情", notes = "用于查询帖子评论详情接口", response = Response.class)
    @RequestMapping(value = "/query_post_comment_particulars", method = RequestMethod.POST)
    public Response queryPostByCommentParticulars(@ApiParam(value = "评论id") @RequestParam String commentid,
                                                  @ApiParam(value = "帖子id") @RequestParam String postid,
                                                  @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<CommentVo> list = postFacade.queryPostByCommentParticulars(commentid, postid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 添加评论接口
     *
     * @param postid
     * @param loginid
     * @param content
     * @return
     */
    @ApiOperation(value = "添加评论", notes = "用于添加评论接口", response = Response.class)
    @RequestMapping(value = "add_post_comment", method = RequestMethod.POST)
    public Response addPostAppraise(@ApiParam(value = "帖子id") @RequestParam String postid,
                                    @ApiParam(value = "评论内容") @RequestParam String content,
                                    @ApiParam(value = "登录用户(评论人)") @RequestParam String loginid) {
        Response response = new Response();
        Map status = postFacade.addPostAppraise(postid, content, loginid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(status);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看评论-删除帖子评论
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除帖子评论", notes = "删除帖子评论", response = Response.class)
    @RequestMapping(value = "/delete_post_appraise", method = RequestMethod.POST)
    public Response deletePostAppraise(@ApiParam(value = "评论id") @RequestParam String id,
                                       @ApiParam(value = "登录用户") @RequestParam String loginid) {
        Response response = new Response();
        Map map = postFacade.deletePostAppraise(id, loginid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 编辑帖子评论
     *
     * @param commentid
     * @param content
     * @return
     */
    @ApiOperation(value = "编辑帖子评论", notes = "用于编辑帖子评论", response = Response.class)
    @RequestMapping(value = "/update_post_comment", method = RequestMethod.POST)
    public Response updatePostComment(@ApiParam(value = "这条评论的id") @RequestParam String commentid,
                                      @ApiParam(value = "评论内容") @RequestParam String content,
                                      @ApiParam(value = "登录用户") @RequestParam String loginid) {
        Response response = new Response();
        Map map = postFacade.updatePostComment(commentid, content, loginid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 回复评论
     *
     * @param pid
     * @param content
     * @param userid
     * @return
     */
    @ApiOperation(value = "回复帖子评论", notes = "用于回复帖子评论接口", response = Response.class)
    @RequestMapping(value = "/reply_post_comment", method = RequestMethod.POST)
    public Response replyPostComment(@ApiParam(value = "父评论的id") @RequestParam String pid,
                                     @ApiParam(value = "评论内容") @RequestParam String content,
                                     @ApiParam(value = "评论的帖子id") @RequestParam String postid,
                                     @ApiParam(value = "回复评论者id") @RequestParam String userid) {
        Response response = new Response();
        Map map = postFacade.replyPostComment(pid, content, postid, userid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 特约嘉宾评论审核
     *
     * @return
     */
    @ApiOperation(value = "帖子评论审核", notes = "用于给特约嘉宾评论审核接口", response = Response.class)
    @RequestMapping(value = "update_comment_audit", method = RequestMethod.POST)
    public Response auditComment(@ApiParam(value = "评论id") @RequestParam String commentid,
                                 @ApiParam(value = "登录用户id") @RequestParam String loginid,
                                 @ApiParam(value = "审核状态") @RequestParam String type) {
        Response response = new Response();
        Map map = postFacade.auditComment(commentid, loginid, type);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看帖子打赏
     *
     * @param postid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看帖子打赏列表", notes = "查看帖子打赏列表", response = Response.class)
    @RequestMapping(value = "/query_post_award", method = RequestMethod.POST)
    public Response queryPostAward(@ApiParam(value = "帖子id") @RequestParam String postid,
                                   @ApiParam(value = "排序 1时间拍倒叙 null正序") @RequestParam(required = false) String pai,
                                   @RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<RewardedVo> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<RewardedVo> list = postFacade.queryPostAward(postid, pai, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 后台管理-帖子列表-帖子预览
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "帖子预览", notes = "帖子预览", response = Response.class)
    @RequestMapping(value = "/query_post_particulars", method = RequestMethod.POST)
    public Response queryPostParticulars(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        PostList list = postFacade.queryPostParticulars(postid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }


    /**
     * 后台管理-帖子列表-活动预览
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "活动预览（查询活动使用，活动详情）", notes = "活动预览", response = Response.class)
    @RequestMapping(value = "/query_post_active", method = RequestMethod.POST)
    public Response queryPostActiveQ(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        PostList list = postFacade.queryPostActiveQ(postid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }


    /**
     * 改版后的发帖
     *
     * @param title
     * @param subtitle
     * @param circleid
     * @param userid
     * @param postcontent
     * @param goodsid
     * @param loginid
     * @return
     */
    @ApiOperation(value = "添加帖子(改版)", notes = "添加帖子（改版）", response = Response.class)
    @RequestMapping(value = "/add_post_test", method = RequestMethod.POST)
    public Response addPostTest(HttpServletRequest request,
                                @ApiParam(value = "帖子标题") @RequestParam String title,//帖子标题
                                @ApiParam(value = "说说关于你拍摄的故事--帖子副标题（限制500字符或汉字）") @RequestParam(required = false) String subtitle,//帖子副标题
                                @ApiParam(value = "圈子id") @RequestParam String circleid,//圈子id
                                @ApiParam(value = "发帖人") @RequestParam String userid,//发帖人
                                @ApiParam(value = "帖子封面(category为0时必填，为其他时传空)") @RequestParam(required = false) String coverimg,//帖子封面
                                @ApiParam(value = "内容") @RequestParam String postcontent,//帖子内容
                                @ApiParam(value = "标签id") @RequestParam(required = false) String labelid,
                                @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid,
                                @ApiParam(value = "登录用户") @RequestParam String loginid,
                                @ApiParam(value = "0图文贴1纯图片贴2视频贴") @RequestParam String category) {
        Response response = new Response();
        Map resaut = postFacade.addPostTest(request, title, subtitle, circleid, userid, coverimg, postcontent, labelid, goodsid, loginid, category);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(resaut);
        return response;
    }

    /**
     * 后台管理-添加活动帖子
     *
     * @param title
     * @param subtitle
     * @param activetype
     * @param activefee
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param orderid
     * @param essencedate
     * @param userid
     * @return
     */
    @ApiOperation(value = "添加活动帖子", notes = "添加活动帖子", response = Response.class)
    @RequestMapping(value = "/add_active_post", method = RequestMethod.POST)
    public Response addPostActiveList(
            @ApiParam(value = "活动标题") @RequestParam String title,
            @ApiParam(value = "活动副标题") @RequestParam String subtitle,
            @ApiParam(value = "活动类型：0 告知类活动 1 商城促销类活动 2 组织类活动") @RequestParam String activetype,
            @ApiParam(value = "1参与人数/0显示剩余结束天数") @RequestParam String partsumEnddays,
            @ApiParam(value = "是否需要投稿 0,投,1不投") @RequestParam(required = false) String iscontribute,
            @ApiParam(value = "单价") @RequestParam(required = false) String activefee,
            @ApiParam(value = "活动封面") @RequestParam(required = false) String coverimg,
            @ApiParam(value = "内容") @RequestParam String postcontent,
            @ApiParam(value = "首页精选") @RequestParam(required = false) String isessence,
            @ApiParam(value = "精选排序") @RequestParam(required = false) String orderid,
            @ApiParam(value = "精选日期（毫秒值）") @RequestParam(required = false) String essencedate,
            @ApiParam(value = "活动日期 ") @RequestParam String intime,
            @ApiParam(value = "发帖人") @RequestParam String userid,
            @ApiParam(value = "方形图片url") @RequestParam(required = false) String hotimgurl,
            @ApiParam(value = "是否设为热门（0 否  1 是）") @RequestParam(required = false) String ishot,
            @ApiParam(value = "热门排序") @RequestParam(required = false) String ishotorder,
            @ApiParam(value = "分享商品") @RequestParam(required = false) String goodsid) {
        Response response = new Response();
        Map<String, Integer> result = postFacade.addPostActive(title, subtitle, activetype, partsumEnddays, iscontribute,
                activefee, coverimg, postcontent, isessence, orderid, essencedate, intime, userid, hotimgurl, ishot, ishotorder, goodsid);
        if (response.getCode() == 200) {
            response.setMessage("添加成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 后台管理-帖子列表-帖子加精
     *
     * @return
     */
    @ApiOperation(value = "帖子加精/取消加精", notes = "用于帖子加精接口", response = Response.class)
    @RequestMapping(value = "/add_post_choiceness", method = RequestMethod.POST)
    public Response addPostChoiceness(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @ApiParam(value = "首页精选") @RequestParam(required = false) String isessence,
                                      @ApiParam(value = "圈子精选") @RequestParam(required = false) String ishot) {
        Response response = new Response();
        Map<String, Integer> result = postFacade.addPostChoiceness(postid, isessence, ishot);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 修改帖子热度值
     *
     * @param id
     * @param heatValue
     * @return
     */
    @ApiOperation(value = "修改帖子热度值", notes = "用于修改帖子热度值", response = Response.class)
    @RequestMapping(value = "updateHeateValue", method = RequestMethod.POST)
    public Response updateHeatValue(@ApiParam(value = "帖子id") @RequestParam String id,
                                    @ApiParam(value = "热度值") @RequestParam String heatValue) {
        Response response = new Response();
        Map map = postFacade.updateHeatValue(id, heatValue);
        if (map.get("resault").equals(1)) {
            response.setMessage("操作成功");
            response.setData(1);
            response.setCode(200);
        } else {
            response.setMessage("热度值超出范围");
            response.setData(-1);
            response.setCode(400);
        }
        return response;

    }

    /**
     * 特邀嘉宾操作帖子，加入精选池
     *
     * @param postid
     * @param loginid
     * @return
     */
    @ApiOperation(value = "帖子加入精选池", notes = "用于特邀嘉宾操作帖子，加入精选池接口", response = Response.class)
    @RequestMapping(value = "add_post_isessencepool", method = RequestMethod.POST)
    public Response addPostByisessencepool(@ApiParam(value = "帖子id") @RequestParam String postid,
                                           @ApiParam(value = "操作用户（登录用户）id") @RequestParam String loginid) {
        Response response = new Response();
        Map map = postFacade.addPostByisessencepool(postid, loginid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 查询精选池帖子列表
     *
     * @return
     */
    @ApiOperation(value = "查询精选池帖子列表", notes = "用于查询精选池帖子列表", response = Response.class)
    @RequestMapping(value = "query_post_isessencepool_list", method = RequestMethod.POST)
    public Response queryPostByIsessencepoolList(@ApiParam(value = "帖子标题") @RequestParam(required = false) String title,
                                                 @ApiParam(value = "圈子id") @RequestParam(required = false) String circleid,
                                                 @ApiParam(value = "发帖人") @RequestParam(required = false) String userid,
                                                 @ApiParam(value = "帖子内容") @RequestParam(required = false) String postcontent,
                                                 @ApiParam(value = "结束时间(yyyy-MM-dd)") @RequestParam(required = false) String endtime,
                                                 @ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(required = false) String begintime,
                                                 @ApiParam(value = "精选日期（yyyy-MM-dd）") @RequestParam(required = false) String essencedate,
                                                 @ApiParam(value = "登录用户") @RequestParam String loginid,
                                                 @ApiParam(value = "排序 0按时间排序") @RequestParam(required = false) String pai,
                                                 @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                 @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = postFacade.queryPostByIsessencepoolList(title, circleid, userid, postcontent, endtime, begintime, essencedate, loginid, pai, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }


    /**
     * 帖子加精数据回显
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "帖子加精数据回显", notes = "用于帖子加精时，数据回填接口", response = Response.class)
    @RequestMapping(value = "/query_post_choiceness", method = RequestMethod.POST)
    public Response queryPostChoiceness(@ApiParam(value = "精选日期") @RequestParam String essencedate,
                                        @ApiParam(value = "帖子id") @RequestParam(required = false) String postid) {
        Response response = new Response();
        PostChoiceness list = postFacade.queryPostChoiceness(postid, essencedate);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(list);
        return response;
    }


    /**
     * 后台管理-帖子列表-帖子分享列表
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "帖子分享列表", notes = "用于帖子分享列表展示接口", response = Response.class)
    @RequestMapping(value = "/query_post_share_list", method = RequestMethod.POST)
    public Response queryPostShareList(@ApiParam(value = "帖子id") @RequestParam String postid,
                                       @ApiParam(value = "排序方式，0升序1降序") @RequestParam String type,
                                       @RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<SharesVo> pager = new Paging<SharesVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SharesVo> list = postFacade.queryPostShareList(postid, type, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 模糊查询发帖人
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "模糊查询发帖人", notes = "用于模糊查询发帖人接口", response = Response.class)
    @RequestMapping(value = "/like_query_post_nickname", method = RequestMethod.POST)
    public Response likeQueryPostByNickname(@ApiParam(value = "关键字") @RequestParam(required = false) String name) {
        Response response = new Response();
        List<UserLike> list = postFacade.likeQueryPostByNickname(name);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }
/*
    *//**
     * 查询发帖人
     *
     * @return
     *//*
    @ApiOperation(value = "查询发帖人", notes = "用于查询发帖人列表接口", response = Response.class)
    @RequestMapping(value = "query_issue_post", method = RequestMethod.POST)
    public Response queryIssuePostManList() {
        Response response = new Response();
        List<BossUser> list = circleFacade.queryIssuePostManList();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }*/


    /**
     * 编辑帖子-数据回显
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "编辑帖子数据回显", notes = "用于编辑帖子时数据回显", response = Response.class)
    @RequestMapping(value = "query_post_echo", method = RequestMethod.POST)
    public Response queryPostByIdEcho(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        PostCompile postCompile = postFacade.queryPostByIdEcho(postid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(postCompile);
        return response;
    }


    /**
     * 编辑帖子
     *
     * @param request
     * @param id
     * @param title
     * @param subtitle
     * @param userid
     * @param circleid
     * @param coverimg
     * @param postcontent
     * @param labelid
     * @param goodsid
     * @param loginid
     * @return
     */
    @ApiOperation(value = "编辑帖子(改版)", notes = "用于帖子编辑接口(改版)", response = Response.class)
    @RequestMapping(value = "update_post_test", method = RequestMethod.POST)
    public Response updatePostByIdTest(HttpServletRequest request, @ApiParam(value = "帖子id（必填）") @RequestParam String id,
                                       @ApiParam(value = "帖子标题") @RequestParam(required = false) String title,//帖子标题
                                       @ApiParam(value = "帖子副标题") @RequestParam(required = false) String subtitle,//帖子副标题
                                       @ApiParam(value = "发帖人（必填且必须是管理员-1）") @RequestParam String userid,//发帖人
                                       @ApiParam(value = "圈子id") @RequestParam(required = false) String circleid,//圈子id
                                       @ApiParam(value = "帖子封面(需要上传的文件)") @RequestParam String coverimg,//帖子封面
                                       @ApiParam(value = "帖子内容（必填）") @RequestParam String postcontent,//帖子内容
                                       @ApiParam(value = "标签id") @RequestParam(required = false) String labelid,
                                       @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid,
                                       @ApiParam(value = "登录用户") @RequestParam String loginid,
                                       @ApiParam(value = "帖子类型0图文贴1纯图贴2视频贴") @RequestParam String category) {
        Response response = new Response();
        Map map = postFacade.updatePostByIdTest(request, id, title, subtitle, userid, circleid, coverimg, postcontent, labelid, goodsid, loginid, category);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }


    /**
     * 编辑活动
     *
     * @param
     * @param id
     * @param title
     * @param subtitle
     * @param userid
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param orderid
     * @param activefee
     * @param activetype
     * @param essencedate
     * @return
     */
    @ApiOperation(value = "编辑活动", notes = "编辑活动", response = Response.class)
    @RequestMapping(value = "update_activepost", method = RequestMethod.POST)
    public Response updateActivePostById(
            @ApiParam(value = "帖子id（必填）") @RequestParam String id,
            @ApiParam(value = "帖子标题") @RequestParam(required = false) String title,
            @ApiParam(value = "帖子副标题") @RequestParam(required = false) String subtitle,
            @ApiParam(value = "发帖人") @RequestParam(required = false) String userid,
            @ApiParam(value = "帖子封面(需要上传的文件)") @RequestParam(required = false, value = "coverimg") String coverimg,
            @ApiParam(value = "帖子内容") @RequestParam(required = false) String postcontent,
            @ApiParam(value = "首页精选") @RequestParam(required = false) String isessence,
            @ApiParam(value = "精选排序(0-9数字)") @RequestParam(required = false) String orderid,
            @ApiParam(value = "费用") @RequestParam(required = false) String activefee,
            @ApiParam(value = "活动类型0 告知类活动 1 商城促销类活动 2 组织类活动") @RequestParam(required = false) String activetype,
            @ApiParam(value = "是否需要投稿 0,投,1不投") @RequestParam(required = false) String iscontribute,
            @ApiParam(value = "活动时间") @RequestParam(required = false) String intime,
            @ApiParam(value = "方形图片url") @RequestParam(required = false) String hotimgurl,
            @ApiParam(value = "是否设为热门（0 否  1 是）") @RequestParam(required = false) String ishot,
            @ApiParam(value = "热门排序") @RequestParam(required = false) String ishotorder,
            @ApiParam(value = "精选日期 毫秒值") @RequestParam(required = false) String essencedate,
            @ApiParam(value = "1参与人数/0显示剩余结束天数") @RequestParam(required = false) String partsumEnddays,
            @ApiParam(value = "编辑商品") @RequestParam(required = false) String goodsid) {
        Response response = new Response();

        Map<String, Integer> map = postFacade.updateActivePostById(id, title, subtitle, userid, coverimg, postcontent,
                isessence, orderid, activefee, activetype, iscontribute, intime, hotimgurl, ishot, ishotorder, essencedate, partsumEnddays, goodsid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }


    /**
     * 帖子多条件联合搜索
     *
     * @param pageNo
     * @param pageSize
     * @param title
     * @param circleid
     * @return
     */
    @ApiOperation(value = "帖子联合条件搜索", notes = "用于精确查找帖子搜索接口", response = Response.class)
    @RequestMapping(value = "/post_search", method = RequestMethod.POST)
    public Response postSearch(@ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                               @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                               @ApiParam(value = "帖子标题") @RequestParam(required = false) String title,
                               @ApiParam(value = "圈子id") @RequestParam(required = false) String circleid,
                               @ApiParam(value = "发帖人") @RequestParam(required = false) String userid,
                               @ApiParam(value = "帖子内容") @RequestParam(required = false) String postcontent,
                               @ApiParam(value = "开始、结束时间(yyyy-MM-dd)") @RequestParam(required = false) String intime,
                               @ApiParam(value = "精选排序方式 0：按时间排序，1：按热度排序(默认不做排序)") @RequestParam(required = false) String pai,
                               @ApiParam(value = "用户id") @RequestParam(required = false) String uid,
                               @ApiParam(value = "用户列表跳转传值：5 帖子 6 精贴") @RequestParam(required = false) String price,
                               @ApiParam(value = "精选日期（yyyy-MM-dd）") @RequestParam(required = false) String essencedate,
                               @ApiParam(value = "帖子类型0图文贴1纯图片贴2视频贴") @RequestParam(required = false) String category,
                               @ApiParam(value = "登录用户") @RequestParam String loginid) {
        Response response = new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = postFacade.postSearch(title, circleid, userid, postcontent, intime, pai, essencedate, uid, price, category, loginid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 评论列表根据点赞人气排序
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "评论点赞排序", notes = "用于查询评论根据点赞人气排序接口", response = Response.class)
    @RequestMapping(value = "comment_zan_sork", method = RequestMethod.POST)
    public Response commentZanSork(@ApiParam(value = "帖子id") @RequestParam String postid,
                                   @RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSiz) {
        Response response = new Response();
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSiz));
        List<CommentVo> list = postFacade.commentZanSork(postid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 敏感字搜索评论
     *
     * @param pageNo
     * @param pageSize
     * @param content
     * @param words
     * @param
     * @return
     */
    @ApiOperation(value = "敏感字模糊搜索评论列表", notes = "用于搜索含有敏感字的评论", response = Response.class)
    @RequestMapping(value = "query_comment_sensitive_words", method = RequestMethod.POST)
    public Response queryCommentSensitiveWords(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                               @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                               @ApiParam(value = "评论内容") @RequestParam(required = false) String content,
                                               @ApiParam(value = "敏感字") @RequestParam(required = false) String words,
                                               @ApiParam(value = "评论结束时间") @RequestParam(required = false) String endtime,
                                               @ApiParam(value = "评论开始时间") @RequestParam(required = false) String begintime) {
        Response response = new Response();
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<CommentVo> list = postFacade.queryCommentSensitiveWords(content, words, begintime, endtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }


    /**
     * 活动条件搜索
     *
     * @param title
     * @param userid
     * @param content
     * @param intime
     * @param statue
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "活动条件搜索", notes = "活动条件搜索", response = Response.class)
    @RequestMapping(value = "query_active_list", method = RequestMethod.POST)
    public Response queryAllActivePostCondition(@ApiParam(value = "活动名称") @RequestParam(required = false) String title,
                                                @ApiParam(value = "发帖人id") @RequestParam(required = false) String userid,
                                                @ApiParam(value = "内容") @RequestParam(required = false) String content,
                                                @ApiParam(value = "活动日期") @RequestParam(required = false) String intime,
                                                @ApiParam(value = "活动状态") @RequestParam(required = false) String statue,
                                                @ApiParam(value = "排序") @RequestParam(required = false) String pai,
                                                @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostActiveList> pager = new Paging<PostActiveList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostActiveList> list = postFacade.queryAllActivePostCondition(title, userid, content, intime, statue, pai, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 查询商品列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询商品列表", notes = "用于帖子选择商品接口", response = Response.class)
    @RequestMapping(value = "query_post_goods_list", method = RequestMethod.POST)
    public Response queryPostByGoodsList(@ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<GoodsVo> pager = new Paging<GoodsVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsVo> list = postFacade.queryPostByGoodsList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 条件查询商品列表
     *
     * @param name
     * @param brandid
     * @param protype
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件查询商品列表", notes = "用于条件搜索接口", response = Response.class)
    @RequestMapping(value = "query_like_goods", method = RequestMethod.POST)
    public Response findAllQueryLikeGoods(@ApiParam(value = "产品名称") @RequestParam(required = false) String name,
                                          @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid,
                                          @ApiParam(value = "品牌名称") @RequestParam(required = false) String brandid,
                                          @ApiParam(value = "产品分类") @RequestParam(required = false) String protype,
                                          @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                          @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<GoodsVo> pager = new Paging<GoodsVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsVo> list = postFacade.findAllQueryLikeGoods(name, goodsid, brandid, protype, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 根据id查询活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询活动（数据回显使用）", notes = "根据id查询活动", response = Response.class)
    @RequestMapping(value = "query_activeById", method = RequestMethod.POST)
    public Response queryActiveById(@ApiParam(value = "活动id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        PostActiveList postActiveList = postFacade.queryActiveById(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(postActiveList);
        return response;
    }


    /**
     * 上传活动图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传活动图片", notes = "上传活动图片", response = Response.class)
    @RequestMapping(value = {"/upload_active_pic"}, method = RequestMethod.POST)
    public Response updateMyInfo(@RequestParam(value = "file", required = false) MultipartFile file) {

        Map m = movisionOssClient.uploadObject(file, "img", "activity");
        String url = String.valueOf(m.get("url"));
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        map.put("width", m.get("width"));
        map.put("height", m.get("height"));
        return new Response(map);
    }

    /**
     * 上传帖子相关图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传帖子相关图片（改版）", notes = "上传帖子相关图片", response = Response.class)
    @RequestMapping(value = {"/upload_post_img_test"}, method = RequestMethod.POST)
    public Response updatePostImgTest(@RequestParam(value = "file", required = false) MultipartFile[] file) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            Map map = postFacade.updatePostImgTest(file[i]);
            list.add(map);
        }
        return new Response(list);
    }

    @ApiOperation(value = "测试上传图片", notes = "测试上传图片", response = Response.class)
    @RequestMapping(value = {"/upload_test_test"}, method = RequestMethod.POST)
    public Response updatetest(@RequestParam(value = "file", required = false) MultipartFile file) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map map = postFacade.updatePostImgTest(file);
        list.add(map);
        return new Response(list);
    }

    /**
     * boss上传帖子封面图
     *
     * @param file
     * @param x
     * @param y
     * @param w
     * @param h
     * @param type
     * @return
     */
    @ApiOperation(value = "上传帖子封面图片（改版）", notes = "PC官网上传帖子封面图片（改版）", response = Response.class)
    @RequestMapping(value = {"/updateCoverImgByPC"}, method = RequestMethod.POST)
    public Response updateCoverImgByPC(@RequestParam(value = "file", required = false) MultipartFile file,
                                       @ApiParam(value = "X坐标") @RequestParam(required = false) String x,
                                       @ApiParam(value = "Y坐标") @RequestParam(required = false) String y,
                                       @ApiParam(value = "宽") @RequestParam String w,
                                       @ApiParam(value = "高") @RequestParam String h,
                                       @ApiParam(value = "1帖子封面 2活动方形图") @RequestParam String type) {
        Map map = facadePost.updateCoverImgByPC(file, x, y, w, h, type);
        return new Response(map);
    }


    /**
     * 上传app开屏图
     *
     * @param file
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    @ApiOperation(value = "上传app开屏图", notes = "上传app开屏图", response = Response.class)
    @RequestMapping(value = {"/updateAppUpimg"}, method = RequestMethod.POST)
    public Response updateAppUpimg(@RequestParam(value = "file", required = false) MultipartFile file,
                                   @ApiParam(value = "X坐标") @RequestParam(required = false) String x,
                                   @ApiParam(value = "Y坐标") @RequestParam(required = false) String y,
                                   @ApiParam(value = "宽") @RequestParam String w,
                                   @ApiParam(value = "高") @RequestParam String h) {
        Map map = facadePost.updateAppUpimg(file, x, y, w, h);
        return new Response(map);
    }

    /**
     * 上传帖子相关图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传帖子相关图片", notes = "上传帖子相关图片", response = Response.class)
    @RequestMapping(value = {"/upload_post_img"}, method = RequestMethod.POST)
    public Response updatePostImg(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @ApiParam(value = "用于选择上传位置（1:封面 2:内容图片）") @RequestParam String type) {
        Map m = new HashMap();
        if (type.equals("1")) {
            m = movisionOssClient.uploadObject(file, "img", "postCover");
        } else if (type.equals("2")) {
            m = movisionOssClient.uploadObject(file, "img", "post");
        }
        String url = String.valueOf(m.get("url"));
        Map map = new HashMap();
        m.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        map.put("width", m.get("width"));
        map.put("height", m.get("height"));
        return new Response(map);
    }


    /**
     * 上传帖子相关视频
     *
     * @param file
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @ApiOperation(value = "上传帖子相关视频", notes = "上传帖子相关视频", response = Response.class)
    @RequestMapping(value = "/upload_post_video", method = RequestMethod.POST)
    public Response queryApplyVipList(@RequestParam(value = "file", required = false) MultipartFile file) throws ServletException, IOException {
        Map m = movisionOssClient.uploadObject(file, "video", "post");
        String url = String.valueOf(m.get("url"));
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        map.put("width", m.get("width"));
        map.put("height", m.get("height"));
        return new Response(map);
    }

    /**
     * 是否设为热门
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "是否设为热门", notes = "是否设为热门", response = Response.class)
    @RequestMapping(value = "update_ishot", method = RequestMethod.POST)
    public Response updateIshot(@ApiParam(value = "活动id") @RequestParam(required = false) Integer id,
                                @ApiParam(value = "排序值") @RequestParam(required = false) Integer ishotorder) {
        Response response = new Response();
        int result = postFacade.updateIshot(id, ishotorder);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 条件查询原生视频投稿列表
     *
     * @param nickname
     * @param email
     * @param type
     * @param vip
     * @param begintime
     * @param endtime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件查询原生视频投稿列表", notes = "用于根据条件查询原生视频投稿列表接口", response = Response.class)
    @RequestMapping(value = "query_unite_condition_contribute", method = RequestMethod.POST)
    public Response queryUniteConditionByContribute(@ApiParam(value = "用户名") @RequestParam(required = false) String nickname,
                                                    @ApiParam(value = "邮箱") @RequestParam(required = false) String email,
                                                    @ApiParam(value = "审核状态 0 待审核 1 审核通过 2 审核未通过") @RequestParam(required = false) String type,
                                                    @ApiParam(value = "vip") @RequestParam(required = false) String vip,
                                                    @ApiParam(value = "圈子id") @RequestParam(required = false) String circleid,
                                                    @ApiParam(value = "投稿主题") @RequestParam(required = false) String title,
                                                    @ApiParam(value = "开始时间") @RequestParam(required = false) String begintime,
                                                    @ApiParam(value = "结束时间") @RequestParam(required = false) String endtime,
                                                    @ApiParam(value = "排序方式 0为时间倒叙") @RequestParam(required = false) String pai,
                                                    @ApiParam(value = "当前登录用户id") @RequestParam String loginid,
                                                    @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                    @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<SubmissionVo> pager = new Paging<SubmissionVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SubmissionVo> list = userManageFacade.queryUniteConditionByContribute(nickname, email, type, vip, circleid, title, begintime, endtime, pai, loginid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }


    /**
     * 查询活动投稿列表
     *
     * @param nickname
     * @param email
     * @param type
     * @param postname
     * @param intime
     * @param pai
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询活动投稿列表", notes = "用于条件查询活动投稿列表", response = Response.class)
    @RequestMapping(value = "query_activity_contribute", method = RequestMethod.POST)
    public Response queryActivityContribute(@ApiParam(value = "用户名") @RequestParam(required = false) String nickname,
                                            @ApiParam(value = "邮箱") @RequestParam(required = false) String email,
                                            @ApiParam(value = "审核状态 0 待审核 1 审核通过 2 审核未通过") @RequestParam(required = false) String type,
                                            @ApiParam(value = "帖子标题") @RequestParam(required = false) String postname,
                                            @ApiParam(value = "是否是VIP") @RequestParam(required = false) String vip,
                                            @ApiParam(value = "投稿主题") @RequestParam(required = false) String title,
                                            @ApiParam(value = "时间") @RequestParam(required = false) String intime,
                                            @ApiParam(value = "排序方式 0为时间倒叙") @RequestParam(required = false) String pai,
                                            @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                            @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<ActivityContributeVo> pager = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ActivityContributeVo> resault = postFacade.findAllQueryActivityContribute(nickname, email, type, vip, postname, title, intime, pai, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(resault);
        response.setData(pager);
        return response;
    }

    /**
     * 查询活动热门排序
     *
     * @return
     */
    @ApiOperation(value = "查询活动热门可排序", notes = "用于查询活动热门可排序", response = Response.class)
    @RequestMapping(value = "queryActiveByOrderid", method = RequestMethod.POST)
    public Response queryActiveByOrderid() {
        Response response = new Response();
        List<Integer> list = postFacade.queryActiveByOrderid();
        response.setMessage("查询成功");
        response.setData(list);
        return response;
    }


    /**
     * 点击投稿数值跳转到这个活动下投的所有帖子列表页
     *
     * @param activityid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询活动投稿的帖子列表", notes = "点击投稿数值跳转到这个活动下投的所有帖子列表页", response = Response.class)
    @RequestMapping(value = "queryActivitycontributeListById", method = RequestMethod.POST)
    public Response queryActivitycontributeListById(@ApiParam(value = "活动id") @RequestParam String activityid,
                                                    @ApiParam(value = "当前页") @RequestParam(defaultValue = "1") String pageNo,
                                                    @ApiParam(value = "每页几条") @RequestParam(defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Post> pager = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Post> resault = postFacade.findAllQueryActivitycontributeListById(activityid, pager);
        response.setMessage("查询成功");
        pager.result(resault);
        response.setData(pager);
        return response;
    }

    /**
     * 查询活动投稿详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询活动投稿详情", notes = "查询活动投稿详情", response = Response.class)
    @RequestMapping(value = "query_contribute_explain", method = RequestMethod.POST)
    public Response queryContributeExplain(@ApiParam(value = "投稿id") @RequestParam String id) {
        Response response = new Response();
        ActivityContribute ac = postFacade.queryContributeExplain(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(ac);
        return response;
    }

    /**
     * 条件查询帖子标签列表
     *
     * @param name
     * @param type
     * @param userName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件查询帖子标签列表", notes = "用于查询帖子标签列表", response = Response.class)
    @RequestMapping(value = "queryPostLabelList", method = RequestMethod.POST)
    public Response queryPostLabelList(@ApiParam(value = "标签名称") @RequestParam(required = false) String name,
                                       @ApiParam(value = "标签类型") @RequestParam(required = false) String type,
                                       @ApiParam(value = "创建人") @RequestParam(required = false) String userName,
                                       @ApiParam(value = "当前页") @RequestParam(defaultValue = "1") String pageNo,
                                       @ApiParam(value = "每页几条") @RequestParam(defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostLabelDetails> pag = new Paging<PostLabelDetails>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostLabelDetails> labels = postFacade.findAllQueryPostLabelList(name, type, userName, pag);
        pag.result(labels);
        response.setMessage("查询成功");
        response.setData(pag);
        return response;
    }


    /**
     * 新增标签
     *
     * @param name
     * @param type
     * @param userid
     * @param photo
     * @return
     */
    @ApiOperation(value = "新增标签", notes = "用于新增标签接口", response = Response.class)
    @RequestMapping(value = "addPostLabel", method = RequestMethod.POST)
    public Response insertPostLabel(@ApiParam(value = "标签名称") @RequestParam String name,
                                    @ApiParam(value = "标签类型0：圈子，1：活动，2：地理，3：其他") @RequestParam String type,
                                    @ApiParam(value = "创建人登录用户") @RequestParam String userid,
                                    @ApiParam(value = "是否推荐首页") @RequestParam String isrecommend,
                                    @ApiParam(value = "标签头像") @RequestParam(required = false) String photo) {
        Response response = new Response();
        postFacade.insertPostLabel(name, type, userid, isrecommend, photo);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 查询标签详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询标签详情", response = Response.class)
    @RequestMapping(value = "queryPostLabelById", method = RequestMethod.POST)
    public Response queryPostLabelById(@ApiParam(value = "标签id") @RequestParam String id) {
        Response response = new Response();
        PostLabelDetails label = postFacade.queryPostLabelById(id);
        response.setMessage("查询成功");
        response.setData(label);
        return response;
    }

    /**
     * 修改帖子标签
     *
     * @param id
     * @param name
     * @param type
     * @param photo
     * @return
     */
    @ApiOperation(value = "编辑修改帖子标签", response = Response.class)
    @RequestMapping(value = "updatePostLabel", method = RequestMethod.POST)
    public Response updatePostLabel(@ApiParam(value = "标签id") @RequestParam String id,
                                    @ApiParam(value = "标签名称") @RequestParam(required = false) String name,
                                    @ApiParam(value = "标签类型0：圈子，1：活动，2：地理，3：其他") @RequestParam(required = false) String type,
                                    @ApiParam(value = "是否推荐首页") @RequestParam(required = false) String isrecommend,
                                    @ApiParam(value = "标签头像") @RequestParam(required = false) String photo) {
        Response response = new Response();
        postFacade.updatePostLabel(id, name, type, isrecommend, photo);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 删除帖子标签
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除帖子标签", response = Response.class)
    @RequestMapping(value = "deletePostLabel", method = RequestMethod.POST)
    public Response deletePostLabel(@ApiParam(value = "标签id") @RequestParam String id) {
        Response response = new Response();
        postFacade.deletePostLabel(id);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 推荐标签到首页
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "位标签推荐到首页", notes = "用于手动推荐标签到首页", response = Response.class)
    @RequestMapping(value = "updatePostLabelIsRecommend", method = RequestMethod.POST)
    public Response updatePostLabelIsRecommend(@ApiParam(value = "标签id") @RequestParam String id) {
        Response response = new Response();
        postFacade.updatePostLabelIsRecommend(id);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 查询标签名称列表
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "查询标签名称列表", response = Response.class)
    @RequestMapping(value = "queryPostLabelByName", method = RequestMethod.POST)
    public Response queryPostLabelByName(@ApiParam(value = "标签名称") @RequestParam(required = false) String name) {
        Response response = new Response();
        List<PostLabel> list = postFacade.queryPostLabelByName(name);
        response.setMessage("查询成功");
        response.setData(list);
        return response;
    }

    /**
     * 查询帖子详情中原图
     *
     * @param compressimgurl
     * @return
     */
    @ApiOperation(value = "查询原图（后台）", notes = "用于查询帖子详情原图url", response = Response.class)
    @RequestMapping(value = "queryOriginalDrawingUrl", method = RequestMethod.POST)
    public Response queryOriginalDrawingUrl(@ApiParam(value = "压缩图url") @RequestParam String compressimgurl) {
        Response response = new Response();
        String url = postFacade.queryOriginalDrawingUrl(compressimgurl);
        response.setMessage("查询成功");
        response.setData(url);
        return response;
    }

    @ApiOperation(value = "查询24小时内指定帖子的热度变化 EChart 数据", notes = "查询24小时内指定帖子的热度变化 EChart 数据", response = Response.class)
    @RequestMapping(value = "query_24hour_post_heatvalue_echart_data", method = RequestMethod.POST)
    public Response query24HourPostHeatvalueEchartData(@ApiParam(value = "指定的帖子id") @RequestParam Integer postid,
                                                       @ApiParam(value = "指定的日期,yyyy-MM-dd格式") @RequestParam String date)
            throws ParseException {
        Response response = new Response();
        Map map = postFacade.querySpecifyDatePostHeatvalue(postid, date);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "查询指定帖子在一定日期内的热度变化 EChart 数据", notes = "查询指定帖子在一定日期内的热度变化 EChart 数据", response = Response.class)
    @RequestMapping(value = "query_period_post_heatvalue_echart_data", method = RequestMethod.POST)
    public Response queryPeriodPostHeatvalueEchartData(@ApiParam(value = "指定的帖子id") @RequestParam Integer postid,
                                                       @ApiParam(value = "指定的开始日期,yyyy-MM-dd格式") @RequestParam String beginDate,
                                                       @ApiParam(value = "指定的结束日期,yyyy-MM-dd格式") @RequestParam String endDate)
            throws ParseException {
        Response response = new Response();
        Map map = postFacade.wrapEchartEverydayHeatvalueData(postid, beginDate, endDate);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }


    @ApiOperation(value = "测试发帖时，帖子内容对图片排序拼接", notes = "图片排序", response = Response.class)
    @RequestMapping(value = "test_img_picture_merge", method = RequestMethod.POST)
    public Response testPostContentMergePicture(@ApiParam(value = "帖子内容") @RequestParam String postcontent) {
        Response response = new Response();
        String str = null;
        try {
            str = imgSortUtil.mergePicture(postcontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setMessage("操作成功");
        response.setData(str);
        return response;
    }

    @ApiOperation(value = "敏感类容屏蔽开关（用于appstore骗审时使用）", notes = "敏感类容屏蔽开关（用于appstore骗审时使用）", response = Response.class)
    @RequestMapping(value = "switchSensitive", method = RequestMethod.POST)
    public Response switchSensitive(@ApiParam(value = "操作:0 恢复 1 屏蔽") @RequestParam String type){
        Response response = new Response();

        postFacade.switchSensitive(type);

        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }else {
            response.setMessage("操作失败");
        }
        return response;
    }
}
