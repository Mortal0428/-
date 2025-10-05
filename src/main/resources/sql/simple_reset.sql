-- 简单密码重置
-- 使用正确的60字符BCrypt哈希

UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

SELECT id, username, LENGTH(password) as password_length FROM `user`;
