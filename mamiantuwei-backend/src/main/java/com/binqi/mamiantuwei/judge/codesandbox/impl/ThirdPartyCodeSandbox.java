package com.binqi.mamiantuwei.judge.codesandbox.impl;

import com.binqi.mamiantuwei.judge.codesandbox.CodeSandbox;
import com.binqi.mamiantuwei.judge.codesandbox.model.ExecuteCodeRequest;
import com.binqi.mamiantuwei.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
