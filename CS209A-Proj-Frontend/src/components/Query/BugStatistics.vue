<template>
  <div class="error-statistics">
    <h1>错误/异常: {{ bugName }} 详细数据</h1>

    <!-- 用户选择错误类型 -->
    <div class="select-container">
      <v-select
          v-model="bugName"
          :items="options"
          label="选择错误类型"
          item-title="label"
          item-value="value"
          variant="outlined"
          class="custom-select"
      />

      <!-- 查询按钮，点击查询才更新数据 -->
      <v-btn @click="fetchData" :disabled="!bugName" size="large" color="primary" class="query-button">
        <v-icon icon="mdi-magnify"></v-icon>
        查询
      </v-btn>
    </div>

    <!-- 只有当 bugName 有值时才显示统计和图表 -->
    <div v-if="shouldUpdate">
      <div class="stats">
        <div class="stat-card" v-for="(value, key) in stats" :key="key">
          <h4>{{ formatLabel(key) }}</h4>
          <p>{{ value }}</p>
        </div>
      </div>

      <div class="chart">
        <PieChart
            :chartTitle="`${bugName} 错误类型占比`"
            :chartData="pieChartData"
        />
      </div>
    </div>

    <!-- 收起按钮，只有在 bugName 有值时才显示 -->
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
import {fetchBugStatisticsData} from "@/services/api.js";

// 默认错误类型为空
const bugName = ref("");

// 下拉框选项（确保值为字符串）
const options = [
  { value: "NullPointerException", label: "NullPointerException" },
  { value: "IOException", label: "IOException" },
  { value: "SQLException", label: "SQLException" },
  { value: "ClassNotFoundException", label: "ClassNotFoundException" },
];

// 初始错误数据
const statsData = {
  "NullPointerException": {
    frequency: 100,
    totalErrors: 1200,
    criticalCount: 700,
    warningCount: 400,
    infoCount: 100,
    userCount: 300,
    totalViews: 5000,
  },
  "IOException": {
    frequency: 80,
    totalErrors: 900,
    criticalCount: 600,
    warningCount: 200,
    infoCount: 100,
    userCount: 200,
    totalViews: 4000,
  },
  "SQLException": {
    frequency: 120,
    totalErrors: 1300,
    criticalCount: 750,
    warningCount: 400,
    infoCount: 150,
    userCount: 350,
    totalViews: 6000,
  },
  "ClassNotFoundException": {
    frequency: 90,
    totalErrors: 1100,
    criticalCount: 500,
    warningCount: 400,
    infoCount: 200,
    userCount: 250,
    totalViews: 4500,
  },
};

const stats = ref({});
const pieChartData = ref([]);

const shouldUpdate = ref(false);

// 查询按钮的点击事件，显示数据
const fetchData = async () => {
  shouldUpdate.value = true;
  updateStats();

  // try {
  //   const response = await fetchBugStatisticsData(bugName.value);
  //   statsData.value = response.data;
  // } catch (error) {
  //   console.error('获取数据失败:', error);
  // }
};

// 更新统计数据和饼图数据
const updateStats = () => {
  if (bugName.value && statsData[bugName.value]) {
    stats.value = statsData[bugName.value];
    pieChartData.value = [
      { name: 'Critical Errors', value: stats.value.criticalCount },
      { name: 'Warning Errors', value: stats.value.warningCount },
      { name: 'Info Errors', value: stats.value.infoCount },
    ];
  }
};

// 格式化标签，转换为更友好的展示形式
const formatLabel = (key) => {
  switch (key) {
    case 'frequency': return '错误频率';
    case 'totalErrors': return '错误总数';
    case 'criticalCount': return '严重错误数';
    case 'warningCount': return '警告错误数';
    case 'infoCount': return '信息错误数';
    case 'userCount': return '用户数';
    case 'totalViews': return '访问量';
    default: return key;
  }
};

// 收起按钮的点击事件，恢复默认值并隐藏内容
const collapse = () => {
  bugName.value = "";
  shouldUpdate.value = false;
  stats.value = {}; // 清空统计数据
  pieChartData.value = []; // 清空饼图数据
};
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
