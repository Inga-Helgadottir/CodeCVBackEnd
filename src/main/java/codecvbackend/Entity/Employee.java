package codecvbackend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    @ManyToMany(mappedBy = "employeeShifts")
    private Set<Shift> shifts = new HashSet<>();

    public Employee(String firstName, String lastName, String email, Set<Shift> shifts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.shifts = shifts;
    }

    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addShiftToEmployee(Shift shift) {
        this.shifts.add(shift);
        shift.addEmployeeToShift(this);
    }
}
