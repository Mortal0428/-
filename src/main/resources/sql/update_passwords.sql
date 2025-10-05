-- 更新所有用户密码为123456（BCrypt加密）
-- 密码123456的BCrypt哈希值：$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi

-- 更新所有用户密码
UPDATE `user` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi' WHERE `id` > 0;

-- 验证更新结果
SELECT id, username, name, role, password FROM `user`;
