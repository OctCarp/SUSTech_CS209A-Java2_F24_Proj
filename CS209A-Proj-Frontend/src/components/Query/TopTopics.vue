<template>
  <div class="top-topics">
    <h1>高频话题</h1>

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
          v-model="numTopics"
          :items="options"
          label="选择显示话题个数"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="topic-select"
      />

      <v-btn @click="fetchData" :disabled="!numTopics" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <!-- 使用 BarChart 组件，并传入自定义配置 -->
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

    <v-dialog v-model="startDateDialog" max-width="290px">
      <v-date-picker v-model="startDate" @input="startDateDialog = false" :max="endDate" color="primary"></v-date-picker>
    </v-dialog>

    <v-dialog v-model="endDateDialog" max-width="290px">
      <v-date-picker v-model="endDate" @input="endDateDialog = false" :min="startDate" color="primary"></v-date-picker>
    </v-dialog>

  </div>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import BarChart from '../Chart/BarChart.vue';
import {fetchTopTopicData} from "@/services/api.js";

let shouldUpdate = ref(false);
const numTopics = ref('');

const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);

// 自定义图表标题和轴标签
const chartTitle = ref(`最高频的${numTopics.value}个话题`);
const xAxisLabel = ref('话题');
const yAxisLabel = ref('频率');

let data = ref([]);
const chartData = ref([
  { name: '面向对象编程', value: 25, date: '2024-01-01' },
  { name: 'Spring 框架', value: 20, date: '2024-02-01' },
  { name: '异常处理', value: 15, date: '2024-03-01' },
  { name: '并发与多线程', value: 12, date: '2024-04-01' },
  { name: '流与 Lambda', value: 10, date: '2024-05-01' },
  { name: 'Hibernate / JPA', value: 8, date: '2024-06-01' },
  { name: '设计模式', value: 7, date: '2024-07-01' },
  { name: 'JVM 内部机制', value: 5, date: '2024-08-01' },
  { name: '垃圾回收', value: 4, date: '2024-09-01' },
  { name: 'Git 和版本控制', value: 3, date: '2024-10-01' },
]);

const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
  { value: 20, label: '20个' },
];

// 根据选择的日期范围和话题数量进行过滤
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
  chartTitle.value = `最高频的${numTopics.value}个话题`;

  // try {
  //   const response = await fetchTopTopicData(numTopics.value, startDate.value, endDate.value);
  //   data.value = response.data.sort((a, b) => b.frequency - a.frequency);
  // } catch (error) {
  //   console.error('获取数据失败:', error);
  // }
};

// 收起按钮的点击事件，恢复默认值并隐藏图表
const collapse = () => {
  numTopics.value = '';
  startDate.value = null;
  endDate.value = null;
  shouldUpdate.value = false;
  data.value = [];
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
