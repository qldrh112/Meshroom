import axios from 'axios'

const API_URL = '/api/v1'

export default {
  getContents(success, failure) {
    // * All : Contents List를 가져온다.
    return axios.get(`${API_URL}/contents`).then(success).catch(failure)
  },
  callNextContents(isStart, success, failure) {
    // IMP Manager : 다음 Contents를 호출한다.
    // ! 다음 Contents의 value가 'null'이면 Ending
    return axios.get(`${API_URL}/contents/next/${isStart}`).then(success).catch(failure)
  },
  recallContents(success, failure) {
    // IMP Manager : 현재 Contents를 다시 호출한다.
    return axios.get(`${API_URL}/contents/reload`).then(success).catch(failure)
  },
  finishContents(subSessionId, success, failure) {
    // IMP Team Leader : Team이 현재 Contents를 종료했음을 알린다.
    return axios.post(`${API_URL}/contents/finish/${subSessionId}`).then(success).catch(failure)
  },

  // 진실 혹은 거짓 관련 API
  createStatements(sessionId, statesObject, success, failure) {
    return axios.post(`${API_URL}/game/tf/${sessionId}`, statesObject).then(success).catch(failure)
  },
  getStatements(sessionId, success, failure) {
    return axios.get(`${API_URL}/game/tf/${sessionId}`).then(success).catch(failure)
  },

  // 초성 게임 관련 API
  getCategory(sessionId, success, failure) {
    return axios.get(`${API_URL}/game/ini-quiz/category/${sessionId}`).then(success).catch(failure)
  },
  createQuizWord(sessionId, subSessionId, object, success, failure) {
    return axios.post(`${API_URL}/game/ini-quiz/${sessionId}/${subSessionId}`, object).then(success).catch(failure)
  },
  getQuizWords(sessionId, success, failure) {
    return axios.get(`${API_URL}/game/ini-quiz/${sessionId}`).then(success).catch(failure)
  }
}
