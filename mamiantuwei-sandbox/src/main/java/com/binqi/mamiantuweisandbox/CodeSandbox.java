package com.binqi.mamiantuweisandbox;

import com.binqi.mamiantuweisandbox.model.ExecuteCodeRequest;
import com.binqi.mamiantuweisandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
