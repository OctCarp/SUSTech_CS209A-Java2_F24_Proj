<template>
  <div class="top-topics">
    <h1>答案质量</h1>

    <div class="select-container">
      <v-btn @click="fetchData" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate">
      <div class="stats">
        <div class="stat-card">
          <h4>答案总数</h4>
          <p>{{ data.length }}</p>
        </div>
      </div>

      <div class="chart-container">
        <div class="chart-item">
          <div class="chart-title-container">
            <h2>答案质量分布</h2>
          </div>
          <PieChart :chartData="pieChartData" />
        </div>

        <div class="chart-item">
          <div class="chart-title-container">
            <h2>答案质量相关性热力图</h2>
          </div>
          <HeatMap :chartData="heatMapData" :label-data="labels" />
        </div>

        <div class="chart-item scatter-chart">
          <div class="chart-title-container">
            <h2>答案质量散点图</h2>
          </div>
          <ScatterChart :chartData="scatterChartData" />
        </div>
      </div>
    </div>

    <div v-if="shouldUpdate" class="toggle-button-container">
      <v-btn @click="collapse" variant="text">
        <v-icon left>mdi-chevron-up</v-icon>
        收起
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import PieChart from "@/components/Chart/PieChart.vue";
import HeatMap from "@/components/Chart/HeatMap.vue";
import ScatterChart from "@/components/Chart/ScatterChart.vue";
import { fetchAnswerQualityData } from "@/services/api.js";

const pieChartData = ref([]);
const heatMapData = ref([]);
const scatterChartData = ref([]);
const labels = ['答案质量', '答案长度', '作答用户采纳率', '作答用户名誉', '作答时间'];
let shouldUpdate = ref(false);
let data = ref([]);

const fetchData = async () => {
  shouldUpdate.value = true;
  try {
    const response = await fetchAnswerQualityData();
    if (Array.isArray(response)) {
      data.value = response.map(item => {
        // 如果ownerAcceptRate为-1(表示从未被接受过),设置为0
        if (item.ownerAcceptRate === -1) {
          item.ownerAcceptRate = 0;
        }
        return item;
      }).filter(item =>
          item && item.qualityLevel &&
          item.answerLength !== undefined &&
          item.ownerAcceptRate !== undefined &&
          item.ownerReputation !== undefined &&
          item.responseSeconds !== undefined &&
          item.answerLength >= 0 &&
          item.ownerAcceptRate >= 0 &&
          item.ownerReputation >= 0 &&
          item.responseSeconds >= 0
      );
      updateStats();
    } else {
      console.error("返回的数据格式不正确", response);
      data.value = [];
    }
  } catch (error) {
    console.error('获取数据失败:', error);
    data.value = [];
  }
};

const updateStats = () => {
  const qualityCounts = { EXCELLENT: 0, GOOD: 0, FAIR: 0, POOR: 0 };
  let totalCount = 0;
  const fields = ["qualityLevelNum", "answerLength", "ownerAcceptRate", "ownerReputation", "responseSeconds"];

  if (data.value) {
    data.value.forEach(item => {
      if (item.qualityLevel in qualityCounts) {
        qualityCounts[item.qualityLevel]++;
        totalCount++;
      }
    });
    pieChartData.value = Object.keys(qualityCounts).map(key => ({
      name: key,
      value: qualityCounts[key],
    }));

    const qualityLevelMap = {
      EXCELLENT: 3,
      GOOD: 2,
      FAIR: 1,
      POOR: 0
    };

    data.value = data.value.map(item => ({
      ...item,
      qualityLevelNum: qualityLevelMap[item.qualityLevel]
    }));

    const correlations = {};

    // 计算每对字段之间的相关性
    fields.forEach(field1 => {
      correlations[field1] = {};
      fields.forEach(field2 => {
        const values1 = data.value.map(item => item[field1]);
        const values2 = data.value.map(item => item[field2]);

        correlations[field1][field2] = calculateCorrelation(values1, values2);
      });
    });

    heatMapData.value = Object.keys(correlations).map((label1, i) => {
      return Object.keys(correlations).map((label2, j) => {
        return {
          xlabel: label1,
          ylabel: label2,
          x: i,
          y: j,
          value: Number(correlations[label1][label2].toFixed(3))
        };
      });
    }).flat();

    scatterChartData.value = data.value.map(item => ({
      answerLength: item.answerLength,
      ownerReputation: item.ownerReputation,
      responseSeconds: item.responseSeconds,
      qualityLevel: item.qualityLevel,
    }));
  }
};

// 计算皮尔逊相关系数
const calculateCorrelation = (x, y) => {
  const n = x.length;
  const avgX = x.reduce((sum, val) => sum + val, 0) / n;
  const avgY = y.reduce((sum, val) => sum + val, 0) / n;

  const numerator = x.reduce((sum, xi, i) => sum + (xi - avgX) * (y[i] - avgY), 0);
  const denominator = Math.sqrt(
      x.reduce((sum, xi) => sum + Math.pow(xi - avgX, 2), 0) *
      y.reduce((sum, yi) => sum + Math.pow(yi - avgY, 2), 0)
  );

  return denominator === 0 ? 0 : numerator / denominator;
};

const collapse = () => {
  shouldUpdate.value = false;
  data.value = [];
};

</script>

<style scoped>
.top-topics {
  position: relative;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.select-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.query-button {
  margin-top: 5px;
  margin-left: auto;
  margin-right: auto;
}

.toggle-button-container {
  position: absolute;
  bottom: 0;
  right: 0;
  z-index: 10;
}

.stats {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 25px;
  margin-top: 30px;
  margin-bottom: 25px;
}

.stat-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-size: 18px;
  width: 150px;
}

.stat-card h4 {
  font-size: 16px;
  color: #333;
  margin-bottom: 10px;
}

.stat-card p {
  font-size: 20px;
  font-weight: bold;
  color: #3498db;
}

.chart-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 200px;
}

.chart-item {
  flex: 1 1 calc(30% - 20px);
  min-width: 300px;
  max-width: 500px;
  height: 400px;
  box-sizing: border-box;
}

.chart-item.scatter-chart {
  flex: 1 1 calc(40% - 20px);
  min-width: 300px;
  max-width: 500px;
  height: 400px;
  box-sizing: border-box;
}

.chart-title-container {
  display: flex;
  justify-content: center;
}

@media (max-width: 1200px) {
  .chart-container {
    flex-direction: column;
    gap: 10px;
  }

  .chart-item {
    flex: 1 1 100%;
    min-width: 100%;
  }
}
</style>
