import { defineStore } from 'pinia'
const { VITE_BASE_URL } = import.meta.env

/**
 * IMP : SessionStore 정의
 * * sessionId ( Main Session )과 subSessionId을 저장하고 있음
 * * 이 정보는 Session Storage에 저장되고 있음.
 */
export const useSessionStore = defineStore('sessionInfo', {
  state: () => ({
    sessionId: null,
    subSessionId: null,
    sessionUrl: ''
  }),
  actions: {
    setSessionId(sessionId) {
      this.sessionId = sessionId
      this.setSessionUrl(sessionId)
    },
    setSubSessionId(subSessionId) {
      this.subSessionId = subSessionId
    },
    setSessionUrl(sessionId) {
      this.sessionUrl = `${VITE_BASE_URL}/${sessionId}`
    }
  },
  getters: {
    getSessionId: (state) => state.sessionId,
    getSubSessionId: (state) => state.subSessionId,
    getSessionUrl: (state) => state.sessionUrl
  },
  persist: {
    key: 'session-info-store',
    storage: sessionStorage, // 세션 스토리지에 저장
    paths: ['sessionId', 'subSessionId', 'sessionUrl'] // 저장할 상태의 경로
  }
})
