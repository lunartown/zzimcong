-- 유제품 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(10, '맛있는 우유', 2500, 2500, '신선한 1등급 우유입니다.', 'milk.jpg', 100),
(11, '프로바이오틱스 요구르트', 3000, 2700, '장 건강에 좋은 요구르트입니다.', 'yogurt.jpg', 80),
(12, '모짜렐라 치즈', 5000, 4750, '피자에 잘 어울리는 치즈입니다.', 'mozzarella.jpg', 50);

-- 육류 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(20, '한우 등심', 30000, 30000, '1++ 등급 한우 등심입니다.', 'beef.jpg', 30),
(21, '삼겹살 500g', 15000, 14250, '국내산 돼지고기 삼겹살입니다.', 'pork_belly.jpg', 50),
(22, '닭가슴살 200g', 4000, 4000, '단백질이 풍부한 닭가슴살입니다.', 'chicken_breast.jpg', 100);

-- 해산물 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(25, '제철 고등어', 8000, 7200, '기름진 제철 고등어입니다.', 'mackerel.jpg', 40),
(26, '완도 전복', 20000, 20000, '신선한 완도산 전복입니다.', 'abalone.jpg', 20),
(27, '킹크랩 1kg', 50000, 47500, '알래스카산 킹크랩입니다.', 'king_crab.jpg', 10);

-- 과일 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(29, '청송 사과 5kg', 25000, 22500, '아삭한 청송 사과입니다.', 'apple.jpg', 30),
(30, '필리핀 바나나 1송이', 3500, 3500, '달콤한 필리핀 바나나입니다.', 'banana.jpg', 50),
(31, '논산 딸기 2kg', 18000, 17100, '새콤달콤한 논산 딸기입니다.', 'strawberry.jpg', 40);

-- 채소 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(33, '상추 200g', 2000, 2000, '신선한 상추입니다.', 'lettuce.jpg', 100),
(34, '대저 토마토 2kg', 10000, 9000, '당도 높은 대저 토마토입니다.', 'tomato.jpg', 40),
(35, '오이 3개', 2500, 2500, '아삭한 오이입니다.', 'cucumber.jpg', 80);

-- 빵/베이커리 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(37, '식빵', 3000, 3000, '부드러운 식빵입니다.', 'bread.jpg', 50),
(38, '초코 케이크', 25000, 23750, '달콤한 초코 케이크입니다.', 'choco_cake.jpg', 20),
(39, '마카롱 세트', 12000, 10800, '다양한 맛의 마카롱 세트입니다.', 'macaron.jpg', 30);

-- 음료 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(45, '콜라 1.5L', 2500, 2500, '시원한 콜라입니다.', 'cola.jpg', 100),
(47, '오렌지 주스 1L', 3000, 2850, '신선한 오렌지 주스입니다.', 'orange_juice.jpg', 80),
(47, '아메리카노', 1500, 1500, '진한 아메리카노입니다.', 'americano.jpg', 200);

-- 냉동식품 카테고리 상품
INSERT INTO product (category_id, name, price, sale, content, image, count)
VALUES 
(49, '바닐라 아이스크림', 5000, 4500, '부드러운 바닐라 아이스크림입니다.', 'vanilla_icecream.jpg', 50),
(52, '치즈 피자', 12000, 11400, '고소한 치즈 피자입니다.', 'cheese_pizza.jpg', 30),
(55, '고기 만두 500g', 8000, 8000, '육즙 가득한 고기 만두입니다.', 'meat_dumpling.jpg', 40);