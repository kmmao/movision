package com.movision.facade.index;

import com.movision.common.constant.HeatValueConstant;
import com.movision.mybatis.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/7/11 11:05
 */
@Service
public class FacadeHeatValue {
    private static Logger log = LoggerFactory.getLogger(FacadeHeatValue.class);

    @Autowired
    private PostService postService;

    /**
     * 增加帖子热度值
     */
    public int addHeatValue(int postid, int type) {
        int new_point = 0;
        if (type == 1) {//首页精选
            int isessence = postService.queryPostIsessenceHeat(postid);
            if (isessence == 1) {//首页精选
                new_point = HeatValueConstant.POINT.home_page_selection.getCode();
                postService.updatePostHeatValue(new_point, postid);
            }
        } else if (type == 2) {//帖子精选
            int hot = postService.queryPostHotHeat(postid);
            if (hot == 1) {
                new_point = HeatValueConstant.POINT.post_selection.getCode();
                postService.updatePostHeatValue(new_point, postid);
            }
        } else if (type == 3) {//点赞
            int level = userLevel(postid);
            new_point = level * HeatValueConstant.POINT.zan_number.getCode();
            postService.updatePostHeatValue(new_point, postid);
        } else if (type == 4) {//评论
            int level = userLevel(postid);
            new_point = level * HeatValueConstant.POINT.comments_number.getCode();
            postService.updatePostHeatValue(new_point, postid);
        } else if (type == 5) {//转发
            int level = userLevel(postid);
            new_point = level * HeatValueConstant.POINT.forwarding_number.getCode();
            postService.updatePostHeatValue(new_point, postid);
        } else if (type == 6) {//收藏
            int level = userLevel(postid);
            new_point = level * HeatValueConstant.POINT.collection_number.getCode();
            postService.updatePostHeatValue(new_point, postid);
        }
        return new_point;
    }

    /**
     * 查询发帖人级别
     *
     * @param postid
     * @return
     */
    public int userLevel(int postid) {
        //查询发帖人级别
        int level = postService.selectUserLevel(postid);
        if (level == 0) {
            level = 1;
        } else if (level == 1) {
            level = 2;
        } else if (level == 2) {
            level = 3;
        } else if (level == 3) {
            level = 4;
        } else if (level == 4) {
            level = 5;
        } else if (level == 5) {
            level = 6;
        } else if (level == 6) {
            level = 7;
        } else if (level == 7) {
            level = 8;
        } else if (level == 8) {
            level = 9;
        } else if (level == 9) {
            level = 10;
        }
        return level;
    }
}
