<a name="top"></a>
[![.NET](https://img.shields.io/badge/.NET-6.0%2C%207.0%2C%208.0-512BD4)](https://docs.abblix.com/docs/technical-requirements)
[![language](https://img.shields.io/badge/language-C%23-239120)](https://learn.microsoft.com/ru-ru/dotnet/csharp/tour-of-csharp/overview)
[![OS](https://img.shields.io/badge/OS-linux%2C%20windows%2C%20macOS-0078D4)](https://docs.abblix.com/docs/technical-requirements)
[![CPU](https://img.shields.io/badge/CPU-x86%2C%20x64%2C%20ARM%2C%20ARM64-FF8C00)](https://docs.abblix.com/docs/technical-requirements)
[![security rating](https://sonarcloud.io/api/project_badges/measure?project=Abblix_Oidc.Server&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Abblix_Oidc.Server)
[![reliability rating](https://sonarcloud.io/api/project_badges/measure?project=Abblix_Oidc.Server&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Abblix_Oidc.Server)
[![maintainability rating](https://sonarcloud.io/api/project_badges/measure?project=Abblix_Oidc.Server&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Abblix_Oidc.Server)
[![CodeQL analysis](https://github.com/Abblix/Oidc.Server/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Abblix/Oidc.Server/security/code-scanning?query=is%3Aopen)
[![GitHub release](https://img.shields.io/github/v/release/Abblix/Oidc.Server)](#)
[![GitHub release date](https://img.shields.io/github/release-date/Abblix/Oidc.Server)](#)
[![GitHub last commit](https://img.shields.io/github/last-commit/Abblix/Oidc.Server)](#)
[![ChatGPT](https://img.shields.io/badge/ChatGPT-available-brightgreen.svg?logo=data:image/svg%2bxml;base64,PHN2ZyByb2xlPSJpbWciIHZpZXdCb3g9IjAgMCAyNCAyNCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48dGl0bGU+T3BlbkFJPC90aXRsZT48cGF0aCBmaWxsPSIjRkZGRkZGIiBkPSJNMjIuMjgxOSA5LjgyMTFhNS45ODQ3IDUuOTg0NyAwIDAgMC0uNTE1Ny00LjkxMDggNi4wNDYyIDYuMDQ2MiAwIDAgMC02LjUwOTgtMi45QTYuMDY1MSA2LjA2NTEgMCAwIDAgNC45ODA3IDQuMTgxOGE1Ljk4NDcgNS45ODQ3IDAgMCAwLTMuOTk3NyAyLjkgNi4wNDYyIDYuMDQ2MiAwIDAgMCAuNzQyNyA3LjA5NjYgNS45OCA1Ljk4IDAgMCAwIC41MTEgNC45MTA3IDYuMDUxIDYuMDUxIDAgMCAwIDYuNTE0NiAyLjkwMDFBNS45ODQ3IDUuOTg0NyAwIDAgMCAxMy4yNTk5IDI0YTYuMDU1NyA2LjA1NTcgMCAwIDAgNS43NzE4LTQuMjA1OCA1Ljk4OTQgNS45ODk0IDAgMCAwIDMuOTk3Ny0yLjkwMDEgNi4wNTU3IDYuMDU1NyAwIDAgMC0uNzQ3NS03LjA3Mjl6bS05LjAyMiAxMi42MDgxYTQuNDc1NSA0LjQ3NTUgMCAwIDEtMi44NzY0LTEuMDQwOGwuMTQxOS0uMDgwNCA0Ljc3ODMtMi43NTgyYS43OTQ4Ljc5NDggMCAwIDAgLjM5MjctLjY4MTN2LTYuNzM2OWwyLjAyIDEuMTY4NmEuMDcxLjA3MSAwIDAgMSAuMDM4LjA1MnY1LjU4MjZhNC41MDQgNC41MDQgMCAwIDEtNC40OTQ1IDQuNDk0NHptLTkuNjYwNy00LjEyNTRhNC40NzA4IDQuNDcwOCAwIDAgMS0uNTM0Ni0zLjAxMzdsLjE0Mi4wODUyIDQuNzgzIDIuNzU4MmEuNzcxMi43NzEyIDAgMCAwIC43ODA2IDBsNS44NDI4LTMuMzY4NXYyLjMzMjRhLjA4MDQuMDgwNCAwIDAgMS0uMDMzMi4wNjE1TDkuNzQgMTkuOTUwMmE0LjQ5OTIgNC40OTkyIDAgMCAxLTYuMTQwOC0xLjY0NjR6TTIuMzQwOCA3Ljg5NTZhNC40ODUgNC40ODUgMCAwIDEgMi4zNjU1LTEuOTcyOFYxMS42YS43NjY0Ljc2NjQgMCAwIDAgLjM4NzkuNjc2NWw1LjgxNDQgMy4zNTQzLTIuMDIwMSAxLjE2ODVhLjA3NTcuMDc1NyAwIDAgMS0uMDcxIDBsLTQuODMwMy0yLjc4NjVBNC41MDQgNC41MDQgMCAwIDEgMi4zNDA4IDcuODcyem0xNi41OTYzIDMuODU1OEwxMy4xMDM4IDguMzY0IDE1LjExOTIgNy4yYS4wNzU3LjA3NTcgMCAwIDEgLjA3MSAwbDQuODMwMyAyLjc5MTNhNC40OTQ0IDQuNDk0NCAwIDAgMS0uNjc2NSA4LjEwNDJ2LTUuNjc3MmEuNzkuNzkgMCAwIDAtLjQwNy0uNjY3em0yLjAxMDctMy4wMjMxbC0uMTQyLS4wODUyLTQuNzczNS0yLjc4MThhLjc3NTkuNzc1OSAwIDAgMC0uNzg1NCAwTDkuNDA5IDkuMjI5N1Y2Ljg5NzRhLjA2NjIuMDY2MiAwIDAgMSAuMDI4NC0uMDYxNWw0LjgzMDMtMi43ODY2YTQuNDk5MiA0LjQ5OTIgMCAwIDEgNi42ODAyIDQuNjZ6TTguMzA2NSAxMi44NjNsLTIuMDItMS4xNjM4YS4wODA0LjA4MDQgMCAwIDEtLjAzOC0uMDU2N1Y2LjA3NDJhNC40OTkyIDQuNDk5MiAwIDAgMSA3LjM3NTctMy40NTM3bC0uMTQyLjA4MDVMOC43MDQgNS40NTlhLjc5NDguNzk0OCAwIDAgMC0uMzkyNy42ODEzem0xLjA5NzYtMi4zNjU0bDIuNjAyLTEuNDk5OCAyLjYwNjkgMS40OTk4djIuOTk5NGwtMi41OTc0IDEuNDk5Ny0yLjYwNjctMS40OTk3WiIvPjwvc3ZnPg==)](https://chatgpt.com/g/g-1icXaNyOR-abblix-oidc-server-helper)
[![getting started](https://img.shields.io/badge/getting_started-guide-1D76DB)](https://docs.abblix.com/docs/getting-started-guide)
[![Free](https://img.shields.io/badge/free_for_non_commercial_use-brightgreen)](#-license)

⭐ Star us on GitHub — it motivates us a lot!

[![Share](https://img.shields.io/badge/share-000000?logo=x&logoColor=white)](https://x.com/intent/tweet?text=Check%20out%20this%20project%20on%20GitHub:%20https://github.com/Abblix/Oidc.Server%20%23OpenIDConnect%20%23Security%20%23Authentication)
[![Share](https://img.shields.io/badge/share-1877F2?logo=facebook&logoColor=white)](https://www.facebook.com/sharer/sharer.php?u=https://github.com/Abblix/Oidc.Server)
[![Share](https://img.shields.io/badge/share-0A66C2?logo=linkedin&logoColor=white)](https://www.linkedin.com/sharing/share-offsite/?url=https://github.com/Abblix/Oidc.Server)
[![Share](https://img.shields.io/badge/share-FF4500?logo=reddit&logoColor=white)](https://www.reddit.com/submit?title=Check%20out%20this%20project%20on%20GitHub:%20https://github.com/Abblix/Oidc.Server)
[![Share](https://img.shields.io/badge/share-0088CC?logo=telegram&logoColor=white)](https://t.me/share/url?url=https://github.com/Abblix/Oidc.Server&text=Check%20out%20this%20project%20on%20GitHub)

## 목차

- [프로젝트 소개](#-프로젝트-소개)
- [Certification](#-certification)
- [실행 방법](#-실행-방법)
- [Documentation](#-documentation)
- [Feedback and Contributions](#-feedback-and-contributions)
- [License](#-license)
- [Contacts](#%EF%B8%8F-contacts)

## 🚀 프로젝트 소개

**찜콩**은 선착순 구매 기능을 핵심으로 하는 이커머스 플랫폼입니다. 이 프로젝트는 대규모 트래픽 처리, 동시성 제어, 그리고 마이크로서비스 아키텍처 설계 등 실제 기업에서 마주할 수 있는 기술적 도전들을 경험하고 해결하기 위해 개발되었습니다.

## 주요 기술 스택

- **Backend**: Java 22, Spring Boot 3.3.2, Spring Cloud
- **Database**: MySQL, Redis
- **Message Broker**: Kafka
- **Infrastructure**: Docker, Docker Compose
- **기타**: JMeter(부하 테스트), Prometheus(모니터링)

프로젝트의 기술 스택은 성능, 확장성, 그리고 현업 적용성을 고려하여 선택했습니다. Java 22와 Spring Boot 3.3.2는 최신 버전으로, 새로운 기능과 향상된 성능을 제공하며 실무에서 널리 사용됩니다. Spring Cloud는 마이크로서비스 아키텍처 구현을 위해 채택했으며, MySQL은 안정성과 광범위한 지원 때문에 주 데이터베이스로 선택했습니다. Redis는 고성능 캐싱과 분산 락 구현을 위해, Kafka는 마이크로서비스 간 이벤트 기반 통신을 위해 도입했습니다. Docker와 Docker Compose는 일관된 개발 및 배포 환경을 제공하기 위해 사용했으며, JMeter와 Prometheus는 각각 성능 테스트와 모니터링을 위해 선택했습니다. 이러한 기술 스택은 대규모 트래픽 처리와 확장 가능한 시스템 구축이라는 프로젝트의 목표를 달성하는 데 적합하며, 동시에 현업에서 많이 사용되는 기술들이라 실무 적용성도 높다고 판단했습니다.

## 📝 실행 방법

프로젝트를 실행하시려면, 다음 단계에 따라 진행해주세요:

1. 프로젝트 클론

```shell
$ git clone https://github.com/your-username/zzimcong.git
$ cd zzimcong
```

2. Docker를 이용한 서비스 실행

```shell
docker-compose up -d --build
```

이 명령어로 MySQL, Redis, Kafka 등의 서비스가 백그라운드로 실행됩니다.

3. 마이크로 서비스 빌드

```shell
# E
cd user-service
./gradlew bootRun

# User Service
cd user-service
./gradlew bootRun

# User Service
cd user-service
./gradlew bootRun

# 새 터미널에서
cd product-service
./gradlew bootRun

# 새 터미널에서
cd order-service
./gradlew bootRun
```

## 📚 API 문서

### [zzimcong API (Postman)](https://documenter.getpostman.com/view/28230650/2sAXjRVUrm)

---

## 🌟 주요 기능

- **유저 관리**: 사용자는 jwt를 이용해 이메일 인증 후 회원가입, 로그인 등 회원 기능을 사용할 수 있습니다.
- **상품 정보 조회 및 구매**: 상품에 대한 정보 게시, 댓글 작성 및 좋아요를 통해 다른 사용자와 경험을 공유할 수 있습니다.
- **예약 구매 기능**: 다양한 상품을 예약 구매할 수 있는 기능을 제공합니다.
- **결제 시스템 통합**: 안전하고 편리한 결제 시스템을 통해 사용자는 손쉽게 상품을 구매할 수 있습니다.

---

## 🛠 기술 스택

### 🖥 Backend

- Spring Boot
- Spring Security
- JPA / Hibernate
- MySQL
- Redis
- Docker / Docker Compose

---

## 🚧 프로젝트 아키텍처

프로젝트의 전체적인 아키텍처는 마이크로서비스 아키텍처를 따르며, 각 기능별로 분리된 서비스들이 REST API를 통해 서로 통신합니다. Docker를 사용하여 각 서비스들을 컨테이너화하였으며, MySQL과 Redis를 사용하여 데이터의 지속성을 관리합니다.

---

## 📈 성능 최적화 및 트러블슈팅

프로젝트 개발 과정에서 발생한 주요 성능 최적화 작업과 트러블슈팅 사례를 공유합니다. 이는 프로젝트 진행 중 직면한 기술적 문제를 해결하고, 프로젝트의 전반적인 성능을 향상시킨 경험을 공유하는 자료입니다.

### 성능 최적화 사례

- **MSA(MicroService Architecture) 도입**: 서비스의 확장성과 유지보수성을 향상시키기 위해 마이크로서비스 아키텍처를 도입했습니다. [자세히 보기](https://jaehyuuk.tistory.com/161)
- **API Gateway 추가**: 시스템의 안정성과 서비스 관리의 용이성을 위해 API Gateway를 추가했습니다. [자세히 보기](https://jaehyuuk.tistory.com/165)
- **실시간 재고 관리 서비스 추가**: 대규모 트래픽 처리를 위한 실시간 재고 관리 서비스를 추가했습니다. [자세히 보기](https://jaehyuuk.tistory.com/180)

### 트러블슈팅 경험

- **Redis와 JWT 토큰 만료 시간 동기화 문제**: Redis에 저장된 JWT 토큰과 실제 만료 시간 사이의 동기화 문제를 해결했습니다. [자세히 보기](https://jaehyuuk.tistory.com/160)
- **Rest API 응답 데이터의 Null 값 문제 해결**: 선택적 필드를 포함하는 API 응답에서 Null 값 처리 문제를 해결했습니다. [자세히 보기](https://jaehyuuk.tistory.com/163)
- **예약 구매 상품의 시간 제한 처리**: 예약 구매 상품에 대한 시간 제한 처리 로직을 개선했습니다. [자세히 보기](https://jaehyuuk.tistory.com/172)

전체 프로젝트 관련 글 및 기술적 고민은 [블로그](https://jaehyuuk.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%28Java%29/%EC%98%88%EC%95%BD%EB%A7%88%EC%BC%93)에서 확인할 수 있습니다.

---

## 🔗 유용한 링크

- **프로젝트 GitHub 저장소**: [yeyak-market GitHub](https://github.com/jaehyuuk/yeyak-market)
- **프로젝트 문서 및 기술적 의사결정 자료**: [의사결정 문서 (yeyak-market)](https://drive.google.com/file/d/11zDsGOgyGlBBeZabj4n0ZMt-inn5qnrR/view?usp=sharing)
