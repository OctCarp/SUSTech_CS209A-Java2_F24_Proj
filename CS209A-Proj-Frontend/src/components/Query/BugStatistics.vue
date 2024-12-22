<template>
  <div class="error-statistics">
    <h1>错误/异常详细信息</h1>

    <div class="select-container">
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

      <v-autocomplete
          v-model="bugName"
          :items="bugOptions"
          label="输入错误/异常名称"
          density="comfortable"
          menu-icon=""
          variant="outlined"
          clearable
          auto-select-first
          item-props
          :no-data-text="'错误/异常不存在'"
          class="input-topic"
      ></v-autocomplete>

      <v-btn @click="fetchData" :disabled="!bugName" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <div v-if="shouldUpdate">
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
import {onMounted, ref, watch} from 'vue';
import PieChart from "@/components/Chart/PieChart.vue";
import {fetchAllBugName, fetchBugStatisticsData} from "@/services/api.js";

const bugName = ref("");
const stats = ref({});

const startDate = ref(null);
const endDate = ref(null);
const startDateText = ref('');
const endDateText = ref('');
const startDateDialog = ref(false);
const endDateDialog = ref(false);

const chartTitle = ref(`${bugName.value} 发布类型占比`);
const pieChartData = ref([]);
const shouldUpdate = ref(false);

const bugOptions = ref([]);

let data = ref();

const fetchAllBugNameData = async () => {
  try {
    const response = await fetchAllBugName();
    if (response && Array.isArray(response)) {
      bugOptions.value = response.map((item) => ({
        title: item.bugName,
      }))
      .sort((a, b) => a.title.localeCompare(b.title));
    }
  } catch (error) {
    console.error("获取所有故障失败:", error);
  }
};

const fetchData = async () => {
  shouldUpdate.value = true;
  chartTitle.value = `${bugName.value} 发布类型占比`;

  try {
    // 将 startDate 和 endDate 转换为 Unix 时间戳（毫秒）
    const startTimestamp = startDate.value ? startDate.value.getTime() : null;
    const endTimestamp = endDate.value ? endDate.value.getTime() : null;

    const response = await fetchBugStatisticsData(bugName.value, startTimestamp, endTimestamp);
    if (response && response.bugStatistics) {
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
  if (bugName.value && data.value.bugStatistics) {
    const bugStatistics = data.value.bugStatistics;
    stats.value = {
      frequency: data.value.bug.bugFrequency,
      totalPosts: bugStatistics.totalPosts,
      questionCount: bugStatistics.questionCount,
      answerCount: bugStatistics.answerCount,
      commentCount: bugStatistics.commentCount,
      userCount: bugStatistics.userCount,
      totalViews: bugStatistics.totalViews,
    };

    pieChartData.value = [
      { name: 'Questions', value: bugStatistics.questionCount },
      { name: 'Answers', value: bugStatistics.answerCount },
      { name: 'Comments', value: bugStatistics.commentCount },
    ];
  }
};

// 格式化标签，转换为更友好的展示形式
const formatLabel = (key) => {
  switch (key) {
    case 'frequency': return '话题频率';
    case 'totalPosts': return '帖子总数';
    case 'questionCount': return '问题数';
    case 'answerCount': return '回答数';
    case 'commentCount': return '评论数';
    case 'userCount': return '参与用户总数';
    case 'totalViews': return '访问量';
    default: return key;
  }
};

const collapse = () => {
  shouldUpdate.value = false;
  bugName.value = "";
  startDate.value = null;
  endDate.value = null;
  stats.value = {};
  pieChartData.value = [];
  data.value = {};
};

// 更新选择日期的文本显示
watch(startDate, (newDate) => {
  startDateText.value = newDate ? newDate.toLocaleDateString() : '';
});
watch(endDate, (newDate) => {
  endDateText.value = newDate ? newDate.toLocaleDateString() : '';
});

onMounted(() => {
  fetchAllBugNameData();
});
</script>

<style scoped>
.error-statistics {
  position: relative;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.select-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.date-picker {
  width: 140px;
  margin-right: 20px;
}

.input-topic {
  width: 120px;
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

.chart-container {
  margin-top: 40px;
}

.chart-title-container {
  display: flex;
  justify-content: center;
}
</style>
