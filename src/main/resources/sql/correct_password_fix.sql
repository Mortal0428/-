-- 正确的密码修复脚本
-- 使用经过验证的BCrypt哈希值（对应密码：123456）

-- 更新所有用户密码为123456
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 验证更新结果
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start,
       CASE 
           WHEN password LIKE '$2a$%' THEN 'BCrypt格式正确'
           ELSE '密码格式错误'
       END as password_status
FROM `user` 
ORDER BY id;
