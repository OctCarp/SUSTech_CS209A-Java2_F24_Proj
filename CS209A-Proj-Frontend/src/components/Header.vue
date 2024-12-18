<template>
  <header class="header">
    <a href="https://stackoverflow.com/" target="_blank" class="link">
      <img src="@/assets/logo-stackoverflow.svg" alt="Stack Overflow Logo" class="stack-logo" />
    </a>

    <div class="header-right">
      <span class="title">数据分析</span>
    </div>

    <a href="https://github.com/OctCarp/SUSTech_CS209A-Java2_F24_Proj/" target="_blank" class="link">
      <img src="@/assets/github-mark.svg" alt="Github Logo" class="github-logo" />
    </a>
  </header>
</template>

<script setup>

</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 将元素分布在左右两端 */
  padding: 10px 20px;
  background-color: #f4f4f4;
  border-bottom: 2px solid #ddd;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.link:hover {
  background-color: inherit;
}

.stack-logo {
  width: auto;
  height: 50px;
  margin-right: 15px;
}

.header-right {
  display: flex;
  align-items: center;
}

.title {
  margin-top: 12px;
  font-size: 24px;
  font-weight: 550;
  color: #F48024;
  font-family: 'Arial', sans-serif;
  line-height: 1.2;
}

.github-logo {
  height: 35px;
  width: 35px;
  margin-right: 30px;
}

/* 将 GitHub 链接推到右边 */
.link:last-child {
  margin-left: auto;
}
</style>
