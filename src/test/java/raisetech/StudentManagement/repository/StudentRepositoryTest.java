package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
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

  //課題30で追加
  @Test
  void 受講生コース情報の新規登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");  // String型ならこのままOK
    studentCourse.setCourseName("AWS_Basic");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual.size()).isEqualTo(3);
    assertThat(actual.stream().anyMatch(c -> "AWS_Basic".equals(c.getCourseName()))).isTrue();
  }

  //課題30で追加
  @Test
  void 受講生の更新が行えること() {
    Student student = sut.searchStudent("1"); // Taro Yamada
    student.setNickname("taro_updated");
    student.setEmail("taro_updated@example.com");

    sut.updateStudent(student);

    Student updated = sut.searchStudent("1");
    assertThat(updated.getNickname()).isEqualTo("taro_updated");
    assertThat(updated.getEmail()).isEqualTo("taro_updated@example.com");
  }

  //課題30で追加
  @Test
  void 受講生コース情報のコース名の更新が行えること() {
    List<StudentCourse> courses = sut.searchStudentCourse("1");
    StudentCourse courseToUpdate = courses.get(0);
    courseToUpdate.setCourseName("Advanced Java");

    sut.updateStudentCourse(courseToUpdate);

    List<StudentCourse> updatedCourses = sut.searchStudentCourse("1");
    assertThat(updatedCourses.stream().anyMatch(c -> "Advanced Java".equals(c.getCourseName()))).isTrue();
  }

  //課題30で追加
  @Test
  void 受講生情報が全フィールド一致すること() {
    // 事前にDBに入っている想定データ（1: Taro Yamada の例）
    Student expected = new Student();
    expected.setId("1");
    expected.setName("Taro Yamada");
    expected.setKanaName("taro yamada");
    expected.setNickname("taro");
    expected.setEmail("taro@example.com");
    expected.setArea("Tokyo");
    expected.setAge(25);
    expected.setGender("male");
    expected.setTelephoneNumber("09011112222");
    expected.setRemarks("");
    expected.setDeleted(false);

    Student actual = sut.searchStudent("1");

    // 全フィールド比較
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  void 受講生コース情報が全フィールド一致すること() {
    // 事前にDBに入っている想定データ（例: 1 の Java_Basic コース）
    StudentCourse expected = new StudentCourse();
    expected.setId("1");
    expected.setStudentId("1");
    expected.setCourseName("Java_Basic");
    expected.setStartDate(LocalDateTime.of(2025, 1, 10, 9, 0, 0));
    expected.setEndDate(LocalDateTime.of(2025, 3, 31, 17, 0, 0));

    StudentCourse actual = sut.searchStudentCourse("1").get(0);

    // 全フィールド比較
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }
}

