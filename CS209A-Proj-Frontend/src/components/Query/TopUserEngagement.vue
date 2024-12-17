<template>
  <div class="top-topics">
    <h1>Top-{{ numTopics }} 热门用户参与 Topic</h1>

    <!-- 用户选择显示的最常见话题个数 -->
    <div class="select-container">
      <v-select
          v-model="numTopics"
          :items="options"
          label="选择显示话题个数"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="custom-select"
      />

      <!-- 查询按钮，点击查询才更新数据 -->
      <v-btn @click="fetchData" :disabled="!numTopics" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <!-- 父容器，使用 Flexbox 布局，只有在 numTopics 有值时才显示 -->
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

    <!-- 收起按钮，只有在 numTopics 有值时才显示 -->
    <div v-if="shouldUpdate" class="toggle-button-container">
      <v-btn @click="collapse" variant="text">
        <v-icon left>mdi-chevron-up</v-icon>  <!-- 收起图标 -->
        收起
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import BarChart from '../Chart/BarChart.vue';

// 自定义图表标题和轴标签
const chartTitle = ref('热门用户参与话题');
const xAxisLabel = ref('话题');
const yAxisLabel = ref('用户参与值');

// 图表数据
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

let data = ref([]);

// 用户选择显示的最常见话题个数，默认为 空字符串
const numTopics = ref("");

// 下拉框选项（确保值为数字）
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

// 控制数据更新的标志
const shouldUpdate = ref(false);

// 查询按钮的点击事件，显示数据
const fetchData = () => {
  shouldUpdate.value = true;
  data.value = filteredChartData.value; // 使用 data.value 更新
};

// 收起按钮的点击事件，恢复默认值并隐藏图表
const collapse = () => {
  numTopics.value = "";
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

.query-button {
  margin-top: 5px;
  margin-left: 20px;
}

/* 收起按钮容器放置在父容器的右下角 */
.toggle-button-container {
  position: absolute;
  bottom: 0;
  right: 0;
  z-index: 10;
}

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