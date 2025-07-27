package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 受講生を扱うオブジェクト。
 */
@Schema(description = "受講生詳細")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

  @Schema(description = "受講生ID（自動採番されます）")
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String id;

  @Schema(description = "氏名", required = true, example = "Yamada Taro")
  @NotBlank(message = "名前は必須です")
  @Size(max = 50, message = "名前は50文字以内で入力してください")
  private String name;

  @Schema(description = "カナ名", required = true, example = "yamada taro")
  @NotBlank(message = "カナ氏名は必須です")
  private String kanaName;

  @Schema(description = "ニックネーム", required = true , example = "tarochan")
  @NotBlank
  private String nickname;

  @Schema(description = "e-mail", required = true , example = "yamada@gmail.com")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "地域", required = true, example = "Tokyo")
  @NotBlank
  private String area;

  @Schema(description = "年齢（登録年齢は10歳以上、100歳以下）", required = true)
  @Min(value = 10, message = "年齢は10歳以上で入力してください")
  @Max(value = 100, message = "年齢は100歳以下で入力してください")
  private int age;

  @Schema(description = "性別", required = true)
  @NotBlank
  private String gender;

  @Schema(description = "電話番号（10桁または11桁の数字のみ（ハイフンなし））", required = true, example = "09012345678")
  @Pattern(regexp = "^[0-9]{10,11}$", message = "電話番号は10桁または11桁の数字のみで入力してください")
  private String telephoneNumber;

  @Schema(description = "備考")
  private String remarks;
  private boolean isDeleted;
}
