# java-filmorate
Educational project with using lombok and spring boot
_________________________
## Database scheme with relations

//todo переделать картинку

![Database](filmorate.png)

Table friendship suppose to store duplicates like in table bellow.
It's necessary to not to scan whole table to find specific user friends.
Also, its simplifies joins.
In addition, it is never gonna be much of a duplicates because most of the users never gonna befriend 
each in such quantities to produce pollution with duplicated rows. 

|user_id|friend_id|is_accepted|
|---|---|---|
|1|2|t|
|1|3|t|
|2|1|t|
_____________
## SQL business-logic examples using this architecture

[sql_examples link](sql_examples.sql)

```sql
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
```