<template>
  <div>
    <h1>常见 Java 话题</h1>

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
    </div>

    <!-- 父容器，使用 Flexbox 布局 -->
    <div class="chart-container">
      <!-- 使用 BarChart 组件，并传入自定义配置 -->
      <div class="chart-item">
        <BarChart
            :chartTitle="chartTitle"
            :xAxisLabel="xAxisLabel"
            :yAxisLabel="yAxisLabel"
            :chartData="filteredChartData"
        />
      </div>

      <!-- 使用 PieChart 组件，并传入自定义配置 -->
<!--      <div class="chart-item">-->
<!--        <PieChart-->
<!--            :chartTitle="chartTitle"-->
<!--            :chartData="filteredChartData"-->
<!--        />-->
<!--      </div>-->

      <!-- 使用 LineChart 组件，并传入自定义配置 -->
<!--      <div class="chart-item">-->
<!--        <LineChart-->
<!--            :chartTitle="chartTitle"-->
<!--            :xAxisLabel="xAxisLabel"-->
<!--            :yAxisLabel="yAxisLabel"-->
<!--            :chartData="filteredChartData"-->
<!--        />-->
<!--      </div>-->
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import BarChart from './Chart/BarChart.vue';
import LineChart from './Chart/LineChart.vue';
import PieChart from './Chart/PieChart.vue';

// 自定义图表标题和轴标签
const chartTitle = ref('常见 Java 话题频率');
const xAxisLabel = ref('话题');
const yAxisLabel = ref('频率');

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

// 用户选择显示的最常见话题个数，默认为 10
const numTopics = ref(10);

// 下拉框选项（确保值为数字）
const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
  { value: 20, label: '20个' },
];

// 根据选择的个数过滤和排序数据
const filteredChartData = computed(() => {
  // 排序数据：按频率从高到低
  const sortedData = [...chartData.value].sort((a, b) => b.value - a.value);

  // 根据用户选择的数量截取数据
  return sortedData.slice(0, numTopics.value);
});
</script>

<style scoped>
/* 样式美化 */
h1 {
  text-align: center;
  margin-bottom: 20px;
}

.select-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.custom-select {
  width: 240px; /* 设置选择框宽度 */
  font-size: 16px;
}

.chart-container {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.chart-item {
  flex: 1; /* 每个图表组件占据 1/3 的空间 */
  max-width: 33%; /* 限制最大宽度为 33% */
  min-width: 400px; /* 最小宽度保证图表不太小 */
  height: 400px; /* 设置图表的固定高度 */
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
