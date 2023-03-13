package it.develhope.TestCrud;

import it.develhope.TestCrud.entities.Student;
import it.develhope.TestCrud.repositories.StudentRepository;
import it.develhope.TestCrud.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Drumstyle92
 * JUnit test class to test the operation of the StudentService class
 */
@SpringBootTest
@ActiveProfiles(value = "test")
public class StudentServiceTest {

    /**
     * Auto-injected dependency of StudentService to be able to use methods belonging to it
     */
    @Autowired
    private StudentService studentService;

    /**
     * Auto-injected dependency in order to use the methods of the Studentrepository interface
     */
    @Autowired
    private StudentRepository studentRepository;

    /**
     * @throws Exception the exception
     * It returns the student from the database and checks
     * that the returned student object is non-null,
     * that the student ID is also non-null, and that
     * the student's work status is set to 'true'
     */
    @Test
    void checkStudentActivation() throws Exception {

        Student student = new Student();

        student.setName("Paul");

        student.setSurname("Burns");

        student.setWorking(true);

        Student studentFromDB = studentRepository.save(student);

        assertThat(studentFromDB).isNotNull();

        assertThat(studentFromDB.getId()).isNotNull();

        Student studentFromService = studentService.setStudentIsWorkingStatus(student.getId(), true);

        assertThat(studentFromService).isNotNull();

        assertThat(studentFromService.getId()).isNotNull();

        assertThat(studentFromService.isWorking()).isTrue();

        Student studentFromFind = studentRepository.findById(studentFromDB.getId()).get();

        assertThat(studentFromFind).isNotNull();

        assertThat(studentFromFind.getId()).isNotNull();

        assertThat(studentFromFind.getId()).isEqualTo(studentFromDB.getId());

        assertThat(studentFromFind.isWorking()).isTrue();

    }

}