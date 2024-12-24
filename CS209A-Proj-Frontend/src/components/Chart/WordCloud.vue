<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 200px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';
import 'echarts-wordcloud';

const props = defineProps({
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
          show: true,
          formatter: (params) => `name: ${params.name}</br>frequency: ${params.value}`,
        },
        grid: {
          left: '0%',
          right: '0%',
          bottom: '0%',
          top: '0%',
        },
        series: [
          {
            type: 'wordCloud',
            shape: 'circle',
            sizeRange: [15, 50],
            rotationRange: [-90, 90],
            rotationStep: 45,
            gridSize: 8,
            drawOutOfBound: false,
            textStyle: {
              fontFamily: 'sans-serif',
              fontWeight: 'bold',
              color: () => {
                return `rgb(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(
                    Math.random() * 255
                )})`;
              },
            },
            data: props.chartData.map((item) => ({
              name: item.name,
              value: item.value,
            })),
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
              data: newData.map((item) => ({
                name: item.name,
                value: item.value,
              })),
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
