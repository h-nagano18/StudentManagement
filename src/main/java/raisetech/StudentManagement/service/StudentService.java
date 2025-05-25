package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
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

  //IDに紐づく受講生情報と受講コース名の取得
  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudentById(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCoursesByStudentId(student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
    return studentDetail;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourse();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.insertStudent(studentDetail.getStudent());

    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
        studentsCourse.setStudentId(studentDetail.getStudent().getId());
        studentsCourse.setStartDate(LocalDateTime.now());
        studentsCourse.setEndDate(LocalDateTime.now().plusYears(1));
        repository.insertStudentsCourse(studentsCourse);
      }
    }
  }

  // 受講生・コースの更新
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      repository.updateStudentsCourse(studentsCourse);
    }
  }
}