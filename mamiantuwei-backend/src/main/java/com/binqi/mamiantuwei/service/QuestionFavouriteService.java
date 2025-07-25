package com.binqi.mamiantuwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.binqi.mamiantuwei.model.entity.Question;
import com.binqi.mamiantuwei.model.entity.QuestionFavourite;
import com.binqi.mamiantuwei.model.entity.User;

import java.util.List;

/**
 * 题目收藏服务
 */
public interface QuestionFavouriteService extends IService<QuestionFavourite> {

    /**
     * 收藏 / 取消收藏
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionFavourite(long questionId, User loginUser);

    /**
     * 获取用户收藏的题目列表
     * @param loginUser
     * @return
     */
    List<Question> listMyFavouriteQuestions(User loginUser);

    /**
     * 判断用户是否已收藏题目
     *
     * @param questionId 题目id
     * @param userId 用户id
     * @return 是否已收藏
     */
    boolean isQuestionFavourite(long questionId, long userId);

    int getFavourite(long questionId);
}