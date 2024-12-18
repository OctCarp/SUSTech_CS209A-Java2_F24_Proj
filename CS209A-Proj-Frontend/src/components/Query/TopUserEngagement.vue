<template>
  <div class="top-topics">
    <h1>高用户参与度话题</h1>

    <div class="select-container">
      <v-select
          v-model="numTopics"
          :items="options"
          label="选择显示话题个数"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="topic-select"
      />

      <v-text-field
          v-model="minReputation"
          label="最低声望值(默认为0)"
          variant="outlined"
          color="primary"
          placeholder="0"
          class="reputation-input"
      ></v-text-field>

      <v-btn @click="fetchData" :disabled="!numTopics" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <div class="chart-item">
        <div class="chart-title-container">
          <h2>{{ chartTitle }}</h2>
        </div>
        <BarChart
            :xAxisLabel="xAxisLabel"
            :yAxisLabel="yAxisLabel"
            :chartData="data"
        />
      </div>
    </div>

    <div v-if="shouldUpdate" class="toggle-button-container">
      <v-btn @click="collapse" variant="text">
        <v-icon left>mdi-chevron-up</v-icon>
        收起
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import BarChart from '../Chart/BarChart.vue';
import {fetchTopTopicEngagementData} from "@/services/api.js";

const shouldUpdate = ref(false);
const numTopics = ref("");
const minReputation = ref(0);

const chartTitle = ref(`用户参与度最高的${numTopics.value}个话题`);
const xAxisLabel = ref('话题');
const yAxisLabel = ref('用户参与度');

let data = ref([]);
const chartData = ref([
  { name: '面向对象编程', value: 25 },
  { name: 'Spring 框架', value: 20 },
  { name: '异常处理', value: 15 },
  { name: '并发与多线程', value: 12 },
  { name: '流与 Lambda', value: 10 },
  { name: 'Hibernate / JPA', value: 8 },
  { name: '设计模式', value: 7 },
  { name: 'JVM 内部机制', value: 5 },
  { name: '垃圾回收', value: 4 },
  { name: 'Git 和版本控制', value: 3 },
]);

const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
  { value: 20, label: '20个' },
];

// 计算过滤后的数据（根据用户选择的数量进行过滤）
const filteredChartData = computed(() => {
  if (!numTopics.value) return []; // 如果 numTopics 为空，返回空数组

  // 排序数据：按频率从高到低
  const sortedData = [...chartData.value].sort((a, b) => b.value - a.value);

  // 根据用户选择的数量截取数据
  return sortedData.slice(0, numTopics.value);
});

// 查询按钮的点击事件，显示数据
const fetchData = async () => {
  shouldUpdate.value = true;
  data.value = filteredChartData.value;
  chartTitle.value = `用户参与度最高的${numTopics.value}个话题`;

  // try {
  //   const response = await fetchTopTopicEngagementData(numTopics.value, minReputation.value);
  //   data.value = response.data.sort((a, b) => b.reputation - a.reputation);
  // } catch (error) {
  //   console.error('获取数据失败:', error);
  // }
};

// 收起按钮的点击事件，恢复默认值并隐藏内容
const collapse = () => {
  numTopics.value = "";
  minReputation.value = 0;
  shouldUpdate.value = false;
};
</script>

<style scoped>
.top-topics {
  position: relative;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.select-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.topic-select {
  width: 100px;
  margin-right: 20px;
}

.reputation-input {
  margin-right: 20px;
}

.query-button {
  margin-top: 5px;
}

.toggle-button-container {
  position: absolute;
  bottom: 0;
  right: 0;
  z-index: 10;
}

.chart-container {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.chart-item {
  flex: 1;
  max-width: 60%;
  min-width: 400px;
  height: 400px;
  box-sizing: border-box;
}

.chart-title-container {
  display: flex;
  justify-content: center;
}

@media (max-width: 1200px) {
  .chart-container {
    flex-direction: column;
    gap: 10px;
  }

  .chart-item {
    max-width: 100%;
    min-width: 100%;
  }
}
</style>