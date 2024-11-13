<script setup>
import { ref } from 'vue';
import ButtonComponent from '@/components/common/ButtonComponent.vue';

const overlay = ref(false);

const rankingData = [
  { rank: 1, sessionName: 'Session1', time: '00:58:94' },
  { rank: 2, sessionName: 'Session1', time: '00:59:54' },
  { rank: 3, sessionName: 'Session4', time: '00:59:87' },
  { rank: 4, sessionName: 'Session2', time: '01:02:33' },
  { rank: 5, sessionName: 'Session7', time: '01:03:19' },
  { rank: 6, sessionName: 'Session8', time: '01:04:78' },
  { rank: 7, sessionName: 'Session5', time: '01:05:03' },
  { rank: 8, sessionName: 'Session6', time: '01:05:94' },
  { rank: 9, sessionName: 'Session10', time: '01:05:99' },
  { rank: 10, sessionName: 'Session9', time: '01:06:74' },
];

</script>

<template>
  <v-overlay
    v-model="overlay"
    class="d-flex flex-column justify-center align-center"
    opacity="0.6"
    contained
  >
    <v-card class="pa-5" width="600">
      <v-card-title>결과</v-card-title>
      <v-card-text>
        <!-- 상단 시상대 -->
        <div class="d-flex justify-center mb-4">
          <div class="d-flex flex-column align-center mx-3">
            <v-avatar size="80" class="mb-2">🥇</v-avatar>
            <p>{{ rankingData[0].sessionName }}</p>
            <p>{{ rankingData[0].time }}</p>
          </div>
          <div class="d-flex flex-column align-center mx-3">
            <v-avatar size="80" class="mb-2">🥈</v-avatar>
            <p>{{ rankingData[1].sessionName }}</p>
            <p>{{ rankingData[1].time }}</p>
          </div>
          <div class="d-flex flex-column align-center mx-3">
            <v-avatar size="80" class="mb-2">🥉</v-avatar>
            <p>{{ rankingData[2].sessionName }}</p>
            <p>{{ rankingData[2].time }}</p>
          </div>
        </div>

        <!-- 나머지 그룹 목록 -->
        <v-divider></v-divider>
        <v-list class="list-rank">
          <v-list-item
            v-for="(item, index) in rankingData.slice(3)"
            :key="index"
          >
            <v-list-item-content>
              <v-list-item-title>
                <v-icon icon="mdi-emoticon-poop"/>{{ item.sessionName }}</v-list-item-title>
              <v-list-item-subtitle>{{ item.time }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </v-overlay>

  <!-- 버튼 -->
  <ButtonComponent
    text="결과 확인하기"
    color="white"
    @click="overlay = !overlay"
  />
</template>

<style scoped>
.d-flex {
  display: flex;
}

.justify-center {
  justify-content: center;
}

.align-center {
  align-items: center;
}

.pa-5 {
  padding: 20px;
}

.mb-4 {
  margin-bottom: 16px;
}

.mx-3 {
  margin-left: 12px;
  margin-right: 12px;
}

.mb-2 {
  margin-bottom: 8px;
}

.list-rank {
  height: 200px;
  overflow: scroll;
}
</style>