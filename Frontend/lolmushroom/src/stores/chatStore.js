import { defineStore } from 'pinia'

/**
 * IMP : 채팅에 대한 정보를 저장하는 Store
 * IMP mainSessionMessages : 전체 Group의 Messages
 * IMP subSessionMessages : 각 Group의 Messages
 * * currentMode : 현재 Chatting의 Mode ( 'All' / 'Group' )
 * * Chatting 정보는 Session Storage에 저장된다.
 */

export const useChatStore = defineStore('chat', {
  state: () => ({
    mainSessionMessages: [],
    subSessionMessages: [],
    currentMode: 'All'
  }),
  actions: {
    addMainSessionMessage(message) {
      this.mainSessionMessages.push(message)
    },
    addSubSessionMessage(message) {
      this.subSessionMessages.push(message)
    },
    resetSubSessionMessage() {
      this.subSessionMessages = []
    },
    setCurrentMode(mode) {
      this.currentMode = mode
    }
  },
  persist: {
    key: 'chat-store',
    storage: sessionStorage, // 세션 스토리지에 저장
    paths: ['mainSessionMessages', 'subSessionMessages', 'currentMode'] // 저장할 상태의 경로
  }
})
