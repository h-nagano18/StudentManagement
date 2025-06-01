package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.contloller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービス。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudents();
    List<StudentsCourses> studentsCoursesList = repository.searchStudentsCourse();
    return  converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudentById(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCoursesByStudentId(student.getId());
    return new StudentDetail(student, studentsCourses);
  }

  /**
   * 受講生新規登録処理です。
   *
   * @param studentDetail　受講生情報
   * @return　受講生情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
      for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
        studentsCourse.setStudentId(studentDetail.getStudent().getId());
        studentsCourse.setStartDate(LocalDateTime.now());
        studentsCourse.setEndDate(LocalDateTime.now().plusYears(1));
        repository.insertStudentsCourse(studentsCourse);
      }
      return  studentDetail;
    }

  /**
   * 受講生情報の更新処理です。
   *
   * @param studentDetail　受講生情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      repository.updateStudentsCourse(studentsCourse);
    }
  }
}