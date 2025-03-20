package raisetech.StudentManagement.repository;
import raisetech.StudentManagement.data.Student;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository1 {

  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourse();
}
