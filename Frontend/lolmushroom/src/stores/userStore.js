import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: null,
    userName: null,
    isTeamLeader: false,
    isStarted: false,
    userOvToken: null
  }),
  actions: {
    setUserId(userId) {
      this.userId = userId
    },
    setName(name) {
      this.userName = name
    },
    setTeamLeader(isTeamLeader) {
      this.isTeamLeader = isTeamLeader
    },
    setIsStarted() {
      this.isStarted = true
    },
    setOvToken(userOvToken) {
      this.userOvToken = userOvToken
    }
  },
  getters: {
    getUserId: (state) => state.userId,
    getName: (state) => state.userName,
    getIsTeamLeader: (state) => state.isTeamLeader,
    getIsStarted: (state) => state.isStarted,
    getUserOvToken: (state) => state.userOvToken
  },
  persist: {
    key: 'user-store',
    storage: sessionStorage,
    paths: ['userId', 'userName', 'isTeamLeader', 'isStarted', 'userOvToken']
  }
})
