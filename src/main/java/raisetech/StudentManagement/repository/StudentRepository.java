package raisetech.StudentManagement.repository;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import raisetech.StudentManagement.data.Student;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourse();

  @Insert("""
  INSERT INTO students (
    name, kana_name, nickname, e_mail, area, age,
    gender, telephone_number, remarks, is_deleted)
    VALUES (
    #{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age},
    #{gender}, #{telephoneNumber}, #{remarks}, #{isDeleted}
    )
  """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Insert("""
  INSERT INTO students_courses (
    course_id, student_id, course_name, start_date, end_date)
    VALUES (
    #{courseId}, #{studentId}, #{courseName}, #{startDate}, #{endDate}
    )
  """)
  void insertStudentsCourse(StudentsCourses studentsCourses);
}
