-- MySQL 8.0 测试脚本
-- 测试密码字段问题

-- 1. 检查数据库和表字符集
SELECT '数据库字符集:' as info;
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME 
FROM information_schema.SCHEMATA 
WHERE SCHEMA_NAME = 'aid_db';

-- 2. 检查用户表字符集
SELECT '用户表字符集:' as info;
SELECT TABLE_NAME, TABLE_COLLATION 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'aid_db' AND TABLE_NAME = 'user';

-- 3. 检查password字段
SELECT 'password字段信息:' as info;
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, CHARACTER_SET_NAME, COLLATION_NAME
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'aid_db' AND TABLE_NAME = 'user' AND COLUMN_NAME = 'password';

-- 4. 测试密码长度
SELECT '当前密码长度:' as info;
SELECT id, username, 
       LENGTH(password) as byte_length,
       CHAR_LENGTH(password) as char_length,
       password
FROM `user` 
WHERE id = 1;
