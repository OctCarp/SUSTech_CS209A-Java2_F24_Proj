<template>
  <header class="header">
    <img src="@/assets/logo-stackoverflow.svg" alt="Stack Overflow Logo" class="logo" />

    <div class="header-right">
      <span class="title">数据分析</span>
    </div>
  </header>
</template>

<script setup>

</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 10px 20px;
  background-color: #f4f4f4;
  border-bottom: 2px solid #ddd;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.logo {
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
</style>
