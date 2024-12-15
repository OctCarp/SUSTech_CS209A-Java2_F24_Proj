<template>
  <div ref="chartRef" style="width: 100%; height: 400px;"></div>
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
        title: {
          text: '各项指标对比',
        },
        tooltip: {},
        radar: {
          indicator: [
            { name: '销量', max: 500 },
            { name: '质量', max: 500 },
            { name: '服务', max: 500 },
            { name: '成本', max: 500 },
            { name: '利润', max: 500 },
          ],
        },
        series: [
          {
            name: '指标数据',
            type: 'radar',  // 使用 radar 类型
            data: [
              {
                value: props.chartData.map((item) => item.value),
                name: '2023年',
              },
            ],
          },
        ],
      };

      chartInstance.setOption(options);
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
              data: [
                {
                  value: newData.map((item) => item.value),
                  name: '2023年',
                },
              ],
            },
          ],
        };
        chartInstance.setOption(options, true);
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

</style>