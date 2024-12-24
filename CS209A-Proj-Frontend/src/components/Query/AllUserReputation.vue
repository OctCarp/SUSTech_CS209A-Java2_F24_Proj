<template>
  <div class="top-topics">
    <h1>用户名誉</h1>

    <div class="select-container">
      <v-btn @click="fetchData" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate">
      <div class="stats">
        <div class="stat-card">
          <h4>用户总数</h4>
          <p>{{ userCount }}</p>
        </div>

        <div class="stat-card">
          <h4>用户名誉平均数</h4>
          <p>{{ meanReputation }}</p>
        </div>

        <div class="stat-card">
          <h4>用户名誉中位数</h4>
          <p>{{ medianReputation }}</p>
        </div>
      </div>

      <div class="chart-container">
        <div class="chart-item">
          <div class="chart-title-container">
            <h2>用户名誉分布</h2>
          </div>
          <LineChart
            :chartData="data"
            :xAxisLabel="xAxisLabel"
            :yAxisLabel="yAxisLabel"
          />
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
import LineChart from "@/components/Chart/LineChart.vue";
import { fetchAllUserReputation } from "@/services/api.js";

const meanReputation = ref(0);
const medianReputation = ref(0);
const userCount = ref(0);
const xAxisLabel = ref("用户名誉");
const yAxisLabel = ref("用户数量");
let shouldUpdate = ref(false);
let data = ref([]);

const fetchData = async () => {
  shouldUpdate.value = true;
  try {
    const response = await fetchAllUserReputation();
    console.log(response);
    if (Array.isArray(response)) {
      data.value = response;
      calculateStats();
    } else {
      console.error("返回的数据格式不正确", response);
      data.value = [];
    }
  } catch (error) {
    console.error('获取数据失败:', error);
    data.value = [];
  }
};

const calculateStats = () => {
  // 计算用户总数
  userCount.value = data.value.reduce((sum, item) => sum + item.count, 0);

  // 计算用户名誉平均数
  const totalReputation = data.value.reduce((sum, item) => sum + item.reputation * item.count, 0);
  meanReputation.value = (totalReputation / userCount.value).toFixed(0);

  // 计算用户名誉中位数
  const sortedData = [];
  data.value.forEach(item => {
    for (let i = 0; i < item.count; i++) {
      sortedData.push(item.reputation);
    }
  });
  sortedData.sort((a, b) => a - b);

  if (sortedData.length % 2 === 0) {
    const mid = sortedData.length / 2;
    medianReputation.value = ((sortedData[mid - 1] + sortedData[mid]) / 2).toFixed(0);
  } else {
    medianReputation.value = sortedData[Math.floor(sortedData.length / 2)].toFixed(0);
  }
};

const collapse = () => {
  shouldUpdate.value = false;
  data.value = [];
  meanReputation.value = 0;
  medianReputation.value = 0;
  userCount.value = 0;
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
  flex-direction: column;
  justify-content: center;
  gap: 20px;
  margin-bottom: 100px;
}

.chart-item {
  max-width: 100%;
  min-width: 500px;
  height: 400px;
  box-sizing: border-box;
  justify-content: center;
  margin-bottom: 20px;
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
