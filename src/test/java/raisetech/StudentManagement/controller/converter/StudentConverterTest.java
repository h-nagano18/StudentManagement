package raisetech.StudentManagement.controller.converter;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentConverterTest {

  private final StudentConverter converter = new StudentConverter();

  @ParameterizedTest
  @MethodSource("provideStudentsAndCourses")
  void 受講生詳細とコース情報を正しくマッピングできること(Student student, List<StudentCourse> courses, int expectedCourseCount) {
    List<Student> students = List.of(student);
    List<StudentDetail> result = converter.convertStudentDetails(students, courses);
    assertEquals(expectedCourseCount, result.get(0).getStudentCourseList().size());
  }

  private static Stream<Arguments> provideStudentsAndCourses() {
    return Stream.of(
        Arguments.of(
            createStudent("1", "Yamada Taro"),
            List.of(createCourse("1", "Java_Basic"), createCourse("1", "Python_Basic")),
            2
        ),
        Arguments.of(
            createStudent("2", "Tanaka Hanako"),
            List.of(createCourse("2", "Python")),
            1
        )
    );
  }

  private static Student createStudent(String id, String name) {
    Student student = new Student();
    student.setId(id);
    student.setName(name);
    return student;
  }

  private static StudentCourse createCourse(String studentId, String courseName) {
    StudentCourse course = new StudentCourse();
    course.setStudentId(studentId);
    course.setCourseName(courseName);
    return course;
  }
}