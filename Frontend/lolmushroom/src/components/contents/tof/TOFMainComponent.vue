<script setup>
import { onMounted, ref, watch } from 'vue'
import { useContentsStore } from '@/stores/contentsStore'
import { useUserStore } from '@/stores/userStore'
import { useSessionStore } from '@/stores/sessionStore'
import { useTOFStore } from '@/stores/tofStore'
import { useRouter } from 'vue-router'
import sessionAPI from '@/api/session'
import contentsAPI from '@/api/contents'
import webSocketAPI from '@/api/webSocket'
import ButtonComponent from '@/components/common/ButtonComponent.vue'
import ProgressBar from '@/components/common/OtherUserWaitingComponent.vue'
import OpenViduComponent from '@/components/contents/tof/openvidu/OpenViduComponent.vue'
import TOFAppBar from '@/components/contents/tof/TOFAppBar.vue'
import TOFResultComponent from '@/components/contents/tof/TOFResultComponent.vue'
import TOFSideUserComponent from '@/components/contents/tof/TOFSideUserComponent.vue'
import CountDownComponent from '@/components/contents/CountDownComponent.vue'
import TOFAnswerComponent from './TOFAnswerComponent.vue'
import WaitingHeader from '@/components/room/playerWaiting/WaitingHeader.vue'

const store = useTOFStore()
const router = useRouter()
const contentsStore = useContentsStore()
const userStore = useUserStore()
const sessionStore = useSessionStore()
const counting = ref(true)
const index = ref(0)
const sessionUserNameList = await getSubSessionInfo(
  sessionStore.sessionId,
  sessionStore.subSessionId
)

const allStatements = ref('')
allStatements.value = await getStatements()
console.log(allStatements.value)
store.targetUserToken = ref(allStatements.value[index.value].ovToken)
store.statements = allStatements.value[index.value].statements
const answer = ref(allStatements.value[index.value]['falseIndex'] + 1)

const userChoice = []
const userCards = []
const firstHalf = ref([])
const secondHalf = ref([])

sessionUserNameList.result.username.forEach((name) => {
  userChoice.push(name)
  // 여기에 ovtoken을 꼽아서 발표 진행 때 highrighted 효과 부여할 수 있게 해야 한다.
  userCards.push({
    name,
    src: 'public/favicon.ico'
  })
  // 랜덤으로 배열 순서를 조작하는 로직 추가 필요
  firstHalf.value = userCards.slice(0, Math.ceil(userCards.length / 2))
  secondHalf.value = userCards.slice(Math.ceil(userCards.length / 2))
})

// 이것도 다른 유저의 진술 가져와서 렌더링하게 로직 수정해야 함
const selectedAnswer = ref()
const isSubmitAnswer = ref()
const areSubmitAnswer = ref()
const isTimeUp = ref()

// 세션에 참가한 유저 정보를 요청하는 함수
async function getSubSessionInfo(sessionId, subSessionId) {
  const response = await sessionAPI.getSubSessionInfo(sessionId, subSessionId)
  return response.data
}

// 세션의 참가한 전체 유저의 모든 진술을 가져오는 함수
async function getStatements() {
  const response = await contentsAPI.getStatements(sessionStore.subSessionId)
  return response.data.result.allTFInfos
}

// 답변을 제출했다면 제출 현황을 확인
const submitAnswer = (selected) => {
  const data = {
    ovToken: userStore.userOvToken,
    chosen: selected
  }
  isSubmitAnswer.value = true
  webSocketAPI.sendAnswerData(`/publish/game/tf/answer/${sessionStore.subSessionId}`, data)
}

// 10초의 시간이 지나면 카메라와 질문이 공개됨
const timeUp = (bool) => {
  isTimeUp.value = bool
  counting.value = !bool
}

// 선택한 버튼 시각적으로 활성화하는 함수
const buttonActivate = (key) => {
  selectedAnswer.value = key
}

const targetUserUpdate = () => {
  // 현재 finish 소켓 사용 불가
  //   if (index.value < store.totalUserCount - 1) {
  webSocketAPI.sendNextData(
    `/publish/game/tf/next/${sessionStore.sessionId}/${sessionStore.subSessionId}`,
    true
  )
  //   } else {
  //     const data = {
  //       sessionId: sessionStore.subSessionId,
  //     }
  //     webSocketAPI.sendFinishData('/publish/game/tf/finish', data)
  //   }
}

