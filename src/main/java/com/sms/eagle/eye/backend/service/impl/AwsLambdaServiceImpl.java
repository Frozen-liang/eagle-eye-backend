package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.service.AwsLambdaService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

@Service
public class AwsLambdaServiceImpl implements AwsLambdaService {

    private final LambdaClient lambdaClient;

    public AwsLambdaServiceImpl(LambdaClient lambdaClient) {
        this.lambdaClient = lambdaClient;
    }

    @Override
    public boolean invoke() {
        InvokeRequest invokeRequest = InvokeRequest.builder()
            .functionName("")
            .payload(SdkBytes.fromUtf8String(""))
            .build();
        lambdaClient.invoke(invokeRequest);
        return false;
    }
}