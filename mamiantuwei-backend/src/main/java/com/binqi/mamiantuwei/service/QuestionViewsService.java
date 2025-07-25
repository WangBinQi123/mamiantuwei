package com.binqi.mamiantuwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.binqi.mamiantuwei.model.entity.QuestionViews;

/**
 * 题目服务

 */
public interface QuestionViewsService extends IService<QuestionViews> {

    /**
     * 获取题目浏览量
     *
     * @param questionId 题目ID
     * @return 浏览量
     */
    long getViewCount(Long questionId);

    /**
     * 同步Redis中的浏览量数据到MySQL
     */
    void syncViewCountToDb();

    /**
     * 增加浏览量
     * @param questionId 题目ID
     * @return 是否成功
     */
    boolean increaseViewCount(Long questionId);
}
