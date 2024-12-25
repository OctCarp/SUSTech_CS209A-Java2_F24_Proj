<template>
  <div class="top-topics">
    <h1>高用户参与度话题</h1>

    <div class="select-container">
      <v-select
          v-model="numTopics"
          :items="options"
          label="选择显示话题个数"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="topic-select"
      />

      <v-text-field
          v-model="minReputation"
          label="用户最低名誉值（默认为 0）"
          variant="outlined"
          clearable
          color="primary"
          placeholder="0"
          class="reputation-input"
          type="number"
          @blur="validateReputation"
          :error-messages="reputationErrorMessages"
      />

      <v-btn @click="fetchData" :disabled="!numTopics || reputationErrorMessages.length > 0" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <div class="chart-item">
        <div class="chart-title-container">
          <h2>{{ chartTitle }}</h2>
        </div>
        <BarChart
            :xAxisLabel="xAxisLabel"
            :yAxisLabel="yAxisLabel"
            :chartData="data"
            :isShowLabel="false"
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
import { ref, watch } from 'vue';
import BarChart from '../Chart/BarChart.vue';
import { fetchTopTopicEngagementData } from "@/services/api.js";

const numTopics = ref("");
const minReputation = ref(0);
const chartTitle = ref(`用户参与度最高的 ${numTopics.value} 个话题`);
const xAxisLabel = ref('话题');
const yAxisLabel = ref('用户参与度');
const reputationErrorMessages = ref([]);
let shouldUpdate = ref(false);
let data = ref([]);

const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
  { value: 16, label: '全部' },
];

const validateReputation = () => {
  reputationErrorMessages.value = [];
  if (minReputation.value < 0) {
    reputationErrorMessages.value.push('用户最低声望值不能低于 0');
  }
};

const fetchData = async () => {
  shouldUpdate.value = true;
  chartTitle.value = `用户参与度最高的 ${numTopics.value} 个话题`;
  try {
    const response = await fetchTopTopicEngagementData(numTopics.value, minReputation.value);
    if (Array.isArray(response)) {
      data.value = response.map(item => ({
        name: item.topicName,
        value: item.engagementScore,
        type: 'TOPIC',
      })).sort((a, b) => b.value - a.value);
    } else {
      console.error("返回的数据格式不正确", response);
      data.value = [];
    }
  } catch (error) {
    console.error('获取数据失败:', error);
    data.value = [];
  }
};

const collapse = () => {
  numTopics.value = "";
  minReputation.value = 0;
  shouldUpdate.value = false;
};

watch(minReputation, (newValue) => {
  if (newValue === '' || newValue === null) {
    minReputation.value = 0; // 自动设为0
  }
});
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
  width: 140px;
  margin-right: 20px;
}

.reputation-input {
  width: 140px;
  margin-right: 20px;
}

.query-button {
  margin-top: 5px;
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
