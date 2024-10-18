select * from requests_and_responses;
select * from users;
select * from addresses;

SELECT datetime, request_text, response_text, userid 
FROM requests_and_responses 
WHERE datetime >= localtimestamp-interval '6 hour' and request_text ='moscow';

SELECT COUNT(1)
FROM addresses
WHERE address_text = 'moscow';