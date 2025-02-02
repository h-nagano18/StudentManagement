package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private Map<String, String> student = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	public StudentManagementApplication() {
		student.put("name", "Yamada Taro");
		student.put("age", "35");
	}

	@GetMapping("/studentInfo")
	public  String getStudentInfo() {
		return student.toString();
	}

	@PostMapping("/studentInfo")
	public void setstudentInfo(@RequestParam String name, @RequestParam String age) {
		student.put("name", name);
		student.put("age", age);
	}

	@PostMapping("/studentName")
	public void updateStudentName(@RequestParam String name) {
		student.put("name", name);
	}
 }
