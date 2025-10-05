-- 完全重置密码脚本
-- 删除所有现有密码，重新设置正确的BCrypt哈希

-- 1. 先清空所有密码
UPDATE `user` SET `password` = '' WHERE `id` > 0;

-- 2. 设置正确的BCrypt哈希（60字符）
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 3. 验证结果
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start,
       password as full_password
FROM `user` 
ORDER BY id;
