package it.develhope.TestCrud.services;

import it.develhope.TestCrud.entities.Student;
import it.develhope.TestCrud.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * @author Drumstyle92
 * Class that provides a service to manage students
 */
@Service
public class StudentService {

    /**
     * Dependency which is injected via the
     * Autowired annotation which gives
     * the possibility to use the methods of the StudentRepository interface
     */
    @Autowired
    private StudentRepository studentRepository;

    /**
     * @param id        Selected student id parameter
     * @param isWorking Parameter where you can set whether or not the student works
     * @return The student returns with the new job status set
     * The method which is intended to set the work status of a student
     * If the student with the specified ID is not found, null is returned
     */
    public Student setStudentIsWorkingStatus(Long id, boolean isWorking){

        Optional<Student> student = studentRepository.findById(id);

        if(!student.isPresent()) return null;

        student.get().setWorking(isWorking);

        return studentRepository.save(student.get());

    }

}
