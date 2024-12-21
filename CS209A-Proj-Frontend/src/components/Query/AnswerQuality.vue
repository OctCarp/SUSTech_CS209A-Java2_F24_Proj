<template>
  <div class="top-topics">
    <h1>答案质量</h1>

    <div class="select-container">
      <v-btn @click="fetchData" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <div class="chart-item">
        <div class="chart-title-container">
          <h2>{{ chartTitle }}</h2>
        </div>
        <PieChart
            :chartData="pieChartData"
        />
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
import {computed, ref, watch} from 'vue';
import PieChart from "@/components/Chart/PieChart.vue";
import { fetchAnswerQualityData } from "@/services/api.js";

let shouldUpdate = ref(false);

const chartTitle = ref('答案质量分布');
const pieChartData = ref([]);

let data = ref([]);

const fetchData = async () => {
  shouldUpdate.value = true;
  chartTitle.value = '答案质量分布';

  try {
    const response = await fetchAnswerQualityData();
    if (Array.isArray(response)) {
      data.value = response;
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
  }
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

.topic-select {
  width: 120px;
  margin-right: 20px;
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

.chart-container {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.chart-item {
  max-width: 90%;
  min-width: 600px;
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
    max-width: 100%;
    min-width: 100%;
  }
}
</style>