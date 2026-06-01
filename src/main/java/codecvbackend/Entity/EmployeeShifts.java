package codecvbackend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeShifts {
    @EmbeddedId
    EmployeeShiftKey id;
    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employeeId")
    Employee employee;
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    Shift shift;
}