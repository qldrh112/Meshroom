import axios from 'axios'
const API_URL = '/api/v1'

export default {
  createSession(contentsOrder, success, failure) {
    // IMP All : Main Session을 생성한다. => 만드는 사람이 Manager가 될 것이다.
    return axios.post(`${API_URL}/sessions`, contentsOrder).then(success).catch(failure)
  },
  createSubSession(sessionId, success, failure) {
    // * Player : Sub Session을 '생성'한다.
    return axios.post(`${API_URL}/sessions/${sessionId}`).then(success).catch(failure)
  },
  getSessionInfo(sessionId, success, failure) {
    // IMP All : Main Or Sub Session 정보를 가져온다.
    return axios.get(`${API_URL}/sessions/${sessionId}`).then(success).catch(failure)
  },
  getSubSessionInfo(sessionId, subSessionId, success, failure) {
    // * Player : Sub Session의 정보를 가져온다.
    return axios
      .get(`${API_URL}/sessions/${sessionId}/${subSessionId}`)
      .then(success)
      .catch(failure)
  },
  getSessionConnection(sessionId, userName, success, failure) {
    // IMP All : Main Or Sub Session에 Connection을 생성한다. ( 입장과 같은 역할을 한다. )
    return axios
      .post(`${API_URL}/sessions/${sessionId}/connections`, userName)
      .then(success)
      .catch(failure)
  },
  changeUserName(userName, success, failure) {
    // * All : userName을 변경한다.
    return axios.patch(`${API_URL}/users/usernmae`, userName).then(success).catch(failure)
  },
  changeSubSessionName(subSessionId, success, failure) {
    // IMP Team Leader : Team의 이름을 변경할 수 있다. 
    return axios.get(`${API_URL}/sessions/${subSessionId}/group-name`)
    .then(success)
    .catch(failure)
  },
  getSubSessionReady(subSessionId, success, failure) {
    // IMP Team Leader : Team의 Ready 여부를 Toggle할 수 있다.
    return axios.get(`${API_URL}/sessions/${subSessionId}/ready`).then(success).catch(failure)
  },
  getSubSessionQuit(subSessionId, success, failure) {
    // IMP Player : Team의 Connection을 끊고 나갈 수 있다.
    return axios.get(`${API_URL}/sessions/${subSessionId}/quit`).then(success).catch(failure)
  }
}
