package codecvbackend.repository;

import codecvbackend.Entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository <Shift, Long> {

}