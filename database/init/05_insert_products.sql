-- 냉장식품 카테고리의 상품들 (카테고리 ID: 6-10)

-- 유제품 (카테고리 ID: 6)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(6, '서울우유 1L', 2500, 0, '신선한 서울우유 1리터', 'seoul_milk.jpg', 100, 0),
(6, '앙팡 요구르트 4개입', 3000, 300, '어린이를 위한 영양 요구르트', 'enfant_yogurt.jpg', 50, 0),
(6, '매일 체다 치즈 200g', 4000, 400, '진한 풍미의 체다 치즈', 'cheddar_cheese.jpg', 60, 0),
(6, '남양 GT 우유 900ml', 2300, 0, '곡물이 들어간 고소한 우유', 'gt_milk.jpg', 80, 0),
(6, '동원 덴마크 버터 200g', 5500, 500, '100% 덴마크산 버터', 'denmark_butter.jpg', 40, 0);

-- 두부/콩나물 (카테고리 ID: 7)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(7, '풀무원 국산콩두부', 1800, 0, '100% 국산콩으로 만든 두부', 'tofu.jpg', 80, 0),
(7, '양반 콩나물 300g', 1500, 100, '아삭한 콩나물', 'bean_sprouts.jpg', 100, 0),
(7, '풀무원 찌개용 두부', 2000, 200, '찌개에 최적화된 두부', 'stew_tofu.jpg', 70, 0),
(7, '참두부 순두부 300g', 1600, 0, '부드러운 순두부', 'soft_tofu.jpg', 60, 0),
(7, '대림 숙주나물 300g', 1700, 100, '신선한 숙주나물', 'soybean_sprouts.jpg', 90, 0);

-- 햄/소시지 (카테고리 ID: 8)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(8, 'CJ 비엔나소시지', 3500, 500, '아이들 간식으로 좋은 소시지', 'sausage.jpg', 70, 0),
(8, '농심 켄터키 훈제치킨', 6000, 600, '훈제향 가득한 치킨', 'smoked_chicken.jpg', 50, 0),
(8, '롯데 로스팜 200g', 4000, 400, '고소한 돼지고기 햄', 'pork_ham.jpg', 60, 0),
(8, '동원 런천미트 200g', 3000, 0, '클래식한 런천미트', 'luncheon_meat.jpg', 80, 0),
(8, '하림 닭가슴살 햄 200g', 3800, 300, '단백질 풍부한 닭가슴살 햄', 'chicken_breast_ham.jpg', 70, 0);

-- 김치/절임류 (카테고리 ID: 9)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(9, '종가집 맛김치 1kg', 8000, 1000, '맛있게 익은 전통 김치', 'kimchi.jpg', 30, 0),
(9, '농협 알타리 무김치 1kg', 7000, 700, '아삭한 알타리 무김치', 'radish_kimchi.jpg', 40, 0),
(9, '참사랑 총각김치 500g', 6000, 0, '매콤한 총각김치', 'young_radish_kimchi.jpg', 50, 0),
(9, '동원 맛깔나는 오이지 500g', 5000, 500, '아삭한 오이지', 'pickled_cucumber.jpg', 60, 0),
(9, '청정원 갓김치 500g', 6500, 650, '향긋한 갓김치', 'mustard_leaf_kimchi.jpg', 45, 0);

-- 연식품 (카테고리 ID: 10)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(10, 'CJ 맛있는 두부', 2000, 200, '부드러운 맛있는 두부', 'delicious_tofu.jpg', 80, 0),
(10, '풀무원 국산콩 연두부', 2500, 0, '부드러운 국산콩 연두부', 'soft_tofu.jpg', 70, 0),
(10, '유기농 콩물 1L', 3000, 300, '신선한 유기농 콩물', 'organic_soymilk.jpg', 60, 0),
(10, '푸름원 순두부 300g', 1800, 180, '찌개용 순두부', 'stew_soft_tofu.jpg', 75, 0),
(10, '콩마을 두부면 200g', 2200, 0, '건강한 두부면', 'tofu_noodles.jpg', 65, 0);

