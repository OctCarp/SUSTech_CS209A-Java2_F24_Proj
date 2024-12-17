<template>
  <div class="top-errors">
    <h1>Top-{{ numBugs }} 高频 Error/Exception</h1>

    <!-- 用户选择显示的最常见错误个数 -->
    <div class="select-container">
      <!-- 日期选择表单 -->
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

      <!-- 用户选择错误个数 -->
      <v-select
          v-model="numBugs"
          :items="options"
          label="选择显示错误个数"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="topic-select"
      />

      <!-- 查询按钮，点击查询才更新数据 -->
      <v-btn @click="fetchData" :disabled="!numBugs" size="large" color="primary" class="query-button">
        <v-icon left>mdi-magnify</v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <!-- 使用 BarChart 组件，并传入自定义配置 -->
      <div class="chart-item">
        <BarChart
            :chartTitle="chartTitle"
            :xAxisLabel="xAxisLabel"
            :yAxisLabel="yAxisLabel"
            :chartData="data"
        />
      </div>
    </div>

    <!-- 收起按钮，只有在 numBugs 有值时才显示 -->
    <div v-if="shouldUpdate" class="toggle-button-container">
      <v-btn @click="collapse" variant="text">
        <v-icon left>mdi-chevron-up</v-icon>  <!-- 收起图标 -->
        收起
      </v-btn>
    </div>

    <!-- 日期选择对话框，选择开始日期 -->
    <v-dialog v-model="startDateDialog" max-width="290px">
      <v-date-picker v-model="startDate" @input="startDateDialog = false" :max="endDate" color="primary"></v-date-picker>
    </v-dialog>

    <!-- 日期选择对话框，选择结束日期 -->
    <v-dialog v-model="endDateDialog" max-width="290px">
      <v-date-picker v-model="endDate" @input="endDateDialog = false" :min="startDate" color="primary"></v-date-picker>
    </v-dialog>

  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import BarChart from "@/components/Chart/BarChart.vue";
import {fetchTopBugData} from "@/services/api.js";

// 自定义图表标题和轴标签
const chartTitle = ref('高频 Error/Exception');
const xAxisLabel = ref('错误类型');
const yAxisLabel = ref('频率');

// 错误数据（假设数据带有日期字段）
const chartData = ref([
  { name: 'NullPointerException', value: 25, date: '2024-01-01' },
  { name: 'ArrayIndexOutOfBoundsException', value: 20, date: '2024-02-01' },
  { name: 'IOException', value: 15, date: '2024-03-01' },
  { name: 'SQLException', value: 12, date: '2024-04-01' },
  { name: 'ClassNotFoundException', value: 10, date: '2024-05-01' },
  { name: 'FileNotFoundException', value: 8, date: '2024-06-01' },
  { name: 'RuntimeException', value: 7, date: '2024-07-01' },
  { name: 'NoSuchMethodException', value: 5, date: '2024-08-01' },
  { name: 'IllegalArgumentException', value: 4, date: '2024-09-01' },
  { name: 'NumberFormatException', value: 3, date: '2024-10-01' },
]);

let data = ref([]);
let shouldUpdate = ref(false);
const numBugs = ref('');
const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);

// 下拉框选项（确保值为数字）
const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
  { value: 20, label: '20个' },
];

// 更新选择日期的文本显示
watch(startDate, (newDate) => {
  startDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
watch(endDate, (newDate) => {
  endDateText.value = newDate ? newDate.toLocaleDateString() : '';
});

// 根据选择的日期范围和错误数量进行过滤
const filteredChartData = computed(() => {
  if (!numBugs.value) return []; // 如果 numBugs 为空，返回空数组

  // 排序数据：按频率从高到低
  const sortedData = [...chartData.value].sort((a, b) => b.value - a.value);

  // 根据用户选择的数量截取数据
  return sortedData.slice(0, numBugs.value);
});

// 查询按钮的点击事件，显示数据
const fetchData = async () => {
  shouldUpdate.value = true;
  data.value = filteredChartData.value; // 使用过滤后的数据

  // try {
  //   const response = await fetchTopBugData(numBugs.value, startDate.value, endDate.value);
  //   data.value = response.data.sort((a, b) => b.frequency - a.frequency);
  // } catch (error) {
  //   console.error('获取数据失败:', error);
  // }
};

// 收起按钮的点击事件，恢复默认值并隐藏图表
const collapse = () => {
  numBugs.value = '';
  startDate.value = null;
  endDate.value = null;
  shouldUpdate.value = false;
  data.value = []; // 清空图表数据
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

.date-picker {
  width: 140px; /* 控制日期输入框宽度 */
  margin-right: 20px;
}

.topic-select {
  width: 120px; /* 控制下拉框宽度 */
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

/* 图表部分样式 */
.chart-container {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.chart-item {
  flex: 1;
  max-width: 33%;
  min-width: 400px;
  height: 400px;
  box-sizing: border-box;
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
