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
          text: props.chartTitle, // 使用自定义的图表标题
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)',
        },
        series: [
          {
            name: props.chartTitle,
            type: 'pie',  // 使用 pie 类型
            radius: '50%',
            data: props.chartData.map((item) => ({
              value: item.value,
              name: item.name,
            })),
            label: {
              normal: {
                formatter: '{b}', // 格式化标签内容
                fontSize: 12,  // 标签字体大小
              }
            },
            labelLine: {
              normal: {
                length: 20,  // 标签线的长度
                lineStyle: {
                  width: 3,  // 标签线的宽度
                },
              }
            }
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

</style>
