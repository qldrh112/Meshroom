# Gitlab ↔ Jira 연동

# 사전 설정

## 1. Jira

### API 토큰 발급 받기

- 지라 프로젝트 → 설정 → Atlassian 계정 설정
    
    ![1.png](1.png)
    
- Atlassian 계정 설정 → 보안 → API 토큰 → API 토큰 만들기 및 관리
    
    ![2.png](2.png)
    
- API 토큰 만들기
    
    ![3.png](3.png)
    
    ![4.png](4.png)
    
    - **주의!**
    API 토큰을 복사할 때, ctrl+C 로 복사하면 안되고, **반드시 ‘복사’ 버튼을 클릭하여 복사하기!**

## 2. Gitlab

### Jira configuration 추가

- gitlab 프로젝트 → Settings → Integrations
    
    ![11.png](11.png)
    
- Jira 앱 추가
    
    ![12.png](12.png)
    
- Jira 설정
    
    ![13.png](13.png)
    
    - Use custom settings 로 변경
    - Web URL : jira 프로젝트의 base url 을 추가할 것
    - Email or username : jira 프로젝트 구성원 중 아무나
    - New API token or password : jira 에서 발급 받은 API 토큰 추가
    
    ![14.png](14.png)
    
    - 설정하고 싶은대로 구성하기
    
    ![15.png](15.png)
    
    - Test settings 로 연동 됐는지 확인하기
    - 완료되면 save changes

---

# 연동 활용하기

## Jira 자동화 (automation) 추가

- Jira 프로젝트 → 프로젝트 설정
    
    ![21.png](21.png)
    
    - 프로젝트 설정을 사용하려면, 프로젝트의 ‘관리자’ 권한이 있어야 한다.
- 프로젝트 설정 → Automation
    
    ![22.png](22.png)
    
- 템플릿 → 원하는 자동화 설정 추가
    
    ![23.png](23.png)
    
    - 하고자 하는 작업에 따라 템플릿을 추가
    - 원한다면 직접 자동화를 구성해서 추가할 수도 있다.
- 규칙 → 추가한 자동화 목록을 볼 수 있음
    
    ![24.png](24.png)
    
    - Automation 페이지에서 다양한 정보들을 활용하고 확인할 수 있다.

## 자동화 적용됐는지 확인해보기