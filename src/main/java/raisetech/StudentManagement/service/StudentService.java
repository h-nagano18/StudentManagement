package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.CourseStatus.CourseStatusType;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
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
   * 受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();

    // ★ 課題31で追加: Service側でCourseStatusを取得してセットする
    for (StudentCourse course : studentCourseList) {
      List<CourseStatus> statuses = repository.searchCourseStatusByCourseId(course.getId());
      if (!statuses.isEmpty()) {
        course.setCourseStatus(statuses.get(0));
      }
    }
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生詳細
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    if (student == null) {
      return null;
    }
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());

    // ★  課題31で追加: Service側でCourseStatusを取得してセットする
    for (StudentCourse course : studentCourse) {
      List<CourseStatus> statuses = repository.searchCourseStatusByCourseId(course.getId());
      if (!statuses.isEmpty()) {
        course.setCourseStatus(statuses.get(0));
      }
    }

    List<CourseStatus> courseStatuses = studentCourse.stream()
        .map(StudentCourse::getCourseStatus)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    return new StudentDetail(student, studentCourse, courseStatuses);
  }

  /**
   * 受講生詳細検索です。
   * 名前または申込状況（ステータス）紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   */
  public List<StudentDetail> searchStudentsByConditions(String name, String status) {
    // 条件に合う Student 一覧を取得
    List<Student> students = repository.searchByConditions(name, status);

    if (students.isEmpty()) {
      return Collections.emptyList();
    }

    // 学生IDを抽出してコース情報取得
    List<String> studentIds = students.stream()
        .map(Student::getId)
        .collect(Collectors.toList());

    List<StudentCourse> studentCourses = new ArrayList<>();
    for (String id : studentIds) {
      List<StudentCourse> courses = repository.searchStudentCourse(id);

      // ★ CourseStatus をセット
      for (StudentCourse course : courses) {
        List<CourseStatus> statuses = repository.searchCourseStatusByCourseId(course.getId());
        if (!statuses.isEmpty()) {
          course.setCourseStatus(statuses.get(0));
        }
      }

      studentCourses.addAll(courses);
    }

    // Converter で StudentDetail にまとめて返却
    return converter.convertStudentDetails(students, studentCourses);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   * @param studentDetail　受講生詳細
   * @return　登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    // 受講生コースが空ならエラー
    if (studentDetail.getStudentCourseList() == null || studentDetail.getStudentCourseList().isEmpty()) {
      throw new IllegalStateException("受講生コース情報が空です。最低1つのコースを登録してください。");
    }

    // 1. 受講生登録
    repository.registerStudent(student);

    // 2. 受講生コース登録
    for (StudentCourse course : studentDetail.getStudentCourseList()) {
      initStudentsCourse(course, student.getId());

      // コース登録
      repository.registerStudentCourse(course);

      // コースステータス登録を PRE_APPLIED にセットして DB に反映
      CourseStatus initialStatus = new CourseStatus(null, course.getId(), CourseStatus.CourseStatusType.PRE_APPLIED);
      repository.registerCourseStatus(initialStatus);

      // Java オブジェクトにも反映
      course.setCourseStatus(initialStatus);
    }

    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse　受講生コース情報
   * @param id　受講生
   */
  void initStudentsCourse(StudentCourse studentCourse, String id) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(id);
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生・受講生コース・申込状況をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    // 受講生情報を更新
    repository.updateStudent(studentDetail.getStudent());

    // コース情報を更新
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      repository.updateStudentCourse(studentCourse);

      // コースに紐づくステータスがある場合は更新
      if (studentCourse.getCourseStatus() != null) {
        repository.updateCourseStatus(studentCourse.getCourseStatus());
      }
    });
  }

  /**
   * 例外処理の発生テスト用（Service）
   * @return
   */
  public void errorFromService() {
    throw new IllegalArgumentException("このリクエストはService内での不正です");
  }
}