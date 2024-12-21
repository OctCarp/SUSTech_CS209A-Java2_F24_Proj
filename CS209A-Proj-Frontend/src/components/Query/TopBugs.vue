<template>
  <div class="top-errors">
    <h1>高频故障</h1>

    <div class="select-container">
      <v-text-field
          v-model="startDateText"
          label="开始日期"
          prepend-inner-icon="mdi-calendar"
          readonly
          color="primary"
          @click="startDateDialog = true"
          class="date-picker"
      ></v-text-field>

      <v-text-field
          v-model="endDateText"
          label="结束日期"
          prepend-inner-icon="mdi-calendar"
          readonly
          color="primary"
          @click="endDateDialog = true"
          class="date-picker"
      ></v-text-field>

      <v-select
          v-model="numBugs"
          :items="options"
          label="选择显示错误个数"
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

    <v-dialog v-model="startDateDialog" max-width="290px">
      <v-date-picker v-model="startDate" @input="startDateDialog = false" :max="endDate" color="primary"></v-date-picker>
    </v-dialog>

    <v-dialog v-model="endDateDialog" max-width="290px">
      <v-date-picker v-model="endDate" @input="endDateDialog = false" :min="startDate" color="primary"></v-date-picker>
    </v-dialog>

  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import BarChart from "@/components/Chart/BarChart.vue";
import {fetchTopBugData} from "@/services/api.js";

let shouldUpdate = ref(false);
const numBugs = ref('');

const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);

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
    // 将 startDate 和 endDate 转换为 Unix 时间戳（毫秒）
    const startTimestamp = startDate.value ? startDate.value.getTime() : null;
    const endTimestamp = endDate.value ? endDate.value.getTime() : null;

    const errorType = 'ERROR';
    const exceptionType = 'EXCEPTION';

    const errorResponse = await fetchTopBugData(numBugs.value, errorType, startTimestamp, endTimestamp);
    const exceptionResponse = await fetchTopBugData(numBugs.value, exceptionType, startTimestamp, endTimestamp);
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

// 收起按钮的点击事件，恢复默认值并隐藏图表
const collapse = () => {
  shouldUpdate.value = false;
  numBugs.value = '';
  startDate.value = null;
  endDate.value = null;
  errorData.value = [];
  exceptionData.value = [];
};

// 更新选择日期的文本显示
watch(startDate, (newDate) => {
  startDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
watch(endDate, (newDate) => {
  endDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
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

.date-picker {
  width: 140px;
  margin-right: 20px;
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
