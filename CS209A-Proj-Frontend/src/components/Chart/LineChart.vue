<template>
  <div ref="chartRef" style="width: 100%; height: 400px;"></div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

// 定义传入的 props
const props = defineProps({
  chartTitle: {
    type: String,
    required: true,
  },
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

// 初始化图表
const initChart = () => {
  nextTick(() => {
    if (chartRef.value) {
      chartInstance = echarts.init(chartRef.value);

      const options = {
        title: {
          text: props.chartTitle,  // 使用自定义的图表标题
          left: 'center',
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          data: props.chartData.map((item) => item.name),
          axisLabel: {
            rotate: 45,  // 旋转标签
            margin: 10,  // 调整标签与轴的距离
          }
        },
        yAxis: {
          name: props.yAxisLabel,  // 使用自定义的 y 轴标签
          type: 'value',
          nameTextStyle: {
            fontSize: 14, // 设置y轴名称的字体大小
          },
          axisLabel: {
            formatter: '{value}', // 格式化y轴标签
          }
        },
        series: [
          {
            name: props.chartTitle,
            type: 'line',  // 使用 line 类型
            data: props.chartData.map((item) => item.value),
            smooth: true,  // 平滑曲线
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
        chartInstance.setOption({
          xAxis: {
            data: newData.map((item) => item.name),
          },
          series: [
            {
              data: newData.map((item) => item.value),
            },
          ],
        });
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
</style>
