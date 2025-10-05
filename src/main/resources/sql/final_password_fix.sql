-- 最终密码修复脚本
-- 使用正确的BCrypt哈希值（密码：123456）

-- 先检查当前密码格式
SELECT '检查当前密码格式:' as info;
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start
FROM `user` 
ORDER BY id;

-- 更新所有用户密码为123456（使用正确的BCrypt哈希）
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 验证更新结果
SELECT '更新后的密码格式:' as info;
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start,
       CASE 
           WHEN password LIKE '$2a$%' THEN 'BCrypt格式正确'
           ELSE '密码格式错误'
       END as password_status
FROM `user` 
ORDER BY id;