-- 냉동식품 카테고리의 상품들 (카테고리 ID: 11-15)

-- 냉동과일 (카테고리 ID: 11)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(11, '냉동 딸기 500g', 5000, 0, '신선하게 얼린 제철 딸기', 'frozen_strawberry.jpg', 40, 0),
(11, '냉동 블루베리 400g', 6000, 600, '상큼한 냉동 블루베리', 'frozen_blueberry.jpg', 50, 0),
(11, '냉동 망고 500g', 7000, 700, '달콤한 냉동 망고', 'frozen_mango.jpg', 45, 0),
(11, '냉동 복숭아 500g', 5500, 0, '달콤한 냉동 복숭아', 'frozen_peach.jpg', 55, 0),
(11, '냉동 파인애플 500g', 6500, 650, '상큼한 냉동 파인애플', 'frozen_pineapple.jpg', 35, 0);

-- 냉동채소 (카테고리 ID: 12)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(12, '냉동 혼합 야채 1kg', 4000, 400, '각종 요리에 편리한 혼합 야채', 'mixed_vegetables.jpg', 60, 0),
(12, '냉동 브로콜리 500g', 3500, 0, '영양 가득한 냉동 브로콜리', 'frozen_broccoli.jpg', 70, 0),
(12, '냉동 깐 마늘 500g', 5000, 500, '손질 없이 바로 사용하는 마늘', 'frozen_garlic.jpg', 50, 0),
(12, '냉동 옥수수 500g', 3000, 300, '달콤한 냉동 옥수수', 'frozen_corn.jpg', 65, 0),
(12, '냉동 시금치 300g', 2500, 250, '신선한 냉동 시금치', 'frozen_spinach.jpg', 75, 0);

-- 냉동육류 (카테고리 ID: 13)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(13, '한우 불고기용 400g', 15000, 1500, '1등급 한우 불고기용', 'beef.jpg', 20, 0),
(13, '돼지고기 삼겹살 500g', 8000, 800, '국내산 돼지고기 삼겹살', 'pork_belly.jpg', 30, 0),
(13, '닭다리 1kg', 7000, 700, '신선한 닭다리', 'chicken_legs.jpg', 40, 0),
(13, '양고기 500g', 12000, 1200, '호주산 양고기', 'lamb.jpg', 15, 0),
(13, '오리고기 슬라이스 300g', 6000, 600, '부드러운 오리고기 슬라이스', 'duck_slices.jpg', 25, 0);

-- 냉동생선 (카테고리 ID: 14)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(14, '생연어 스테이크 300g', 12000, 0, '노르웨이산 생연어 스테이크', 'salmon.jpg', 25, 0),
(14, '고등어 3마리', 8000, 800, '국내산 고등어', 'mackerel.jpg', 35, 0),
(14, '오징어 500g', 7000, 700, '신선한 오징어', 'squid.jpg', 40, 0),
(14, '새우 500g', 10000, 1000, '탱탱한 새우', 'shrimp.jpg', 30, 0),
(14, '꽁치 5마리', 6000, 600, '영양 만점 꽁치', 'saury.jpg', 45, 0);

-- 냉동간편식 (카테고리 ID: 15)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(15, '곰곰 치즈떡볶이', 4500, 500, '간편하게 즐기는 치즈떡볶이', 'tteokbokki.jpg', 70, 0),
(15, '비비고 왕교자 600g', 8000, 800, '큼직한 왕교자', 'dumplings.jpg', 55, 0),
(15, 'CJ 비비고 김치볶음밥', 3500, 350, '맛있는 김치볶음밥', 'kimchi_fried_rice.jpg', 80, 0),
(15, '오뚜기 찰순대 300g', 5000, 500, '간편한 찰순대', 'sundae.jpg', 60, 0),
(15, '해물볶음우동 2인분', 6000, 600, '해물이 듬뿍 들어간 볶음우동', 'seafood_udon.jpg', 50, 0);

