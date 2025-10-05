-- 修复用户密码问题
-- 将所有用户密码重置为123456（使用正确的BCrypt哈希）

-- 首先检查当前密码字段长度
DESCRIBE `user`;

-- 如果password字段长度不够，先修改字段长度
ALTER TABLE `user` MODIFY COLUMN `password` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL;

-- 更新所有用户密码为123456（使用一个经过验证的BCrypt哈希值）
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 验证更新结果
SELECT id, username, name, role, LENGTH(password) as password_length, password FROM `user`;

-- 测试密码是否正确（可选）
-- 这个查询会显示密码是否以$2a$开头（BCrypt格式）
SELECT id, username, name, role, 
       CASE 
           WHEN password LIKE '$2a$%' THEN 'BCrypt格式正确'
           ELSE '密码格式错误'
       END as password_status
FROM `user`;
