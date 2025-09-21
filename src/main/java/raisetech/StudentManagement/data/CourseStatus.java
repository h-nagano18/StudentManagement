package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * コース申込状況を扱うオブジェクト。（課題31回で追加）
 */
@Schema(description = "コース申込状況")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseStatus {

  @Schema(description = "ステータスID（自動採番）")
  private String id;

  @Schema(description = "受講生コースID（StudentCourseに紐づく）")
  private String studentCourseId;

  @Schema(description = "申込状況", example = "PRE_APPLIED / APPLIED / ONGOING / COMPLETED")
  private CourseStatusType status; // ← enum 型に変更

  public enum CourseStatusType {
    PRE_APPLIED,  // 仮申込
    APPLIED,      // 本申込
    ONGOING,      // 受講中
    COMPLETED     // 受講終了
  }
}