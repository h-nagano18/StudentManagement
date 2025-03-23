package raisetech.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.repository.StudentRepository1;

@Service
public class StudentService {

  private StudentRepository1 repository;

  @Autowired
  public StudentService(StudentRepository1 repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    List<Student> studentsIn30List = new ArrayList<>();
    for (Student student : repository.searchStudents()) {
      if (student.getAge() >= 30 && student.getAge() <= 39) {
        studentsIn30List.add(student);
      }
    }
    return studentsIn30List;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    List<StudentsCourses> JavaCourseList = new ArrayList<>();
    for (StudentsCourses course : repository.searchStudentsCourse()) {
      if (course.getCourse_name().contains("JAVA")) {
        JavaCourseList.add(course);
      }
    }
    return  JavaCourseList;
  }
}
