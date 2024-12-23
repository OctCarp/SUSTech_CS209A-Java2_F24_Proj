<template>
  <div class="top-errors">
    <h1>高频错误/异常</h1>

    <div class="select-container">
      <v-select
          v-model="numBugs"
          :items="options"
          label="选择错误/异常个数"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="topic-select"
      />

      <v-btn @click="fetchData" :disabled="!numBugs" size="large" color="primary" class="query-button">
        <v-icon left>mdi-magnify</v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <div class="chart-item">
        <div class="chart-title-container">
          <h2>{{ errorChartTitle }}</h2>
        </div>
        <BarChart
            :xAxisLabel="errorXAxisLabel"
            :yAxisLabel="yAxisLabel"
            :chartData="errorData"
        />
      </div>
      <div class="chart-item">
        <div class="chart-title-container">
          <h2>{{ exceptionChartTitle }}</h2>
        </div>
        <BarChart
            :xAxisLabel="exceptionXAxisLabel"
            :yAxisLabel="yAxisLabel"
            :chartData="exceptionData"
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
import BarChart from "@/components/Chart/BarChart.vue";
import {fetchTopBugData} from "@/services/api.js";

let shouldUpdate = ref(false);
const numBugs = ref('');

const errorChartTitle = ref(`最高频的${numBugs.value}个错误`);
const exceptionChartTitle = ref(`最高频的${numBugs.value}个异常`);
const errorXAxisLabel = ref('错误');
const exceptionXAxisLabel = ref('异常');
const yAxisLabel = ref('频率');

let errorData = ref([]);
let exceptionData = ref([]);

const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
];

const fetchData = async () => {
  shouldUpdate.value = true;
  errorChartTitle.value = `最高频的${numBugs.value}个错误`;
  exceptionChartTitle.value = `最高频的${numBugs.value}个异常`;

  try {
    const errorType = 'ERROR';
    const exceptionType = 'EXCEPTION';

    const errorResponse = await fetchTopBugData(numBugs.value, errorType);
    const exceptionResponse = await fetchTopBugData(numBugs.value, exceptionType);
    if (Array.isArray(exceptionResponse) && Array.isArray(errorResponse)) {
      errorData.value = errorResponse.map(item => ({
        name: item.bugName.replace(/(Error|Exception)$/, ''),  // 裁剪掉 "Error" 或 "Exception"
        value: item.bugFrequency,
        type: item.bugType,
      })).sort((a, b) => b.value - a.value);
      exceptionData.value = exceptionResponse.map(item => ({
        name: item.bugName.replace(/(Error|Exception)$/, ''),  // 裁剪掉 "Error" 或 "Exception"
        value: item.bugFrequency,
        type: item.bugType,
      })).sort((a, b) => b.value - a.value);
    } else {
      console.error("返回的数据格式不正确", errorResponse, exceptionResponse);
      errorData.value = [];
      exceptionData.value = [];
    }
  } catch (error) {
    console.error('获取数据失败:', error);
    errorData.value = [];
    exceptionData.value = [];
  }
};

const collapse = () => {
  shouldUpdate.value = false;
  numBugs.value = '';
  errorData.value = [];
  exceptionData.value = [];
};
</script>

<style scoped>
.top-errors {
  position: relative;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.select-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.topic-select {
  width: 120px;
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
  flex-direction: column;
  justify-content: center;
  gap: 20px;
}

.chart-item {
  max-width: 100%;
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
