<script setup>
import { ref, computed, watch } from 'vue'
import { useUserStore } from '@/stores/userStore'

import Mushroom1 from '@/assets/mushroom1.svg'
import Mushroom2 from '@/assets/mushroom2.svg'
import Mushroom3 from '@/assets/mushroom3.svg'
import CongratsImage from '@/assets/congrates.svg'

/*
 * @ 닉네임 랜덤뽑기와 관련된 Data 영역
 *
 * userNickname : User의 닉네임
 * nicknameDialog : 닉네임 설정 Modal 창의 등장 여부
 * step : 닉네임 설정 Modal 창의 단계
 *
 * inputName : User가 설정한 본인의 이름
 * selectedAdjectives : 랜덤 버섯에 붙어 있는 형용사들
 * selectedMushroom : User가 뽑은 버섯
 * randomAdjective : User가 뽑은 버섯에 붙어 있는 형용사 -> 최종 닉네임 형용사
 * finalNickname : randomAdjective와 inputName을 합쳐서 만들어낸 최종 닉네임
 *
 * mushrooms : 랜덤 버섯 이미지 Array
 * adjectives : 랜덤 형용사 Array
 */

const props = defineProps({ show: Boolean })
const emits = defineEmits(['update:show', 'nickname-saved'])
const userStore = useUserStore()

watch(
  () => props.show,
  (newVal) => {
    nicknameDialog.value = newVal
    if (newVal) openNicknameDialog()
  }
)

const nicknameDialog = ref(props.show)
const step = ref(1)

const inputName = ref('')
const selectedAdjectives = ref([])
const randomAdjective = ref('')
const selectedMushroom = ref(null)
const finalNickname = computed(() => `${randomAdjective.value} ${inputName.value}`)

const mushrooms = [Mushroom1, Mushroom2, Mushroom3]
const adjectives = [
  '가냘픈',
  '가는',
  '가엾은',
  '가파른',
  '같은',
  '거센',
  '거친',
  '검은',
  '게으른',
  '고달픈',
  '고른',
  '고마운',
  '고운',
  '고픈',
  '곧은',
  '괜찮은',
  '구석진',
  '굳은',
  '굵은',
  '귀여운',
  '그런',
  '그른',
  '그리운',
  '기다란',
  '기쁜',
  '긴',
  '깊은',
  '깎아지른',
  '깨끗한',
  '나쁜',
  '나은',
  '난데없는',
  '날랜',
  '날카로운',
  '낮은',
  '너그러운',
  '너른',
  '널따란',
  '넓은',
  '네모난',
  '노란',
  '높은',
  '누런',
  '눅은',
  '느닷없는',
  '느린',
  '늦은',
  '다른',
  '더러운',
  '더운',
  '덜된',
  '동그란',
  '돼먹잖은',
  '된',
  '둥그런',
  '둥근',
  '뒤늦은',
  '드문',
  '딱한',
  '때늦은',
  '뛰어난',
  '뜨거운',
  '막다른',
  '많은',
  '매운',
  '먼',
  '멋진',
  '메마른',
  '메스꺼운',
  '모난',
  '못난',
  '못된',
  '못생긴',
  '무거운',
  '무딘',
  '무른',
  '무서운',
  '미끄러운',
  '미운',
  '바람직한',
  '반가운',
  '밝은',
  '밤늦은',
  '보드라운',
  '보람찬',
  '부드러운',
  '부른',
  '붉은',
  '비싼',
  '빠른',
  '빨간',
  '뻘건',
  '뼈저린',
  '뽀얀',
  '뿌연',
  '새로운',
  '서툰',
  '섣부른',
  '설운',
  '성가신',
  '센',
  '수줍은',
  '쉬운',
  '스스러운',
  '슬픈',
  '시원찮은',
  '싫은',
  '싼',
  '쌀쌀맞은',
  '쏜살같은',
  '쓰디쓴',
  '쓰린',
  '쓴',
  '아니꼬운',
  '아닌',
  '아름다운',
  '아쉬운',
  '아픈',
  '안된',
  '안쓰러운',
  '안타까운',
  '않은',
  '알맞은',
  '약빠른',
  '약은',
  '얇은',
  '얕은',
  '어두운',
  '어려운',
  '어린',
  '언짢은',
  '엄청난',
  '없는',
  '여문',
  '열띤',
  '예쁜',
  '올바른',
  '옳은',
  '외로운',
  '우스운',
  '의심스런',
  '이른',
  '익은',
  '있는',
  '작은',
  '잘난',
  '잘빠진',
  '잘생긴',
  '재미있는',
  '적은',
  '젊은',
  '점잖은',
  '조그만',
  '좁은',
  '좋은',
  '주제넘은',
  '줄기찬',
  '즐거운',
  '지나친',
  '지혜로운',
  '질긴',
  '짓궂은',
  '짙은',
  '짠',
  '짧은',
  '케케묵은',
  '큰',
  '탐스러운',
  '턱없는',
  '푸른',
  '한결같은',
  '흐린',
  '희망찬',
  '흰',
  '힘겨운'
]

