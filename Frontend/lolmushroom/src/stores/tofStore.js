import webSocketAPI from '@/api/webSocket'
import { defineStore } from 'pinia'

export const useTOFStore = defineStore('tof', {
  state: () => ({
    totalUserCount: 0,
    submitUserCount: 0,
    targetUserToken: '',
    chosenArray: {
      1: [],
      2: [],
      3: [],
      4: []
    },
    statements: [],

    /**
     * IMP 1. Manager를 위한 정보
     */
    tofMap: new Map()
  }),

  actions: {
    initSocketConnection(sessionId, groups) {
      groups.forEach((group) => {
        this.tofMap.set(group.sessionId, {
          groupName: group.groupName,
          groupSize: group.occupants,
          groupStep: 1,
          groupStepInfo: 0
        })
      })

      webSocketAPI.connect({
        sessionId: sessionId,
        contentsId: 'tf',
        onEventReceived: this.fetchTOFData,
        subscriptions: ['manageGame']
      })
    },
    fetchTOFData(event) {
      console.log(event)
      const { sessionId, curStep, submitCount, finishCount } = event
      const groupInfo = this.tofMap.get(sessionId)
      if (curStep === 1) {
        groupInfo.groupStep = curStep
        groupInfo.groupStepInfo = submitCount
      } else {
        groupInfo.groupStep = curStep
        groupInfo.groupStepInfo = finishCount
      }
      this.tofMap.set(sessionId, groupInfo)
    },

    submitUserIncrease() {
      this.submitUserCount++
    },
    addChosenData(user, chosen) {
      this.chosenArray[chosen].push(user)
    },
    submitUserClear() {
      this.submitUserCount = 0
    }
  },
  getters: {
    getCurrentInfoBySubSessionId: (state) => (sessionId) => {
      return state.tofMap.get(sessionId)
    }
  }
})
