<script setup>
  import { useTOFStore } from '@/stores/tofStore'
  import { ref, computed } from 'vue'

  const props = defineProps({
    targetNickName: String,
    selectedAnswer: Number,
    answer: Number,
  })

const store = useTOFStore()
  
const correctRate = computed(() => {
  return store.submitUserCount !== 0 ? store.chosenArray[props.answer].length / store.submitUserCount : 0;
})

</script>

<template>
  <!-- 투표 결과 컴포넌트 -->
  <!-- 카드 컨테이너 -->
  <v-container class="tof-result-div">
    <v-row>
      <v-col v-for="i in 4" :key="i" cols="6">
        <v-card
        class="card-border"
          :class="{
            'correct-card': props.answer === i,
            'selected-card': props.answer !== i && selectedAnswer === i
          }"
        > 
          <div class="statement">{{ store.statements[i-1] }}</div>
          <div class="count">{{ store.chosenArray[i].length }}명</div>
        </v-card>
      </v-col>
    </v-row>

    <div class="selectResult-div">
      <div class="correctRate-div">정답률: {{ correctRate * 100 }}%</div>
      <div>총 참여자 수: {{ store.submitUserCount }} / {{ store.totalUserCount-1 }} 명</div>
    </div>
  </v-container>

</template>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

li {
  padding: 5px 0;
}

* {
  font-size: small;
}

.card-border {
  display: flex;
  flex-direction: row;
  min-height: 50px;
  justify-content: center;
  align-items: center;
  padding: 10px;
}

.card-border .statement {
  flex-grow: 1;
  height: 100%;
  font-size: 1.5em;
}

.card-border .count {
  height: 100%;
  font-size: 1.2em;
  font-weight: bold;
}

.tof-result-div {
  display: flex;
  flex-direction: column;
}

.selectResult-div {
  padding: 20px 0;
  margin: 20px 0;
}

.correctRate-div {
  font-size: 25px;
  font-weight: bold;
}

.correct-card {
  background-color: #24A319;
}

.selected-card {
  background-color: rgba(238, 85, 85, 0.706);
}

</style>
