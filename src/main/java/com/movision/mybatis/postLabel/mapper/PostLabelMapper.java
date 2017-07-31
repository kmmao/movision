package com.movision.mybatis.postLabel.mapper;

import com.movision.mybatis.circle.entity.CircleCount;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.*;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface PostLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLabel record);

    int insertSelective(PostLabel record);

    PostLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLabel record);

    int updateByPrimaryKey(PostLabel record);

    List<PostLabelVo> getMineFollowLabel(Map<String, Object> parammap, RowBounds rowBounds);

    List<PostLabel> queryLableName();

    int updateLabelHeatValue(Map map);

    List<PostLabel> queryLabelHeatValue();

    List<PostLabelDetails> findAllQueryPostLabelList(PostLabelVo label, RowBounds rowBounds);

    PostLabelTz queryName(int labelid);

    List<PostLabelCount> queryCountLabelName(int labelid);

    int batchInsert(List<PostLabel> postLabelList);

    PostLabelDetails queryPostLabelById(PostLabel label);

    void deletePostLabel(PostLabel label);

    List<Integer> queryLabelIdList(String[] nameStr);

    CircleVo queryCircleByPostid(int circleid);

    Integer postInCircle(int circleid);

    List<PostLabel> queryLabelCircle(int circleid);

    List<PostVo> findAllHotPost(int circleid, RowBounds rowBounds);

    List<PostVo> findAllNewPost(int circleid, RowBounds rowBounds);

    List<PostVo> findAllIsencePost(int circleid, RowBounds rowBounds);

    List<CircleCount> queryCirclePeople(int circleid);

    List<PostLabel> isrecommendLabel();

    Integer queryPostLabeIsRecommend(PostLabel label);

    void updatePostLabelIsRecommend(PostLabel label);

    Integer countSameNameLabel(String name);

    List<Integer> labelId(int userid);

    List<PostLabel> queryHotValueLabelList(Integer displayCount);

    List<PostLabel> queryPostLabelByName(PostLabel postLabel);
}