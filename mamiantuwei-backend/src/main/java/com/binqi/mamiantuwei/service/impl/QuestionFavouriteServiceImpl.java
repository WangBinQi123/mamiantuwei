package com.binqi.mamiantuwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binqi.mamiantuwei.common.ErrorCode;
import com.binqi.mamiantuwei.exception.BusinessException;
import com.binqi.mamiantuwei.mapper.QuestionFavouriteMapper;
import com.binqi.mamiantuwei.mapper.QuestionMapper;
import com.binqi.mamiantuwei.model.entity.Question;
import com.binqi.mamiantuwei.model.entity.QuestionFavourite;
import com.binqi.mamiantuwei.model.entity.User;
import com.binqi.mamiantuwei.service.QuestionFavouriteService;
import com.binqi.mamiantuwei.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionFavouriteServiceImpl extends ServiceImpl<QuestionFavouriteMapper, QuestionFavourite> implements QuestionFavouriteService {

    @Resource
    private QuestionFavouriteMapper questionFavouriteMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionService questionService;
    @Override
    public int doQuestionFavourite(long questionId, User loginUser) {
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);

        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return 0;
    }

    @Override
    public List<Question> listMyFavouriteQuestions(User loginUser) {
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 先查询收藏记录
        LambdaQueryWrapper<QuestionFavourite> questionFavouriteQueryWrapper = new LambdaQueryWrapper<>();
        questionFavouriteQueryWrapper.eq(QuestionFavourite::getUserId, loginUser.getId());
        List<QuestionFavourite> questionFavouriteList = questionFavouriteMapper.selectList(questionFavouriteQueryWrapper);
        // 获取题目 id 列表
        List<Long> questionIdList = questionFavouriteList.stream()
                .map(QuestionFavourite::getQuestionId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(questionIdList)) {
            return new ArrayList<>();
        }
        // 查询题目信息
        return questionMapper.selectBatchIds(questionIdList);
    }

    @Override
    public boolean isQuestionFavourite(long questionId, long userId) {
        QueryWrapper<QuestionFavourite> questionFavouriteQueryWrapper = new QueryWrapper<>();
        questionFavouriteQueryWrapper.eq("questionId", questionId).eq("userId", userId);
        long count = questionFavouriteMapper.selectCount(questionFavouriteQueryWrapper);
        return count > 0;
    }

    @Override
    public int getFavourite(long questionId) {
        QueryWrapper<QuestionFavourite> questionFavouriteQueryWrapper = new QueryWrapper<>();
        questionFavouriteQueryWrapper.eq("questionId", questionId);
        return (int) this.count(questionFavouriteQueryWrapper);
    }
}
