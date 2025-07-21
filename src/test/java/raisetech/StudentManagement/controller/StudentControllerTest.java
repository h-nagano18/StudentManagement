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

  @Test
  void 受講生詳細の一覧検索ができて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/api/v1/studentList"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }
  @Test
  void 受講生詳細の受講生でIDに適切な値を入力した時に入力チェックに異常が発生しないこと(){
    Student student = new Student();
    student.setId("1");
    student.setName("Yamada Taro");
    student.setKanaName("yamada taro");
    student.setNickname("yamachan");
    student.setEmail("a0001@gmail.com");
    student.setArea("Sendai");
    student.setAge(40);
    student.setGender("male");
    student.setTelephoneNumber("09011112222");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

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

    for (ConstraintViolation<Student> violation : violations) {
      System.out.println("バリデーションエラー: " + violation.getPropertyPath() + " - " + violation.getMessage());
    }
    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("数字のみ入力するようにしてください。");
  }
  @Test
  void 受講生詳細登録時に名前が空の場合に入力チェックに掛かること() throws Exception {
    Student student = new Student();
    student.setId("1");
    student.setName("");
    student.setKanaName("yamada taro");
    student.setNickname("yamachan");
    student.setEmail("a0001@gmail.com");
    student.setArea("Sendai");
    student.setAge(40);
    student.setGender("male");
    student.setTelephoneNumber("09011112222");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    for (ConstraintViolation<Student> violation : violations) {
      System.out.println("バリデーションエラー: " + violation.getPropertyPath() + " - " + violation.getMessage());
    }
    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("名前は必須です");
  }

  @Test
  void 受講生詳細のIDに紐づく任意の受講生の情報が正しく取得できること() throws Exception {
    Student student = new Student();
    student.setId("1");
    student.setName("Yamada Taro");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    when(service.searchStudent("1")).thenReturn(studentDetail);

    mockMvc.perform(get("/api/v1/student/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value("1"))
        .andExpect(jsonPath("$.student.name").value("Yamada Taro"));
  }

  @Test
  void 受講生詳細の登録できること() throws Exception {
    Student student = new Student();
    student.setName("Test Desu");
    student.setKanaName("test desu");
    student.setNickname("test");
    student.setEmail("test@gamil.com");
    student.setArea("Tokyo");
    student.setAge(25);
    student.setGender("male");
    student.setTelephoneNumber("09011111111");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    String jsonRequest = objectMapper.writeValueAsString(studentDetail);

    mockMvc.perform(post("/api/v1/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isOk());
  }
  @Test
  void 受講生詳細を更新できること() throws Exception {
    Student student = new Student();
    student.setId("1");
    student.setName("Test Updated");
    student.setKanaName("test update");
    student.setNickname("TaroUp");
    student.setEmail("taro_updated@gamil.com");
    student.setArea("Osaka");
    student.setAge(26);
    student.setGender("male");
    student.setTelephoneNumber("09011111111");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    String jsonRequest = objectMapper.writeValueAsString(studentDetail);

    mockMvc.perform(put("/api/v1/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isOk());
  }
}