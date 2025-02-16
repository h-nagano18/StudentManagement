package raisetech.StudentManagement.repository;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.Student;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student WHERE name = #{name}")
  Student searchByName(String name);

  @Insert("INSERT INTO student (name, age) VALUES (#{name}, #{age})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(int studentId, String name, int age);

  @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  void updateStudent(String name, int age);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);

  @Select("SELECT * FROM student")
  List<Student> getAllStudents();
  }