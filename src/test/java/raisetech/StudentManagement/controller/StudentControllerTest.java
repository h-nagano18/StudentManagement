package raisetech.StudentManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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