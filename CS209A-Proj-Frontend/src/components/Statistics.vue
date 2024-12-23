<template>
  <section class="statistics-double-column">
    <div class="stat-item">
      <TopTopics />
    </div>

    <div class="stat-item">
      <TopicStatistics />
    </div>

    <div class="stat-item">
      <TopBugs />
    </div>

    <div class="stat-item">
      <BugStatistics />
    </div>

    <div class="stat-item">
      <AllUserReputation />
    </div>

    <div class="stat-item">
      <TopUserEngagement />
    </div>

  </section>

  <section class="statistics-single-column">
    <div class="stat-item">
      <AnswerQuality />
    </div>
  </section>
</template>

<script setup>
import TopTopics from "@/components/Query/TopTopics.vue";
import TopBugs from "@/components/Query/TopBugs.vue";
import TopicStatistics from "@/components/Query/TopicStatistics.vue";
import BugStatistics from "@/components/Query/BugStatistics.vue";
import AllUserReputation from "@/components/Query/AllUserReputation.vue";
import TopUserEngagement from "@/components/Query/TopUserEngagement.vue";
import AnswerQuality from "@/components/Query/AnswerQuality.vue";
</script>

<style scoped>
.statistics-double-column {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 48%));
  gap: 20px;
  padding: 20px;
  justify-content: center;
}

.statistics-single-column {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 100%));
  gap: 20px;
  padding: 20px;
  justify-content: center;
}

.stat-item {
  background-color: #fafafa;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  min-height: 0;               /* 防止父容器的高度影响 */
  height: auto;                /* 根据内容自适应高度 */
  box-sizing: border-box;      /* 包括内边距和边框 */
}
</style>
