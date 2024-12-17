<template>
  <div class="topic-statistics">
    <h1>话题: {{ topicName }} 详细数据</h1>

    <!-- 用户选择话题 -->
    <div class="select-container">
      <v-select
          v-model="topicName"
          :items="options"
          label="选择话题"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="custom-select"
      />

      <!-- 查询按钮，点击查询才更新数据 -->
      <v-btn @click="fetchData" :disabled="!topicName" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <!-- 只有当 topicName 有值时才显示统计和图表 -->
    <div v-if="shouldUpdate">
      <div class="stats">
        <div class="stat-card" v-for="(value, key) in stats" :key="key">
          <h4>{{ formatLabel(key) }}</h4>
          <p>{{ value }}</p>
        </div>
      </div>

      <div class="chart">
        <PieChart
            :chartTitle="`${topicName} 帖子类型占比`"
            :chartData="pieChartData"
        />
      </div>
    </div>

    <!-- 收起按钮，只有当 topicName 有值时才显示 -->
    <div v-if="shouldUpdate" class="toggle-button-container">
      <v-btn @click="collapse" variant="text">
        <v-icon left>mdi-chevron-up</v-icon>  <!-- 收起图标 -->
        收起
      </v-btn>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import PieChart from "@/components/Chart/PieChart.vue";
import {fetchTopicStatisticsData} from "@/services/api.js";

// 默认话题为空
const topicName = ref("");

// 下拉框选项（确保值为数字）
const options = [
  { value: "Java Programming", label: "Java Programming" },
  { value: "Python Programming", label: "Python Programming" },
  { value: "Web Development", label: "Web Development" },
];

// 初始统计数据
const statsData = {
  "Java Programming": {
    frequency: 320,
    totalPosts: 1200,
    questionCount: 700,
    answerCount: 450,
    commentCount: 500,
    userCount: 900,
    totalViews: 10000,
  },
  "Python Programming": {
    frequency: 500,
    totalPosts: 1500,
    questionCount: 800,
    answerCount: 600,
    commentCount: 550,
    userCount: 1000,
    totalViews: 15000,
  },
  "Web Development": {
    frequency: 400,
    totalPosts: 1100,
    questionCount: 650,
    answerCount: 500,
    commentCount: 600,
    userCount: 850,
    totalViews: 12000,
  },
};

// 初始化当前数据
const stats = ref({});

// 饼图数据
const pieChartData = ref([]);

// 控制数据更新的标志
const shouldUpdate = ref(false);

// 查询按钮的点击事件，显示数据
const fetchData = async () => {
  shouldUpdate.value = true;
  updateStats();

  // try {
  //   const response = await fetchTopicStatisticsData(topicName.value);
  //   statsData.value = response.data;
  // } catch (error) {
  //   console.error('获取数据失败:', error);
  // }
};

// 更新统计数据和饼图数据
const updateStats = () => {
  if (topicName.value && statsData[topicName.value]) {
    stats.value = statsData[topicName.value];
    pieChartData.value = [
      { name: 'Questions', value: stats.value.questionCount },
      { name: 'Answers', value: stats.value.answerCount },
      { name: 'Comments', value: stats.value.commentCount },
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

// 收起按钮的点击事件，恢复默认值并隐藏内容
const collapse = () => {
  topicName.value = "";
  shouldUpdate.value = false;
  stats.value = {}; // 清空统计数据
  pieChartData.value = []; // 清空饼图数据
};
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
  justify-content: center;
  margin-bottom: 20px;
}

.query-button {
  margin-top: 5px;
  margin-left: 20px;
}

.toggle-button-container {
  position: absolute;
  bottom: 0;
  right: 0;
  z-index: 10;
}

/* 数字统计部分样式 */
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
  width: 160px;
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

/* 图表部分样式 */
.chart {
  margin-bottom: 30px;
}
</style>
