<template>
  <header class="header">
    <img src="@/assets/logo-stackoverflow.svg" alt="Stack Overflow Logo" class="logo" />

    <div class="header-right">
      <span class="title">Data Analysis</span>
    </div>
  </header>
</template>

<script setup>
/* 这里无需添加逻辑，仅仅展示静态 Header */
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
  align-items: center; /* 垂直居中对齐文字和图标 */
}

.title {
  margin-top: 12px;
  font-size: 30px;
  font-weight: 550;
  color: #F48024;
  font-family: 'Arial', sans-serif;
  //letter-spacing: 1px;
  line-height: 1.2;
}
</style>
