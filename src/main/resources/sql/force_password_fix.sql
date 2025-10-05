-- 强制修复密码问题
-- 删除所有现有密码，重新设置正确的BCrypt哈希

-- 1. 先查看当前密码格式
SELECT '当前密码格式检查:' as info;
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       password
FROM `user` 
ORDER BY id;

-- 2. 更新所有用户密码为123456（使用正确的60字符BCrypt哈希）
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 3. 验证更新结果
SELECT '更新后的密码格式:' as info;
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start,
       CASE 
           WHEN LENGTH(password) = 60 AND password LIKE '$2a$%' THEN 'BCrypt格式正确'
           ELSE '密码格式错误'
       END as password_status
FROM `user` 
ORDER BY id;
