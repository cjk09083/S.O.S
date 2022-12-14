# S.O.S
<div>
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white"/>
<img src="https://img.shields.io/badge/PHP-777BB4?style=for-the-badge&logo=PHP&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<a href="https://youtu.be/l7W__i4ox20" target="_blank">
<img src="https://img.shields.io/badge/소개영상-FF0000?style=for-the-badge&logo=YouTube&logoColor=white"/>
</a>
</div>

## Senior Observation System : 독거노인 안전 돌보미 (학부 졸업작품) 

<div align="center">
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/%EC%A1%B0%EA%B0%90%EB%8F%842.jpg" width="70%"/>
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/APP%20%EC%A0%84%EC%B2%B4%20%EC%8A%A4%EC%83%B7.jpg" width="17%"/>
</div>



## 목적
- 독거노인의 주거상황 및 건강상태 측정 기술 개발.
- 측정된 정보를 실시간으로 클라우드에 업로드하고 비상상황 발생 유무를 어플리케이션을 통해 관리자가 모니터링 할 수 있는 기술 개발.
- 관리자의 간단한 어플리케이션 조작으로 가스밸브 등 주거상황을 제어하는 시스템 구축.

## 담당
- 안드로이드 어플리케이션
- 웹서버 php, sql 
- 아틱&네이버 클라우드와 데이터 연동, 비상상황 탐지 알고리즘, 알람 발생
- 앱 <-> 서버 <-> 하드웨어 통신 (HTTP, MQTT)

## 기능
### 1. 센서 정보 서버업로드 및 모니터링
 - 실내에서 측정한 각종 센서(전력, 지진, 방문자, 문열림, 화재, 비상벨, 모션, 온습도) 정보를 서버와 클라우드에 업로드
 - 전용 어플리케이션(Android)에서 이를 모니터링
<div align="center">
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/sensor.png" width="40%"/>
</div></br>

 - 측정한 데이터중 온습도, 모션센서 데이터는 차트로 확인할 수 있다
<div align="center">
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180926-122917.png" width="55%"/>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/motion.jpg" width="35%"/>
</div></br>

### 2. 비상 상황시 알람
- 비상상황 감지 시 보호자의 어플리케이션으로 알람이 전송
- 알람 종류는 각종 센서값 이상 혹은 독거노인의 움직임 패턴 이상 등 8가지 
<div align="center">
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180925-205157.png" width="30%"/>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180925-205144.png" width="30%"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180925-230019.png" width="30%"/>
</div></br>

### 3. 상담 예약 & 문자 전송
- 보호자가 어플리케이션으로 날짜 선택 후 상담 예약을 잡거나, 문자를 입력하면 독거노인 실내에 설치된 디바이스가 음성으로 안내
<div align="center">
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180926-192714.png" width="30%"/>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180926-192801.png" width="30%"/>
</div></br>

### 4. 외부 기기 제어
- 삼성 SmartThings 와 연동하여 앱에서 제어시, 실내에 있는 가스밸브, 콘센터를 ON/OFF 

### 5. 실내 스트리밍
- 비상상황 감지 시 경고알림 후 실내에 설치된 웹캠이 자동 스트리밍 되며, 이를 보호자가 확인

## 수상
- 문제 해결형 하드웨어 메이커톤 (세종창조경제혁신센터) : 	입선 2018.06
- 창의적 종합설계 경진대회 지역예선 (영남대공학교육혁신센터) : 	대상 2018.09
- IoT 이노베이션 챌린지 (한국전자정보통신산업진흥회) : 	입선 2018.10
- 창의적 종합설계 경진대회 본선 (한국산업진흥원) : 		한국산업진흥원장상 2018.11
