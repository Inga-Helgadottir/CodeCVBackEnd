package codecvbackend.dto;

import codecvbackend.Entity.Employee;
import codecvbackend.Entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Shift> shifts = new HashSet<>();

    public EmployeeDTO(Employee employee) {
        if(employee != null){
            this.id = employee.getId();
            this.firstName = employee.getFirstName();
            this.lastName = employee.getLastName();
            this.email = employee.getEmail();
            this.shifts = employee.getShifts();
        }
    }

}
