-- 测试BCrypt哈希
-- 验证密码哈希是否正确

-- 1. 检查当前密码格式
SELECT '当前密码检查:' as info;
SELECT id, username, 
       LENGTH(password) as password_length,
       password as full_password
FROM `user` 
WHERE id = 1;

-- 2. 测试不同的BCrypt哈希
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE id = 1;

-- 3. 验证结果
SELECT '更新后检查:' as info;
SELECT id, username, 
       LENGTH(password) as password_length,
       password as full_password
FROM `user` 
WHERE id = 1;
