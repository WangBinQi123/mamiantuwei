package com.binqi.mamiantuwei.judge.strategy;

import com.binqi.mamiantuwei.judge.codesandbox.model.JudgeInfo;
import com.binqi.mamiantuwei.model.dto.questionCode.JudgeCase;
import com.binqi.mamiantuwei.model.entity.QuestionCode;
import com.binqi.mamiantuwei.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private QuestionCode questionCode;

    private QuestionSubmit questionSubmit;

}
