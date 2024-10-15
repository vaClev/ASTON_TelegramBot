-- сброс всей базы
DROP TABLE IF EXISTS requests_and_responses;
DROP TABLE IF EXISTS users;

-- Таблица пользователей бота
CREATE TABLE users
(
	id SERIAL PRIMARY KEY,  -- автоинкрементный идентификатор
    telegram_chat_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(20)
);

-- Таблица запросов и ответов бота
CREATE TABLE requests_and_responses
(
	id character varying(100) PRIMARY KEY,  
    datetime timestamp with time zone NOT NULL,
    request_text character varying(100) NOT NULL,
    response_text character varying(350) NOT NULL,
	userid bigint NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(telegram_chat_id) ON DELETE CASCADE
);

INSERT INTO users (telegram_chat_id, username)
VALUES 
(1386864283, 'OlegVasilev');

INSERT INTO requests_and_responses (id, datetime, request_text, response_text, userid)
VALUES ('2004-10-19 10:23:54+02 1386864283', '2004-10-19 10:23:54+02', 'moscow', 'test response', 1386864283);