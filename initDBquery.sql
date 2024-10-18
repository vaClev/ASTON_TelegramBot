-- сброс всей базы
DROP TABLE IF EXISTS requests_and_responses2;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS users;

-- Таблица пользователей бота
CREATE TABLE users
(
	id BIGSERIAL PRIMARY KEY,  -- автоинкрементный идентификатор
    telegram_chat_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(20)
);
--Таблица запросов адресов
CREATE TABLE addresses
(    
    address_text character varying(500) PRIMARY KEY,
	id uuid NOT NULL UNIQUE
);

-- Таблица запросов пользователей и ответов бота
CREATE TABLE requests_and_responses2
(
	id uuid NOT NULL,  
    datetime timestamp NOT NULL,
    addressid uuid NOT NULL,
    response_text character varying(500) NOT NULL,
	userid bigint NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(telegram_chat_id) ON DELETE CASCADE,
    FOREIGN KEY (addressid) REFERENCES addresses(id) ON DELETE CASCADE,
    PRIMARY KEY ( datetime, userid)
);


INSERT INTO users (telegram_chat_id, username)
VALUES 
(1386864283, 'OlegVasilev');

INSERT INTO addresses (id, address_text)
VALUES (gen_random_uuid(),'moscow')

INSERT INTO requests_and_responses ( id, datetime, addressid, response_text, userid)
VALUES 
(gen_random_uuid(),'2023-10-17 10:23:55+02', 
'b2e69fa8-0660-4e09-b95c-f6e674d1ee9c', 
'test response', 1386864283);