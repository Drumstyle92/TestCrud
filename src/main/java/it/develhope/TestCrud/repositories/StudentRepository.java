package it.develhope.TestCrud.repositories;

import it.develhope.TestCrud.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Drumstyle92
 * interface that defines methods for interacting with the database and managing Student entities
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
