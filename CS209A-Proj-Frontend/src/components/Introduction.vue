<template>
  <section class="introduction">
    <div class="text">
      <h2 class="title">简介</h2>
      <p class="description">
        StackOverflow 是一个程序设计领域的问答网站，隶属 Stack Exchange Network。本页面收集和分析 StackOverflow 网站上与 Java 相关的数据，并提供可视化的结果。
      </p>
    </div>

    <div class="image">
      <img src="@/assets/stack-overflow.png" alt="stack-overflow image" />
    </div>
  </section>
</template>

<script setup>

</script>

<style scoped>
.introduction {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: #fafafa;
  border-radius: 8px;
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.1);
  margin: 10px 0;
}

.text {
  flex: 1;
  padding-right: 20px;
  margin-right: 120px;
  margin-left: 20px;
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
  margin-top: 30px;
}

.image {
  flex: 0 0 45%;
  max-width: 600px;
}

.image img {
  width: 100%;
  height: auto;
  border-radius: 8px;
}
</style>
