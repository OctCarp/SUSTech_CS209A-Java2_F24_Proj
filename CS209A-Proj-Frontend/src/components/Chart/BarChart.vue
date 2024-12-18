<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

// 定义传入的 props
const props = defineProps({
  xAxisLabel: {
    type: String,
    required: true,
  },
  yAxisLabel: {
    type: String,
    required: true,
  },
  chartData: {
    type: Array,
    required: true,
  },
});

const chartRef = ref(null);
let chartInstance = null;

const initChart = () => {
  nextTick(() => {
    if (chartRef.value) {
      chartInstance = echarts.init(chartRef.value);

      const options = {
        tooltip: {
          trigger: 'axis',
        },
        grid: {
          left: '20%', // 设置图表左边距
          right: '20%', // 设置图表右边距
          bottom: '50%', // 设置图表底边距，给横坐标更多空间
        },
        xAxis: {
          name: props.xAxisLabel, // 使用自定义的 x 轴标签
          type: 'category',
          data: props.chartData.map((item) => item.name),
          axisLabel: {
            rotate: 70, // 旋转坐标轴标签，避免重叠
            margin: 10,  // 调整标签与坐标轴的间距
            interval: 0, // 每个标签都显示
            fontSize: 12,  // 减小字体大小
          },
        },
        yAxis: {
          name: props.yAxisLabel, // 使用自定义的 y 轴标签
          type: 'value',
          nameTextStyle: {
            fontSize: 12, // 设置y轴名称的字体大小
          },
          axisLabel: {
            formatter: '{value}', // 格式化y轴标签
          }
        },
        series: [
          {
            name: props.chartTitle,
            type: 'bar',
            data: props.chartData.map((item) => item.value),
            label: {
              show: true,  // 显示每个柱子的值
              position: 'top',  // 数值显示在柱子顶部
              fontSize: 10, // 字体大小
              color: '#666',  // 字体颜色
            },
          },
        ],
      };

      chartInstance.setOption(options);
    }
  });
};

// 初始化图表
onMounted(() => {
  initChart();
});

// 监听数据变化，更新图表
watch(
    () => props.chartData,
    (newData) => {
      if (chartInstance) {
        const options = {
          xAxis: {
            data: newData.map((item) => item.name),
          },
          series: [
            {
              data: newData.map((item) => item.value),
            },
          ],
        };
        chartInstance.setOption(options);
      }
    }
);

// 在窗口大小变化时重新调整图表
window.addEventListener('resize', () => {
  if (chartInstance) {
    chartInstance.resize();
  }
});
</script>

<style scoped>
/* 可以为图表容器添加样式 */
.chart-container {
  /* 可以为图表容器添加样式 */
}
</style>
