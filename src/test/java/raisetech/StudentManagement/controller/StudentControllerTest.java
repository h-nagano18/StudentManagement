package raisetech.StudentManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.util.List;
import java.util.Set;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  @Autowired
  private ObjectMapper objectMapper;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  // 共通のStudent作成メソッド
  private Student createValidStudent() {
    return new Student(
        "1",                     // id
        "Yamada Taro",           // name
        "yamada taro",           // kanaName
        "yamachan",              // nickname
        "yamada@gmail.com",    // email
        "Tokyo",                 // area
        25,                      // age
        "male",                  // gender
        "09012345678",           // telephoneNumber
        "備考なし",               // remarks
        false                    // isDeleted
    );
  }

  //講義28回で追加
  @Test
  void 受講生詳細の一覧検索ができて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/api/v1/studentList"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  //講義28回で追加
  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること(){
    Student student = new Student();
    student.setId("テストです。");
    student.setName("Yamada Taro");
    student.setKanaName("yamada taro");
    student.setNickname("yamachan");
    student.setEmail("a0001@gmail.com");
    student.setArea("Sendai");
    student.setAge(40);
    student.setGender("male");
    student.setTelephoneNumber("09011112222");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertEquals(1, violations.size());
  }

  //①以下講義29回で実装(エラー回避のため、Controllerの一部を修正)
  @Test
  void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/api/v1/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  //課題31回で追加
  @Test
  void 名前とステータスで検索できること() throws Exception {
    // モックの戻り値を準備
    StudentDetail detail = new StudentDetail();
    detail.setStudent(createValidStudent());
    when(service.searchStudentsByConditions("Taro", "APPLIED"))
        .thenReturn(List.of(detail));

    mockMvc.perform(get("/api/v1/search")
            .param("name", "Taro")
            .param("status", "APPLIED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].student.name").value("Yamada Taro"));

    verify(service, times(1)).searchStudentsByConditions("Taro", "APPLIED");
  }

  @Test
  void 条件なしで検索できること() throws Exception {
    when(service.searchStudentsByConditions(null, null))
        .thenReturn(List.of());

    mockMvc.perform(get("/api/v1/search"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentsByConditions(null, null);
  }

  @Test
  void 名前のみで検索できること() throws Exception {
    StudentDetail detail = new StudentDetail();
    detail.setStudent(createValidStudent());

    when(service.searchStudentsByConditions("Taro", null))
        .thenReturn(List.of(detail));

    mockMvc.perform(get("/api/v1/search").param("name", "Taro"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].student.name").value("Yamada Taro"));
  }

  @Test
  void ステータスのみで検索できること() throws Exception {
    StudentDetail detail = new StudentDetail();
    detail.setStudent(createValidStudent());

    when(service.searchStudentsByConditions(null, "APPLIED"))
        .thenReturn(List.of(detail));

    mockMvc.perform(get("/api/v1/search").param("status", "APPLIED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].student.name").value("Yamada Taro"));
  }

  @Test
  void 名前やステータス検索で不正なステータスを渡すと400が返ること() throws Exception {
    // 存在しないステータスを指定
    String invalidStatus = "INVALID_STATUS";

    mockMvc.perform(get("/api/v1/search")
            .param("name", "Taro")
            .param("status", "INVALID_STATUS"))
        .andExpect(status().isBadRequest());

    // Service は呼ばれないことを確認
    verify(service, times(0)).searchStudentsByConditions(any(), any());
  }

  //②以下講義29回で実装=>31回課題で修正
  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(put("/api/v1/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
              "student": {
                "id": "1",
                "name": "Yamada Taro",
                "kanaName": "yamada taro",
                "nickname": "yamachan",
                "email": "a0001@gmail.com",
                "area": "Sendai",
                "age": "40",
                "gender": "male",
                "telephoneNumber": "09011112222",
                "remarks": ""
              },
              "studentCourseList": [
                {
                  "id": "1",
                  "studentId": "1",
                  "courseName": "JAVA_Basic",
                  "startDate": "2023-06-15T00:00:00",
                  "endDate": "2025-06-30T00:00:00",
                  "courseStatus": {
                    "status": "APPLIED"
                  }
                }
              ]
            }
            """))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  //④以下講義29回で実装
  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/api/v1/triggerErrorController"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("【不正な引数】このリクエストはController内での不正です"));
  }

  //以下課題28回修正
  @Test
  void IDに紐づく受講生詳細の取得が成功すること() throws Exception {
    Student student = createValidStudent();
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);

    when(service.searchStudent("1")).thenReturn(detail);

    mockMvc.perform(get("/api/v1/student/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value("1"))
        .andExpect(jsonPath("$.student.name").value("Yamada Taro"));
  }

  @Test
  void 入力チェックに違反しないこと() {
    Student student = createValidStudent();
    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void IDが不正な場合はバリデーションエラーになること() {
    Student student = createValidStudent();
    student.setId("テストです。"); // 数字以外を入れて違反させる

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    assertThat(violations).extracting("message").containsOnly("数字のみ入力するようにしてください。");
  }

  @Test
  void 名前が空文字のときにバリデーションエラーになること() {
    Student student = createValidStudent();
    student.setName(""); // 名前だけ空にする

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    assertThat(violations).extracting("message").containsOnly("名前は必須です");
  }
  @Test
  void 受講生詳細の登録が成功すること() throws Exception {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(createValidStudent());

    String json = objectMapper.writeValueAsString(studentDetail);

    mockMvc.perform(post("/api/v1/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk());
  }
  @Test
  void 受講生詳細の更新が成功すること() throws Exception {
    Student updatedStudent = createValidStudent();
    updatedStudent.setName("Yamada Ichiro");

    StudentDetail updatedDetail = new StudentDetail();
    updatedDetail.setStudent(updatedStudent);

    mockMvc.perform(put("/api/v1/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedDetail)))
        .andExpect(status().isOk())
        .andExpect(result -> assertThat(result.getResponse().getContentAsString())
            .isEqualTo("更新処理が成功しました。"));
  }
}