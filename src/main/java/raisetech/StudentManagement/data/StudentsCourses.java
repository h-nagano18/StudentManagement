package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private String courseId;
  private String studentId;
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
}
