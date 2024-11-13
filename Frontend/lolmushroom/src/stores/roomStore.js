import { defineStore } from 'pinia'

/**
 * IMP RoomStore
 * * Progress Socket Subscribe -> Main Session의 현재 상황에 대한 종합적인 정보를 가져온다.
 * * 1. activeButtonIndex : 활성화된 Room의 Index를 표시한다. (=> 활성화 Group + 1)
 * * 2. maxUserCount : 현재 Main Session가 수용할 수 있는 최대 참여자를 체크한다.
 * * 3. currentUserCount : 현재 Main Session에 참여자가 몇명 있는 지 체크한다.
 * * 4. url : 현재 Main Session에 대한 URL을 가져온다.
 * IMP roomCount : 전체 Room의 개수를 나타낸다.
 * IMP rooms : Main Session의 정보를 바탕으로 갱신한 Room에 대한 정보
 * ! sessionId, groupName, capacity, occupants, users, isReady, buttonClicked
 * * Room Store에 대한 정보는 Session Storage에 저장된다.
 */
export const useRoomStore = defineStore('room', {
  state: () => ({
    maxUserCount: 0,
    currentUserCount: 0,
    url: '',
    roomCount: 0,
    maxRoomCount: 10,
    rooms: Array.from({ length: 10 }, () => ({
      sessionId: '',
      groupName: '',
      capacity: 10,
      occupants: 0,
      users: [],
      isReady: false,
      teamLeaderId: '',
      isActive: false
    }))
  }),
  actions: {
    setSessionData(sessionData) {
      this.maxUserCount = sessionData.maxUserCount
      this.currentUserCount = sessionData.currentUserCount
      this.url = sessionData.url
      this.setRooms(sessionData.groups)
    },
    setRooms(groups) {
      groups.forEach((group, index) => {
        this.rooms[index] = {
          sessionId: group.sessionId,
          groupName: group.groupName,
          capacity: group.maxUserCount,
          occupants: group.currentUserCount,
          users: group.username,
          isReady: group.isReady,
          teamLeaderId: group.teamLeaderId,
          isActive: true
        }
        this.activeButtonIndex = index + 1
      })
    },

    setButtonClicked(index) {
      if (index >= 0 && index < this.rooms.length) {
        this.rooms[index].isActive = true
      }
    }
  },
  getters: {
    getRooms: (state) => state.rooms,
    getActiveRooms: (state) => state.rooms.filter((room) => room.isActive),
    getTotalUserCount: (state) => state.currentUserCount,
    getTotalMaxUserCount: (state) => state.maxUserCount,
    getGroupInfoBySessionId: (state) => (sessionId) => {
      const groupInfo = state.rooms.find((room) => room.sessionId === sessionId)
      return groupInfo
    }
  },
  persist: {
    key: 'room-info-store',
    storage: sessionStorage,
    paths: ['activeButtonIndex', 'maxUserCount', 'currentUserCount', 'roomCount', 'rooms']
  }
})
