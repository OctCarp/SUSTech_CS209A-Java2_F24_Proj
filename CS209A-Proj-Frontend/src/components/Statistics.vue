<template>
  <section class="statistics">
    <!-- 左侧和右侧组件会自动排列为两列 -->
    <div class="stat-item">
      <CommonJavaTopics />
    </div>

    <div class="stat-item">
      <CommonJavaTopics />
    </div>

    <div class="stat-item">
      <CommonJavaTopics />
    </div>

    <div class="stat-item">
      <CommonJavaTopics />
    </div>

    <!-- 添加更多组件可以继续按上述方式添加 -->
  </section>
</template>

<script setup>
import CommonJavaTopics from "@/components/CommonJavaTopics.vue"; /* 引入 CommonJavaTopics 组件 */
</script>

<style scoped>
.statistics {
  display: grid; /* 使用 Grid 布局 */
  grid-template-columns: repeat(2, 1fr); /* 每行两列，每列占 50% */
  gap: 20px; /* 组件之间的间距 */
  padding: 20px;
}

.stat-item {
  background-color: #fafafa;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

</style>
