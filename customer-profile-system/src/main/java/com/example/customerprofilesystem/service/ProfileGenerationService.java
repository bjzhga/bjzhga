package com.example.customerprofilesystem.service;

import com.example.customerprofilesystem.model.Customer;
import com.example.customerprofilesystem.model.CustomerTag;
import com.example.customerprofilesystem.repository.CustomerRepository;
import com.example.customerprofilesystem.repository.CustomerTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smile.clustering.KMeans;
import smile.math.MathEx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProfileGenerationService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerTagRepository customerTagRepository;

    @Autowired
    private LLMService llmService;

    // 定时任务，每天凌晨2点执行。@Scheduled注解需要你在主应用类上加 @EnableScheduling
//    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void generateAllProfiles() {
        System.out.println("开始执行每日客户画像生成任务...");
        List<Customer> customers = customerRepository.findAll();

        // 1. 先清除所有旧的、非手动的标签
        for(Customer customer : customers) {
            // 这里用流式操作过滤并删除，避免在循环中直接修改集合
            List<CustomerTag> tagsToDelete = customer.getTags().stream()
                    .filter(tag -> !"MANUAL".equalsIgnoreCase(tag.getTagSource()))
                    .collect(Collectors.toList());
            customer.getTags().removeAll(tagsToDelete);
            customerTagRepository.deleteAll(tagsToDelete);
        }
        customerRepository.saveAllAndFlush(customers);

        // 2. 生成规则标签
        generateRuleBasedTags(customers);

        // 3. 生成算法标签 (聚类)
        generateClusteringTags(customers);

        // 4. 生成LLM标签 (由于API调用耗时，可以一个个来)
        for(Customer customer : customers) {
            generateLLMTags(customer);
        }

        System.out.println("每日客户画像生成任务完成！");
    }

    private void generateRuleBasedTags(List<Customer> customers) {
        System.out.println("正在生成规则标签...");
        for (Customer customer : customers) {
            List<CustomerTag> newTags = new ArrayList<>();
            // 规则1：高净值客户
            if (customer.getAssetScale() != null && customer.getAssetScale().compareTo(new BigDecimal("100")) >= 0) {
                newTags.add(new CustomerTag(customer, "高净值潜力", "RULE"));
            }
            // 规则2：投资新手
            if (customer.getInvestmentExperience() != null && customer.getInvestmentExperience() <= 2) {
                newTags.add(new CustomerTag(customer, "投资新手", "RULE"));
            }
            // 规则3：风险偏好
            if (customer.getRiskProfile() != null) {
                String riskTag = "风险偏好-" + customer.getRiskProfile().getRiskLevel();
                newTags.add(new CustomerTag(customer, riskTag, "RULE"));
            }
            customer.getTags().addAll(newTags);
        }
        customerRepository.saveAll(customers);
    }

    private void generateClusteringTags(List<Customer> customers) {
        System.out.println("正在生成算法聚类标签...");
        // 准备数据：使用资产规模和投资经验作为特征
        double[][] data = customers.stream()
                .map(c -> new double[]{
                        c.getAssetScale() != null ? c.getAssetScale().doubleValue() : 0.0,
                        c.getInvestmentExperience() != null ? (double) c.getInvestmentExperience() : 0.0
                })
                .toArray(double[][]::new);

        // 数据标准化 (很重要，因为两个特征的量纲不同)
        MathEx.standardize(data);

        // 使用K-Means算法，聚成3类
        KMeans kmeans = KMeans.fit(data, 3);

        // 为每个客户打上聚类标签
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            int clusterId = kmeans.y[i];
            customer.getTags().add(new CustomerTag(customer, "客群-" + mapClusterIdToName(clusterId, kmeans, data), "ALGORITHM"));
        }
        customerRepository.saveAll(customers);
    }

    // 辅助方法：给聚类结果一个有意义的名字
    private String mapClusterIdToName(int clusterId, KMeans model, double[][] originalData) {
        // 简单地通过类簇中心点的特征值来命名
        double centroidAsset = model.centroids[clusterId][0]; // 标准化后的资产
        double centroidExp = model.centroids[clusterId][1]; // 标准化后的经验

        // 这是一个简化的逻辑，你可以设计的更复杂
        if (centroidAsset > 0.5 && centroidExp > 0.5) {
            return "资深高价值型";
        } else if (centroidAsset < -0.5 && centroidExp < -0.5) {
            return "新手探索型";
        } else {
            return "稳健成长型";
        }
    }


    private void generateLLMTags(Customer customer) {
        System.out.println("正在为客户 " + customer.getName() + " 生成LLM标签...");
        String summary = llmService.generateCustomerSummary(customer);
        if (summary != null && !summary.isEmpty()) {
            // 将整个摘要作为一个特殊的标签存起来
            customer.getTags().add(new CustomerTag(customer, "投资摘要:" + summary, "LLM"));
            customerRepository.save(customer);
        }
    }
}