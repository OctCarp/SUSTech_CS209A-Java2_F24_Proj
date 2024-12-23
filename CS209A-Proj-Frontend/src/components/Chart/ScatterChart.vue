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
      const symbolSize = 5;

      const qualityColors = {
        EXCELLENT: '#91cc75', // 优秀
        GOOD: '#5470c6',      // 好
        FAIR: '#fac858',      // 中等
        POOR: '#ee6666',      // 差
      };

      // 为每个 qualityLevel 设置对应的系列
      const seriesData = {
        POOR: [],
        FAIR: [],
        GOOD: [],
        EXCELLENT: [],
      };

      // Convert responseSeconds from seconds to hours (3600 seconds in an hour)
      props.chartData.forEach(item => {
        const qualityLevel = item.qualityLevel;
        const responseHours = item.responseSeconds / 3600; // Convert to hours
        if (qualityColors[qualityLevel]) {
          seriesData[qualityLevel].push({
            value: [item.ownerReputation, item.answerLength, responseHours],
            itemStyle: { color: qualityColors[qualityLevel] },
            qualityLevel: qualityLevel,
          });
        }
      });

      const options = {
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            const data = params.data;
            return `用户名誉: ${data.value[0]}<br/>答案长度: ${data.value[1]}<br/>回复时间: ${data.value[2].toFixed(2)}小时<br/>答案质量: ${data.qualityLevel}`;
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
          name: '回复时间（小时）',
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
        dataset: {
          dimensions: [
            'ownerReputation',
            'answerLength',
            'responseSeconds',
          ],
          source: props.chartData.map(item => ({
            ...item,
            responseSeconds: item.responseSeconds / 3600, // Convert to hours
          })),
        },
        legend: {
          data: ['EXCELLENT', 'GOOD', 'FAIR', 'POOR'],
          top: '5%',
          left: 'center',
        },
        series: [
          {
            type: 'scatter3D',
            name: 'POOR',
            data: seriesData.POOR,
            symbolSize: symbolSize,
            encode: {
              x: 'ownerReputation',
              y: 'answerLength',
              z: 'responseSeconds',
            },
            itemStyle: {
              color: qualityColors.POOR,
              opacity: 0.6,
            },
            emphasis: {
              itemStyle: {
                opacity: 1,
              },
            },
          },
          {
            type: 'scatter3D',
            name: 'FAIR',
            data: seriesData.FAIR,
            symbolSize: symbolSize,
            encode: {
              x: 'ownerReputation',
              y: 'answerLength',
              z: 'responseSeconds',
            },
            itemStyle: {
              color: qualityColors.FAIR,
              opacity: 0.6,
            },
            emphasis: {
              itemStyle: {
                opacity: 1,
              },
            },
          },
          {
            type: 'scatter3D',
            name: 'GOOD',
            data: seriesData.GOOD,
            symbolSize: symbolSize,
            encode: {
              x: 'ownerReputation',
              y: 'answerLength',
              z: 'responseSeconds',
            },
            itemStyle: {
              color: qualityColors.GOOD,
              opacity: 0.6,
            },
            emphasis: {
              itemStyle: {
                opacity: 1,
              },
            },
          },
          {
            type: 'scatter3D',
            name: 'EXCELLENT',
            data: seriesData.EXCELLENT,
            symbolSize: symbolSize,
            encode: {
              x: 'ownerReputation',
              y: 'answerLength',
              z: 'responseSeconds',
            },
            itemStyle: {
              color: qualityColors.EXCELLENT,
              opacity: 0.6,
            },
            emphasis: {
              itemStyle: {
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
        const symbolSize = 5;

        const qualityColors = {
          EXCELLENT: '#91cc75', // 优秀
          GOOD: '#5470c6',      // 好
          FAIR: '#fac858',      // 中等
          POOR: '#ee6666',      // 差
        };

        // 为每个 qualityLevel 设置对应的系列
        const seriesData = {
          POOR: [],
          FAIR: [],
          GOOD: [],
          EXCELLENT: [],
        };

        // Convert responseSeconds from seconds to hours (3600 seconds in an hour)
        newData.forEach(item => {
          const qualityLevel = item.qualityLevel;
          const responseHours = item.responseSeconds / 3600; // Convert to hours
          if (qualityColors[qualityLevel]) {
            seriesData[qualityLevel].push({
              value: [item.ownerReputation, item.answerLength, responseHours],
              itemStyle: { color: qualityColors[qualityLevel] },
              qualityLevel: qualityLevel,
            });
          }
        });

        const options = {
          dataset: {
            dimensions: [
              'ownerReputation',
              'answerLength',
              'responseSeconds',
            ],
            source: newData.map(item => ({
              ...item,
              responseSeconds: item.responseSeconds / 3600, // Convert to hours
            })),
          },
          series: [
            {
              type: 'scatter3D',
              name: 'POOR',
              data: seriesData.POOR,
              symbolSize: symbolSize,
              encode: {
                x: 'ownerReputation',
                y: 'answerLength',
                z: 'responseSeconds',
              },
              itemStyle: {
                color: qualityColors.POOR,
                opacity: 0.6,
              },
              emphasis: {
                itemStyle: {
                  opacity: 1,
                },
              },
            },
            {
              type: 'scatter3D',
              name: 'FAIR',
              data: seriesData.FAIR,
              symbolSize: symbolSize,
              encode: {
                x: 'ownerReputation',
                y: 'answerLength',
                z: 'responseSeconds',
              },
              itemStyle: {
                color: qualityColors.FAIR,
                opacity: 0.6,
              },
              emphasis: {
                itemStyle: {
                  opacity: 1,
                },
              },
            },
            {
              type: 'scatter3D',
              name: 'GOOD',
              data: seriesData.GOOD,
              symbolSize: symbolSize,
              encode: {
                x: 'ownerReputation',
                y: 'answerLength',
                z: 'responseSeconds',
              },
              itemStyle: {
                color: qualityColors.GOOD,
                opacity: 0.6,
              },
              emphasis: {
                itemStyle: {
                  opacity: 1,
                },
              },
            },
            {
              type: 'scatter3D',
              name: 'EXCELLENT',
              data: seriesData.EXCELLENT,
              symbolSize: symbolSize,
              encode: {
                x: 'ownerReputation',
                y: 'answerLength',
                z: 'responseSeconds',
              },
              itemStyle: {
                color: qualityColors.EXCELLENT,
                opacity: 0.6,
              },
              emphasis: {
                itemStyle: {
                  opacity: 1,
                },
              },
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
