<template>
  <div class="topic-statistics">
    <h1>话题详细信息</h1>

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
          />

          <v-text-field
              v-model="endDateText"
              label="结束日期"
              prepend-inner-icon="mdi-calendar"
              readonly
              clearable
              color="primary"
              @click="endDateDialog = true"
              class="date-picker"
          />
        </div>

        <div class="topic-input-container">
          <v-autocomplete
              v-model="topicName"
              :items="topicOptions"
              label="输入话题名称"
              density="comfortable"
              menu-icon=""
              variant="outlined"
              clearable
              auto-select-first
              item-props
              :no-data-text="'话题不存在'"
              class="input-topic"
          />
        </div>
      </div>

      <v-btn @click="fetchData" :disabled="!topicName" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate">
      <div class="statistic-title-container">
        <h2>{{ statisticTitle }}</h2>
      </div>
      <div class="stats">
        <div class="stat-card" v-for="(value, key) in stats" :key="key">
          <h4>{{ formatLabel(key) }}</h4>
          <p>{{ value }}</p>
        </div>
      </div>

      <div class="chart-container">
        <div class="chart-title-container">
          <h2>{{ chartTitle }}</h2>
        </div>
        <PieChart
            :chartData="pieChartData"
            style="margin-bottom: 50px; margin-top: auto;"
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
      <v-date-picker v-model="startDate" @input="startDateDialog = false" :max="endDate"
                     color="primary"></v-date-picker>
    </v-dialog>

    <v-dialog v-model="endDateDialog" max-width="290px">
      <v-date-picker v-model="endDate" @input="endDateDialog = false" :min="startDate" color="primary"></v-date-picker>
    </v-dialog>

  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue';
import PieChart from "@/components/Chart/PieChart.vue";
import {fetchTopicStatisticsData, fetchAllTopicName} from "@/services/api.js";

const topicName = ref("");
const stats = ref({});
const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);
const statisticTitle = ref(`${topicName.value} 详细数据`);
const chartTitle = ref(`${topicName.value} 发布类型占比`);
const pieChartData = ref([]);
const topicOptions = ref([]);
let shouldUpdate = ref(false);
let data = ref();

const fetchAllTopicNameData = async () => {
  try {
    const response = await fetchAllTopicName();
    if (response && Array.isArray(response)) {
      topicOptions.value = response.map((item) => ({
        title: item.topicName,
      }))
          .sort((a, b) => a.title.localeCompare(b.title));
    }
  } catch (error) {
    console.error("获取所有话题失败:", error);
  }
};

const fetchData = async () => {
  shouldUpdate.value = true;
  statisticTitle.value = `${topicName.value} 详细数据`;
  chartTitle.value = `${topicName.value} 发布类型占比`;
  try {
    // 将 startDate 和 endDate 转换为 Unix 时间戳（毫秒）
    const startTimestamp = startDate.value ? startDate.value.getTime() / 1000 : 0;
    const endTimestamp = endDate.value ? endDate.value.getTime() / 1000 : 1735660800;

    const response = await fetchTopicStatisticsData(topicName.value, startTimestamp, endTimestamp);
    if (response && response.topicStatistics) {
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
  if (topicName.value && data.value.topicStatistics) {
    const topicStatistics = data.value.topicStatistics;
    stats.value = {
      frequency: data.value.topic.frequency,
      totalPosts: topicStatistics.totalPosts,
      questionCount: topicStatistics.questionCount,
      answerCount: topicStatistics.answerCount,
      commentCount: topicStatistics.commentCount,
      userCount: topicStatistics.userCount,
      totalViews: topicStatistics.totalViews,
    };

    pieChartData.value = [
      {name: 'Questions', value: topicStatistics.questionCount},
      {name: 'Answers', value: topicStatistics.answerCount},
      {name: 'Comments', value: topicStatistics.commentCount},
    ];
  }
};

const formatLabel = (key) => {
  switch (key) {
    case 'frequency':
      return '话题频率';
    case 'totalPosts':
      return '帖子总数';
    case 'questionCount':
      return '问题数';
    case 'answerCount':
      return '回答数';
    case 'commentCount':
      return '评论数';
    case 'userCount':
      return '参与用户总数';
    case 'totalViews':
      return '访问量';
    default:
      return key;
  }
};

const collapse = () => {
  shouldUpdate.value = false;
  topicName.value = "";
  startDate.value = null;
  endDate.value = null;
  stats.value = {};
  pieChartData.value = [];
  data.value = {};
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

onMounted(() => {
  fetchAllTopicNameData();
});
</script>

<style scoped>
.topic-statistics {
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

.topic-input-container {
  display: flex;
}

.input-topic {
  width: 120px;
  margin-right: 20px;
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

.statistic-title-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.chart-container {
  display: flex;
  flex-direction: column;
  margin-top: 40px;
}

.chart-title-container {
  display: flex;
  justify-content: center;
}
</style>
