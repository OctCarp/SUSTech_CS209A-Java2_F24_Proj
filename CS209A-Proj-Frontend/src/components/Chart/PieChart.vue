<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 300px;"></div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

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
        legend: {
          top: '10%',
          left: 'center'
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '60%'],
            avoidLabelOverlap: false,
            padAngle: 5,
            itemStyle: {
              borderRadius: 10
            },
            data: props.chartData.map((item) => ({
              value: item.value,
              name: item.name,
            })),
            label: {
              normal: {
                show: false,
                position: 'center',
                formatter: '{b}: {c} ({d}%)',  // 显示扇区名称、值、百分比
                fontSize: 12,
              }
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              normal: {
                length: 20,
                lineStyle: {
                  width: 3,
                },
              }
            }
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
        const options = {
          series: [
            {
              data: newData.map((item) => ({
                value: item.value,
                name: item.name,
              })),
            },
          ],
        };
        chartInstance.setOption(options);
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
