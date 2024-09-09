[![프로젝트 문서](https://img.shields.io/badge/프로젝트_문서-000000?style=for-the-badge&logo=notion&logoColor=white)](여기에_노션_링크_삽입)
[![소스 코드](https://img.shields.io/badge/소스_코드-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/username/repository)
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

## 📚 API 문서

---

## 🌟 주요 기능

- **유저 관리**: 사용자는 jwt를 이용해 이메일 인증 후 회원가입, 로그인 등 회원 기능을 사용할 수 있습니다.
- **상품 정보 조회 및 구매**: 상품에 대한 정보 게시, 댓글 작성 및 좋아요를 통해 다른 사용자와 경험을 공유할 수 있습니다.
- **예약 구매 기능**: 다양한 상품을 예약 구매할 수 있는 기능을 제공합니다.
- **결제 시스템 통합**: 안전하고 편리한 결제 시스템을 통해 사용자는 손쉽게 상품을 구매할 수 있습니다.

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

### 트러블슈팅 경험

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
