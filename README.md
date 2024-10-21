# ⚾ lets-baseball-batch 

### 1. 프로젝트 개요

네이버에서 제공하는 KBO 구단 순위, 경기 일정 등의 데이터를 스크래핑하는 배치 서비스

### 2. 프로젝트 구성

![image](https://github.com/user-attachments/assets/1c8ec93a-1abc-4bb2-ae6b-b5561c188885)

1. 회원가입/로그인 및 예매 기능을 수행하는 `API 서비스`와 KBO 경기 일정 및 순위, 점수를 스크래핑하는 `배치 서비스`로 구성
2. 배치 서비스의 경우, 젠킨스를 통해 배포 및 이력 관리
3. 로그인 시, 회원의 TOKEN 값을 관리하는 redis 서버를 구성

### 3. 프로젝트 목표

1. 정적 스크래핑과 동적 스크래핑을 위한 라이브러리 추가 및 사용
2. 웹 사이트에서 필요한 데이터를 받아오는 배치 서비스 구현
3. 배치 실행 시, 예외가 발생하였을 때 failover 기능 구현(retry, skip)

### 4. 주요기능

1. 필요 데이터를 받아오는 스크래핑 기능
2. 젠킨스를 통한 스프링 배치 실행

### 5. DB 설계

![image](https://github.com/user-attachments/assets/d3b4bcff-5a33-49da-ac4f-85c32cc21a18)

### 6. 그 외 프로젝트 관련 블로그 글

<a href="https://jejeongeun.tistory.com/entry/JSOUP%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9B%B9-%EC%8A%A4%ED%81%AC%EB%9E%98%ED%95%91">JSOUP을 이용한 웹 스크래핑</a><br>
<a href="https://jejeongeun.tistory.com/entry/selenium%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9B%B9-%EC%8A%A4%ED%81%AC%EB%9E%98%ED%95%91">selenium을 이용한 웹 스크래핑</a><br>
추가 작성 중...
