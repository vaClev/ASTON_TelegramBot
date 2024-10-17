-- сброс всей базы
DROP TABLE IF EXISTS requests_and_responses;
DROP TABLE IF EXISTS users;

-- Таблица пользователей бота
CREATE TABLE users
(
	id BIGSERIAL PRIMARY KEY,  -- автоинкрементный идентификатор
    telegram_chat_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(20)
);

-- Таблица запросов пользователей и ответов бота
CREATE TABLE requests_and_responses
(
	id uuid,  
    datetime timestamp NOT NULL,
    request_text character varying(500) NOT NULL,
    response_text character varying(500) NOT NULL,
	userid bigint NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(telegram_chat_id) ON DELETE CASCADE,
    PRIMARY KEY ( datetime, userid)
);


INSERT INTO users (telegram_chat_id, username)
VALUES 
(1386864283, 'OlegVasilev');

INSERT INTO requests_and_responses ( id, datetime, request_text, response_text, userid)
VALUES 
(gen_random_uuid(),'2023-10-17 10:23:55+02', 'moscow', 'test response', 1386864283),
(gen_random_uuid(), localtimestamp, 'moscow', '-5', 1386864283);