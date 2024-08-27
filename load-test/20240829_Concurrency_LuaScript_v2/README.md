# JMeter 테스트 보고서: 동시성 제어 메커니즘 성능 분석

## 1. 테스트 목적

현재 구현된 동시성 제어 메커니즘의 성능 검증

## 2. 테스트 환경

- 테스트 도구: Apache JMeter
- 대상 시스템: 주문 처리 API
- 총 스레드 수: 10,000
- Ramp-up 시간: 100초

## 3. 테스트 시나리오

1. `/api/v1/orders/prepare` 엔드포인트 호출 (GET)
2. `/api/v1/orders/create/{orderId}` 엔드포인트 호출 (POST)

## 4. 테스트 결과 요약

### 4.1 전체 성능 지표

- 총 샘플 수: 4,030
- 총 에러 수: 38 (0.94%)
- 평균 응답 시간: 5,580.92 ms
- 중간값 응답 시간: 3,971.5 ms
- 90번째 백분위 응답 시간: 15,481.80 ms
- 95번째 백분위 응답 시간: 19,747.35 ms
- 99번째 백분위 응답 시간: 22,304.80 ms
- 최소 응답 시간: 40 ms
- 최대 응답 시간: 26,560 ms
- 처리량: 약 113.82 requests/second

### 4.2 엔드포인트별 성능

#### 4.2.1 Prepare Order (/api/v1/orders/prepare)

- 샘플 수: 2,015
- 평균 응답 시간: 9,293.85 ms
- 중간값 응답 시간: 6,982.0 ms
- 최소 응답 시간: 103.0 ms
- 최대 응답 시간: 26,560.0 ms
- 90번째 백분위 응답 시간: 19,730.4 ms
- 95번째 백분위 응답 시간: 21,365.2 ms
- 99번째 백분위 응답 시간: 26,285.52 ms
- 에러율: 0.94%
- 처리량: 약 57.39 requests/second

#### 4.2.2 Create Order (/api/v1/orders/create/{orderId})

- 샘플 수: 2,015
- 평균 응답 시간: 1,867.98 ms
- 중간값 응답 시간: 1,395.0 ms
- 최소 응답 시간: 40.0 ms
- 최대 응답 시간: 20,114.0 ms
- 90번째 백분위 응답 시간: 3,960.2 ms
- 95번째 백분위 응답 시간: 4,468.0 ms
- 99번째 백분위 응답 시간: 5,213.44 ms
- 에러율: 0.94%
- 처리량: 약 62.26 requests/second

## 5. 성능 분석

1. **에러율**: 전체 테스트에서 0.94%의 에러율 발생, 높은 부하 상황에서도 시스템이 비교적 안정적으로 작동

2. **응답 시간**:

   - Prepare Order 엔드포인트의 평균 응답 시간(9,293.85 ms)이 Create Order 엔드포인트(1,867.98 ms)보다 약 5배 길음
   - 전체적으로 응답 시간이 높은 편이며, 특히 Prepare Order 작업에서 병목 현상 발생 추정

3. **처리량**:

   - 전체 시스템은 초당 약 113.82개의 요청 처리
   - Create Order 작업(62.26 req/sec)이 Prepare Order 작업(57.39 req/sec)보다 약간 높은 처리량 보임

4. **분포**:
   - 응답 시간의 분포가 넓어 일부 요청에서 매우 긴 지연 발생
   - 99번째 백분위 응답 시간이 평균의 4배 이상으로, 일부 요청에서 극단적인 지연 발생

## 6. 결론 및 향후 계획

1. **로컬 테스트의 한계**:

   - 현재 테스트는 로컬 환경에서 수행되어 실제 서비스 환경과 차이 존재 가능
   - 결과는 초기 성능 지표로 활용하되, 실제 서버 환경 성능과의 차이 인지 필요

2. **주요 개선 포인트**:

   - Prepare Order 작업의 응답 시간 최적화 필요
   - 전체적인 응답 시간과 처리량 개선 여지 있음

3. **실 서버 환경 테스트 계획**:

   - 향후 실제 서버 환경에서 동일 테스트 수행 예정
   - 네트워크 지연, 실제 DB 부하, 외부 서비스 통합 등 실제 운영 환경 조건 고려

4. **단계적 개선 접근**:

   - 실 서버 테스트 결과 바탕으로 구체적 개선 포인트 식별 및 우선순위 결정
   - 각 개선 단계마다 성능 테스트 반복하여 효과 정량적 측정 및 검증

5. **지속적인 모니터링과 최적화**:
   - 서비스 런칭 후에도 지속적 성능 모니터링으로 안정성과 효율성 유지/개선
   - 사용자 수 증가, 데이터 증가 등 변화하는 환경에 맞춘 시스템 최적화 계획

이번 로컬 환경 테스트는 초기 성능 평가와 개선 방향 설정의 기초 자료로 활용 예정. 앞으로 실제 서버 환경에서의 테스트를 통해 더욱 정확한 성능 분석과 최적화 진행 계획.