-- 가공식품 카테고리의 상품들 (카테고리 ID: 16-20)

-- 과자/스낵 (카테고리 ID: 16)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(16, '오리온 초코파이 12개입', 4000, 400, '달콤한 초코파이', 'choco_pie.jpg', 100, 0),
(16, '롯데 빼빼로 4개입', 2000, 200, '클래식한 빼빼로', 'pepero.jpg', 150, 0),
(16, '농심 새우깡 400g', 2500, 250, '바삭한 새우맛 과자', 'shrimp_snack.jpg', 120, 0),
(16, '해태 맛동산 46g', 1000, 100, '옥수수맛 스낵', 'corn_snack.jpg', 200, 0),
(16, '크라운 쿠크다스 36개입', 5000, 500, '부드러운 버터 쿠키', 'couque_dasse.jpg', 80, 0);

-- 라면/즉석식품 (카테고리 ID: 17) 계속
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(17, '농심 신라면 5개입', 3500, 0, '대한민국 대표 라면', 'shin_ramyun.jpg', 150, 0),
(17, '오뚜기 진라면 5개입', 3000, 300, '진한 맛의 라면', 'jin_ramyun.jpg', 140, 0),
(17, '삼양 불닭볶음면 5개입', 4000, 400, '매운맛의 대명사', 'buldak_ramyun.jpg', 100, 0),
(17, '농심 육개장 큰사발', 1200, 120, '든든한 한 끼 식사', 'yukgaejang_ramyun.jpg', 200, 0),
(17, 'CJ 햇반 210g 3개입', 3500, 350, '간편한 즉석밥', 'cooked_rice.jpg', 180, 0);

-- 통조림 (카테고리 ID: 18)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(18, '동원 참치캔 150g 3개입', 5000, 500, '고단백 참치캔', 'tuna_can.jpg', 80, 0),
(18, '농심 덴마크 고소한 치즈 180g', 3500, 350, '고소한 덴마크 치즈', 'cheese_can.jpg', 70, 0),
(18, '리더스 훈제 굴 70g', 4000, 400, '신선한 훈제 굴', 'smoked_oyster.jpg', 60, 0),
(18, '세방 옥수수콘 340g', 2000, 200, '달콤한 옥수수 통조림', 'corn_can.jpg', 90, 0),
(18, '리비 연어 통조림 213g', 6000, 600, '고급 연어 통조림', 'canned_salmon.jpg', 50, 0);

-- 장류 (카테고리 ID: 19)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(19, '청정원 순창 찰고추장 1kg', 8000, 800, '매콤달콤한 찰고추장', 'gochujang.jpg', 40, 0),
(19, '샘표 양조간장 501 930ml', 5000, 500, '깊은 맛의 양조간장', 'soy_sauce.jpg', 60, 0),
(19, '해찬들 재래식 된장 1kg', 7000, 700, '구수한 재래식 된장', 'doenjang.jpg', 50, 0),
(19, '청정원 쌈장 500g', 4000, 400, '매콤한 쌈장', 'ssamjang.jpg', 70, 0),
(19, '백설 약고추장 500g', 6000, 600, '맵지 않은 약고추장', 'mild_gochujang.jpg', 55, 0);

-- 조미료 (카테고리 ID: 20)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(20, '백설 맛소금 500g', 1500, 0, '요리에 필수적인 맛소금', 'salt.jpg', 120, 0),
(20, '청정원 다시다 500g', 4000, 400, '국물 요리의 감칠맛', 'dasida.jpg', 80, 0),
(20, '해표 고소한 참기름 320ml', 8000, 800, '고소한 참기름', 'sesame_oil.jpg', 60, 0),
(20, '오뚜기 올리고당 1.2kg', 5000, 500, '건강한 단맛', 'oligosaccharide.jpg', 70, 0),
(20, '사조 마늘맛기름 400g', 3000, 300, '풍부한 마늘 향', 'garlic_oil.jpg', 90, 0);

