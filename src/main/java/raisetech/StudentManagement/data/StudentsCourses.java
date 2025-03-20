package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private String course_id;
  private String student_id;
  private String course_name;
  private LocalDate start_date;
  private LocalDate end_date;
}
