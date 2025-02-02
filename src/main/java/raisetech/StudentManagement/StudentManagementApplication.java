package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		student.put("name2", "Tanaka Jiro");
		student.put("age2", "18");
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

	@PostMapping("/studentInfo2")
	public void setstudentInfo2(@RequestParam String name2, @RequestParam String age2) {
		student.put("name2", name2);
		student.put("age2", age2);
	}

	@PostMapping("/studentName")
	public void updateStudentName(@RequestParam String name) {
		student.put("name", name);
	}
 }
