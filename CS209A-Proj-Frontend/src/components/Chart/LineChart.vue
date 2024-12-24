<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 300px;"></div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';
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
          formatter: (params) => {
            const point = params[0];
            return `Reputation: ${point.data[0]}<br>Count: ${point.data[1]}`;
          },
        },
        grid: {
          left: '15%',
          right: '15%',
          bottom: '20%',
          top: '10%',
        },
        xAxis: {
          name: props.xAxisLabel,
          type: 'log', // 使用对数坐标
          logBase: 10, // 基数为 10
          nameTextStyle: {
            fontSize: 12,
          },
          axisLabel: {
            formatter: '{value}',
          },
        },
        yAxis: {
          name: props.yAxisLabel,
          type: 'value',
          nameTextStyle: {
            fontSize: 12,
          },
          axisLabel: {
            formatter: '{value}',
          },
        },
        dataZoom: [
          {
            type: 'slider',
            show: true,
            xAxisIndex: 0,
            start: 0, // 显示数据的起始百分比
            end: 0.01, // 显示数据的结束百分比
            bottom: 10,
          },
        ],
        series: [
          {
            name: 'User Count',
            type: 'line',
            data: props.chartData.map((item) => [item.reputation, item.count]),
            smooth: true,
            symbol: 'none', // 隐藏点标记
            sampling: 'average', // 数据采样以提高性能
            lineStyle: {
              width: 2,
            },
          },
        ],
      };
      chartInstance.setOption(options);
      chartInstance.resize();
    }
  });
};

onMounted(() => {
  initChart();
});

watch(
    () => props.chartData,
    (newData) => {
      if (chartInstance) {
        chartInstance.setOption({
          series: [
            {
              data: newData.map((item) => [item.reputation, item.count]),
            },
          ],
        });
      }
    }
);

window.addEventListener('resize', () => {
  if (chartInstance) {
    chartInstance.resize();
  }
});
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
