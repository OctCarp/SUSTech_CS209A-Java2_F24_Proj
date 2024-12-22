<template>
  <div class="header">
    <Header />
  </div>

  <div class="content">
    <div class="intro">
      <Introduction />
    </div>
    <div class="statistics">
      <Statistics />
    </div>
  </div>
</template>

<script setup>
import Introduction from "@/components/Introduction.vue";
import Header from "@/components/Header.vue";
import Statistics from "@/components/Statistics.vue";
</script>

<style scoped>
.header {
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 10;
}

/* 内容区域，调整顶部内边距，避免被 Header 遮挡 */
.content {
  margin-top: 100px; /* 留出 Header 的空间，确保不被遮挡 */
  position: absolute;
  top: 0;
  left: 5%;
  width: 90%; /* content 宽度占页面宽度的 90% */
}

.intro {
  width: 90%;
  margin: 0 auto;
}

</style>