-- MySQL 8.0 专用密码修复脚本
-- 解决字符集和字段长度问题

-- 1. 检查当前数据库字符集
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME 
FROM information_schema.SCHEMATA 
WHERE SCHEMA_NAME = 'aid_db';

-- 2. 检查用户表字符集
SELECT TABLE_NAME, TABLE_COLLATION 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'aid_db' AND TABLE_NAME = 'user';

-- 3. 检查password字段定义
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, CHARACTER_SET_NAME, COLLATION_NAME
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'aid_db' AND TABLE_NAME = 'user' AND COLUMN_NAME = 'password';

-- 4. 修改password字段（MySQL 8.0兼容）
ALTER TABLE `user` MODIFY COLUMN `password` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL;

-- 5. 清空密码
UPDATE `user` SET `password` = '' WHERE `id` > 0;

-- 6. 设置正确的BCrypt哈希
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 7. 验证结果
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       CHAR_LENGTH(password) as char_length,
       LEFT(password, 10) as password_start,
       password as full_password
FROM `user` 
ORDER BY id;
