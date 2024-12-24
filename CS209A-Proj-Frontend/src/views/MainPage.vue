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

  <v-btn
      v-show="showFab"
      class="fab-button"
      icon
      color="#fafafa"
      @click="scrollToTop"
  >
    <v-icon>mdi-chevron-up</v-icon>
  </v-btn>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import Introduction from "@/components/Introduction.vue";
import Header from "@/components/Header.vue";
import Statistics from "@/components/Statistics.vue";

const showFab = ref(false);

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

const handleScroll = () => {
  showFab.value = window.scrollY > 300; // 当滚动超过 300px 时显示按钮
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped>
.header {
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
}

.content {
  margin-top: 100px;
  position: absolute;
  top: 0;
  left: 5%;
  width: 90%;
}

.intro {
  width: 90%;
  margin: 0 auto;
}

.fab-button {
  position: fixed;
  bottom: 30px;
  right: 20px;
  z-index: 1000;
}
</style>
