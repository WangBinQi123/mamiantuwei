package com.binqi.mamiantuwei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.binqi.mamiantuwei.model.dto.questionCode.QuestionQueryRequest;
import com.binqi.mamiantuwei.model.entity.QuestionCode;
import com.binqi.mamiantuwei.model.vo.QuestionCodeVO;


import javax.servlet.http.HttpServletRequest;


public interface QuestionCodeService extends IService<QuestionCode> {
    /**
     * 校验
     *
     * @param questionCode
     * @param add
     */
    void validQuestionCode(QuestionCode questionCode, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<QuestionCode> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionCode
     * @param request
     * @return
     */
    QuestionCodeVO getQuestionCodeVO(QuestionCode questionCode, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionCodePage
     * @param request
     * @return
     */
    Page<QuestionCodeVO> getQuestionCodeVOPage(Page<QuestionCode> questionCodePage, HttpServletRequest request);

}
