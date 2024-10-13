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
    datetime time with time zone NOT NULL,
    request_text character varying(100) NOT NULL,
    response_text character varying(350) NOT NULL,
	userid bigint NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (telegram_chat_id, username)
VALUES 
(1386864283, 'OlegVasilev');