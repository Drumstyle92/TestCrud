package it.develhope.TestCrud;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.develhope.TestCrud.controllers.StudentController;
import it.develhope.TestCrud.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Drumstyle92
 *
 */
@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTest {

    /**
     * Dependency with Autowired annotation to be ablJunit class to test
     * the controller end-to-end including mock HTTP calls and verify that
     * the controller is working as expected also sets the active profile
     * to "test"e to test the CRUD of the StudentController
     */
    @Autowired
    private StudentController studentController;

    /**
     * MockMvc dependency with Autowired annotation to simulate HTTP requests and get responses
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Autowired annotated dependency to serialize and deserialize JSON objects into Java objects
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This test checks if the student controller instance has been successfully
     * loaded and initialized, verifying that it is not null. If it is null,
     * the test fails. Otherwise, the test passes
     */
    @Test
    void studentControllerLoads() {
        assertThat(studentController).isNotNull();
    }

    /**
     * @param id Selected id parameter
     * @return The selected student returns if the test is successful
     * @throws Exception
     * Method that receives a student id as argument, and simulates a get request
     * to return the selected student then verifies that the obtained Student object
     * is not null and that the student id is valid. If an exception occurs during
     * the process, the method returns null
     */
    private Student getStudentFromId(Long id) throws Exception{

        MvcResult result = this.mockMvc.perform(get("/student/" + id))

                .andDo(print())

                .andExpect(status().isOk())

                .andReturn();

        try {

            String studentJSON = result.getResponse().getContentAsString();

            Student student = objectMapper.readValue(studentJSON, Student.class);

            assertThat(student).isNotNull();

            assertThat(student.getId()).isNotNull();

            return student;

        }catch (Exception e){

            return null;

        }

    }

    /**
     * @return Return a student with all arguments
     * @throws Exception
     * method that creates a Student object
     */
    private Student createAStudent() throws Exception {

        Student student = new Student();

        student.setName("Paul");

        student.setSurname("Burns");

        student.setWorking(true);

        return createAStudent(student);

    }

    /**
     * @param student Parameter where to insert a Student object
     * @return the created student returns
     * @throws Exception
     * method used to create a student and takes an object of type Student
     * as input and passes it to another method that creates a POST request
     * to create a student using the data passed as a parameter
     */
    private Student createAStudent(Student student) throws Exception {

        MvcResult result = createAStudentRequest(student);

        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

        assertThat(studentFromResponse).isNotNull();

        assertThat(studentFromResponse.getId()).isNotNull();

        return studentFromResponse;

    }

    /**
     * @return returns the result as "MvcResult" object
     * @throws Exception
     * Method that creates an HTTP request to create a student with the specified details
     */
    private MvcResult createAStudentRequest() throws Exception {

        Student student = new Student();

        student.setName("Paul");

        student.setSurname("Burns");

        student.setWorking(true);

        return createAStudentRequest(student);

    }

    /**
     * @param student Parameter passed into a Student object
     * @return returns the result as a MvcResult object
     * @throws Exception
     * Post method to create a student using the details passed and
     * checks that the student passed as an argument is not null.
     * Otherwise, the method returns a null value
     */
    private MvcResult createAStudentRequest(Student student) throws Exception {

        if(student == null) return null;

        String studentJSON = objectMapper.writeValueAsString(student);

        return this.mockMvc.perform(post("/student")

                .contentType(MediaType.APPLICATION_JSON)

                .content(studentJSON))

                .andDo(print())

                .andExpect(status().isOk())

                .andReturn();
    }

    /**
     * @throws Exception the exception
     * Test method that checks a student's creation
     */
    @Test
    void createAStudentTest() throws Exception {

        Student studentFromResponse = createAStudent();

    }

    /**
     * @throws Exception the exception
     * Test that checks the reading of the student list
     * from the application backend checks if the student
     * list is not empty and prints the number of students
     * in the database on the screen.
     * If the student list is not empty, the test will be successful.
     * If instead the list is empty, the test will fail
     */
    @Test
    void readStudentsList() throws Exception {

        createAStudentRequest();

        MvcResult result =this.mockMvc.perform(get("/student/"))

                .andDo(print())

                .andExpect(status().isOk())

                .andReturn();

        List<Student> studentsFromResponse = objectMapper.readValue(result.getResponse()

                .getContentAsString(), List.class);

        System.out.println("Students in database are: " + studentsFromResponse.size());

        assertThat(studentsFromResponse.size()).isNotZero();

    }

    /**
     * @throws Exception the exception
     * Test method that checks reading of a specific student
     * from the application backend checks if the student ID
     * returned by the backend is the same as the newly created student ID.
     * If the ID is the same, the test will be successful.
     * If the two IDs do not match, the test will fail
     */
    @Test
    void readSingleStudent() throws Exception {

        Student student = createAStudent();

        Student studentFromResponse = getStudentFromId(student.getId());

        assertThat(studentFromResponse.getId()).isEqualTo(student.getId());

    }

    /**
     * @throws Exception the exception
     * Test method that checks if a student's information is updated
     * and checks if the student ID and name returned by the backend
     * match those of the updated student. If the information has been
     * updated correctly, the test will be successful, if the information
     * has not been updated correctly, the test will fail
     */
    @Test
    void updateStudent() throws Exception{

        Student student = createAStudent();

        String newName = "Frank";

        student.setName(newName);

        String studentJSON = objectMapper.writeValueAsString(student);

        MvcResult result = this.mockMvc.perform(put("/student/"+student.getId())

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(studentJSON))

                .andDo(print())

                .andExpect(status().isOk())

                .andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse()

                .getContentAsString(), Student.class);

        assertThat(studentFromResponse.getId()).isEqualTo(student.getId());

        assertThat(studentFromResponse.getName()).isEqualTo(newName);

        Student studentFromResponseGet = getStudentFromId(student.getId());

        assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());

        assertThat(studentFromResponseGet.getName()).isEqualTo(newName);

    }

    /**
     * @throws Exception the exception
     * est method which checks for deletion of a student from the application
     * backend checks if the created student ID is not null If the student record
     * was deleted successfully, the test will be successful. However, if the student's
     * record has not been deleted correctly, the test will fail
     */
    @Test
    void deleteStudent() throws Exception{

        Student student = createAStudent();

        assertThat(student.getId()).isNotNull();

        this.mockMvc.perform(delete("/student/"+student.getId()))

                .andDo(print())

                .andExpect(status().isOk())

                .andReturn();

        Student studentFromResponseGet = getStudentFromId(student.getId());

        assertThat(studentFromResponseGet).isNull();

    }

    /**
     * @throws Exception the exception
     * Test method that checks the activation of a student on the application
     * backend and checks if the created student ID is not null. If the student's
     * work status has been updated successfully, the test will pass, if the student's
     * work status has not been updated correctly, the test will fail
     */
    @Test
    void activateStudent() throws Exception{

        Student student = createAStudent();

        assertThat(student.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/student/"+student.getId()+"/work?working=true"))

                .andDo(print())

                .andExpect(status().isOk())

                .andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse()

                .getContentAsString(), Student.class);

        assertThat(studentFromResponse).isNotNull();

        assertThat(studentFromResponse.getId()).isEqualTo(student.getId());

        assertThat(studentFromResponse.isWorking()).isEqualTo(true);

        Student studentFromResponseGet = getStudentFromId(student.getId());

        assertThat(studentFromResponseGet).isNotNull();

        assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());

        assertThat(studentFromResponseGet.isWorking()).isEqualTo(true);

    }

}

