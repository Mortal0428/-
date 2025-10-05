-- 检查数据库中的用户数据
SELECT id, username, name, role, staff_no, student_no, password FROM user ORDER BY role, username;

-- 检查辅导员用户
SELECT * FROM user WHERE role = 'counselor';

-- 检查所有角色
SELECT role, COUNT(*) as count FROM user GROUP BY role;
