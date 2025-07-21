package com.binqi.mamiantuwei.judge;

import com.binqi.mamiantuwei.model.entity.QuestionSubmit;
import com.binqi.mamiantuwei.service.QuestionCodeService;
import com.binqi.mamiantuwei.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 判题服务实现类
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionCodeService questionCodeService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type}")
    private String judgeType;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        return null;
    }
}
