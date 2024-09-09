-- 최상위 카테고리 (가상의 루트 카테고리)
INSERT INTO categories (name, depth, path) VALUES ('전체 카테고리', 0, '/1');

-- 1단계 하위 카테고리
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉장식품', 1, 1, '/1/2');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉동식품', 1, 1, '/1/3');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('가공식품', 1, 1, '/1/4');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('신선식품', 1, 1, '/1/5');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('음료', 1, 1, '/1/6');

-- 냉장식품 (category_id = 2)의 하위 카테고리
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('유제품', 2, 2, '/1/2/7');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('두부/콩나물', 2, 2, '/1/2/8');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('햄/소시지', 2, 2, '/1/2/9');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('김치/절임류', 2, 2, '/1/2/10');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('연식품', 2, 2, '/1/2/11');

-- 냉동식품 (category_id = 3)의 하위 카테고리
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉동과일', 3, 2, '/1/3/12');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉동채소', 3, 2, '/1/3/13');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉동육류', 3, 2, '/1/3/14');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉동생선', 3, 2, '/1/3/15');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('냉동간편식', 3, 2, '/1/3/16');

-- 가공식품 (category_id = 4)의 하위 카테고리
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('과자/스낵', 4, 2, '/1/4/17');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('라면/즉석식품', 4, 2, '/1/4/18');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('통조림', 4, 2, '/1/4/19');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('장류', 4, 2, '/1/4/20');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('조미료', 4, 2, '/1/4/21');

-- 신선식품 (category_id = 5)의 하위 카테고리
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('채소', 5, 2, '/1/5/22');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('과일', 5, 2, '/1/5/23');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('육류', 5, 2, '/1/5/24');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('생선', 5, 2, '/1/5/25');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('계란', 5, 2, '/1/5/26');

-- 음료 (category_id = 6)의 하위 카테고리
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('생수/탄산수', 6, 2, '/1/6/27');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('탄산음료', 6, 2, '/1/6/28');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('과일주스', 6, 2, '/1/6/29');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('커피', 6, 2, '/1/6/30');
INSERT INTO categories (name, parent_category_id, depth, path) VALUES ('차', 6, 2, '/1/6/31');