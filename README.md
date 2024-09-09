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

<details>
<summary>주요 기술 스택 선정 이유</summary>
<details>

<summary style="margin-left:20px">Java 22 & Spring Boot 3.3.2</summary>

1. 레코드 패턴 활용

   - Java 22의 레코드 패턴을 DTO(Data Transfer Object) 구현에 적극 활용
   - 불변성 보장 및 보일러플레이트 코드 감소로 가독성 높은 코드 작성
   - `equals()`, `hashCode()`, `toString()` 메서드 자동 생성으로 개발 생산성 향상

2. 향상된 보안

   - Spring Cloud Gateway 활용: 최신 버전의 Spring Boot와 잘 통합
   - 중앙 집중식 보안 관리: API 요청에 대한 인증 및 권한 부여를 Gateway 레벨에서 처리
   - 요청 필터링: 악성 요청 필터링 및 rate limiting 적용으로 시스템 보호
   - 최신 보안 패치: Java 22와 Spring Boot 3.3.2의 최신 보안 업데이트로 취약점 대응

3. 미래 지향적 개발
   - 최신 기술 스택 사용으로 향후 기술 변화에 유연하게 대응 가능
   - 프로젝트의 장기적인 유지보수성과 확장성을 고려한 선택

</details>

<details>
<summary style="margin-left:20px">Spring Cloud Eureka</summary>

1. 서비스 디스커버리 기본 구조 구현

   - User, Order, Product 서비스의 위치를 Eureka 서버에서 중앙 관리

2. 설정 간소화

   - URL 대신 서비스 이름을 사용하여 코드를 더 간결하게 구성

3. 서비스 확장 고려
   - 추후 트래픽 증가에 따른 동적 스케일링 시, Eureka의 로드밸런싱과 서비스 디스커버리 기능 활용 가능

</details>
<details>
<summary style="margin-left:20px">MySQL</summary>

1. 안정성과 커뮤니티 지원

   - 오랜 기간 검증된 안정성과 높은 성능 제공
   - 활발한 커뮤니티 지원으로 문제 해결 및 정보 공유가 용이

2. 가벼움과 설정 편의성

   - 기본 설정만으로 ACID, 트랜잭션, 인덱싱 즉시 사용 가능
   - 256MB 메모리로 기본 구동 가능한 낮은 리소스 요구
   - 이커머스에 유리한 쿼리 캐시 기능으로 읽기 작업 성능 향상

3. 프로젝트 특성에 따른 적합성

   - 구조화된 데이터(주문, 사용자, 상품)와 복잡한 관계 처리에 적합
   - ACID 속성 보장으로 NoSQL 대비 데이터 일관성 유지에 우수
   - 현 단계에서 MongoDB의 유연한 스키마, 대용량 처리 능력 굳이 필요하지 않음
   - 트랜잭션 처리, 조인 연산 등 RDBMS의 강점이 프로젝트에 부합

</details>
<details>
<summary style="margin-left:20px">Redis
</summary>

1. 재고 관리 최적화

   - 인메모리 특성으로 빠른 재고 조회/갱신 가능
   - 주문 과정의 동시성 문제 해결에 적합

2. 분산 락 기능

   - Redisson 라이브러리 통해 간편한 분산 락 구현
   - 추후 개발 예정인 다중 서버 환경에서 동시성 제어 용이

3. Spring과의 높은 호환성

   - Spring Data Redis로 쉬운 연동
   - 트랜잭션 관리, 캐싱 등 다양한 기능 활용

4. 다른 인메모리 저장소와의 비교
   - Memcached: 단순 key-value 저장만 가능, 분산 락 기능 부재
   - Hazelcast: 학습 곡선이 높고, Redis에 비해 커뮤니티 지원 부족
   </details>

<details>
<summary style="margin-left:20px">Kafka
</summary>

1. 로그 기반 메시지 저장

   - 메시지를 디스크에 순차적으로 저장, 데이터 영속성 보장
   - 주문 히스토리 추적 및 시스템 장애 시 복구에 유리

2. 높은 처리량과 낮은 지연시간

   - 초당 수백만 건의 메시지 처리 가능
   - 주문 폭주 시에도 안정적인 성능 유지

3. 컨슈머 그룹 개념

   - 주문, 결제, 배송 등 다양한 서비스가 독립적으로 메시지 소비
   - 시스템 확장 시 컨슈머만 추가하여 처리량 향상 가능

4. 데이터 리플레이 기능

   - 과거 주문 데이터 재처리 가능
   - 시스템 장애 복구나 데이터 분석에 활용
   </details>

<details>
<summary style="margin-left:20px">Docker & Docker Compose
</summary>

1.  일관된 개발 환경 제공

    - 모든 개발자가 동일한 환경에서 작업 가능
    - "내 컴퓨터에서는 작동합니다" 문제 해결

