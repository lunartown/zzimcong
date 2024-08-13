CREATE DATABASE IF NOT EXISTS zzimcong CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 데이터베이스를 사용하도록 설정
USE zzimcong;

-- 서버 문자셋 설정
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(64) NOT NULL,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    abuser BIT(1) DEFAULT 0,
    signout BIT(1) DEFAULT 0,
    role VARCHAR(20) DEFAULT 'USER',
    PRIMARY KEY (user_id)
);
CREATE INDEX idx_user_email ON user(email);

-- 주소 테이블
CREATE TABLE address (
    addr_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    addr VARCHAR(255) NOT NULL,
    addrDetail VARCHAR(255) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,
    tel VARCHAR(20) NOT NULL,
    message VARCHAR(255),
    def VARCHAR(1) DEFAULT 'N',
    PRIMARY KEY (addr_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 카테고리 테이블
CREATE TABLE category (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    parent_category_id BIGINT,
    name VARCHAR(64) NOT NULL,
    PRIMARY KEY (category_id),
    FOREIGN KEY (parent_category_id) REFERENCES category(category_id)
);

-- 상품 테이블
CREATE TABLE product (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    sale INT NOT NULL,
    content TEXT,
    image VARCHAR(255),
    count INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted VARCHAR(1) DEFAULT 'N',
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);
CREATE INDEX idx_product_name ON product(name);


-- 장바구니 상품 테이블
CREATE TABLE cart_item (
    cart_item_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    count INT NOT NULL,
    PRIMARY KEY (cart_item_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 주문 테이블
CREATE TABLE `order` (
    order_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_amount INT NOT NULL,
    payment_amount INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment VARCHAR(20),
    status VARCHAR(20) DEFAULT '주문완료',
    deleted VARCHAR(1) DEFAULT 'N',
    reason VARCHAR(255),
    name VARCHAR(64),
    addr VARCHAR(255) NOT NULL,
    addrDetail VARCHAR(255) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,
    tel VARCHAR(20) NOT NULL,
    message VARCHAR(255),
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);
CREATE INDEX idx_order_user_id ON `order`(user_id);

-- 주문 상품 테이블
CREATE TABLE order_item (
    order_item_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    price INT,
    count INT NOT NULL,
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES `order`(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);