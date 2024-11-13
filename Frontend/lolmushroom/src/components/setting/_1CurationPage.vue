<script setup>
import ContentsList from './_1ContentsList.vue'
import ContentsCard from './_1ContentsCard.vue'
import SelectedList from './_1SelectedList.vue'
</script>

<template>
  <div class="main-container">
    <!-- 상단 섹션 -->
    <div class="top-list">
      <div fluid class="card-section">
        <ContentsCard />
      </div>
      <div class="list-section">
        <ContentsList />
      </div>
    </div>
    <!-- 하단 섹션 -->
    <div class="bottom-list">
      <SelectedList />
    </div>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
}

.top-list {
  display: flex;
  flex-direction: row;
  height: 80%;
}

.card-section {
  flex: 1.8;
}

.list-section {
  flex: 1;
  overflow-y: auto;
}

.bottom-list {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-top: solid 1px rgb(199, 199, 199);
}

/* Responsive design */
@media (max-width: 768px) {
  .top-list {
    flex-direction: column;
    height: 70%;
  }

  .bottom-list {
    height: auto;
  }
}
</style>
