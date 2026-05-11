package codecvbackend.repository;

import codecvbackend.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository <Employee, Long> {

}