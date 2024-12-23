<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

const props = defineProps({
  chartData: {
    type: Array,
    required: true
  },
  labelData: {
    type: Array,
    required: true
  }
});

const chartRef = ref(null);
let chartInstance = null;

const initChart = () => {
  nextTick(() => {
    if (chartRef.value) {
      chartInstance = echarts.init(chartRef.value);

      const options = {
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            const xLabel = props.labelData[params.data[0]];
            const yLabel = props.labelData[params.data[1]];
            const correlation = params.data[2].toFixed(3);
            return `${xLabel} vs ${yLabel}<br/>相关性: ${correlation}`;
          }
        },
        grid: {
          left: '17%',
          bottom: '20%',
          top: '10%',
        },
        xAxis: {
          type: 'category',
          data: props.labelData,
          axisLabel: {
            interval: 0,  // 强制显示所有标签
            rotate: 30,  // 旋转45度
            fontSize: 10,
          },
          axisTick: {
            alignWithLabel: true,  // 确保刻度线与标签对齐
          },
        },
        yAxis: {
          type: 'category',
          axisLabel: {
            interval: 0,  // 强制显示所有标签
            fontSize: 10,
          },
          data: props.labelData,
        },
        visualMap: {
          min: -1,
          max: 1,
          calculable: true,
          orient: 'horizontal',
          left: 'center',
          bottom: '0',  // 设置距离底部为0，确保位于图表下方
        },
        series: [
          {
            name: '相关性',
            type: 'heatmap',
            data: props.chartData.map((item) => [
              item.x,
              item.y,
              item.value,
            ]),
            label: {
              show: true,
            },
            emphasis: {
              itemStyle: {
                color: '#f00',
              },
            },
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
              data: newData.map((item) => [
                item.x,
                item.y,
                item.value
              ]),
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
