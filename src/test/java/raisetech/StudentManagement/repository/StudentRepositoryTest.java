package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  //講義30回
  @Test
  void 受講生の全件検索が行えること(){
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  //課題30で追加
  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(8);
  }

  //課題30で追加
  @Test
  void IDに紐づく受講生の検索が行えること() {
    Student student = sut.searchStudent("1");
    assertThat(student).isNotNull();
    assertThat(student.getName()).isEqualTo("Taro Yamada");
  }

  //講義30回
  @Test
  void 受講生の登録が行えること(){
    Student student = new Student();
    student.setName("Yamada Taro");
    student.setKanaName("yamada taro");
    student.setNickname("yamachan");
    student.setEmail("yamada@gmail.com");
    student.setArea("Tokyo");
    student.setAge(25);
    student.setGender("male");
    student.setTelephoneNumber("09012345678");
    student.setRemarks("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }
  //講義30回=>課題30で修正（コンストラクタを利用）
  @Test
  void 受講生の登録が行えること_v2() {
    Student student = new Student(
        null,
        "Yamada Taro",
        "yamada taro",
        "yamachan",
        "yamada@gmail.com",
        "Tokyo",
        25,
        "male",
        "09012345678",
        "",
        false
    );

    sut.registerStudent(student);

    List<Student> actual = sut.search();
    assertThat(actual).hasSize(6);
  }

  //課題30で追加 => 修正(開始日・終了日も更新確認を行う)=>課題31で一部修正
  @Test
  void 受講生コース情報の新規登録が行えること() {
    LocalDateTime start = LocalDateTime.of(2025, 4, 1, 9, 0, 0);
    LocalDateTime end = LocalDateTime.of(2026, 4, 1, 17, 0, 0);

    StudentCourse studentCourse = new StudentCourse(
        null,               // id
        "1",                // studentId
        "AWS_Basic",        // courseName
        start,              // startDate
        end,                // endDate
        null                // courseStatus
    );

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual).hasSize(3);
    assertThat(actual)
        .anyMatch(c ->
            "AWS_Basic".equals(c.getCourseName()) &&
                start.equals(c.getStartDate()) &&
                end.equals(c.getEndDate())
        );
  }

  //課題30で追加 => 修正（全項目更新して一致を確認）
  @Test
  void 受講生の更新が行えること() {
    Student before = sut.searchStudent("1");

    Student updatedInfo = new Student(
        before.getId(),
        "Taro Updated",        // 氏名
        "taro updated",              // カナ氏名
        "taro_nickname_updated",     // ニックネーム
        "taro_updated@example.com",  // メール
        "Osaka",                     // 住所
        30,                          // 年齢
        "male",                      // 性別
        "09099998888",               // 電話番号
        "Updated remarks",           // 備考
        false                        // 削除フラグ
    );

    sut.updateStudent(updatedInfo);

    Student actual = sut.searchStudent("1");
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(updatedInfo);
  }

  //課題30で追加 => 修正
  @Test
  void 受講生コース情報のコース名の更新が行えること() {
    StudentCourse course = sut.searchStudentCourse("1").get(0);
    StudentCourse updatedCourse = new StudentCourse(
        course.getId(),
        course.getStudentId(),
        "Advanced Java",
        course.getStartDate(),
        course.getEndDate(),
        course.getCourseStatus() // 元の status を保持
    );

    sut.updateStudentCourse(updatedCourse);

    StudentCourse actual = sut.searchStudentCourse("1").stream()
        .filter(c -> "Advanced Java".equals(c.getCourseName()))
        .findFirst()
        .orElse(null);

    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(updatedCourse);
  }

  //課題30で追加 => 修正
  @Test
    // 事前にDBに入っている想定データ（1: Taro Yamada の例）
  void 受講生情報が全フィールド一致すること() {
    Student expected = new Student(
        "1",
        "Taro Yamada",
        "taro yamada",
        "taro",
        "taro@example.com",
        "Tokyo",
        25,
        "male",
        "09011112222",
        "",
        false
    );

    Student actual = sut.searchStudent("1");

    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  void 受講生コース情報が全フィールド一致すること() {
    // 事前にDBに入っている想定データ（例: 1 の Java_Basic コース）
    StudentCourse expected = new StudentCourse(
        "1",
        "1",
        "Java_Basic",
        LocalDateTime.of(2025, 1, 10, 9, 0, 0),
        LocalDateTime.of(2025, 3, 31, 17, 0, 0),
        null // status は今回は未設定
    );

    StudentCourse actual = sut.searchStudentCourse("1").get(0);

    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }
}

