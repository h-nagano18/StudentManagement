package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生を扱うオブジェクト。
 */
@Getter
@Setter
public class Student {

  private String id;

  @NotBlank(message = "名前は必須です")
  @Size(max = 50, message = "名前は50文字以内で入力してください")
  private String name;

  @NotBlank(message = "カナ氏名は必須です")
  private String kanaName;

  private String nickname;
  private String email;
  private String area;

  @Min(value = 10, message = "年齢は10歳以上で入力してください")
  @Max(value = 100, message = "年齢は100歳以下で入力してください")
  private int age;

  private String gender;

  @Pattern(regexp = "^[0-9]{10,11}$", message = "電話番号は10桁または11桁の数字のみで入力してください")
  private String telephoneNumber;

  private String remarks;
  private boolean isDeleted;
}
