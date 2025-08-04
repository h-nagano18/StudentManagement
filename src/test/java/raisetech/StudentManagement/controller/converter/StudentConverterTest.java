package raisetech.StudentManagement.controller.converter;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentConverterTest {

  private final StudentConverter converter = new StudentConverter();

  @ParameterizedTest
  @MethodSource("provideStudentsAndCourses")
  void 受講生詳細とコース情報の全フィールドの移送ができること(
      Student student, List<StudentCourse> courses, int expectedCourseCount) {

    List<Student> students = List.of(student);
    List<StudentDetail> result = converter.convertStudentDetails(students, courses);

    assertEquals(1, result.size());

    StudentDetail detail = result.get(0);

    // Studentフィールドの移送確認
    assertEquals(student.getId(), detail.getStudent().getId());
    assertEquals(student.getName(), detail.getStudent().getName());
    assertEquals(student.getKanaName(), detail.getStudent().getKanaName());
    assertEquals(student.getNickname(), detail.getStudent().getNickname());
    assertEquals(student.getEmail(), detail.getStudent().getEmail());
    assertEquals(student.getArea(), detail.getStudent().getArea());
    assertEquals(student.getAge(), detail.getStudent().getAge());
    assertEquals(student.getGender(), detail.getStudent().getGender());
    assertEquals(student.getTelephoneNumber(), detail.getStudent().getTelephoneNumber());
    assertEquals(student.getRemarks(), detail.getStudent().getRemarks());

    // Courseフィールドの移送確認
    assertEquals(expectedCourseCount, detail.getStudentCourseList().size());
    for (int i = 0; i < expectedCourseCount; i++) {
      StudentCourse expected = courses.get(i);
      StudentCourse actual = detail.getStudentCourseList().get(i);

      assertEquals(expected.getId(), actual.getId());
      assertEquals(expected.getStudentId(), actual.getStudentId());
      assertEquals(expected.getCourseName(), actual.getCourseName());
      assertEquals(expected.getStartDate(), actual.getStartDate());
      assertEquals(expected.getEndDate(), actual.getEndDate());
    }
  }

  private static Stream<Arguments> provideStudentsAndCourses() {
    return Stream.of(
        Arguments.of(
            new Student(
                "1", "Yamada Taro", "yamada taro", "Yama",
                "yamada@example.com", "Tokyo", 25, "male",
                "09012345678", "New", false
            ),
            List.of(
                new StudentCourse("1", "1", "Java_Basic",
                    LocalDateTime.of(2025, 1, 10, 9, 0),
                    LocalDateTime.of(2025, 3, 20, 17, 0)),
                new StudentCourse("2", "1", "Python_Basic",
                    LocalDateTime.of(2025, 4, 1, 9, 0),
                    LocalDateTime.of(2025, 6, 15, 17, 0))
            ),
            2
        ),
        Arguments.of(
            new Student(
                "2", "Tanaka Hanako", "tanaka hanako", "Tana",
                "tanaka@example.com", "Osaka", 30, "female",
                "08098765432", "", false
            ),
            List.of(
                new StudentCourse("3", "2", "Python",
                    LocalDateTime.of(2025, 2, 1, 10, 0),
                    LocalDateTime.of(2025, 4, 30, 16, 0))
            ),
            1
        )
    );
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
        LocalDateTime.of(2025, 3, 31, 17, 0)
    );

    List<StudentDetail> result = converter.convertStudentDetails(List.of(student), List.of(course));

    assertEquals(1, result.size());
    assertEquals("99", result.get(0).getStudent().getId());
    assertEquals(0, result.get(0).getStudentCourseList().size()); // 紐づいていない → 0件
  }

  @Test
  void 受講生詳細がnullの場合_エラー処理となること() {
    assertThrows(NullPointerException.class, () -> {
      converter.convertStudentDetails(null, List.of());
    });
  }
}