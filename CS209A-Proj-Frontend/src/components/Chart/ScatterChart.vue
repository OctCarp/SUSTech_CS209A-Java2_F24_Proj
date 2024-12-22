<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 500px;"></div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, watch } from 'vue';
import * as echarts from 'echarts';
import 'echarts-gl'; // 引入echarts-gl来支持3D图表

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
      var symbolSize = 5;

      const options = {
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            const data = params.data;
            return `用户名誉: ${data[0]}<br/>答案长度: ${data[1]}<br/>答案质量: ${['POOR', 'FAIR', 'GOOD', 'EXCELLENT'][data[2]]}`;
          },
        },
        grid3D: {
          boxWidth: 200,
          boxHeight: 200,
          boxDepth: 200,
          viewControl: {
            projection: 'perspective',
            panMouseButton: 'left',
            zoomSensitivity: 5,
            rotateSensitivity: 3,
            distance: 500,
            alpha: 10,
            beta: 50,
          },
        },
        xAxis3D: {
          type: 'value',
          name: '用户名誉',
          nameLocation: 'middle',
          nameTextStyle: {
            fontSize: 14,
          },
          axisLine: {
            lineStyle: {
              color: '#ccc',
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#ccc',
            },
          },
        },
        yAxis3D: {
          type: 'value',
          name: '答案长度',
          nameLocation: 'middle',
          nameTextStyle: {
            fontSize: 14,
          },
          axisLine: {
            lineStyle: {
              color: '#ccc',
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#ccc',
            },
          },
        },
        zAxis3D: {
          type: 'value',
          name: '答案质量',
          nameLocation: 'middle',
          nameTextStyle: {
            fontSize: 14,
          },
          axisLine: {
            lineStyle: {
              color: '#ccc',
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#ccc',
            },
          },
          axisLabel: {
            formatter: function (value) {
              // 映射数字为相应的质量等级
              const qualityLevel = ['POOR', 'FAIR', 'GOOD', 'EXCELLENT'];
              return qualityLevel[value] || '';
            },
          },
        },
        dataset: {
          dimensions: [
            'ownerReputation',
            'answerLength',
            'qualityLevelNum',
          ],
          source: props.chartData,
        },
        series: [
          {
            type: 'scatter3D',
            symbolSize: symbolSize,
            encode: {
              x: 'ownerReputation',
              y: 'answerLength',
              z: 'qualityLevelNum',
            },
            itemStyle: {
              color: '#FF6347', // 默认颜色
              opacity: 0.6, // 设置透明度，增加视觉效果
            },
            emphasis: {
              itemStyle: {
                color: '#FF4500', // 鼠标悬停时的颜色
                opacity: 1,
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
          dataset: {
            dimensions: [
              'ownerReputation',
              'answerLength',
              'qualityLevelNum',
            ],
            source: props.chartData,
          },
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
