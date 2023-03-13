package it.develhope.TestCrud.controllers;

import it.develhope.TestCrud.entities.Student;
import it.develhope.TestCrud.repositories.StudentRepository;
import it.develhope.TestCrud.services.StudentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * @author Drumstyle92
 * Controller class for managing Student entities.
 * The class has some basic CRUD operations (create, read, update, delete)
 * implemented using HTTP methods
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    /**
     * Self-injected dependency to be able to use the methods of the StudentRepository interface
     */
    @Autowired
    private StudentRepository studentRepository;

    /**
     * Self-injected dependency to be able to use the methods of the StudentService class
     */
    @Autowired
    private StudentService studentService;

    /**
     * @param student Request body parameter where to create the student
     * @return Returns the created student
     * post method used to create a new student
     */
// create
    @PostMapping("")
    public @ResponseBody
    Student create(@RequestBody Student student){
        return studentRepository.save(student);
    }

    /**
     * @return Back to the student list
     * Get method to display the entire list of students
     */
// read all
    @GetMapping("/")
    public @ResponseBody
    List<Student> getStudents(){
        return studentRepository.findAll();
    }

    /**
     * @param id parameter where the id of the selected student is inserted
     * @return The chosen student returns
     * get method which displays the chosen student
     */
// read just one
    @GetMapping("/{id}")
    public @ResponseBody  Student getAStudent(@PathVariable long id){

        Optional<Student> student =  studentRepository.findById(id);

        if(student.isPresent()){

            return student.get();

        }else{

            return null;

        }

    }

    /**
     * @param id      Selected student id parameter
     * @param student Request body parameter where to edit the selected student
     * @return Return the modified student
     * Put method is used to update a student's information
     */
// update the id of a student
    @PutMapping("/{id}")
    public @ResponseBody Student update(@PathVariable long id, @RequestBody @NotNull Student student){

        student.setId(id);

        return studentRepository.save(student);

    }

    /**
     * @param id Parameter where you can select the student to be modified using the id
     * @param working Parameter where to set whether the student works or not
     * @return Returns the student with the modified work status
     * put method that allows you to change a specific status of the student
     */
// update the isWorking column of a student
    @PutMapping("/{id}/work")
    public @ResponseBody Student setStudentIsWorking(@PathVariable long id, @RequestParam("working") boolean working){

        return studentService.setStudentIsWorkingStatus(id, working);

    }

    /**
     * @param id id parameter of the selected student
     * is used to delete a student from the database
     */
// delete a student
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        studentRepository.deleteById(id);
    }

}

