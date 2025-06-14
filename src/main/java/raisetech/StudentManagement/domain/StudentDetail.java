package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生詳細を扱うオブジェクト。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StudentDetail {
  @Valid
  private Student student;
  private List<StudentCourse> studentCourseList;

}