// subscribe하면 받는다.
const onAnswerReceived = (event) => {
  const { ovToken, chosen, userName } = event
  store.submitUserIncrease()
  store.addChosenData(userName, chosen)
  if (store.submitUserCount === store.totalUserCount - 1) {
    areSubmitAnswer.value = true
  }
}

const onNextReceived = async (_event) => {
  if (index.value < store.totalUserCount - 1) {
    selectedAnswer.value = null
    isSubmitAnswer.value = false
    areSubmitAnswer.value = false
    store.submitUserCount = 0
    index.value++
    timeUp('')
  } else if (store.targetUserToken === userStore.userOvToken) {
    setTimeout(async () => {
      await contentsAPI.finishContents(sessionStore.subSessionId)
    }, 3000); 
  }
}

const getCardColor = (key, isHovering) => {
  if (selectedAnswer.value === key) {
    return '#24A319'
  } else if (isHovering) {
    return '#66FF4F'
  } else {
    return undefined
  }
}

// index 값이 증가할 때마다 관련 값 갱신
watch(index, (newIndex, oldIndex) => {
  if (newIndex < store.totalUserCount) {
    store.targetUserToken = allStatements.value[newIndex]['ovToken']
    store.statements = allStatements.value[newIndex].statements
    store.chosenArray = {
      1: [],
      2: [],
      3: [],
      4: []
    }
  }
})

onMounted(async () => {
  console.log('연결 좀...')
  webSocketAPI.connect({
    sessionId: sessionStore.sessionId,
    subSessionId: sessionStore.subSessionId,
    contentsName: 'tf',
    onEventReceived: onAnswerReceived,
    onNextReceived: onNextReceived,
    subscriptions: ['answer', 'next']
  })
})
</script>

<template>
  <v-container v-show="isTimeUp" class="container">
    <WaitingHeader
      first-description="진실 혹은 거짓"
      second-description="진실 3개와 거짓 1개로 나를 소개해 보세요!"
      third-description="카메라가 켜지면 자신을 소개해 보세요."
    />
    <div class="keynote-speecher">
      <div class="video-container">
        <OpenViduComponent />
      </div>
    </div>
    <div v-if="store.targetUserToken === userStore.userOvToken" class="next-button">
      <v-btn text="발표 종료" @click.stop="targetUserUpdate()" color="#24A319" />
    </div>

    <!-- 남의 발표 차례일 때 -->
    <div v-if="store.targetUserToken !== userStore.userOvToken">
      <!-- 아직 정답을 고르지 않았다면 -->
      <div v-if="!isSubmitAnswer" class="mt-5">
        <TOFAnswerComponent
          :target-nick-name="allStatements[index].username"
          :selected-answer="selectedAnswer"
          :all-statements="allStatements"
          :index="index"
          @cardSelected="buttonActivate"
          class="my-5"
          width="500"
        />

        <div class="button-container" v-if="selectedAnswer" @click="submitAnswer(selectedAnswer)">
          <ButtonComponent text="선택하기" size="x-large" />
        </div>
      </div>

      <!-- 나는 골랐지만, 남들은 아직 안 골랐다면 -->
      <div v-else-if="isSubmitAnswer && !areSubmitAnswer" class="mt-5" width="500">
        <ProgressBar :current="store.submitUserCount" :total="store.totalUserCount - 1" />
      </div>

      <!-- 모두 답을  -->
      <div v-else>
        <TOFResultComponent
          :target-nick-name="allStatements[index].username"
          :selected-answer="selectedAnswer"
          :answer="answer"
          class="mt-5"
          width="500"
        />
      </div>
    </div>

    <!-- 본인의 발표 차례일 때 -->
    <div v-else>
      <TOFResultComponent
        :target-nick-name="allStatements[index].username"
        :selected-answer="selectedAnswer"
        :answer="answer"
        class="mt-5"
        width="500"
      />
    </div>
  </v-container>

  <v-container class="countdown-container" v-show="!isTimeUp">
    <div class="countdown-timer">
      <CountDownComponent
        v-if="counting"
        time="2.5"
        text="발표자는 누가 될까요? 두근두근."
        @end-count-down="timeUp(true)"
      />
    </div>
  </v-container>
</template>

<style scoped>
.button-container {
  display: flex;
  justify-content: end;
  margin: 16px;
}

.card-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.video-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
}

.text-black {
  font-size: xx-small;
}

.container {
  max-height: 80%;
}

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

.countdown-timer {
  margin-bottom: 20px;
}

.card-border {
  border: solid black 0.5px;
}

.next-button {
  display: flex;
  justify-content: flex-end;
  margin-right: 16px;
}

.keynote-speecher {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
