package com.binqi.mamiantuweisandbox;

import com.binqi.mamiantuweisandbox.model.ExecuteCodeRequest;
import com.binqi.mamiantuweisandbox.model.ExecuteCodeResponse;

import org.springframework.stereotype.Component;


@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}


