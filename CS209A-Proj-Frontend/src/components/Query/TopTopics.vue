<template>
  <div class="top-topics">
    <h1>高频话题</h1>

    <div class="select-container">
      <div class="filter-container">
        <div class="date-picker-container">
          <v-text-field
              v-model="startDateText"
              label="开始日期"
              prepend-inner-icon="mdi-calendar"
              readonly
              clearable
              color="primary"
              @click="startDateDialog = true"
              class="date-picker"
          ></v-text-field>

          <v-text-field
              v-model="endDateText"
              label="结束日期"
              prepend-inner-icon="mdi-calendar"
              readonly
              clearable
              color="primary"
              @click="endDateDialog = true"
              class="date-picker"
          ></v-text-field>
        </div>

        <div class="topic-select-container">
          <v-select
              v-model="numTopics"
              :items="options"
              label="选择显示话题个数"
              item-title="label"
              item-value="value"
              variant="outlined"
              class="topic-select"
          />
        </div>
      </div>

      <v-btn @click="fetchData" :disabled="!numTopics" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate" class="chart-container">
      <div class="word-cloud">
        <div class="chart-title-container">
          <h2>{{ wordCloudTitle }}</h2>
        </div>
        <WordCloud :chartData="data" />
      </div>

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
import { computed, onMounted, ref, watch } from 'vue';
import BarChart from '../Chart/BarChart.vue';
import WordCloud from '../Chart/WordCloud.vue';
import { fetchAllTopicName, fetchTopTopicData } from "@/services/api.js";

let shouldUpdate = ref(false);
const numTopics = ref('');

const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);

const wordCloudTitle = ref('高频话题词云');
const chartTitle = ref(`最高频的${numTopics.value}个话题`);
const xAxisLabel = ref('话题');
const yAxisLabel = ref('频率');

let data = ref([]);

const options = [
  { value: 5, label: '5个' },
  { value: 10, label: '10个' },
  { value: 15, label: '15个' },
  { value: 16, label: '全部' },
];

const fetchData = async () => {
  shouldUpdate.value = true;
  chartTitle.value = `最高频的${numTopics.value}个话题`;

  try {
    const startTimestamp = startDate.value ? startDate.value.getTime() / 1000 : 0;
    const endTimestamp = endDate.value ? endDate.value.getTime() / 1000 : 1735660800;

    const response = await fetchTopTopicData(numTopics.value, startTimestamp, endTimestamp);
    if (Array.isArray(response)) {
      data.value = response.map(item => ({
        name: item.topicName,
        value: item.frequency,
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
  shouldUpdate.value = false;
  numTopics.value = '';
  startDate.value = null;
  endDate.value = null;
  data.value = [];
};

watch(startDate, (newDate) => {
  startDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
watch(endDate, (newDate) => {
  endDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
watch(startDateText, (newValue) => {
  if (!newValue) {
    startDate.value = null;
  }
});
watch(endDateText, (newValue) => {
  if (!newValue) {
    endDate.value = null;
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
  align-items: center;
  margin-bottom: 20px;
}

.filter-container {
  width: 83%;
  display: flex;
  flex-direction: column;
}

.date-picker-container {
  display: flex;
  justify-content: space-between;
}

.date-picker {
  width: 250px;
  margin-right: 20px;
}

.topic-select-container {
  display: flex;
}

.topic-select {
  width: 120px;
  margin-right: 20px;
}

.toggle-button-container {
  position: absolute;
  bottom: 0;
  right: 0;
  z-index: 10;
}

.chart-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 20px;
}

.word-cloud {
  width: 100%;
  height: 300px;
}

.chart-item {
  max-width: 100%;
  min-width: 500px;
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
