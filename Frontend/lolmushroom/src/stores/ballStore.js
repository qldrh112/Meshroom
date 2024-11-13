import { defineStore } from 'pinia'
import webSocketAPI from '@/api/webSocket'

export const useBallStore = defineStore('ballStore', {
  state: () => ({
    userGroup: null,
    currentGroup: null,
    groupNameMap: new Map(),
    ballMap: new Map()
  }),
  actions: {
    /**
     * IMP 1. BallGrow Game에 대한 Subscription 진행
     * * 1.1 ballMap에 대한 초기화
     * * 1.2 userGroup과 currentGroup 초기화
     * * 1.3 ballGrow Contents에 대한 Subscription 진행
     */
    initSocketConnection(sessionId, subSessionId, groups) {
      groups.forEach((group) => {
        this.groupNameMap.set(group.sessionId, group.groupName)
        this.ballMap.set(group.sessionId, 100)
      })
      this.userGroup = this.currentGroup = subSessionId
      console.log(this.currentGroup)
      webSocketAPI.connect({
        sessionId: sessionId,
        contentsId: 'touch',
        onEventReceived: this.fetchBallData,
        subscriptions: ['game']
      })
    },
    fetchBallData(event) {
      const { sessionId, size } = event
      this.ballMap.set(sessionId, size)
    },
    onBallClick(sessionId, subSessionId) {
      const clickData = {
        mainSessionId: sessionId,
        sessionId: subSessionId,
        type: 'DECREASE'
      }
      if (this.currentGroup === this.userGroup) {
        clickData.type = 'INCREASE'
      }
      console.log(clickData)
      webSocketAPI.sendClickData('/publish/game/touch', clickData)
    },
    onChangeClick(sessionId) {
      this.currentGroup = sessionId
    },
    onReturnClick() {
      this.currentGroup = this.userGroup
    }
  },
  getters: {
    getCurrentGroup: (state) => {
      return state.currentGroup
    },
    getCurrentGroupName: (state) => (groupId) => {
      return state.groupNameMap.get(groupId)
    },
    getBallSize: (state) => (groupId) => {
      return state.ballMap.get(groupId)
    },
    getIsMyBall: (state) => (groupId) => {
      return groupId === state.userGroup
    },
    getTotalBalls: (state) => {
      const totalBalls = []
      state.ballMap.forEach((size, sessionId) => {
        totalBalls.push({
          sessionId,
          groupName: state.groupNameMap.get(sessionId),
          size
        })
      })
      return totalBalls.sort((a, b) => b.size - a.size)
    },
    getUserGroup: (state) => {
      return state.userGroup
    }
  }
})
