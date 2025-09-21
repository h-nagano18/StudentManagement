INSERT INTO students (name, kana_name, nickname, e_mail, area, age, gender, telephone_number, remarks, is_deleted)
VALUES ('Taro Yamada', 'taro yamada', 'taro', 'taro@example.com', 'Tokyo', 25, 'male', '09011112222', '', false),
       ('Hanako Sato', 'hanako sato', 'hana', 'hana@example.com', 'Osaka', 22, 'female', '08033334444', '', false),
       ('Jiro Suzuki', 'jiro suzuki', 'jiro', 'jiro@example.com', 'Nagoya', 28, 'male', '07055556666', '', false),
       ('Misaki Takahashi', 'misaki takahashi', 'misaki', 'misaki@example.com', 'Fukuoka', 24, 'female', '09077778888', '', false),
       ('Ken Tanaka', 'ken tanaka', 'ken', 'ken@example.com', 'Sapporo', 30, 'male', '08099990000', '', false);

INSERT INTO students_courses (student_id, course_name, start_date, end_date)
VALUES (1, 'Java_Basic', '2025-01-10 09:00:00', '2025-03-31 17:00:00'),
       (1, 'Spring_Intro', '2025-04-01 09:00:00', '2025-06-30 17:00:00'),
       (2, 'Python_Basic', '2025-01-15 09:00:00', '2025-04-15 17:00:00'),
       (2, 'Data_Science_Intro', '2025-05-01 09:00:00', '2025-07-31 17:00:00'),
       (3, 'Web_App_Development', '2025-02-01 09:00:00', '2025-05-01 17:00:00'),
       (4, 'Database_Design', '2025-03-01 09:00:00', '2025-06-01 17:00:00'),
       (5, 'Cloud_Computing', '2025-04-01 09:00:00', '2025-07-01 17:00:00'),
       (5, 'DevOps_Fundamentals', '2025-07-01 09:00:00', '2025-09-30 17:00:00');

INSERT INTO course_status (student_course_id, status)
VALUES
  (1, 'ONGOING'),
  (2, 'COMPLETED'),
  (3, 'PRE_APPLIED'),
  (4, 'APPLIED'),
  (5, 'ONGOING'),
  (6, 'PRE_APPLIED'),
  (7, 'COMPLETED'),
  (8, 'APPLIED');