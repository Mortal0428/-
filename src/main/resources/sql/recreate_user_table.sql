-- 重新创建用户表以修复字段问题
-- 备份现有数据，重新创建表结构

-- 1. 备份现有用户数据
CREATE TABLE `user_backup` AS SELECT * FROM `user`;

-- 2. 删除原表
DROP TABLE `user`;

-- 3. 重新创建用户表（正确的字段定义）
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `college` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `student_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `staff_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `student_no` (`student_no`),
  UNIQUE KEY `staff_no` (`staff_no`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 恢复用户数据（除了密码）
INSERT INTO `user` (id, username, name, role, class_name, college, student_no, staff_no, created_at)
SELECT id, username, name, role, class_name, college, student_no, staff_no, created_at 
FROM `user_backup`;

-- 5. 设置正确的密码
UPDATE `user` SET `password` = '$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq' WHERE `id` > 0;

-- 6. 验证结果
SELECT id, username, name, role, 
       LENGTH(password) as password_length,
       LEFT(password, 10) as password_start
FROM `user` 
ORDER BY id;

-- 7. 删除备份表
DROP TABLE `user_backup`;
