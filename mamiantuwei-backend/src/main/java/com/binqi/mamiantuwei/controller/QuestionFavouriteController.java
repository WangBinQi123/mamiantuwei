package com.binqi.mamiantuwei.controller;

import com.binqi.mamiantuwei.common.BaseResponse;
import com.binqi.mamiantuwei.common.ErrorCode;
import com.binqi.mamiantuwei.common.ResultUtils;
import com.binqi.mamiantuwei.exception.BusinessException;
import com.binqi.mamiantuwei.model.dto.questionfavourite.QuestionFavouriteAddRequest;
import com.binqi.mamiantuwei.model.entity.Question;
import com.binqi.mamiantuwei.model.entity.User;
import com.binqi.mamiantuwei.service.QuestionFavouriteService;
import com.binqi.mamiantuwei.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/question_favourite")
public class QuestionFavouriteController {

    @Resource
    private QuestionFavouriteService questionFavouriteService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param questionFavouriteAddRequest
     * @param request
     * @return resultNum 本次收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doFavourite(@RequestBody QuestionFavouriteAddRequest questionFavouriteAddRequest,
                                             HttpServletRequest request){
        if(questionFavouriteAddRequest == null || questionFavouriteAddRequest.getQuestionId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        int result = questionFavouriteService.doQuestionFavourite(questionFavouriteAddRequest.getQuestionId(), loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取用户收藏的题目列表
     *
     * @param request
     * @return 收藏题目列表
     */
    @GetMapping("/my/list")
    public BaseResponse<List<Question>> listMyFavouriteQuestions(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        List<Question> questionList = questionFavouriteService.listMyFavouriteQuestions(loginUser);
        return ResultUtils.success(questionList);
    }

    /**
     * 判断当前用户是否已收藏题目
     *
     * @param questionId 题目id
     * @param request HTTP请求
     * @return 是否已收藏
     */
    @GetMapping("/check")
    public BaseResponse<Boolean> checkFavourite(@RequestParam("questionId") long questionId,
                                                HttpServletRequest request){
        if(questionId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        boolean isFavourite = questionFavouriteService.isQuestionFavourite(questionId, loginUser.getId());
        return ResultUtils.success(isFavourite);
    }

    /**
     * 获取题目收藏数
     *
     * @param questionId 题目id
     * @return 收藏数
     */
    @GetMapping("/count")
    public BaseResponse<Integer> getFavourite(@RequestParam("questionId") long questionId) {
        if (questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否已收藏
        int isFavourite = questionFavouriteService.getFavourite(questionId);
        return ResultUtils.success(isFavourite);
    }

}
