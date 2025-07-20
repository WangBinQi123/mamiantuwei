package com.binqi.mamiantuwei.controller;

import com.binqi.mamiantuwei.common.BaseResponse;
import com.binqi.mamiantuwei.common.ErrorCode;
import com.binqi.mamiantuwei.common.ResultUtils;
import com.binqi.mamiantuwei.exception.BusinessException;
import com.binqi.mamiantuwei.model.dto.questionthumb.QuestionThumbAddRequest;
import com.binqi.mamiantuwei.model.entity.User;
import com.binqi.mamiantuwei.service.QuestionThumbService;
import com.binqi.mamiantuwei.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question/thumb")
public class QuestionThumbController {

    @Resource
    private QuestionThumbService questionThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionThumbAddRequest questionThumbAddRequest,
                                         HttpServletRequest request){
        if(questionThumbAddRequest == null || questionThumbAddRequest.getQuestionId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //登录了才能点赞
        final User loginUser = userService.getLoginUser(request);
        //获取题目id
        long questionId = questionThumbAddRequest.getQuestionId();
        int result = questionThumbService.doQuestionThumb(questionId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取题目点赞数
     *
     * @param questionId 题目id
     * @return 题目点赞数
     */
    @GetMapping("/get/count")
    public BaseResponse<Integer> getThumbCount(@RequestParam("questionId") long questionId){
        if(questionId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int count = questionThumbService.getQuestionThumbCount(questionId);
        return ResultUtils.success(count);
    }

    /**
     * 判断用户是否点赞题目
     *
     * @param questionId 题目id
     * @param request
     * @return 是否点赞
     */
    @GetMapping("/get/check")
    public BaseResponse<Boolean> checkThumb(@RequestParam("questionId") long questionId,
                                            HttpServletRequest request){
        if(questionId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        boolean isThumb = questionThumbService.isQuestionThumb(questionId, loginUser);
        return ResultUtils.success(isThumb);
    }
}
