<template>
  <div class="chart-container">
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
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
  isShowLabel: {
    type: Boolean,
    default: true,
  },
});

// 存储颜色映射
const colorMap = {
  'TOPIC': '#5470c6',
  'ERROR': '#ee6666',
  'EXCEPTION': '#fac858',
};

const chartRef = ref(null);
let chartInstance = null;

const initChart = () => {
  nextTick(() => {
    if (chartRef.value) {
      chartInstance = echarts.init(chartRef.value);

      // 根据 xAxisLabel 动态决定 legend 和 series 的 name 配置
      let legendData = [];
      let seriesName = '';
      let seriesData = [];

      if (props.xAxisLabel === '话题') {
        legendData = ['Topic'];
        seriesName = 'Topic';
        seriesData = props.chartData.map((item) => item.value);
      } else if (props.xAxisLabel === '错误') {
        legendData = ['Error'];
        seriesName = 'Error';
        seriesData = props.chartData.map((item) => item.value);
      } else if (props.xAxisLabel === '异常') {
        legendData = ['Exception'];
        seriesName = 'Exception';
        seriesData = props.chartData.map((item) => item.value);
      }

      const options = {
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            const param = params[0]; // 获取当前悬停的柱子数据
            const { name, value, dataIndex } = param;
            let type = props.chartData[dataIndex].type;

            // 根据 type 对 name 和 type 做不同的处理
            let formattedName = name;
            let formattedType = type.charAt(0).toUpperCase() + type.slice(1).toLowerCase(); // 首字母大写，后面的字母小写

            if (type === 'ERROR' || type === 'EXCEPTION') {
              // 对于 ERROR 或 EXCEPTION，修改 name 为原 name 加上格式化后的 type
              formattedName = `${name}${formattedType}`;
            }

            // 返回格式化后的 tooltip 内容
            return `${formattedName}<br/>Type: ${formattedType}<br/>Value: ${value}`;
          }
        },
        grid: {
          left: '20%',
          right: '20%',
          bottom: '40%',
        },
        xAxis: {
          name: props.xAxisLabel,
          type: 'category',
          data: props.chartData.map((item) => item.name),
          axisLabel: {
            rotate: 45,
            margin: 10,
            interval: 0,
            fontSize: 12,
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
          }
        },
        series: [
          {
            name: seriesName,
            type: 'bar',
            data: seriesData[0] || [], // 如果只有一个系列，使用单个数据项
            label: {
              show: props.isShowLabel,  // 显示每个柱子的值
              position: 'top',  // 数值显示在柱子顶部
              fontSize: 10,
              color: '#666',
            },
            itemStyle: {
              // 根据每个项的type来动态设置颜色
              color: (params) => {
                const type = props.chartData[params.dataIndex].type;
                return colorMap[type] || '#91cc75';
              },
            },
          },
        ],
      };

      chartInstance.setOption(options);
      chartInstance.resize();  // 确保图表在加载后立即自适应容器大小
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
          xAxis: {
            data: newData.map((item) => item.name),
          },
          series: [
            {
              data: newData.map((item) => item.value),
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
