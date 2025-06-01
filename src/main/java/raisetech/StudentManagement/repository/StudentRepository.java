package raisetech.StudentManagement.repository;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.StudentsCourses;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return　受講生一覧（全件）
   */
  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  /**
   * 受講生の検索を行います。
   * @param id　受講生ID
   * @return　受講生
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(int id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourse();

  /**
   *受講生Dに紐づく受講生コース情報を検索します。
   *
   * @param studentId　受講生ID
   * @return　受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentsCoursesByStudentId(int studentId);

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

  // UPDATE文（updateStudent用のコード）
  @Update("""
  UPDATE students SET
    name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, e_mail = #{email},
    area = #{area}, age = #{age}, gender = #{gender},
    telephone_number = #{telephoneNumber}, remarks = #{remarks}, is_deleted = #{deleted}
  WHERE id = #{id}
  """)
  void updateStudent(Student student);

  // UPDATE文（updateStudentCourse用のコード）
  @Update("""
  UPDATE students_courses SET
    course_name =  #{courseName}
  WHERE id = #{id}
  """)
  void updateStudentsCourse(StudentsCourses studentsCourses);
}
