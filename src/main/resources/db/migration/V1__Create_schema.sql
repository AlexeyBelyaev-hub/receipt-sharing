CREATE TABLE IF NOT EXISTS app_user(
    app_user_id UUID NOT NULL PRIMARY KEY ,
    nick_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL CHECK(email LIKE '%@%.%'),
    password VARCHAR(60),
    is_enabled BOOLEAN,
    granted_authorities VARCHAR(150),
    UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS person(
    person_id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(150) CHECK(email LIKE '%@%.%'),
    phone_number VARCHAR(20),
    app_user_id UUID REFERENCES app_user(app_user_id)
);
CREATE TABLE IF NOT EXISTS contacts(
    app_user_id UUID REFERENCES app_user(app_user_id) NOT NULL,
    person_id UUID REFERENCES person(person_id) NOT NULL,
    create_date TIMESTAMP
);
CREATE TABLE IF NOT EXISTS receipt(
    receipt_id BIGSERIAL  NOT NULL PRIMARY KEY,
    receipt_number INTEGER NOT NULL,
    date_time TIMESTAMP NOT NULL,
    seller VARCHAR(150) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL CHECK(total_price>0),
    notes TEXT,
    owner_person_id UUID REFERENCES person(person_id) NOT NULL
);
CREATE TABLE IF NOT EXISTS receipt_items(
    item_id BIGSERIAL PRIMARY KEY,
    receipt_id BIGINT REFERENCES receipt(receipt_id) NOT NULL,
    title VARCHAR(150) NOT NULL,
    quantity INTEGER NOT NULL CHECK(quantity>0) ,
    price_per_one DECIMAL(12,2)  NOT NULL CHECK(price_per_one>0),
    price DECIMAL(12,2) NOT NULL CHECK(price>0) ,
    owner_of_item UUID REFERENCES person(person_id) NOT NULL
);

CREATE TABLE IF NOT EXISTS token(
    token UUID PRIMARY KEY,
    app_user_uuid UUID REFERENCES app_user(app_user_id) NOT NULL,
    expiry_date TIMESTAMP
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
