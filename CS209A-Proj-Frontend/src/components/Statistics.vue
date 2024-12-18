<template>
  <section class="statistics">
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
      <TopUserEngagement />
    </div>

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
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
}

.stat-item {
  flex: 1 1 45%;
  background-color: #fafafa;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  min-height: 0;
  height: auto;
  box-sizing: border-box;
}
</style>
