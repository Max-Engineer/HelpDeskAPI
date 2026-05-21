-- Default users for testing
-- admin / admin123
-- tech1 / tech123
-- Passwords are BCrypt encoded
INSERT INTO users (username, password, role) VALUES ('admin', '$2a$10$DDQ.YWrlL.X48J8gDfAnSupXbm0KWXmE1oeTzb6jHsCDLQ01MDpVC', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('tech1', '$2a$10$rchNzdqZX11tdoJ3YRbrOOtN3doJIgL.x1l2lcvv99p76XNnXGjVC', 'TECHNICIAN');