package com.binqi.mamiantuwei.judge.codesandbox;

import com.binqi.mamiantuwei.judge.codesandbox.model.ExecuteCodeRequest;
import com.binqi.mamiantuwei.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 代码沙箱执行代码接口
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
