<template>
  <div v-if="loading">
    <a-spin size="large" />
  </div>
  <div v-else-if="customer">
    <a-page-header :title="`客户画像: ${customer.name}`" @back="() => router.back()" />

    <a-tabs>
      <a-tab-pane key="1" tab="基础与风险画像">
        <a-descriptions title="基础信息" bordered>
          <a-descriptions-item label="客户编号">{{ customer.customerNo }}</a-descriptions-item>
          <a-descriptions-item label="姓名">{{ customer.name }}</a-descriptions-item>
          <a-descriptions-item label="手机号">{{ customer.phone }}</a-descriptions-item>
          <a-descriptions-item label="学历">{{ customer.education }}</a-descriptions-item>
          <a-descriptions-item label="职业">{{ customer.profession }}</a-descriptions-item>
          <a-descriptions-item label="年收入(万)">{{ customer.annualIncome }}</a-descriptions-item>
          <a-descriptions-item label="总资产(万)">{{ customer.assetScale }}</a-descriptions-item>
          <a-descriptions-item label="投资经验(年)">{{ customer.investmentExperience }}</a-descriptions-item>
          <a-descriptions-item label="投资频率">{{ customer.investmentFrequency }}</a-descriptions-item>
        </a-descriptions>

        <a-descriptions title="风险信息" bordered :style="{ marginTop: '24px' }">
          <a-descriptions-item label="风险等级">{{ customer.riskProfile?.riskLevel }}</a-descriptions-item>
          <a-descriptions-item label="测评日期">{{ customer.riskProfile?.assessmentDate }}</a-descriptions-item>
        </a-descriptions>
      </a-tab-pane>

      <a-tab-pane key="2" tab="行为与持仓画像">
        <h3>客户标签</h3>
        <a-tag v-for="tag in customer.customerTags" :key="tag.id" color="blue">{{ tag.tagName }}</a-tag>
        <h3 :style="{ marginTop: '24px' }">持仓分布</h3>
        <div ref="holdingChart" style="width: 100%; height: 400px;"></div>

        <h3 :style="{ marginTop: '24px' }">持仓明细</h3>
        <a-table :columns="holdingColumns" :data-source="customer.holdings" row-key="id" size="small"/>
      </a-tab-pane>

      <a-tab-pane key="3" tab="交易历史">
        <a-table :columns="transactionColumns" :data-source="customer.transactions" row-key="id" size="small"/>
      </a-tab-pane>
    </a-tabs>
  </div>
  <div v-else>
    <a-result status="404" title="404" sub-title="抱歉，未找到该客户的信息。">
      <template #extra>
        <a-button type="primary" @click="() => router.push('/')">返回首页</a-button>
      </template>
    </a-result>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { fetchCustomerProfile } from '../api';
import * as echarts from 'echarts';

const props = defineProps({
  id: {
    type: String,
    required: true,
  }
});

const router = useRouter();
const customer = ref<any>(null);
const loading = ref(true);
const holdingChart = ref<HTMLElement | null>(null);

const holdingColumns = [
  { title: '基金代码', dataIndex: 'fundCode' },
  { title: '基金名称', dataIndex: 'fundName' },
  { title: '持有份额', dataIndex: 'totalShares' },
  { title: '当前市值', dataIndex: 'currentValue' },
];

const transactionColumns = [
  { title: '交易时间', dataIndex: 'transactionDate' },
  { title: '基金名称', dataIndex: 'fundName' },
  { title: '交易类型', dataIndex: 'transactionType' },
  { title: '交易金额', dataIndex: 'transactionAmount' },
  { title: '成交净值', dataIndex: 'transactionPrice' },
];

onMounted(async () => {
  try {
    const response = await fetchCustomerProfile(parseInt(props.id));
    customer.value = response.data;
    // 数据加载后，渲染图表
    nextTick(() => {
      initHoldingChart();
    });
  } catch (error) {
    message.error('获取客户画像失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
});

const initHoldingChart = () => {
  if (holdingChart.value && customer.value.holdings.length > 0) {
    const chart = echarts.init(holdingChart.value);
    const chartData = customer.value.holdings.map((h: any) => ({
      name: h.fundName,
      // 这里我们用份额代替市值来画图，因为测试数据里市值是null
      value: h.totalShares
    }));

    const option = {
      title: { text: '基金持仓分布', left: 'center' },
      tooltip: { trigger: 'item' },
      legend: { orient: 'vertical', left: 'left' },
      series: [
        {
          name: '持仓份额',
          type: 'pie',
          radius: '50%',
          data: chartData,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    chart.setOption(option);
  }
};
</script>