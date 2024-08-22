DROP DATABASE IF EXISTS zzimcong;
CREATE DATABASE IF NOT EXISTS zzimcong
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 데이터베이스를 사용하도록 설정
USE zzimcong;

-- 서버 문자셋 설정
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(64) NOT NULL,
    name VARCHAR(64) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(64) NOT NULL,
    abuser TINYINT(1) NOT NULL DEFAULT 0,
    signout TINYINT(1) NOT NULL DEFAULT 0,
    role ENUM('USER', 'ADMIN', 'SELLER') NOT NULL DEFAULT 'USER',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 주소 테이블
CREATE TABLE IF NOT EXISTS address (
    address_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(64) NOT NULL,
    street_address VARCHAR(255) NOT NULL,
    address_detail VARCHAR(255) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    message VARCHAR(255),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (address_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 카테고리 테이블
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    parent_category_id BIGINT UNSIGNED,
    name VARCHAR(64) NOT NULL,
    depth INT NOT NULL DEFAULT 0,
    path VARCHAR(255),
    PRIMARY KEY (category_id),
    FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 상품 테이블
CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    category_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    sale INT NOT NULL,
    content TEXT,
    image VARCHAR(255),
    available_quantity INT NOT NULL,
    reserved_quantity INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    INDEX idx_product_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 장바구니 테이블
CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    product_id BIGINT UNSIGNED NOT NULL,
    count INT NOT NULL,
    PRIMARY KEY (cart_item_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 주문 주소 테이블
CREATE TABLE IF NOT EXISTS order_addresses (
    order_address_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    street_address VARCHAR(255) NOT NULL,
    address_detail VARCHAR(255) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    message VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_address_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 결제 정보 테이블
CREATE TABLE IF NOT EXISTS payment_details (
    payment_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    cardNumber VARCHAR(16) NOT NULL,
    cardHolderName VARCHAR(64) NOT NULL,
    expirationDate VARCHAR(5) NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    PRIMARY KEY (payment_id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
)

-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    order_address_id BIGINT UNSIGNED,
    order_amount DECIMAL(10,2) NOT NULL,
    payment_amount DECIMAL(10,2) NOT NULL,
    payment_id BIGINT UNSIGNED,
    payment_method ENUM('KB', 'KAKAO', 'NAVER', 'KEB', 'IBK', 'NH') NOT NULL,
    status ENUM('CREATED', 'STOCK_RESERVED', 'PAYMENT_PROCESSED', 'SAGA_FAILED', 'ORDER_COMPLETED', 
                'PREPARING_FOR_SHIPMENT', 'SHIPPING', 'DELIVERED', 'ORDER_CONFIRMED', 'CANCELED', 
                'REFUND_REQUESTED', 'REFUND_COMPLETED') NOT NULL DEFAULT 'CREATED',
    deleted BOOLEAN DEFAULT FALSE,
    cancellation_reason VARCHAR(255),
    refund_reason VARCHAR(255),
    delivered_at DATETIME,
    refund_requested_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (order_address_id) REFERENCES order_addresses(order_address_id),
    FOREIGN KEY (payment_id) REFERENCES payment_details(payment_id),
    INDEX idx_order_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 주문 상품 테이블
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id BIGINT UNSIGNED NOT NULL,
    product_id BIGINT UNSIGNED NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;