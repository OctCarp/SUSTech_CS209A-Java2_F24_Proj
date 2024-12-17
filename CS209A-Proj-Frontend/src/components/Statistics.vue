<template>
  <section class="statistics">
    <!-- 左侧和右侧组件会自动排列为两列 -->
    <div class="stat-item">
      <TopTopics />
    </div>

    <div class="stat-item">
      <TopBugs />
    </div>

    <div class="stat-item">
      <TopicStatistics />
    </div>

    <div class="stat-item">
      <BugStatistics />
    </div>

    <div class="stat-item">
      <TopUserEngagement />
    </div>

    <div class="stat-item">

    </div>

    <!-- 添加更多组件可以继续按上述方式添加 -->
  </section>
</template>

<script setup>
import TopTopics from "@/components/Query/TopTopics.vue";
import TopBugs from "@/components/Query/TopBugs.vue";
import TopicStatistics from "@/components/Query/TopicStatistics.vue";
import BugStatistics from "@/components/Query/BugStatistics.vue";
import TopUserEngagement from "@/components/Query/TopUserEngagement.vue";
</script>

<style scoped>
.statistics {
  display: flex; /* 使用 Flexbox 布局 */
  flex-wrap: wrap; /* 允许换行 */
  gap: 20px; /* 组件间距 */
  padding: 20px;
}

.stat-item {
  flex: 1 1 45%; /* 每个 item 占 45% 宽度，保证每行最多有两个 */
  background-color: #fafafa;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: flex-start; /* 内容顶部对齐 */
  min-height: 0; /* 防止 flex 子项被强制拉伸 */
  height: auto; /* 允许自适应高度 */
  box-sizing: border-box; /* 保证 padding 不影响高度计算 */
}
</style>
