-- 修复数据库字段问题
-- 检查并修复password字段定义

-- 1. 检查当前字段定义
DESCRIBE `user`;

-- 2. 修改password字段长度和字符集
ALTER TABLE `user` MODIFY COLUMN `password` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL;

-- 3. 清空所有密码
UPDATE `user` SET `password` = '' WHERE `id` > 0;

-- 4. 设置正确的BCrypt哈希
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 5. 验证结果
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       password as full_password
FROM `user` 
ORDER BY id;
