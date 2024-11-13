import { Client } from '@stomp/stompjs'
const { VITE_API_WEBSOCKET_URL } = import.meta.env

let stompClient = null // IMP StompClient는 Singleton으로 관리한다.
let subscriptionMap = new Map() // 구독을 관리할 Map 객체

const connect = ({
  sessionId,
  subSessionId,
  contentsId,
  onMessageReceived,
  onEventReceived,
  onProgressReceived,
  onFinishReceived,
  onNextReceived,
  onConnect,
  onError,
  subscriptions // 추가된 파라미터: 구독할 리스트
}) => {
  // stompClient가 이미 존재하고 연결된 상태라면 새로운 클라이언트를 생성하지 않음
  if (stompClient && stompClient.connected) {
    console.log('Already connected, adding new subscriptions')

    // 기존 연결 상태에서 새 구독 추가
    addSubscriptions(
      sessionId,
      subSessionId,
      contentsId,
      onMessageReceived,
      onEventReceived,
      onProgressReceived,
      onFinishReceived,
      onNextReceived,
      subscriptions
    )

    if (onConnect) {
      onConnect('Already connected')
    }
    return
  }

  stompClient = new Client({
    brokerURL: VITE_API_WEBSOCKET_URL,
    reconnectDelay: 5000,
    heartbeatIncoming: 100000, // 서버에서 클라이언트로 보내는 심장박동 간격
    heartbeatOutgoing: 100000, // 클라이언트에서 서버로 보내는 심장박동 간격
    onConnect: (frame) => {
      console.log('Connected: ' + frame)

      // 구독 설정 및 Join 메시지 전송 (필요한 경우에만)
      addSubscriptions(
        sessionId,
        subSessionId,
        contentsId,
        onMessageReceived,
        onEventReceived,
        onProgressReceived,
        onFinishReceived,
        onNextReceived,
        subscriptions
      )

      if (onConnect) {
        onConnect(frame)
      }
    },
    onStompError: (frame) => {
      console.error('Broker reported error: ' + frame.headers['message'])
      console.error('Additional Details: ' + frame.body)
      if (onError) {
        onError(frame)
      }
    },
    onWebSocketClose: (error) => {
      console.error('WebSocket closed: ', error)
      if (onError) {
        onError(error)
      }
    },
    onWebSocketError: (error) => {
      console.error('WebSocket error: ', error)
      if (onError) {
        onError(error)
      }
    }
  })
  stompClient.activate()
}

const addSubscriptions = (
  sessionId,
  subSessionId,
  contentsId,
  onMessageReceived,
  onEventReceived,
  onProgressReceived,
  onFinishReceived,
  onNextReceived,
  subscriptions
) => {
  if (!stompClient || !stompClient.connected) {
    console.error('STOMP client is not connected')
    return
  }

  if (!subscriptions || subscriptions.length === 0) {
    console.warn('No subscriptions provided')
    return
  }

  // 구독을 선택적으로 추가
  subscriptions.forEach((subscription) => {
    switch (subscription) {
      case 'chat':
        addChatSubscription(sessionId, onMessageReceived)
        break
      case 'session':
        addSessionSubscription(sessionId, onEventReceived)
        break
      case 'progress':
        addProgressSubscription(sessionId, onProgressReceived)
        break
      case 'finish':
        addFinishSubscription(sessionId, onFinishReceived)
        break
      case 'game':
        addGameSubscription(sessionId, contentsId, onEventReceived)
        break
      case 'manageGame':
        addManagerGameSubscription(sessionId, contentsId, onEventReceived)
        break
      case 'question':
        addQuestionSubscription(sessionId, subSessionId, onEventReceived)
        break
      case 'answer':
        addAnswerSubscription(subSessionId, onEventReceived)
        break
      case 'next':
        addNextSubscription(sessionId, subSessionId, onNextReceived)
        break
      case 'word':
        addWordSubscription(subSessionId, onEventReceived)
        break
      case 'guess':
        addGuessSubscription(sessionId, subSessionId, onEventReceived)
        break
      default:
        console.warn(`Unknown subscription type: ${subscription}`)
    }
  })
}

const addChatSubscription = (sessionId, onMessageReceived) => {
  const sessionKey = `session-${sessionId}`
  if (!subscriptionMap.has(sessionKey)) {
    const sessionSubscription = stompClient.subscribe(
      `/subscribe/chat/session/${sessionId}`,
      (message) => {
        console.log(`Received message from session ${sessionId}:`, message.body)
        if (onMessageReceived) {
          onMessageReceived(JSON.parse(message.body), sessionId) // sessionId를 함께 전달
        }
      }
    )
    subscriptionMap.set(sessionKey, sessionSubscription)

    // 처음 구독할 때만 Join 메시지를 전송
    const joinMessage = {
      sessionId: sessionId,
      content: '',
      timestamp: new Date().toISOString()
    }
    stompClient.publish({
      destination: `/publish/chat/join`,
      body: JSON.stringify(joinMessage)
    })
  }
}

