package raisetech.StudentManagement.repository;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
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
    #{gender}, #{telephoneNumber}, #{remarks}, false
    )
  """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Insert("""
  INSERT INTO students_courses (
    student_id, course_name, start_date, end_date)
    VALUES (
    #{studentId}, #{courseName}, #{startDate}, #{endDate}
    )
  """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudentsCourse(StudentsCourses studentsCourses);

  //IDに紐づいた学生を検索
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(int id);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentsCoursesByStudentId(int studentId);

  // UPDATE文（updateStudent用）
  @Update("""
  UPDATE students SET
    name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, e_mail = #{email},
    area = #{area}, age = #{age}, gender = #{gender},
    telephone_number = #{telephoneNumber}, remarks = #{remarks}
  WHERE id = #{id}
  """)
  void updateStudent(Student student);

  //UPDATE文（コース情報の削除→再登録）
  @Delete("DELETE FROM students_courses WHERE student_id = #{studentId}")
  void deleteStudentsCoursesByStudentId(int studentId);
}