2.  다양한 환경 통합 및 설정

    - Redis, Kafka, Zookeeper 등 외부 서비스 쉽게 통합
    - 복잡한 의존성 관리 단순화
    - 환경 설정을 docker-compose.yml 파일로 관리
    - 버전 관리 시스템을 통한 인프라 변경 추적 용이

3.  리소스 효율성

    - 가상머신 대비 가볍고 빠른 실행
    - 개발 환경에서의 리소스 사용 최적화

4.  MSA 구조에서 장점(차후 계획)

    - User, Order, Product 서비스를 독립적인 컨테이너로 관리
    - 서비스 간 격리로 의존성 문제 최소화
    - 컨테이너 기반으로 서비스 빠른 배포 가능
    - 트래픽 증가 시 컨테이너 수평 확장 용이

</details>
</details>

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

- **이메일 인증**: Gmail SMTP 서버를 사용해 회원가입 시 사용자의 이메일로 인증 메일을 발송하여, 사용자가 실제로 소유한 이메일 주소임을 확인.
- **개인정보 암호화**: 사용자의 민감한 개인정보는 **AES256** 알고리즘으로 암호화되어 안전하게 저장되며, 비밀번호는 **BCrypt**를 통해 해싱 및 암호화하여 저장, 보안을 강화.

### **로그인**

- **Spring Security 기반 인증**: 사용자 인증 및 인가를 **Spring Security**로 처리하여, 보안성이 높은 로그인 시스템 구축.
- **JWT 기반 인증 처리**: 로그인 성공 시 **JWT**를 발급하여 사용자의 세션을 유지하며, 이후 요청 시 JWT를 이용해 인증을 처리.
- **Refresh Token 관리**: Refresh token을 레디스에 저장하여 access token이 만료되었을 때 사용자 인증.

### **상품**

- **상품 리스트 제공**: 전체 상품의 목록을 효율적으로 제공하며, 필터 및 검색 옵션을 통해 사용자 편의성을 증대.
- **상품 상세 페이지 제공**: 각 상품에 대한 상세 정보를 조회할 수 있는 페이지 제공, 상품 정보는 사용자에게 실시간으로 제공됨.
- **상품 재고 관리**: **Redis**를 활용한 재고 정보 캐싱을 통해 실시간 재고 상태를 빠르게 확인 가능, 재고 변경 시 자동 업데이트.

### **주문**

- **실시간 재고 확인**: 주문 생성 시 **Redis**에 캐싱된 재고 정보를 기반으로 즉시 확인하여, 재고가 부족한 경우 빠르게 사용자에게 알림을 제공. 재고 정보는 상품별로 캐싱되어 성능 저하 없이 다수의 주문 요청을 처리.

- **동시성 제어**: 여러 사용자가 동시에 같은 상품을 주문할 경우 발생할 수 있는 **동시성 문제**를 해결하기 위해, Redis의 **Lua Script**을 사용해 재고 수정 시 데이터의 일관성을 보장. 이로 인해 다수의 사용자가 동시에 주문을 시도해도 재고 오류가 발생하지 않음.

- **주문 상태 관리**: 주문 생성, 결제 대기, 결제 성공, 결제 실패 등 **주문 상태**를 여러 단계로 나누어 관리. 주문이 생성된 후에도 실시간으로 상태가 변경되어 사용자와 관리자 모두 정확한 정보를 확인할 수 있음.

- **SAGA 패턴을 사용한 트랜잭션 관리**: 결제 성공 시 주문 정보와 재고가 동시에 갱신되도록 **트랜잭션 처리**를 적용하여, 주문 생성 및 결제 과정에서 데이터의 일관성을 보장. 또한 주문 과정 중 결제 오류 또는 재고 부족 등 예기치 못한 상황이 발생할 경우, 모든 작업이 자동으로 **롤백**되며, 사용자는 즉시 문제 상황을 안내받고 적절한 대응을 할 수 있음.

- **성능 최적화**: Redis와 같은 **인메모리 데이터베이스**를 사용해 재고 확인 및 주문 처리 속도를 최적화하고, 대규모 트래픽 상황에서도 안정적인 주문 처리가 가능하게 설계.

- **에러 처리**: 주문 시 발생할 수 있는 예상치 못한 오류(예: 외부 결제 서버 오류, 통신 지연 등)에 대해 타임아웃을 설정하여 예외 처리가 가능하도록 함.

### **구현 예정**

- **결제 연동**: 외부 결제 게이트웨이와 연동하여 주문 생성과 동시에 **결제 요청**을 처리. 결제 과정은 **비동기 처리**로 구현하여 결제 서버와의 통신 시간이 길어도 다른 서비스에 영향을 주지 않음. 결제가 완료되면, 주문 상태가 자동으로 업데이트되고 사용자는 즉시 알림을 받음.

