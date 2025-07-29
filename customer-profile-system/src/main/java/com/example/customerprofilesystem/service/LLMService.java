package com.example.customerprofilesystem.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.customerprofilesystem.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class LLMService {

    @Value("${llm.api.key}")
    private String apiKey;

    public String generateCustomerSummary(Customer customer) {
        try {
            Generation gen = new Generation();
            String customerData = buildCustomerDataString(customer);

            String prompt = String.format("你是一位资深的基金投顾。请根据以下客户的交易历史、持仓偏好和盈亏情况，用不超过100字总结他的投资风格和潜在的知识盲区。要求只输出总结内容，不要有任何额外的话。客户数据如下：\n%s", customerData);

            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build();

            GenerationParam param = GenerationParam.builder()
                    .model("qwen-turbo")
                    .messages(Arrays.asList(userMsg))
                    .apiKey(apiKey)
                    .build();

            GenerationResult result = gen.call(param);
            return result.getOutput().getChoices().get(0).getMessage().getContent();

        } catch (NoApiKeyException | InputRequiredException | ApiException e) {
            System.err.println("Error calling LLM Service: " + e.getMessage());
            return "大模型服务调用失败：" + e.getMessage();
        }
    }


    // buildCustomerDataString 方法保持不变
    private String buildCustomerDataString(Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("- 基础信息: 姓名=%s, 年收入=%s万, 总资产=%s万, 投资经验=%d年\n",
                customer.getName(), customer.getAnnualIncome(), customer.getAssetScale(), customer.getInvestmentExperience()));
        if (customer.getRiskProfile() != null) {
            sb.append(String.format("- 风险等级: %s\n", customer.getRiskProfile().getRiskLevel()));
        }
        if (customer.getTransactions() != null && !customer.getTransactions().isEmpty()) {
            String transactionsStr = customer.getTransactions().stream()
                    .limit(10) // 最多看最近10条
                    .map(t -> String.format("%s %s %s %.2f元", t.getTransactionDate(), t.getTransactionType(), t.getFundName(), t.getTransactionAmount()))
                    .collect(Collectors.joining("; "));
            sb.append("- 近期交易: ").append(transactionsStr).append("\n");
        }
        return sb.toString();
    }
}