-- 신선식품 카테고리의 상품들 (카테고리 ID: 21-25)

-- 채소 (카테고리 ID: 21)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(21, '무농약 상추 200g', 3000, 300, '신선한 무농약 상추', 'lettuce.jpg', 50, 0),
(21, '양파 1.5kg', 2500, 250, '국내산 양파', 'onion.jpg', 80, 0),
(21, '당근 1kg', 2000, 200, '비타민 풍부한 당근', 'carrot.jpg', 70, 0),
(21, '깐마늘 300g', 4000, 400, '손질된 깐마늘', 'peeled_garlic.jpg', 60, 0),
(21, '방울토마토 500g', 3500, 350, '달콤한 방울토마토', 'cherry_tomato.jpg', 55, 0);

-- 과일 (카테고리 ID: 22)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(22, '제주 감귤 2kg', 10000, 1000, '새콤달콤한 제주 감귤', 'tangerine.jpg', 30, 0),
(22, '청송 사과 4입', 12000, 1200, '아삭한 청송 사과', 'apple.jpg', 40, 0),
(22, '성주 참외 1kg', 8000, 800, '달콤한 성주 참외', 'korean_melon.jpg', 35, 0),
(22, '나주 배 3입', 15000, 1500, '시원달콤한 나주 배', 'pear.jpg', 25, 0),
(22, '딸기 500g', 9000, 900, '상큼한 딸기', 'strawberry.jpg', 45, 0);

-- 육류 (카테고리 ID: 23)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(23, '무항생제 닭가슴살 400g', 6000, 0, '단백질 듬뿍 닭가슴살', 'chicken_breast.jpg', 40, 0),
(23, '한우 등심 200g', 25000, 2500, '1++ 등급 한우 등심', 'beef_sirloin.jpg', 20, 0),
(23, '돼지 앞다리살 500g', 7000, 700, '구이용 돼지 앞다리살', 'pork_shoulder.jpg', 35, 0),
(23, '양고기 갈비 300g', 18000, 1800, '호주산 양고기 갈비', 'lamb_ribs.jpg', 15, 0),
(23, '오리 훈제 슬라이스 200g', 5000, 500, '훈제향 가득한 오리 슬라이스', 'smoked_duck.jpg', 30, 0);

-- 생선 (카테고리 ID: 24)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(24, '제철 고등어 2마리', 8000, 800, '오메가3 풍부한 고등어', 'mackerel.jpg', 35, 0),
(24, '제주 갈치 2마리', 20000, 2000, '은빛 갈치', 'hairtail.jpg', 25, 0),
(24, '완도 전복 5마리', 15000, 1500, '신선한 완도 전복', 'abalone.jpg', 20, 0),
(24, '생연어 300g', 12000, 1200, '노르웨이산 생연어', 'salmon.jpg', 30, 0),
(24, '손질 오징어 500g', 7000, 700, '신선한 손질 오징어', 'squid.jpg', 40, 0);

-- 계란 (카테고리 ID: 25)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(25, '무항생제 유정란 10구', 5000, 500, '신선한 무항생제 유정란', 'eggs.jpg', 60, 0),
(25, '구운계란 10구', 4000, 400, '간편한 구운계란', 'boiled_eggs.jpg', 70, 0),
(25, '메추리알 30구', 3000, 300, '영양 만점 메추리알', 'quail_eggs.jpg', 80, 0),
(25, '오메가3 달걀 6구', 4500, 450, '오메가3 강화 달걀', 'omega3_eggs.jpg', 50, 0),
(25, '백색란 30구', 7000, 700, '신선한 백색란', 'white_eggs.jpg', 40, 0);

-- 음료 카테고리의 상품들 (카테고리 ID: 26-30)

