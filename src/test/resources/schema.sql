CREATE TABLE IF NOT EXISTS students
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    e_mail VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    telephone_number VARCHAR(20),
    remarks TEXT,
    is_deleted BOOLEAN
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);