package com.binqi.mamiantuwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binqi.mamiantuwei.common.ErrorCode;
import com.binqi.mamiantuwei.exception.BusinessException;
import com.binqi.mamiantuwei.exception.ThrowUtils;
import com.binqi.mamiantuwei.mapper.QuestionThumbMapper;
import com.binqi.mamiantuwei.model.entity.Question;
import com.binqi.mamiantuwei.model.entity.QuestionThumb;
import com.binqi.mamiantuwei.model.entity.User;
import com.binqi.mamiantuwei.service.QuestionService;
import com.binqi.mamiantuwei.service.QuestionThumbService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb>
        implements QuestionThumbService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionThumbMapper questionThumbMapper;
    @Override
    public int doQuestionThumb(long questionId, User loginUser) {
        //题目要存在
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionThumbService questionThumbService = (QuestionThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionThumbService.doQuestionThumbInner(userId, questionId);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionThumbInner(long userId, long questionId) {
        QuestionThumb questionThumb = new QuestionThumb();
        questionThumb.setUserId(userId);
        questionThumb.setQuestionId(questionId);
        //判断是否存在
        QueryWrapper<QuestionThumb> queryWrapper = new QueryWrapper<>(questionThumb);
        QuestionThumb oldQuestionThumb = this.getOne(queryWrapper);
        boolean result;
        //已经点赞了删除点赞
        if(oldQuestionThumb != null){
            result = this.remove(queryWrapper);
            if(result){
                return -1;
            }else{
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }else{
            result = this.save(questionThumb);
            if(result){
                return 1;
            }else{
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

    @Override
    public int getQuestionThumbCount(long questionId) {
        // 查询条件：题目id）
        QueryWrapper<QuestionThumb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId", questionId);
        return Math.toIntExact(questionThumbMapper.selectCount(queryWrapper));
    }

    @Override
    public boolean isQuestionThumb(long questionId, User loginUser) {
        QueryWrapper<QuestionThumb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId", questionId).eq("userId", loginUser.getId());
        long count = this.count(queryWrapper);
        return count > 0;
    }
}