-- 생수/탄산수 (카테고리 ID: 26)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(26, '삼다수 2L 6병', 12000, 1200, '제주 화산암반수 삼다수', 'samdasoo.jpg', 80, 0),
(26, '에비앙 1.5L 2병', 5000, 500, '프랑스 알프스 천연 미네랄워터', 'evian.jpg', 70, 0),
(26, '펠레그리노 탄산수 750ml 4병', 10000, 1000, '이탈리아 탄산수의 명가', 'pellegrino.jpg', 60, 0),
(26, '아이시스 8.0 2L 6병', 7000, 700, '8.0 칼슘 풍부한 생수', 'icis.jpg', 90, 0),
(26, '트레비 레몬 탄산수 500ml 4병', 4500, 450, '상큼한 레몬향 탄산수', 'trevi_lemon.jpg', 75, 0);

-- 탄산음료 (카테고리 ID: 27)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(27, '코카콜라 1.5L 2병', 4000, 400, '시원한 코카콜라', 'coca_cola.jpg', 100, 0),
(27, '펩시콜라 1.5L 2병', 3800, 380, '상쾌한 펩시콜라', 'pepsi.jpg', 95, 0),
(27, '칠성사이다 1.5L 2병', 3500, 350, '청량한 칠성사이다', 'chilsung_cider.jpg', 90, 0),
(27, '스프라이트 1.5L 2병', 3700, 370, '시원한 스프라이트', 'sprite.jpg', 85, 0),
(27, '환타 오렌지 1.5L 2병', 3600, 360, '오렌지향 가득한 환타', 'fanta_orange.jpg', 80, 0);

-- 과일주스 (카테고리 ID: 28)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(28, '델몬트 오렌지 주스 1.5L', 3500, 0, '100% 오렌지 주스', 'orange_juice.jpg', 70, 0),
(28, '미닛메이드 사과 주스 1L', 2800, 280, '신선한 사과 주스', 'apple_juice.jpg', 75, 0),
(28, '화이트 포도 주스 1L', 3000, 300, '달콤한 포도 주스', 'grape_juice.jpg', 65, 0),
(28, '자연은 석류 주스 1L', 4500, 450, '새콤달콤한 석류 주스', 'pomegranate_juice.jpg', 60, 0),
(28, '트로피카나 망고 주스 1L', 3200, 320, '열대과일 망고 주스', 'mango_juice.jpg', 70, 0);

-- 커피 (카테고리 ID: 29) 계속
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(29, '맥심 모카골드 커피믹스 100개입', 15000, 1500, '부드러운 모카골드 커피', 'coffee_mix.jpg', 50, 0),
(29, '네스카페 수프리모 커피믹스 100개입', 14000, 1400, '깊고 진한 수프리모 커피', 'supremo_coffee.jpg', 55, 0),
(29, '카누 미니 아메리카노 100개입', 16000, 1600, '언제 어디서나 아메리카노', 'kanu_americano.jpg', 45, 0),
(29, '스타벅스 하우스 블렌드 원두 200g', 10000, 1000, '균형 잡힌 스타벅스 블렌드', 'starbucks_house_blend.jpg', 40, 0),
(29, '맥심 카누 라떼 30개입', 8000, 800, '부드러운 라떼', 'kanu_latte.jpg', 60, 0);

-- 차 (카테고리 ID: 30)
INSERT INTO products (category_id, name, price, sale, content, image, available_quantity, reserved_quantity) VALUES 
(30, '동서 녹차 50티백', 5000, 500, '향긋한 녹차', 'green_tea.jpg', 60, 0),
(30, '타바론 캐모마일 티 20티백', 6000, 600, '편안한 캐모마일 티', 'chamomile_tea.jpg', 55, 0),
(30, '트와이닝 잉글리시 브렉퍼스트 50티백', 7000, 700, '진한 영국식 홍차', 'english_breakfast.jpg', 50, 0),
(30, '옥수수수염차 50티백', 4500, 450, '건강에 좋은 옥수수수염차', 'corn_silk_tea.jpg', 65, 0),
(30, '생강차 20티백', 5500, 550, '몸을 따뜻하게 해주는 생강차', 'ginger_tea.jpg', 55, 0);