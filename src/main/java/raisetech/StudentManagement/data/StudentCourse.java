package raisetech.StudentManagement.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生コースを扱うオブジェクト。
 */
@Getter
@Setter
public class StudentCourse {

  private String id;
  private String studentId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
