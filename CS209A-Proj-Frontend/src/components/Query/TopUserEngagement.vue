<template>
  <div class="top-topics">
    <h1>高用户参与度话题</h1>

    <div class="select-container">
      <div class="filter-container">
        <!-- 第一行：开始日期和结束日期 -->
        <div class="date-picker-container">
          <v-text-field
              v-model="startDateText"
              label="开始日期"
              prepend-inner-icon="mdi-calendar"
              readonly
              color="primary"
              @click="startDateDialog = true"
              class="date-picker"
          ></v-text-field>

          <v-text-field
              v-model="endDateText"
              label="结束日期"
              prepend-inner-icon="mdi-calendar"
              readonly
              color="primary"
              @click="endDateDialog = true"
              class="date-picker"
          ></v-text-field>
        </div>

        <!-- 第二行：话题个数选择框和声望值输入框 -->
        <div class="topic-reputation-container">
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
              label="最低声望值(默认为0)"
              variant="outlined"
              color="primary"
              placeholder="0"
              class="reputation-input"
          />
        </div>
      </div>

      <!-- 查询按钮放在右侧并垂直居中 -->
      <div class="query-button-container">
        <v-btn @click="fetchData" :disabled="!numTopics" size="large" color="primary" class="query-button">
          <v-icon icon="mdi-magnify"></v-icon>
          查询
        </v-btn>
      </div>
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
        />
      </div>
    </div>

    <div v-if="shouldUpdate" class="toggle-button-container">
      <v-btn @click="collapse" variant="text">
        <v-icon left>mdi-chevron-up</v-icon>
        收起
      </v-btn>
    </div>

    <v-dialog v-model="startDateDialog" max-width="290px">
      <v-date-picker v-model="startDate" @input="startDateDialog = false" :max="endDate" color="primary"></v-date-picker>
    </v-dialog>

    <v-dialog v-model="endDateDialog" max-width="290px">
      <v-date-picker v-model="endDate" @input="endDateDialog = false" :min="startDate" color="primary"></v-date-picker>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import BarChart from '../Chart/BarChart.vue';
import { fetchTopTopicData, fetchTopTopicEngagementData } from "@/services/api.js";

const shouldUpdate = ref(false);
const numTopics = ref("");
const minReputation = ref(0);

const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);

const chartTitle = ref(`用户参与度最高的${numTopics.value}个话题`);
const xAxisLabel = ref('话题');
const yAxisLabel = ref('用户参与度');

let data = ref([]);

const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
];

// 查询按钮的点击事件，显示数据
const fetchData = async () => {
  shouldUpdate.value = true;
  chartTitle.value = `用户参与度最高的${numTopics.value}个话题`;

  try {
    // 将 startDate 和 endDate 转换为 Unix 时间戳（毫秒）
    const startTimestamp = startDate.value ? startDate.value.getTime() : null;
    const endTimestamp = endDate.value ? endDate.value.getTime() : null;

    const response = await fetchTopTopicEngagementData(numTopics.value, minReputation.value, startTimestamp, endTimestamp);
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

// 更新选择日期的文本显示
watch(startDate, (newDate) => {
  startDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
watch(endDate, (newDate) => {
  endDateText.value = newDate ? newDate.toLocaleDateString() : '';
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
  gap: 15px;
  margin-bottom: 20px;
}

.filter-container {
  width: 100%;
}

.date-picker-container,
.topic-reputation-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.date-picker {
  width: 140px;
  margin-right: 20px;
}

.topic-select {
  width: 140px;
  margin-right: 20px;
}

.reputation-input {
  width: 140px;
  margin-right: 20px;
}

/* 将查询按钮放置在右侧并垂直居中 */
.query-button-container {
  display: flex;
  justify-content: flex-end; /* 右侧对齐 */
  align-items: center; /* 垂直居中 */
}

.query-button {
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
