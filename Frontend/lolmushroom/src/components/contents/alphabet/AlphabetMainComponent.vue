<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { useSessionStore } from '@/stores/sessionStore'
import { useUserStore } from '@/stores/userStore'
import contentsAPI from '@/api/contents'
import webSocketAPI from '@/api/webSocket'
import CountDownComponent from '@/components/contents/CountDownComponent.vue'
import WaitingHeader from '@/components/room/playerWaiting/WaitingHeader.vue'

const userStore = useUserStore()
const sessionStore = useSessionStore()
const isTimeUp = ref()
const counting = ref(true)
const showAlert = ref()
const index = ref(0)
const guessWord = ref('')
const guessWords = ref([])
// const areSubmitAnswer = computed(() => store.submitUserCount === store.totalUserCount)
const isCorrected = ref()

const turn = reactive({
  ownerOvToken: '',
  answer: '',
  winner: ''
})

// 카운트 다운이 종료되면 Main화면을 렌더링하는 함수
const timeUp = (bool) => {
  isTimeUp.value = bool
  counting.value = !bool
}

// 모든 문제를 가져옴
const quizWords = await contentsAPI.getQuizWords(sessionStore.subSessionId)
const alliIniQuizInfos = quizWords['data']['result']['alliIniQuizInfos']
turn.ownerOvToken = alliIniQuizInfos[index.value]['ovToken']

// 정답을 발행
const publishGuess = () => {
  if (guessWord.value === '') {
    showAlert.value = true
  } else {
    const data = {
      ownerOvToken: turn.ownerOvToken,
      ovToken: userStore.userOvToken,
      userName: userStore.userName,
      guessWord: guessWord.value.trim()
    }
    webSocketAPI.sendAnswerData(
      `/publish/game/ini-quiz/guess/${sessionStore.sessionId}/${sessionStore.subSessionId}`,
      data
    )
    guessWord.value = ''
  }
}

// 랜덤 색상 생성 함수
const generateRandomColor = () => {
  const letters = '0123456789ABCDEF'
  let color = '#'
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)]
  }
  return color
}

// 다른 사용자의 정답을 구독
const onGuessReceived = (event) => {
  const { ovToken, result, submittedWord, userName } = event
  const wordData = {
    word: submittedWord,
    top: Math.random(),
    left: Math.random(),
    // 10% ~ 90% 사이의 랜덤 위치
    initialTop: Math.random() * 80 + 10 + '%',
    initialLeft: Math.random() * 80 + 10 + '%',
    color: generateRandomColor(),
    ovToken: ovToken
  }
  guessWords.value.push(wordData)

  // 만약 정답이 나오면
  if (result === true && ovToken !== turn.ownerOvToken) {
    turn.answer = submittedWord
    turn.winner = userName
    isCorrected.value = true
    guessWords.value = []
    index.value += 1
  }
  
}

// index 값이 증가할 때마다 관련 값 갱신
watch(index, async (newIndex) => {
  if (newIndex < alliIniQuizInfos.length) {
    turn.ownerOvToken = alliIniQuizInfos[newIndex]['ovToken']
    // 3초 후에 isCorrected를 false로 변경하고 index 증가
    setTimeout(() => {
      isCorrected.value = false
    }, 3000)
    return
  } else if (turn.ownerOvToken === userStore.userOvToken){
    setTimeout(async () => {
      await contentsAPI.finishContents(sessionStore.subSessionId)
    }, 100);
  } 
})

onMounted(async () => {
  console.log('초성 게임 연결 중..')
  // 세션 연결
  webSocketAPI.connect({
    sessionId: sessionStore.sessionId,
    subSessionId: sessionStore.subSessionId,
    onEventReceived: onGuessReceived,
    subscriptions: ['guess']
  })
})
</script>

<template>
  <!-- <div class="header">
      공통 컴포넌트인 헤더 넣어야됨
  </div> -->
  <div class="container" v-show="isTimeUp && !isCorrected">
    <WaitingHeader
      first-description="초성 맞추기"
      second-description="초성을 토대로 단어를 맞춰보세요!"
      third-description="순서는 랜덤이랍니다!"
    />

    <div class="statusContainer">
      <div class="info">
        <div class="info-category">카테고리</div>
        <div class="info-text">{{ alliIniQuizInfos[index]['categoryName'] }}</div>
      </div>
    </div>
    <div class="playContainer">
      <div class="userInput">
        <div class="emojiField1"></div>
        <div div :guessWord class="initialBox">{{ alliIniQuizInfos[index]['quizWord'] }}</div>
      </div>
      <div class="userInput">
        <div class="emojiField2"></div>
        <input
          v-html="guessWord"
          class="inputText"
          :placeholder="
            turn.ownerOvToken !== userStore.userOvToken
              ? '정답을 입력 해주세요.'
              : '힌트를 줄 수 있습니다.'
          "
          v-model="guessWord"
          @keyup.enter="publishGuess()"
        />
      </div>
      <div></div>
      <button class="submit" @click="publishGuess()">정답 맞추기</button>
      <!-- 침여자의 답변 렌더링 -->
      <div
        v-for="wordData in guessWords"
        :key="wordData.ovToken"
        class="moving-word"
        :style="{
          '--initial-top': wordData.initialTop,
          '--initial-left': wordData.initialLeft,
          '--random-top': wordData.top,
          '--random-left': wordData.left,
          color: wordData.color
        }"
      >
        <h2 v-if="wordData.ovToken === turn.ownerOvToken" style="font-size: 200%">
          {{ wordData.word }}
        </h2>
        <h2 v-else>{{ wordData.word }}</h2>
      </div>
    </div>
  </div>

  <v-container class="countdown-container" v-show="!isTimeUp">
    <div class="countdown-timer">
      <CountDownComponent
        v-if="counting"
        time="2.5"
        text="집중해서 초성을 맞춰보세요!"
        @end-count-down="timeUp(true)"
      />
    </div>
  </v-container>

  <div class="countdown-container" v-show="isCorrected">
    <h1 class="countdown-number">정답: {{ turn.answer }}</h1>
    <br />
    <br />
    <div class="countdown-tooltip">
      <h2 style="color: white">정답자: {{ turn.winner }}</h2>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  background-color: #e7ffde;
  height: 100%;
}
.header {
  margin: 20px auto auto;
  width: 1856px;
  height: 94px;
  background-color: blueviolet;
}

