SELECT * FROM sample21;

select * from sample21;
Select * From Sample21;
SELECT * FROM SAMPLE21;


DESC sample21; // 테이블 구조 참조


SELECT no, name FROM sample21;


SELECT * FROM sample21;
SELECT * From sample21 WHERE no=2;

SELECT * FROM sample21;
SELECT * FROM sample21 	WHERE no <>2;

SELECT * FROM sample21;
SELECT * FROM sample21 WHERE name='박준용';

SELECT * FROM sample21;
SELECT * FROM sample21 WHERE birthday = NULL;

SELECT * FROM sample21;
SELECT * FROM sample21 WHERE birthday IS NULL;


SELECT * FROM sample24;


SELECT * FROM sample24;
SELECT * FROM sample24 WHERE a<>0 AND b<>0;

SELECT * FROM sample24;
SELECT * FROM sample24 WHERE a <> 0 OR b<>0;


SELECT * FROM sample24 WHERE no=1 OR 2;
SELECT * FROM sample24 WHERE no=1 OR no=2;

SELECT * FROM sample24;
SELECT * FROM sample24 WHERE a=1 OR a=2 AND b=1 OR b=2;

SELECT * FROM sample24 WHERE (a=1 OR a=2) AND (b=1 OR b=2);


SELECT * FROM sample24;
SELECT * FROM sample24 WHERE NOT (a<>0 OR b<>0);


SELECT * FROM sample25;


SELECT * FROM sample25;
SELECT * FROM sample25 WHERE text LIKE 'SQL%'

SELECT * FROM sample25;
SELECT * FROM sample25 WHERE text LIKE '%SQL%'

SELECT * FROM sample25;
SELECT * FROM sample25 WHERE test LIKE '%/%%'