package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import raisetech.StudentManagement.repository.StudentRepository;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	@Autowired
	private StudentRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/student")
	public  String getStudent(@RequestParam String name) {
		Student student = repository.searchByName(name);
		return student.getStudentId() + " " + student.getName() + " " + student.getAge() + "歳";
	}

	@PostMapping("/student")
	public void registerStudent(@RequestParam int studentId, @RequestParam String name, @RequestParam int age) {
			repository.registerStudent(studentId, name, age);
	}

	@PatchMapping("/student")
	public void updateStudent(@RequestParam String name, @RequestParam int age) {
		repository.updateStudent(name, age);
	}

	@DeleteMapping("/student")
	public void deleteStudent(String name) {
		repository.deleteStudent(name);
	}

	@GetMapping("/student-all")
	public List<Student> getAllStudents(){
		return repository.getAllStudents();
	}
 }
