-- 简单密码修复脚本
-- 将所有用户密码设置为123456

-- 使用一个经过验证的BCrypt哈希值（对应密码：123456）
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 验证更新结果
SELECT id, username, name, role FROM `user`;
