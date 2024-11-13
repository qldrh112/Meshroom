import { defineStore } from 'pinia'
import webSocketAPI from '@/api/webSocket'

// import router from '@/router'

// TOFStore 정의
export const useAlphabetStore = defineStore('alphabet', {
  state: () => ({
    totalUserCount: 0,
    submitUserCount: 0,

    /**
     * IMP 1. Manager를 위한 정보
     */
    alphabetMap: new Map()
  }),

  actions: {
    initSocketConnection(sessionId, groups) {
      groups.forEach((group) => {
        this.alphabetMap.set(group.sessionId, {
          groupName: group.groupName,
          groupSize: group.occupants,
          groupStep: 1,
          groupStepInfo: 0
        })
      })

      webSocketAPI.connect({
        sessionId: sessionId,
        contentsId: 'ini-quiz',
        onEventReceived: this.fetchAlphaBetData,
        subscriptions: ['manageGame']
      })
    },
    fetchAlphaBetData(event) {
      const { sessionId, curStep, submitCount, finishCount } = event
      const groupInfo = this.alphabetMap.get(sessionId)
      if (curStep === 1) {
        groupInfo.groupStep = curStep
        groupInfo.groupStepInfo = submitCount
      } else {
        groupInfo.groupStep = curStep
        groupInfo.groupStepInfo = finishCount
      }
      this.alphabetMap.set(sessionId, groupInfo)
    },
    submitUserIncrease() {
      this.submitUserCount++
    },
    setTotalUser(userCount) {
      this.totalUserCount = userCount
    },
    submitUserClear() {
      this.submitUserCount = 0
    }
  },
  getters: {
    getCurrentInfoBySubSessionId: (state) => (sessionId) => {
      return state.alphabetMap.get(sessionId)
    }
  }
})
