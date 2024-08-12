-- 더미 데이터

-- 카테고리

-- 최상위 카테고리
INSERT INTO category (name) VALUES ('식품');

-- 1단계 하위 카테고리
INSERT INTO category (name, parent_category_id) VALUES ('냉장식품', 1);
INSERT INTO category (name, parent_category_id) VALUES ('냉동식품', 1);
INSERT INTO category (name, parent_category_id) VALUES ('가공식품', 1);
INSERT INTO category (name, parent_category_id) VALUES ('신선식품', 1);
INSERT INTO category (name, parent_category_id) VALUES ('음료', 1);

-- 2단계 하위 카테고리 (냉장식품의 하위)
INSERT INTO category (name, parent_category_id) VALUES ('유제품', 2);
INSERT INTO category (name, parent_category_id) VALUES ('육류', 2);
INSERT INTO category (name, parent_category_id) VALUES ('해산물', 2);

-- 3단계 하위 카테고리 (유제품의 하위)
INSERT INTO category (name, parent_category_id) VALUES ('우유', 7);
INSERT INTO category (name, parent_category_id) VALUES ('요구르트', 7);
INSERT INTO category (name, parent_category_id) VALUES ('치즈', 7);

-- 2단계 하위 카테고리 (일부 이미 있음, 추가)
INSERT INTO category (name, parent_category_id) VALUES ('즉석식품', 2);  -- 냉장식품의 하위
INSERT INTO category (name, parent_category_id) VALUES ('과일', 5);      -- 신선식품의 하위
INSERT INTO category (name, parent_category_id) VALUES ('채소', 5);      -- 신선식품의 하위
INSERT INTO category (name, parent_category_id) VALUES ('빵/베이커리', 4); -- 가공식품의 하위
INSERT INTO category (name, parent_category_id) VALUES ('과자/스낵', 4);   -- 가공식품의 하위
INSERT INTO category (name, parent_category_id) VALUES ('면류', 4);       -- 가공식품의 하위

-- 3단계 하위 카테고리 (확장)

-- 냉장식품 > 유제품 하위
INSERT INTO category (name, parent_category_id) VALUES ('버터', 7);
INSERT INTO category (name, parent_category_id) VALUES ('크림', 7);
INSERT INTO category (name, parent_category_id) VALUES ('마가린', 7);

-- 냉장식품 > 육류 하위
INSERT INTO category (name, parent_category_id) VALUES ('소고기', 8);
INSERT INTO category (name, parent_category_id) VALUES ('돼지고기', 8);
INSERT INTO category (name, parent_category_id) VALUES ('닭고기', 8);
INSERT INTO category (name, parent_category_id) VALUES ('오리고기', 8);

-- 냉장식품 > 해산물 하위
INSERT INTO category (name, parent_category_id) VALUES ('생선', 9);
INSERT INTO category (name, parent_category_id) VALUES ('조개류', 9);
INSERT INTO category (name, parent_category_id) VALUES ('갑각류', 9);
INSERT INTO category (name, parent_category_id) VALUES ('해조류', 9);

-- 냉장식품 > 즉석식품 하위
INSERT INTO category (name, parent_category_id) VALUES ('샐러드', 13);
INSERT INTO category (name, parent_category_id) VALUES ('샌드위치', 13);
INSERT INTO category (name, parent_category_id) VALUES ('도시락', 13);
INSERT INTO category (name, parent_category_id) VALUES ('반찬', 13);

-- 신선식품 > 과일 하위
INSERT INTO category (name, parent_category_id) VALUES ('사과', 14);
INSERT INTO category (name, parent_category_id) VALUES ('바나나', 14);
INSERT INTO category (name, parent_category_id) VALUES ('딸기', 14);
INSERT INTO category (name, parent_category_id) VALUES ('포도', 14);

-- 신선식품 > 채소 하위
INSERT INTO category (name, parent_category_id) VALUES ('상추', 15);
INSERT INTO category (name, parent_category_id) VALUES ('토마토', 15);
INSERT INTO category (name, parent_category_id) VALUES ('오이', 15);
INSERT INTO category (name, parent_category_id) VALUES ('당근', 15);

-- 가공식품 > 빵/베이커리 하위
INSERT INTO category (name, parent_category_id) VALUES ('식빵', 16);
INSERT INTO category (name, parent_category_id) VALUES ('케이크', 16);
INSERT INTO category (name, parent_category_id) VALUES ('쿠키', 16);
INSERT INTO category (name, parent_category_id) VALUES ('도넛', 16);

-- 가공식품 > 과자/스낵 하위
INSERT INTO category (name, parent_category_id) VALUES ('초콜릿', 17);
INSERT INTO category (name, parent_category_id) VALUES ('사탕', 17);
INSERT INTO category (name, parent_category_id) VALUES ('젤리', 17);
INSERT INTO category (name, parent_category_id) VALUES ('칩스', 17);

-- 가공식품 > 면류 하위
INSERT INTO category (name, parent_category_id) VALUES ('라면', 18);
INSERT INTO category (name, parent_category_id) VALUES ('파스타', 18);
INSERT INTO category (name, parent_category_id) VALUES ('우동', 18);
INSERT INTO category (name, parent_category_id) VALUES ('소바', 18);

-- 음료 하위 (2단계와 3단계 추가)
INSERT INTO category (name, parent_category_id) VALUES ('탄산음료', 6);
INSERT INTO category (name, parent_category_id) VALUES ('주스', 6);
INSERT INTO category (name, parent_category_id) VALUES ('커피', 6);
INSERT INTO category (name, parent_category_id) VALUES ('차', 6);

INSERT INTO category (name, parent_category_id) VALUES ('콜라', 41);
INSERT INTO category (name, parent_category_id) VALUES ('사이다', 41);
INSERT INTO category (name, parent_category_id) VALUES ('오렌지주스', 42);
INSERT INTO category (name, parent_category_id) VALUES ('사과주스', 42);
INSERT INTO category (name, parent_category_id) VALUES ('아메리카노', 43);
INSERT INTO category (name, parent_category_id) VALUES ('라떼', 43);
INSERT INTO category (name, parent_category_id) VALUES ('녹차', 44);
INSERT INTO category (name, parent_category_id) VALUES ('홍차', 44);

-- 냉동식품 하위 (2단계와 3단계 추가)
INSERT INTO category (name, parent_category_id) VALUES ('아이스크림', 3);
INSERT INTO category (name, parent_category_id) VALUES ('냉동피자', 3);
INSERT INTO category (name, parent_category_id) VALUES ('냉동만두', 3);

INSERT INTO category (name, parent_category_id) VALUES ('바닐라', 49);
INSERT INTO category (name, parent_category_id) VALUES ('초콜릿', 49);
INSERT INTO category (name, parent_category_id) VALUES ('치즈피자', 50);
INSERT INTO category (name, parent_category_id) VALUES ('페퍼로니피자', 50);
INSERT INTO category (name, parent_category_id) VALUES ('고기만두', 51);
INSERT INTO category (name, parent_category_id) VALUES ('김치만두', 51);