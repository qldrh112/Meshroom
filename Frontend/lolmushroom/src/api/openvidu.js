// import dotenv from 'dotenv';
// // env 파일에서 환경 변수를 읽어 node.js 환경에 설정
// dotenv.config(!!process.env.CONFIG ? {path: process.env.CONFIG} : {});

// 웹 서버 프레임워크
import express from 'express'
// HTTP 요청 본문을 파싱하는 미들웨어
import bodyParser from 'body-parser'
// 기본 http 서버 모듈
import http from 'http'
// openvidu 서버와의 통신을 위한 클라이언트 라이브러리
import { OpenVidu } from 'openvidu-node-client'
// cors를 설정하는 미들웨어
import cors from 'cors'

// express 인스턴스 생성
const app = express()

// Environment variable: PORT where the node server is listening
const { VITE_SERVER_PORT } = import.meta.env
// Environment variable: URL where our OpenVidu server is listening
// const OPENVIDU_URL = process.env.OPENVIDU_URL || 'http://localhost:4443';
const { VITE_OPENVIDU_URL } = import.meta.env || 'http://localhost:4443'
// Environment variable: secret shared with our OpenVidu server
const { VITE_OPENVIDU_SECRET } = import.meta.env || 'MY_SECRET'
// const OPENVIDU_SECRET = process.env.OPENVIDU_SECRET || 'MY_SECRET';

// Enable CORS support
// cors 활성화
// 어떤 도메인에서의 요청을 허용할 것인가를 설정
app.use(
  cors({
    origin: '*'
  })
)

// http 서버 생성, openvidu 클라이언트 인스턴스 생성
const server = http.createServer(app)
const openvidu = new OpenVidu(VITE_OPENVIDU_URL, VITE_OPENVIDU_SECRET)

// body-parser 미들웨어를 사용하여 URL 인코딩 데이터와 Json 데이터 파싱
// Allow application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true }))
// Allow application/json
app.use(bodyParser.json())

// public 디렉토리의 정적 파일을 서빙
app.use(express.static('public'))

// 서버를 설정된 포트에서 시작하고 openvidu 서버에 연결된다는 로그 출력
server.listen(VITE_SERVER_PORT, () => {
  console.log('Application started on port: ', VITE_SERVER_PORT)
  console.warn('Application server connecting to OpenVidu at ' + VITE_OPENVIDU_URL)
})

// POST 요청 수신 시 새로운 Openvidu 세션 생성
app.post('/api/sessions', async (req, res) => {
  const session = await openvidu.createSession(req.body)
  res.send(session.sessionId)
})

// POST 요청 수신 시 주어진 세션 ID에 대한 연결 생성
app.post('/api/sessions/:sessionId/connections', async (req, res) => {
  const session = openvidu.activeSessions.find((s) => s.sessionId === req.params.sessionId)
  if (!session) {
    res.status(404).send()
  } else {
    const connection = await session.createConnection(req.body)
    res.send(connection.token)
  }
})

// 예상치 못한 오류는 콘솔에 출력
process.on('uncaughtException', (err) => console.error(err))
