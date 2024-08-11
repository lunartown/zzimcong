# zzimcong

[항해99] 찜콩 프로젝트

## 설정 방법

1. 이 저장소를 클론합니다:

   ```
   git clone https://github.com/lunartown/zzimcong.git
   ```

2. `.env.example` 파일을 복사하여 `.env` 파일을 만들고 필요한 환경 변수를 설정합니다:

   ```
   cp .env.example .env
   ```

3. `.env` 파일을 편집하여 실제 데이터베이스 비밀번호 등을 입력합니다.

4. Docker Compose를 사용하여 서비스를 시작합니다:
   ```
   docker-compose up --build
   ```
