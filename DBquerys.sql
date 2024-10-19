select * from requests_and_responses;
select * from users;
select * from addresses;

SELECT datetime, request_text, response_text, userid 
FROM requests_and_responses 
WHERE datetime >= localtimestamp-interval '6 hour' and request_text ='moscow';

SELECT COUNT(1)
FROM addresses
WHERE address_text = 'moscow';

SELECT 
requests_and_responses.id,
requests_and_responses.datetime,
users.username,
addresses.address_text, 
requests_and_responses.response_text  
FROM requests_and_responses
JOIN users ON requests_and_responses.userid = users.telegram_chat_id
JOIN addresses ON requests_and_responses.addressid = addresses.id
ORDER BY requests_and_responses.datetime;