package com.binqi.mamiantuwei.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binqi.mamiantuwei.common.ErrorCode;
import com.binqi.mamiantuwei.constant.CommonConstant;
import com.binqi.mamiantuwei.exception.BusinessException;
import com.binqi.mamiantuwei.exception.ThrowUtils;
import com.binqi.mamiantuwei.mapper.QuestionCodeMapper;
import com.binqi.mamiantuwei.model.dto.questionCode.QuestionQueryRequest;
import com.binqi.mamiantuwei.model.entity.QuestionCode;
import com.binqi.mamiantuwei.model.entity.User;
import com.binqi.mamiantuwei.model.vo.QuestionCodeVO;
import com.binqi.mamiantuwei.model.vo.UserVO;
import com.binqi.mamiantuwei.service.QuestionCodeService;
import com.binqi.mamiantuwei.service.UserService;
import com.binqi.mamiantuwei.utils.SqlUtils;
import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionCodeServiceImpl extends ServiceImpl<QuestionCodeMapper, QuestionCode>
        implements QuestionCodeService {

    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;
    @Override
    public void validQuestionCode(QuestionCode questionCode, boolean add) {
        if (questionCode == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = questionCode.getUserId();
        String title = questionCode.getTitle();
        String content = questionCode.getContent();
        String tags = questionCode.getTags();
        String answer = questionCode.getAnswer();
        String judgeCase = questionCode.getJudgeCase();
        String judgeConfig = questionCode.getJudgeConfig();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }



    @Override
    public QueryWrapper<QuestionCode> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        ;QueryWrapper<QuestionCode> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        Long userId = questionQueryRequest.getUserId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();


        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionCodeVO getQuestionCodeVO(QuestionCode questionCode, HttpServletRequest request) {
        QuestionCodeVO questionCodeVO = QuestionCodeVO.objToVo(questionCode);
        // 1. 关联查询用户信息
        Long userId = questionCode.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionCodeVO.setUserVO(userVO);
        return questionCodeVO;
    }

    @Override
    public Page<QuestionCodeVO> getQuestionCodeVOPage(Page<QuestionCode> questionCodePage, HttpServletRequest request) {
        List<QuestionCode> questionCodeList = questionCodePage.getRecords();
        Page<QuestionCodeVO> questionCodeVOPage = new Page<>(questionCodePage.getCurrent(), questionCodePage.getSize(), questionCodePage.getTotal());
        if (CollectionUtils.isEmpty(questionCodeList)) {
            return questionCodeVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionCodeList.stream().map(QuestionCode::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionCodeVO> questionVOList = questionCodeList.stream().map(questionCode -> {
            QuestionCodeVO questionCodeVO = QuestionCodeVO.objToVo(questionCode);
            Long userId = questionCode.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionCodeVO.setUserVO(userService.getUserVO(user));
            return questionCodeVO;
        }).collect(Collectors.toList());
        questionCodeVOPage.setRecords(questionVOList);
        return questionCodeVOPage;
    }
}
