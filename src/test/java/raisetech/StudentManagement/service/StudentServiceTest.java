package raisetech.StudentManagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {


  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    //事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    //実行
    sut.searchStudentList();
    //検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細検索_該当のIDが存在する場合_リポジトリが適切に呼び出せていること() {
    // 事前準備
    String studentId = "1";
    Student student = new Student();
    student.setId(studentId);
    List<StudentCourse> courseList = new ArrayList<>();
    when(repository.searchStudent(studentId)).thenReturn(student);
    when(repository.searchStudentCourse(studentId)).thenReturn(courseList);

    // 実行
    sut.searchStudent(studentId);

    // 検証
    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchStudentCourse(studentId);
  }

  @Test
  void 受講生詳細検索_該当のIDが存在しない場合_nullが返ること() {
    // 事前準備
    String studentId = "1";
    when(repository.searchStudent(studentId)).thenReturn(null);

    // 実行
    StudentDetail result = sut.searchStudent(studentId);

    // 検証
    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(0)).searchStudentCourse(studentId);
    assert result == null;
  }

  @Test
  void 受講生詳細登録_正常処理確認_リポジトリが適切に呼び出せていること() {
    // 事前準備
    Student student = new Student();
    student.setId("1");
    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = List.of(course);
    StudentDetail studentDetail = new StudentDetail(student, courseList);

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(course);
  }

  @Test
  void 受講生詳細登録_異常処理確認_コース情報が空の場合_例外が発生すること() {
    // 事前準備
    Student student = new Student();
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>());

    // 実行・検証
    try {
      sut.registerStudent(studentDetail);
      assert false : "例外が発生すべき";
    } catch (IllegalStateException e) {
      assert e.getMessage().equals("受講生コース情報が空です。最低1つのコースを登録してください。");
    }
  }

  @Test
  void 受講生詳細更新_リポジトリが適切に呼び出せていること() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = List.of(course);
    StudentDetail studentDetail = new StudentDetail(student, courseList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(course);
  }

  @Test
  void 受講生詳細登録_コース情報にstudentIdと開始日終了日が正しく設定されること() {
    //initStudentsCourseのテストはprivateを削除せずregisterStudentのテストにてAssertをすることで動作確認する
    // 事前準備
    Student student = new Student();
    student.setId("1");

    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = List.of(course);

    StudentDetail studentDetail = new StudentDetail(student, courseList);

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    assert course.getStudentId().equals("1");

    LocalDate startDate = course.getStartDate().toLocalDate();
    LocalDate endDate = course.getEndDate().toLocalDate();
    LocalDate today = LocalDate.now();

    // 開始日は登録日と一致するか
    assert startDate.equals(today) : "開始日が登録日ではありません";

    // 終了日は1年後の同日か
    assert endDate.equals(today.plusYears(1)) : "終了日が1年後ではありません";
  }
}