.statusContainer {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.progress-bar {
  width: 1000px;
  height: 69px;
  background-color: yellow;
}

.info {
  display: flex;
  width: 700px;
  height: 60px;
  border-radius: 5px;
  margin-top: 20px;
}
.info-category {
  margin: 0;
  height: 60px;
  width: 350px;
  border-radius: 10px 0 0 10px;
  background-color: #fcee59d5;
  font-weight: bold;
  font-size: 20px;
  letter-spacing: 7px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 5px;
}
.info-text {
  margin: 0;
  height: 60px;
  width: 550px;
  border-radius: 0 10px 10px 0;
  background-color: #d4d4d474;
  font-weight: bold;
  font-size: 30px;
  letter-spacing: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 5px;
}
.playContainer {
  margin-top: 10px;
  height: 650px;
  display: flex;
  flex-direction: column;
  align-items: center; /* 수직으로 가운데 정렬 */
  /* flex: 1; */
  justify-content: space-around; /* 수평으로 가운데 정렬 */
}

.userInput {
  width: 600px;
  height: 134px;
  border-radius: 20px;
  background-color: #fff;
  border: 2px solid black;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 3px 15px;
}
.emojiField1 {
  width: 60px;
  height: 60px;
  /* background-color: #1F4F16; */
  background-image: url('../../../../src/assets/image/thinking_face.svg');
  margin-right: 10px;
}

.emojiField2 {
  width: 60px;
  height: 60px;
  background-image: url('../../../../src/assets/image/smile_face.svg');
  margin-right: 10px;
}
.initialBox {
  flex: 1;
  height: 120px;
  outline: none; /* 포커스 시 나타나는 기본 테두리 제거 */
  border: none; /* 기본 테두리 제거 */
  background-color: transparent; /* 배경색 투명하게 설정 */
  font-size: 64px; /* 원하는 폰트 크기로 조정 */
  font-weight: bold;
  padding: 10px; /* 내부 여백 추가 */
  text-align: center;
}
.inputText::placeholder{
  font-size: 20px;
  padding: 10px;
}
.inputText {
  flex: 1;
  height: 60px;
  outline: none; /* 포커스 시 나타나는 기본 테두리 제거 */
  border: none; /* 기본 테두리 제거 */
  background-color: transparent; /* 배경색 투명하게 설정 */
  font-size: 25px; /* 원하는 폰트 크기로 조정 */
  padding: 10px; /* 내부 여백 추가 */
  letter-spacing: 5px;
}

/* 정답 확인창 스타일 */
.countdown-container {
  margin-top: 10%;
  background: #247719;
  opacity: 0.7;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  text-align: center;
}

.countdown-number {
  font-size: 120px;
  animation: countdown-animation 1s linear infinite;
}

@keyframes countdown-animation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.countdown-tooltip h1 {
  font-size: 40px;
  margin: 10px 0;
  color: '#FFFFFF';
}

/* 선택적: WebKit 브라우저(Chrome, Safari 등)의 자동 채우기 스타일 제거 */
.inputText:-webkit-autofill,
.inputText:-webkit-autofill:hover,
.inputText:-webkit-autofill:focus,
.inputText:-webkit-autofill:active {
  transition: background-color 5000s ease-in-out 0s;
  -webkit-text-fill-color: inherit !important;
}

.submit {
  font-size: 32px;
  width: 292px;
  height: 94px;
  background-color: #24a319;
  border-radius: 20px;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.warning-alert {
  width: 100%;
}

/* css 애니메이션 정의 */
.moving-word {
  position: absolute;
  animation: moveWord 3s infinite alternate ease-in-out;
}

@keyframes moveWord {
  0% {
    top: var(--initial-top);
    left: var(--initial-left);
  }
  100% {
    top: calc(90% * var(--random-top));
    left: calc(90% * var(--random-left));
  }
}
</style>
