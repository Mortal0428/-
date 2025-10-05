-- 检查当前数据库中的密码格式
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start,
       CASE 
           WHEN password LIKE '$2a$%' THEN 'BCrypt格式'
           WHEN password LIKE '$2b$%' THEN 'BCrypt格式'
           WHEN LENGTH(password) = 60 AND password LIKE '$%' THEN '可能是BCrypt'
           ELSE '不是BCrypt格式'
       END as password_format
FROM `user` 
ORDER BY id;