const openNicknameDialog = () => {
  inputName.value = ''
  randomAdjective.value = ''
  selectedMushroom.value = null
  selectedAdjectives.value = []
  step.value = 1
  nicknameDialog.value = true
}

const nextStep = () => {
  if (inputName.value.trim() !== '') {
    generateRandomNickname()
    step.value = 2
  }
}

const generateRandomNickname = () => {
  // randomAdjective.value = adjectives[Math.floor(Math.random() * adjectives.length)]
  selectedAdjectives.value = mushrooms.map(
    () => adjectives[Math.floor(Math.random() * adjectives.length)]
  )
  console.log(selectedAdjectives.value)
}

const selectMushroom = (index) => {
  selectedMushroom.value = mushrooms[index]
  randomAdjective.value = selectedAdjectives.value[index]
}

const saveNickname = () => {
  if (inputName.value.trim() !== '') {
    userStore.setNickname(finalNickname.value)
    emits('nickname-saved')
    closeModal()
  }
}

const closeModal = () => {
  emits('update:show', false)
}
</script>

<template>
  <v-dialog v-model="nicknameDialog" persistent max-width="600px">
    <v-card>
      <v-card-title> 닉네임 만들어보기! </v-card-title>
      <v-card-text v-if="step === 1">
        <v-text-field
          v-model="inputName"
          label="이름을 입력하세요!"
          outlined
          full-width
          @keyup.enter="nextStep"
        ></v-text-field>
      </v-card-text>
      <v-card-text v-if="step === 2">
        <div class="mushroom-container">
          <v-row>
            <v-col cols="4" v-for="(mushroom, index) in mushrooms" :key="index">
              <v-img
                :src="mushroom"
                class="mushroom-animation"
                @click="selectMushroom(index)"
              ></v-img>
            </v-col>
          </v-row>
        </div>
        <div v-if="selectedMushroom" class="final-nickname-overlay">
          <v-img :src="CongratsImage" class="congrats-image"></v-img>
          <h3 class="nickname-animation">
            축하합니다! 당신의 새로운 닉네임은: {{ finalNickname }}
          </h3>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn v-if="step === 1" @click="nextStep" color="primary">Next</v-btn>
        <v-btn v-if="step === 2" @click="saveNickname" color="primary">Save</v-btn>
        <v-btn @click="closeModal">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>
.lm {
  margin-left: 65px;
}
.rm {
  margin-right: 65px;
  cursor: pointer;
}
.mushroom-animation {
  animation: bounce 1s infinite;
  cursor: pointer;
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-30px);
  }
  60% {
    transform: translateY(-15px);
  }
}

.mushroom-container {
  position: relative;
}

.final-nickname-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.8);
}

.congrats-image {
  width: 100%;
  max-width: 300px;
  display: block;
  margin: 0 auto;
}

.final-nickname {
  text-align: center;
  margin-top: 20px;
}

.nickname-animation {
  margin-bottom: 45px;
  margin-bottom: 45px;
  animation: fadeIn 2s ease-in-out;
  font-family: 'Comic Sans MS', cursive, sans-serif;
  color: #ff6f61;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>
