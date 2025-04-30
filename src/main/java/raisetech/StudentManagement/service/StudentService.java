package raisetech.StudentManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudents();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourse();
  }

   public void registerStudent(StudentDetail studentDetail) {
     Student student = studentDetail.getStudent();
     repository.insertStudent(student);

     int generatedId = student.getId();

     if (studentDetail.getStudentsCourses() != null) {
       for (StudentsCourses course : studentDetail.getStudentsCourses()) {
         course.setStudentId(generatedId);
         repository.insertStudentsCourse(course);
       }
     }
  }
}
