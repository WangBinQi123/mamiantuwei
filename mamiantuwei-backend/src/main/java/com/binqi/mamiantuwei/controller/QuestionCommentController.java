package com.binqi.mamiantuwei.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binqi.mamiantuwei.common.BaseResponse;
import com.binqi.mamiantuwei.common.DeleteRequest;
import com.binqi.mamiantuwei.common.ErrorCode;
import com.binqi.mamiantuwei.common.ResultUtils;
import com.binqi.mamiantuwei.exception.BusinessException;
import com.binqi.mamiantuwei.exception.ThrowUtils;
import com.binqi.mamiantuwei.model.dto.questioncomment.QuestionCommentAddRequest;
import com.binqi.mamiantuwei.model.dto.questioncomment.QuestionCommentQueryRequest;
import com.binqi.mamiantuwei.model.dto.questioncomment.QuestionCommentUpdateRequest;
import com.binqi.mamiantuwei.model.entity.QuestionComment;
import com.binqi.mamiantuwei.model.entity.User;
import com.binqi.mamiantuwei.service.QuestionCommentService;
import com.binqi.mamiantuwei.service.QuestionThumbService;
import com.binqi.mamiantuwei.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question_comment")
public class QuestionCommentController {

    @Resource
    private QuestionCommentService questionCommentService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addQuestionComment(@RequestBody QuestionCommentAddRequest questionCommentAddRequest,
                                                 HttpServletRequest request){
        if(questionCommentAddRequest == null || questionCommentAddRequest.getQuestionId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(questionCommentAddRequest, questionComment);
        long result = questionCommentService.addComment(questionComment, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if(deleteRequest == null || deleteRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        boolean result = questionCommentService.deleteComment(deleteRequest.getId(), user);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestionComment(@RequestBody QuestionCommentUpdateRequest questionCommentUpdateRequest,
                                                       HttpServletRequest request){
        if(questionCommentUpdateRequest == null || questionCommentUpdateRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(questionCommentUpdateRequest, questionComment);
        boolean result = questionCommentService.updateComment(questionComment, user);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<QuestionComment> getQuestionCommentById(long id){
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionComment questionComment = questionCommentService.getById(id);
        ThrowUtils.throwIf(questionComment == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(questionComment);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionComment>> listQuestionCommentByPage(@RequestBody QuestionCommentQueryRequest questionCommentQueryRequest){
        if(questionCommentQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<QuestionComment> questionCommentPage = questionCommentService.getCommentPage(questionCommentQueryRequest);
        return ResultUtils.success(questionCommentPage);
    }
}
