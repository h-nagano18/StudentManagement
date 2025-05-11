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

  public List<StudentDetail> searchStudentDetailList() {
    List<Student> students = repository.searchStudents();
    List<StudentDetail> details = new ArrayList<>();

    for (Student student : students) {
      List<StudentsCourses> courses = repository.searchStudentsCoursesByStudentId(student.getId());
      StudentDetail detail = new StudentDetail();
      detail.setStudent(student);
      detail.setStudentsCourses(courses);
      details.add(detail);
    }

    return details;
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

  @Transactional
  public StudentDetail getStudentDetailById(int id) {
    Student student = repository.searchStudentById(id);
    List<StudentsCourses> courses = repository.searchStudentsCoursesByStudentId(id);
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentsCourses(courses);
    return detail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    // ① students テーブルは常に更新
    Student student = studentDetail.getStudent();
    //更新対象の確認
    System.out.println("Taget updated studentId: " + studentDetail.getStudent().getId());
    repository.updateStudent(studentDetail.getStudent());

    // 現在のDBにあるコース情報と比較
    int studentId = studentDetail.getStudent().getId();
    List<StudentsCourses> dbCourses = repository.searchStudentsCoursesByStudentId(studentId);
    List<StudentsCourses> inputCourses = studentDetail.getStudentsCourses();

    if (!isSameCourses(dbCourses, inputCourses)) {
      repository.deleteStudentsCoursesByStudentId(studentId);
      for (StudentsCourses studentsCourse : inputCourses) {
        studentsCourse.setStudentId(studentId);
        studentsCourse.setStartDate(LocalDateTime.now());
        studentsCourse.setEndDate(LocalDateTime.now().plusYears(1));
        repository.insertStudentsCourse(studentsCourse);
      }
    }
  }
    // 差分があるか比較するヘルパーメソッド
    private boolean isSameCourses(List<StudentsCourses> dbCourses, List<StudentsCourses> inputCourses) {
      if (dbCourses.size() != inputCourses.size()) {
        return false;
     }

     for (int i = 0; i < dbCourses.size(); i++) {
        StudentsCourses db = dbCourses.get(i);
       StudentsCourses input = inputCourses.get(i);
        if (!Objects.equals(db.getCourseName(), input.getCourseName())) {
          return false;
        }
    }
    return true;
  }
}