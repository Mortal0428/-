-- 检查所有用户数据
SELECT 
    id, 
    username, 
    name, 
    role, 
    staff_no, 
    student_no, 
    LENGTH(password) as password_length,
    LEFT(password, 10) as password_start
FROM user 
ORDER BY role, username;

-- 检查辅导员用户
SELECT 
    id, 
    username, 
    name, 
    role, 
    staff_no,
    LENGTH(password) as password_length,
    LEFT(password, 10) as password_start
FROM user 
WHERE role = 'counselor';

-- 检查所有角色分布
SELECT role, COUNT(*) as count 
FROM user 
GROUP BY role;
