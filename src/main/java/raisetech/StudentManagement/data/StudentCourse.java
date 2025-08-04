package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 受講生コースを扱うオブジェクト。
 */
@Schema(description = "受講生コース情報")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "コースID（自動採番されます）")
  private String id;
  @Schema(description = "受講生ID（Studentクラスと紐づきます）")
  private String studentId;
  @Schema(description = "コース名", required = true, example = "Python_Basic")
  private String courseName;
  @Schema(description = "開始日")
  private LocalDateTime startDate;
  @Schema(description = "終了日")
  private LocalDateTime endDate;
}
