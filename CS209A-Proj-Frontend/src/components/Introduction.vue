<template>
  <section class="introduction">
    <div class="text">
      <h2 class="title">Introduction</h2>
      <p class="description">
        Stack Overflow is a question-and-answer site for programming. This website collects and analyzes data about Java on the stack overflow website, and supports users to obtain relevant results.
      </p>
    </div>

    <div class="image">
      <img src="@/assets/stack-overflow.png" alt="stack-overflow image" />
    </div>
  </section>
</template>

<script setup>
/* 这里无需添加逻辑，仅展示静态内容 */
</script>

<style scoped>
.introduction {
  display: flex;
  justify-content: space-between; /* 左右分布 */
  align-items: center; /* 垂直居中对齐 */
  padding: 20px;
  background-color: #fafafa; /* 背景颜色 */
  border-radius: 8px; /* 边框圆角 */
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.1); /* 添加阴影 */
  margin: 10px 0;
}

.text {
  flex: 1; /* 让文字部分占据剩余空间 */
  padding-right: 20px;
  margin-right: 10%;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.description {
  font-size: 18px;
  color: #666;
  line-height: 1.6;
  margin-top: 20px;
}

.image {
  flex: 0 0 45%;
  max-width: 600px; /* 图片最大宽度 */
}

.image img {
  width: 100%; /* 让图片占满父容器的宽度 */
  height: auto;
  border-radius: 8px; /* 图片圆角 */
}
</style>