const addSessionSubscription = (sessionId, onEventReceived) => {
  const sessionKey = `session`
  if (!subscriptionMap.has(sessionKey)) {
    const sessionSubscription = stompClient.subscribe(
      `/subscribe/sessions/${sessionId}`,
      (message) => {
        console.log('Received event from session Subscribe:', message.body)
        if (onEventReceived) {
          onEventReceived(JSON.parse(message.body))
        }
      }
    )
    subscriptionMap.set(sessionKey, sessionSubscription)
  }
}

const addProgressSubscription = (sessionId, onProgressReceived) => {
  const progressKey = 'progress'
  if (!subscriptionMap.has(progressKey)) {
    const contentsSubscription = stompClient.subscribe(
      `/subscribe/contents/${sessionId}`,
      (message) => {
        console.log('Received event from Contents Subscribe', message.body)
        if (onProgressReceived) {
          onProgressReceived(JSON.parse(message.body))
        }
      }
    )
    subscriptionMap.set(progressKey, contentsSubscription)
  }
}

// 진행자가 콘텐츠 종료를 구독하는 함수 => 이거 아님..
const addFinishSubscription = (sessionId, onFinishReceived) => {
  const finishKey = 'finsih'
  if (!subscriptionMap.has(finishKey)) {
    const finishSubscription = stompClient.subscribe(
      `/subscribe/contents/${sessionId}/finish`,
      (message) => {
        console.log('Received event from Finish Subscribe', message.body)
        if (onFinishReceived) {
          onFinishReceived(JSON.parse(message.body))
        }
      }
    )
    subscriptionMap.set(finishKey, finishSubscription)
  }
}