- **실시간 모니터링 시스템 도입**: Prometheus와 Grafana와 같은 모니터링 도구를 도입하여, 시스템의 실시간 상태를 시각적으로 모니터링. CPU 사용량, 메모리 상태, 네트워크 트래픽과 같은 자원 사용 현황을 대시보드 형태로 시각화. 이를 통해, 트래픽 급증이나 서버 성능 저하를 사전에 감지하고, 즉각적인 대응 가능.

- **오케스트레이션**: **마이크로서비스 아키텍처(MSA)** 와 SAGA 패턴을 도입하여 각 서비스를 독립적으로 관리하고 확장할 수 있는 구조로 설계. 향후 Kubernetes와 같은 오케스트레이션 도구를 도입해 각 서비스의 자동 배포, 스케일링, 롤백을 관리. 이를 통해 트래픽 급증 시 자동 스케일링을 적용해 성능을 유지하고, 장애 발생 시에도 무중단 운영 가능토록 구현 예정.

- **확장성**: 클라우드 네이티브 인프라로 확장하여, AWS EKS나 GCP GKE를 통해 자원을 유연하게 관리. 이로 인해 시스템의 자동 복구, 로드 밸런싱 및 확장성을 강화할 수 있으며, 클라우드의 기본 기능을 활용해 효율적인 리소스 관리와 신속한 장애 대응이 가능.

---

## 🚧 문서 및 아키텍쳐

### [API 문서](https://wooden-dust-ea9.notion.site/API-de29ea6a3535422d84290f5b1ef9423a)

### 시퀀스 다이어그램

![diagram](https://file.notion.so/f/f/072d6599-b568-4618-8dae-62b022713c6a/de4a5868-7000-42ce-9b6d-087320425525/NCvH2i8m3CRnzvqYx2IYFWuKjml4D61XRKUI2hkzSrGbFkRFxuCSr52ifhb3iY9mgcebiNJBbYF51RD1Vv700YFBCnBtW0fxyUWg9LGonrhxWxWcpXwsvk2DGrG_dQO1XgZ6cMNVRU5pUpckcM2a1uSwD_jEtz85DBoZyk_ny_5LQTNij0q4uRkVOcw50v7wt-PGLOXptm6972f5q0FgcH-tId5KeC8jGGvAX4j02f93ACeX.png?table=block&id=383e91e1-4dd6-46bd-b33f-c509e26221c6&spaceId=072d6599-b568-4618-8dae-62b022713c6a&expirationTimestamp=1725940800000&signature=EwK6A-Hwjnn84qpoktk5Wo19CpIEzEh1ZC3NVmSzTBk&downloadName=NCvH2i8m3CRnzvqYx2IYFWuKjml4D61XRKUI2hkzSrGbFkRFxuCSr52ifhb3iY9mgcebiNJBbYF51RD1Vv700YFBCnBtW0fxyUWg9LGonrhxWxWcpXwsvk2DGrG_dQO1XgZ6cMNVRU5pUpckcM2a1uSwD_jEtz85DBoZyk_ny_5LQTNij0q4uRkVOcw50v7wt-PGLOXptm6972f5q0FgcH-tId5KeC8jGGvAX4j02f93ACeX.png)

---

## 📈 성능 최적화 및 트러블슈팅

### 성능 최적화 사례

- **MSA(MicroService Architecture) 도입**: 서비스의 확장성과 유지보수성을 향상시키기 위해 마이크로서비스 아키텍처를 도입했습니다. [자세히 보기](https://jaehyuuk.tistory.com/161)
- **API Gateway 추가**: 시스템의 안정성과 서비스 관리의 용이성을 위해 API Gateway를 추가했습니다. [자세히 보기](https://jaehyuuk.tistory.com/165)
- **실시간 재고 관리 서비스 추가**: 대규모 트래픽 처리를 위한 실시간 재고 관리 서비스를 추가했습니다. [자세히 보기](https://jaehyuuk.tistory.com/180)

### 트러블슈팅

- **Redis와 JWT 토큰 만료 시간 동기화 문제**: Redis에 저장된 JWT 토큰과 실제 만료 시간 사이의 동기화 문제를 해결했습니다. [자세히 보기](https://jaehyuuk.tistory.com/160)
- **Rest API 응답 데이터의 Null 값 문제 해결**: 선택적 필드를 포함하는 API 응답에서 Null 값 처리 문제를 해결했습니다. [자세히 보기](https://jaehyuuk.tistory.com/163)
- **예약 구매 상품의 시간 제한 처리**: 예약 구매 상품에 대한 시간 제한 처리 로직을 개선했습니다. [자세히 보기](https://jaehyuuk.tistory.com/172)

전체 프로젝트 관련 글 및 기술적 고민은 [블로그](https://jaehyuuk.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%28Java%29/%EC%98%88%EC%95%BD%EB%A7%88%EC%BC%93)에서 확인할 수 있습니다.

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
