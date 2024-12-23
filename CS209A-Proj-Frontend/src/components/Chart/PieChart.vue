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
            radius: ['40%', '70%'], // 设置饼图的内外半径
            center: ['50%', '60%'], // 将饼图居中
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
                // 格式化标签内容：显示名称、数值和百分比
                show: false,
                position: 'center',
                formatter: '{b}: {c} ({d}%)',  // 显示扇区名称、值、百分比
                fontSize: 12,  // 标签字体大小
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
.chart-container {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
