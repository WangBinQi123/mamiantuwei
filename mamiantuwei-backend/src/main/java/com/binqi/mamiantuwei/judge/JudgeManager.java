package com.binqi.mamiantuwei.judge;

import com.binqi.mamiantuwei.judge.codesandbox.model.JudgeInfo;
import com.binqi.mamiantuwei.judge.strategy.DefaultJudgeStrategy;
import com.binqi.mamiantuwei.judge.strategy.JavaLanguageJudgeStrategy;
import com.binqi.mamiantuwei.judge.strategy.JudgeContext;
import com.binqi.mamiantuwei.judge.strategy.JudgeStrategy;
import com.binqi.mamiantuwei.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getSubmitLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}