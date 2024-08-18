-- 더미 데이터

-- 카테고리

-- 최상위 카테고리

-- 1단계 하위 카테고리
INSERT INTO categories (name) VALUES ('냉장식품');
INSERT INTO categories (name) VALUES ('냉동식품');
INSERT INTO categories (name) VALUES ('가공식품');
INSERT INTO categories (name) VALUES ('신선식품');
INSERT INTO categories (name) VALUES ('음료');

-- 냉장식품 (category_id = 1)의 하위 카테고리
INSERT INTO categories (name, parent_category_id) VALUES ('유제품', 1);
INSERT INTO categories (name, parent_category_id) VALUES ('두부/콩나물', 1);
INSERT INTO categories (name, parent_category_id) VALUES ('햄/소시지', 1);
INSERT INTO categories (name, parent_category_id) VALUES ('김치/절임류', 1);
INSERT INTO categories (name, parent_category_id) VALUES ('연식품', 1);

-- 냉동식품 (category_id = 2)의 하위 카테고리
INSERT INTO categories (name, parent_category_id) VALUES ('냉동과일', 2);
INSERT INTO categories (name, parent_category_id) VALUES ('냉동채소', 2);
INSERT INTO categories (name, parent_category_id) VALUES ('냉동육류', 2);
INSERT INTO categories (name, parent_category_id) VALUES ('냉동생선', 2);
INSERT INTO categories (name, parent_category_id) VALUES ('냉동간편식', 2);

-- 가공식품 (category_id = 3)의 하위 카테고리
INSERT INTO categories (name, parent_category_id) VALUES ('과자/스낵', 3);
INSERT INTO categories (name, parent_category_id) VALUES ('라면/즉석식품', 3);
INSERT INTO categories (name, parent_category_id) VALUES ('통조림', 3);
INSERT INTO categories (name, parent_category_id) VALUES ('장류', 3);
INSERT INTO categories (name, parent_category_id) VALUES ('조미료', 3);

-- 신선식품 (category_id = 4)의 하위 카테고리
INSERT INTO categories (name, parent_category_id) VALUES ('채소', 4);
INSERT INTO categories (name, parent_category_id) VALUES ('과일', 4);
INSERT INTO categories (name, parent_category_id) VALUES ('육류', 4);
INSERT INTO categories (name, parent_category_id) VALUES ('생선', 4);
INSERT INTO categories (name, parent_category_id) VALUES ('계란', 4);

-- 음료 (category_id = 5)의 하위 카테고리
INSERT INTO categories (name, parent_category_id) VALUES ('생수/탄산수', 5);
INSERT INTO categories (name, parent_category_id) VALUES ('탄산음료', 5);
INSERT INTO categories (name, parent_category_id) VALUES ('과일주스', 5);
INSERT INTO categories (name, parent_category_id) VALUES ('커피', 5);
INSERT INTO categories (name, parent_category_id) VALUES ('차', 5);