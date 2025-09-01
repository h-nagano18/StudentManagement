package raisetech.StudentManagement.controller.converter;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

import java.util.List;
import raisetech.StudentManagement.repository.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before(){
    sut = new StudentConverter();
  }

  //講義30回で修正①
  @Test
  void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Java_Basic");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
  }

  //講義30回で修正②
  @Test
  void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("2");
    studentCourse.setCourseName("Javaコース");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
  }

  private static Student createStudent() {
    Student student = new Student();
    student.setId("1");
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
    return student;
  }

  @Test
  void コースが受講生に紐づかない場合は空のコースリストになる() {
    Student student = new Student(
        "99", "Ghost Student", "ghost student", "Ghost",
        "ghost@example.com", "Nowhere", 0, "unknown",
        "00000000000", "", false
    );

    StudentCourse course = new StudentCourse(
        "1", "1", "Java",
        LocalDateTime.of(2025, 1, 1, 9, 0),
        LocalDateTime.of(2025, 3, 31, 17, 0),
        null
    );

    List<StudentDetail> result = sut.convertStudentDetails(List.of(student), List.of(course));

    assertEquals(1, result.size());
    assertEquals("99", result.get(0).getStudent().getId());
    assertEquals(0, result.get(0).getStudentCourseList().size()); // 紐づいていない → 0件
  }

  @Test
  void 受講生詳細がnullの場合_エラー処理となること() {
    assertThrows(NullPointerException.class, () -> {
      sut.convertStudentDetails(null, List.of());
    });
  }
}