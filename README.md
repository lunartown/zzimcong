[![프로젝트 문서](https://img.shields.io/badge/프로젝트_문서-000000?style=for-the-badge&logo=notion&logoColor=white)](https://wooden-dust-ea9.notion.site/99-ea2a1783525a40ca8495ba38eaed2151)
[![소스 코드](https://img.shields.io/badge/소스_코드-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/lunartown/zzimcong)
[![프로젝트 Q&A](https://img.shields.io/badge/프로젝트_Q&A-74aa9c?style=for-the-badge&logo=openai&logoColor=white)](여기에_ChatGPT_링크_삽입)

## 🚀 프로젝트 소개

선착순 구매가 가능한 식품 이커머스 플랫폼

- 이 프로젝트는 대규모 트래픽 처리, 동시성 제어, 그리고 마이크로서비스 아키텍처 설계 등 실제 기업에서 마주할 수 있는 기술적 도전들을 경험하고 해결하기 위해 개발되었습니다.

## 목차

- [프로젝트 소개](#-프로젝트-소개)
- [기술 스택](#-기술-스택)
- [실행 방법](#-실행-방법)
- [문서 및 아키텍쳐](#-문서-및-아키텍쳐)
- [주요 기능](#-주요-기능)
- [기술적 의사 결정](#-기술적-의사-결정)
- [트러블 슈팅](#-트러블-슈팅)
- [폴더 구조](#-폴더-구조)

## 🛠 기술 스택

### 🖥 Backend

- **Backend**: Java 22, Spring Boot 3.3.2, Spring Cloud
- **Database**: MySQL, Redis
- **Message Broker**: Kafka
- **Infrastructure**: Docker, Docker Compose
- **기타**: JMeter(부하 테스트), Prometheus(모니터링)

자세한 기술 스택 선정 이유는 [기술적 의사 결정](#-기술적-의사-결정) 항목 참고

## 📝 실행 방법

프로젝트를 실행하시려면, 다음 단계에 따라 진행해주세요:

1. 프로젝트 클론

```shell
$ git clone https://github.com/your-username/zzimcong.git
$ cd zzimcong
```

2. 쉘 스크립트 실행

```shell
chmod +x /run-script.sh
/run-script.sh
```

## 🌟 주요 기능

### **회원가입**

- **이메일 인증**: Gmail SMTP 서버를 사용해 회원가입 시 사용자의 이메일로 인증 메일을 발송하여, 사용자가 실제로 소유한 이메일 주소임을 확인
- **개인정보 암호화**: 사용자의 민감한 개인정보는 **AES256** 알고리즘으로 암호화되어 안전하게 저장되며, 비밀번호는 **BCrypt**를 통해 해싱 및 암호화하여 저장, 보안을 강화

### **로그인**

- **Spring Security 기반 인증**: 사용자 인증 및 인가를 **Spring Security**로 처리하여, 보안성이 높은 로그인 시스템 구축
- **JWT 기반 인증 처리**: 로그인 성공 시 **JWT**를 발급하여 사용자의 세션을 유지하며, 이후 요청 시 JWT를 이용해 인증을 처리
- **Refresh Token 관리**: Refresh token을 레디스에 저장하여 access token이 만료되었을 때 사용자 인증

### **상품**

- **상품 리스트 제공**: 전체 상품의 목록을 효율적으로 제공하며, 필터 및 검색 옵션을 통해 사용자 편의성을 증대
- **상품 상세 페이지 제공**: 각 상품에 대한 상세 정보를 조회할 수 있는 페이지 제공, 상품 정보는 사용자에게 실시간으로 제공됨
- **상품 재고 관리**: **Redis**를 활용한 재고 정보 캐싱을 통해 실시간 재고 상태를 빠르게 확인 가능, 재고 변경 시 자동 업데이트

### **주문**

- **실시간 재고 확인**: 주문 생성 시 **Redis**에 캐싱된 재고 정보를 기반으로 즉시 확인하여, 재고가 부족한 경우 빠르게 사용자에게 알림을 제공. 재고 정보는 상품별로 캐싱되어 성능 저하 없이 다수의 주문 요청을 처리

- **동시성 제어**: 여러 사용자가 동시에 같은 상품을 주문할 경우 발생할 수 있는 **동시성 문제**를 해결하기 위해, Redis의 **Lua Script**을 사용해 재고 수정 시 데이터의 일관성을 보장. 이로 인해 다수의 사용자가 동시에 주문을 시도해도 재고 오류가 발생하지 않음

- **주문 상태 관리**: 주문 생성, 결제 대기, 결제 성공, 결제 실패 등 **주문 상태**를 여러 단계로 나누어 관리. 주문이 생성된 후에도 실시간으로 상태가 변경되어 사용자와 관리자 모두 정확한 정보를 확인할 수 있음

- **SAGA 패턴을 사용한 트랜잭션 관리**: 결제 성공 시 주문 정보와 재고가 동시에 갱신되도록 **트랜잭션 처리**를 적용하여, 주문 생성 및 결제 과정에서 데이터의 일관성을 보장. 또한 주문 과정 중 결제 오류 또는 재고 부족 등 예기치 못한 상황이 발생할 경우, 모든 작업이 자동으로 **롤백**되며, 사용자는 즉시 문제 상황을 안내받고 적절한 대응을 할 수 있음

- **성능 최적화**: Redis와 같은 **인메모리 데이터베이스**를 사용해 재고 확인 및 주문 처리 속도를 최적화하고, 대규모 트래픽 상황에서도 안정적인 주문 처리가 가능하게 설계

- **에러 처리**: 주문 시 발생할 수 있는 예상치 못한 오류(예: 외부 결제 서버 오류, 통신 지연 등)에 대해 타임아웃을 설정하여 예외 처리가 가능하도록 함

### **구현 예정**

- **결제 연동**: 외부 결제 게이트웨이와 연동하여 주문 생성과 동시에 **결제 요청**을 처리. 결제 과정은 **비동기 처리**로 구현하여 결제 서버와의 통신 시간이 길어도 다른 서비스에 영향을 주지 않음. 결제가 완료되면, 주문 상태가 자동으로 업데이트되고 사용자는 즉시 알림을 받음

- **실시간 모니터링 시스템 도입**: Prometheus와 Grafana와 같은 모니터링 도구를 도입하여, 시스템의 실시간 상태를 시각적으로 모니터링. CPU 사용량, 메모리 상태, 네트워크 트래픽과 같은 자원 사용 현황을 대시보드 형태로 시각화. 이를 통해, 트래픽 급증이나 서버 성능 저하를 사전에 감지하고, 즉각적인 대응 가능

- **오케스트레이션**: **마이크로서비스 아키텍처(MSA)** 와 SAGA 패턴을 도입하여 각 서비스를 독립적으로 관리하고 확장할 수 있는 구조로 설계. 향후 Kubernetes와 같은 오케스트레이션 도구를 도입해 각 서비스의 자동 배포, 스케일링, 롤백을 관리. 이를 통해 트래픽 급증 시 자동 스케일링을 적용해 성능을 유지하고, 장애 발생 시에도 무중단 운영 가능토록 구현 예정

- **확장성**: 클라우드 네이티브 인프라로 확장하여, AWS EKS나 GCP GKE를 통해 자원을 유연하게 관리. 이로 인해 시스템의 자동 복구, 로드 밸런싱 및 확장성을 강화할 수 있으며, 클라우드의 기본 기능을 활용해 효율적인 리소스 관리와 신속한 장애 대응이 가능

## 🚧 문서 및 아키텍쳐

### [API 문서](https://wooden-dust-ea9.notion.site/API-de29ea6a3535422d84290f5b1ef9423a)

### ERD 다이어그램

![ERD](<https://file.notion.so/f/f/072d6599-b568-4618-8dae-62b022713c6a/e69bd240-2c38-4446-8039-70a2d93336d5/exported_from_idea.drawio_(1).png?table=block&id=c436fd86-613c-4114-adf8-4902b7b8de52&spaceId=072d6599-b568-4618-8dae-62b022713c6a&expirationTimestamp=1726128000000&signature=NL9jE0EajGrH7ps9pdZ8ZF9oNmqeMHdiUSGq0WBJpW8&downloadName=exported_from_idea.drawio+%281%29.png>)

### 시퀀스 다이어그램

![diagram](https://file.notion.so/f/f/072d6599-b568-4618-8dae-62b022713c6a/de4a5868-7000-42ce-9b6d-087320425525/NCvH2i8m3CRnzvqYx2IYFWuKjml4D61XRKUI2hkzSrGbFkRFxuCSr52ifhb3iY9mgcebiNJBbYF51RD1Vv700YFBCnBtW0fxyUWg9LGonrhxWxWcpXwsvk2DGrG_dQO1XgZ6cMNVRU5pUpckcM2a1uSwD_jEtz85DBoZyk_ny_5LQTNij0q4uRkVOcw50v7wt-PGLOXptm6972f5q0FgcH-tId5KeC8jGGvAX4j02f93ACeX.png?table=block&id=383e91e1-4dd6-46bd-b33f-c509e26221c6&spaceId=072d6599-b568-4618-8dae-62b022713c6a&expirationTimestamp=1726128000000&signature=tymiSG-6KGvYY1y6mq09CHYayGtRrRAO0ZkIrlsw458&downloadName=NCvH2i8m3CRnzvqYx2IYFWuKjml4D61XRKUI2hkzSrGbFkRFxuCSr52ifhb3iY9mgcebiNJBbYF51RD1Vv700YFBCnBtW0fxyUWg9LGonrhxWxWcpXwsvk2DGrG_dQO1XgZ6cMNVRU5pUpckcM2a1uSwD_jEtz85DBoZyk_ny_5LQTNij0q4uRkVOcw50v7wt-PGLOXptm6972f5q0FgcH-tId5KeC8jGGvAX4j02f93ACeX.png)

기타 문서는 [문서(명세, 다이어그램)](https://wooden-dust-ea9.notion.site/e5dc953d30814e8689e5bed8dcd7be31) 참고

## ✔️ 기술적 의사 결정

### 1. Java 22 & Spring Boot 3.3.2 선택

- Java 22의 레코드 패턴을 활용하여 DTO 구현 시 코드량 감소 및 가독성 향상
- 최신 버전 사용으로 보안 이슈 최소화 및 최신 기능 활용
- 프로젝트에 새로운 Java 기능을 적용해 봄으로써 기술적 역량 강화

### 2. MSA 구조 선택 및 관련 기술

- MSA 구조 채택:

  - 향후 대규모 확장 가능성을 고려한 선제적 설계
  - 트래픽 증가 시 유연한 스케일링 대응
  - 서비스별 독립 배포로 장애 영향 최소화
  - 복잡한 비즈니스 로직의 효율적 처리

- Spring Cloud Eureka 도입:

  - 서비스 디스커버리 자동화로 관리 편의성 증대
  - 서비스 위치 변경 시 설정 변경 최소화

- Docker & Docker Compose 활용:

  - 개발 환경 일관성 확보
  - Kafka, Redis 등 복잡한 인프라 구성 간소화
  - 컨테이너화를 통한 배포 프로세스 개선

- 현 프로젝트 규모에서는 다소 과도한 구조일 수 있으나, 학습 및 미래 확장성을 고려하여 채택

  실제 서비스 운영 시에는 트래픽과 복잡도에 따라 점진적으로 도입할 계획

### 3. 데이터베이스 선정

- MySQL 선택:

  - 개발자 친화적인 특성과 풍부한 생태계
  - 로컬 개발 환경에 적합한 가벼운 구조
  - 향후 샤딩, 레플리케이션 등 확장성 고려

- NoSQL(MongoDB 등) 미채택 이유:

  - 현재 데이터 구조와 관계형 모델의 적합성
  - ACID 트랜잭션 보장의 중요성
  - 현 단계에서 대규모 확장성보다 데이터 일관성 우선

- Redis 활용:
  - 재고 관리 시 동시성 이슈 해결
  - 분산 락 구현의 용이성
  - 캐싱 기능 활용으로 성능 개선 가능성

### 4. 마이크로서비스 간 통신 방식

- 동기 통신 (OpenFeign):

  - 재고 확인 및 차감 등 즉시 응답이 필요한 작업
  - 상품 정보 실시간 조회 시 활용

- 비동기 통신 (Kafka):
  - 주문 생성, 결제 요청 등 지연 허용 가능한 처리
  - 주문 취소, 배송 상태 업데이트 등 이벤트 기반 작업
- Kafka 선택 이유:
  - 대용량 메시지 처리 능력
  - 메시지 영속성으로 데이터 안정성 확보
  - 향후 데이터 분석 활용 가능성
- 비동기 통신 보완 전략:
  - 메시지 처리 실패 시 재시도 로직 구현 예정
  - 중요 작업에 대한 타임아웃 처리 고려 중

노션 페이지 [개발환경 명세](https://wooden-dust-ea9.notion.site/0ea2f4c67a7c4f60871b8b51d170d55d), [마이크로 서비스 간 통신 방법](https://wooden-dust-ea9.notion.site/2938710b03ff4f848ae5e386a573700f) 참고

## 📈 트러블슈팅

### [Entity 자기 참조 문제](https://wooden-dust-ea9.notion.site/Entity-b14a3f11ebf741e782fa82ac064b7a9b)

1. **초기 설계**: 카테고리 계층 구조를 위해 `@ManyToOne`으로 부모 카테고리 참조

2. **발생한 문제**:

   - 전체 카테고리 조회 시 과도한 쿼리 생성 및 자식 카테고리 중복 조회
   - N+1 문제 및 무한 재귀 쿼리 발생

3. **해결 과정**:

   - Lazy loading, Fetch join, Jackson 라이브러리 설정 시도
   - 실패 원인: N+1 문제 지속, 다중 계층에서의 중복 문제 등

4. **최종 해결**: 카테고리 엔티티에 'path'와 'depth' 컬럼 추가로 쿼리 수 감소 및 중복 조회 해결

5. **주요 학습점**:
   - 자기 참조 관계의 복잡성 이해
   - ORM 사용 시 쿼리 최적화와 데이터 모델링의 중요성

### [동시성 제어 문제 해결](https://wooden-dust-ea9.notion.site/9e33bc279b004f72a81b9335cebb9594)

1. **초기 문제**:

   - JMeter 테스트 결과, 동시 요청 시 재고 차감 오류 발생

2. **해결 과정**:

   1. Redis 분산 락 도입:
      - 선택 이유: DB 락 대비 성능 우수, synchronized 대비 분산 환경 적합
   2. 재시도 로직 및 지수 백오프 전략 적용:
      - 목적: 일시적 락 획득 실패 대응, 시스템 부하 분산
   3. Redisson 및 Redlock 알고리즘 적용:
      - 목적: 더 강력한 분산 락 보장, 세션 관리 간소화

3. **결과**:
   - 동시성 문제 해결, 그러나 성능 개선 필요성 대두 (아래 '응답시간 개선' 참조)

### [응답 시간 개선](https://wooden-dust-ea9.notion.site/ee9035498462463885c6108c50c83fd0)

1. **초기 문제**:

   - 재고 예약/차감/롤백 시 과도한 DB I/O로 응답시간 3,754ms 발생
   - 동시성 제어 해결 후에도 지속된 성능 이슈

2. **개선 과정**:

   1. Redis 캐싱 도입: 응답시간 3,606ms (미미한 개선)
   2. 레디스 락 구현 변경 및 주문 저장 시 CompletableFuture 활용한 병렬적 배치 처리:

   - 결과: 응답시간 5,634ms (성능 악화)

3. **최종 해결**:

   - IntelliJ Profiler로 병목 지점 정확히 분석
   - Lua Script를 이용한 원자적 연산으로 세밀한 락킹 구현
   - 결과: 응답시간 5,634ms → 191ms (97% 개선)

4. **주요 학습점**:
   - 정확한 병목 지점 파악의 중요성
   - 세밀한 락 관리와 원자적 연산의 효과
   - 단순 캐싱이나 병렬 처리가 항상 해결책이 아님을 인식
   - 동시성 제어와 성능 최적화는 상호 연관된 문제임을 이해

## 📂 폴더 구조

```
zzimcong
├── 📂 backend
│   ├── 📂 api-gateway
│   ├── 📂 eureka-server
│   ├── 📂 order
│   │   └── 📂 src.main.java.com.zzimcong
│   │       └── 📂 order
│   │           ├── 📂 api
│   │           │   ├── 📂 client
│   │           │   ├── 📂 controller
│   │           │   └── 📂 response
│   │           ├── 📂 application
│   │           │   ├── 📂 dto
│   │           │   ├── 📂 mapper
│   │           │   ├── 📂 queue
│   │           │   ├── 📂 saga
│   │           │   └── 📂 service
│   │           ├── 📂 aspect
│   │           ├── 📂 common
│   │           │   ├── 📂 exception
│   │           │   └── 📂 util
│   │           ├── 📂 domain
│   │           │   ├── 📂 entity
│   │           │   └── 📂 repository
│   │           └── 📂 infrastructure
│   │               ├── 📂 config
│   │               ├── 📂 kafka
│   │               ├── 📂 redis
│   │               └── 📂 statemachine
│   ├── 📂 product
│   ├── 📂 user
│   └── 📂 zzimcong-inventory-core
├── 📂 docker
│   ├── 📂 jenkins
│   ├── 📂 mysql
│   │   └── 📂 init
│   └── 📂 prometheus
├── 📂 frontend
│   └── 📂 zzimcong
│       ├── 📂 public
│       └── 📂 src
│           ├── 📂 components
│           ├── 📂 context
│           ├── 📂 pages
│           └── 📂 services
├── 📂 load-test
│   ├── 📂 20240829_Concurrency_LuaScript_v1
│   │   ├── 📂 results
│   │   ├── 📂 reports
│   │   └── 📂 tests
│   ├── 📂 20240829_Concurrency_LuaScript_v2
│   ├── 📂 20240829_Concurrency_Redlock_v1
│   └── 📂 20240830_Concurrency_RedisReservation_v1
└── 📂 scripts
```
