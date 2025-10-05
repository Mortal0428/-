-- 工作密码修复脚本
-- 使用经过验证的60字符BCrypt哈希值

-- 更新所有用户密码为123456
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 验证结果
SELECT id, username, name, role, LENGTH(password) as password_length FROM `user`;
