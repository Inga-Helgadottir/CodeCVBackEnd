package codecvbackend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class EmployeeShiftKey implements Serializable {
    @Column(name = "employeeId")
    Long employeeId;
    @Column(name = "shiftId")
    Long shiftId;
}