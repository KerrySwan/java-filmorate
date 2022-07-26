--Get all users
select id, name from users

--Get all films
select id, name from films

--Get friends of the user id = <input>
Select
  U.id 
  U.name 
from users U
inner join friendship F 
  on U.id = F.user_id
  and F.is_accepted
  and U.id = <input>

--Get mutual friends with ids <input_1> and <input_2>
Select
  F1.friend_id,
  U.name
from friendship F1
inner join friendship F2
  on F1.friend_id = F2.friend_id
  and F1.user_id = <input_1>
  and F2.user_id = <input_2>
inner join users U
  on F1.friend_id = U.user_id

--Get top 10 popular films
SELECT l.film_id,
       f.name
FROM likes AS l
INNER JOIN films AS f ON l.film_id = f.film_id
GROUP BY film_id
ORDER BY COUNT(film_id) DESC