// 진술서 구독 시 내용 전달받기
const addQuestionSubscription = (sessionId, subSessionId, onEventReceived) => {
  const questionKey = 'question'
  if (!subscriptionMap.has(questionKey)) {
    const gameSubscription = stompClient.subscribe(
      `/subscribe/game/tf/question/${sessionId}/${subSessionId}`,
      (event) => {
        console.log('Received event from Subscribe - 진술서 작성')
        if (onEventReceived) {
          onEventReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(questionKey, gameSubscription)
  }
}

const addAnswerSubscription = (subSessionId, onEventReceived) => {
  const answerKey = 'answer'
  if (!subscriptionMap.has(answerKey)) {
    const contentsSubscription = stompClient.subscribe(
      `/subscribe/game/tf/answer/${subSessionId}`,
      (event) => {
        console.log('Received event from Subscribe - 답변')
        if (onEventReceived) {
          onEventReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(answerKey, contentsSubscription)
  }
}

const addNextSubscription = (sessionId, subSessionId, onNextReceived) => {
  const nextKey = 'next'
  if (!subscriptionMap.has(nextKey)) {
    const contentsSubscription = stompClient.subscribe(
      `/subscribe/game/tf/next/${sessionId}/${subSessionId}`,
      (event) => {
        console.log('Received event fromSubscribe - 다음 발표자', event.body)
        if (onNextReceived) {
          onNextReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(nextKey, contentsSubscription)
  }
}

const addWordSubscription = (subSessionId, onEndReceived) => {
  const wordKey = 'word'
  if (!subscriptionMap.has(wordKey)) {
    const wordSubscription = stompClient.subscribe(
      `/subscribe/game/ini-quiz/word/${subSessionId}`,
      (event) => {
        console.log('Received event from word')
        if (onEndReceived) {
          onEndReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(wordKey, wordSubscription)
  }
}

// 초성 게임 단어 제출 및 다른 사람의 예측 단어 구독
const addGuessSubscription = (sessionId, subSessionId, onEventReceived) => {
  const guessKey = 'guess'
  console.log(`/subscribe/game/ini-quiz/guess/${sessionId}/${subSessionId}`)
  if (!subscriptionMap.has(guessKey)) {
    const guessSubscription = stompClient.subscribe(
      `/subscribe/game/ini-quiz/guess/${sessionId}/${subSessionId}`,
      (event) => {
        console.log('Received event from guess')
        if (onEventReceived) {
          onEventReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(guessKey, guessSubscription)
  }
}

/**
 * IMP : ContentsId을 통해 구독을 하기 때문에, DB를 수정해야 하는 일이 생길 수 있음.
 * @param {*} sessionId
 * @param {*} contentsId
 * @param {*} onEventReceived
 */
const addGameSubscription = (sessionId, contentsId, onEventReceived) => {
  const gameKey = `game-${contentsId}`
  console.log(`${contentsId} Contents에 입장했습니다.`)

  if (!subscriptionMap.has(gameKey)) {
    const gameSubscription = stompClient.subscribe(
      `/subscribe/game/${contentsId}/${sessionId}`,
      (event) => {
        console.log(`Received event from ${contentsId} Subscribe`)
        if (onEventReceived) {
          onEventReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(gameKey, gameSubscription)
  }
}

const addManagerGameSubscription = (sessionId, contentsId, onEventReceived) => {
  const gameKey = `game-${contentsId}`
  console.log(`${contentsId} Contents에 입장했습니다.`)

  if (!subscriptionMap.has(gameKey)) {
    const gameSubscription = stompClient.subscribe(
      `/subscribe/manage/game/${contentsId}/${sessionId}`,
      (event) => {
        console.log(`Received event from ${contentsId} Subscribe`)
        if (onEventReceived) {
          onEventReceived(JSON.parse(event.body))
        }
      }
    )
    subscriptionMap.set(gameKey, gameSubscription)
  }
}

const unsubscribeInput = (targetInput) => {
  const key = targetInput
  if (subscriptionMap.has(key)) {
    const subscription = subscriptionMap.get(key)
    subscription.unsubscribe()
    console.log(`Unsubscribed from Contents ${targetInput}`)
    subscriptionMap.delete(key)
  } else {
    console.error(`No subscription found for Contents ${targetInput}`)
  }
}

const unsubscribeSession = (sessionId) => {
  const sessionKey = `session-${sessionId}`
  if (subscriptionMap.has(sessionKey)) {
    const subscription = subscriptionMap.get(sessionKey)
    subscription.unsubscribe()
    console.log(`Unsubscribed from session ${sessionId}`)
    subscriptionMap.delete(sessionKey)
  } else {
    console.error(`No subscription found for session ${sessionId}`)
  }
}

const unsubscribeGame = (contentsId) => {
  const gameKey = `game-${contentsId}`
  if (subscriptionMap.has(gameKey)) {
    const subscription = subscriptionMap.get(gameKey)
    subscription.unsubscribe()
    console.log(`Unsubscribed from Contents ${contentsId}`)
    subscriptionMap.delete(gameKey)
  } else {
    console.error(`No subScription found for Contents ${contentsId}`)
  }
}

const disconnect = () => {
  if (stompClient !== null) {
    stompClient.deactivate(() => {
      console.log('Disconnected')
    })
  }
}
/**
 * IMP 1. Destination에 따라 Message를 보내는 Code
 * @param {*} destination
 * @param {*} message
 */
const sendMessage = (destination, message) => {
  if (stompClient && stompClient.connected) {
    stompClient.publish({
      destination: destination,
      body: JSON.stringify(message)
    })
    console.log('Sent message:', message)
  } else {
    console.error('WebSocket is not Connected')
  }
}
/**
 * IMP 2. 버섯 키우기 Game 진행 중에, Click Data를 보내는 Code
 * @param {*} destination
 * @param {*} message
 */
const sendClickData = (destination, click) => {
  if (stompClient && stompClient.connected) {
    stompClient.publish({
      destination: destination,
      body: JSON.stringify(click)
    })
  } else {
    console.error('WebSocket is not Connected')
  }
}

// 진술서 작성 완료 시, 제출 여부를 전송하는
// 초성 게임에서 작성 단어를 전송
const sendSubmitData = (destination, data) => {
  if (stompClient && stompClient.connected) {
    stompClient.publish({
      destination: destination,
      body: data
    })
  } else {
    console.error('WebSocket is not Connected')
  }
}

// 진술 선택 완료시, 제출 유저와 제출 문항 전달
const sendAnswerData = (destination, data) => {
  console.log(destination)
  if (stompClient && stompClient.connected) {
    stompClient.publish({
      destination: destination,
      body: JSON.stringify(data)
    })
  } else {
    console.error('WebSocket is not Connected')
  }
}

// 한 참여자의 발표 종료 시
const sendNextData = (destination, data) => {
  if (stompClient && stompClient.connected) {
    stompClient.publish({
      destination: destination,
      body: JSON.stringify(data)
    })
    console.log('발표 종료')
  } else {
    console.error('WebSocket is not Connected')
  }
}

export default {
  connect,
  disconnect,
  sendMessage,
  sendClickData,
  unsubscribeInput,
  unsubscribeSession,
  unsubscribeGame,
  sendSubmitData,
  sendAnswerData,
  sendNextData
}
