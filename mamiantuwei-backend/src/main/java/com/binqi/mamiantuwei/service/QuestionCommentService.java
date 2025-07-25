package com.binqi.mamiantuwei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.binqi.mamiantuwei.model.dto.questioncomment.QuestionCommentQueryRequest;
import com.binqi.mamiantuwei.model.entity.QuestionComment;
import com.binqi.mamiantuwei.model.entity.User;

/**
 * 题目评论服务
 */
public interface QuestionCommentService extends IService<QuestionComment> {

    /**
     * 校验评论
     */
    void validQuestionComment(QuestionComment questionComment, boolean add);

    /**
     * 创建评论
     */
    long addComment(QuestionComment questionComment, User loginUser);

    /**
     * 删除评论
     */
    boolean deleteComment(long id, User loginUser);

    /**
     * 更新评论
     */
    boolean updateComment(QuestionComment questionComment, User loginUser);

    /**
     * 分页获取评论
     */
    Page<QuestionComment> getCommentPage(QuestionCommentQueryRequest questionCommentQueryRequest);
}