package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
/**
 * 受講生の検索や登録、更新を行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
@RequestMapping("/api/v1")
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧（全件）
   */
  //OpenAPI用
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @ApiResponse(responseCode = "200", description = "一覧取得成功")

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  //OpenAPI用
  @Operation(summary = "受講生詳細のID検索", description = "IDに紐づく任意の受講生の情報を検索します。")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "取得成功"),
      @ApiResponse(responseCode = "404", description = "受講生が見つかりません")
  })

  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      //OpenAPI用
      @Parameter(description = "受講生のID", required = true)
      //例外処理の追加（存在しないID）
      @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    StudentDetail studentDetail = service.searchStudent(id);
    if (studentDetail == null) {
      throw new java.util.NoSuchElementException("受講生ID " + id + " に該当するデータは存在しません");
    }
    return studentDetail;
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  //OpenAPI用
  @Operation(summary = "受講生登録", description = "受講生を登録します。")

  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います（論理削除）
   *
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  //OpenAPI用
  @Operation(summary = "受講生更新", description = "受講生詳細の更新を行います。キャンセルフラグの更新もここで行います（論理削除）。")

  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 例外処理の発生テスト用(Cntoroller)
   * @return
   */
  @GetMapping("/triggerErrorController")
  public ResponseEntity<String> triggerErrorController() {
    throw new IllegalArgumentException("このリクエストはController内での不正です");
  }

  /**
   * 例外処理の発生テスト用(Service)
   * @return
   */
  @GetMapping("/triggerErrorService")
  public ResponseEntity<String> triggerErrorService() {
    service.errorFromService();
    return ResponseEntity.ok("この行には到達しません");
  }
}
