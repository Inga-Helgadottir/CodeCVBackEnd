package codecvbackend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"employeesOnShifts"})
@Table(name = "shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "department")
    private String department;
    @Column(name = "startTime")
    private LocalDateTime startTime;
    @Column(name = "endTime")
    private LocalDateTime endTime;
    @Column(name = "ammountOfEmployeesNeeded")
    private Long amountOfEmployeesNeeded;
    @ManyToMany
    @JoinTable(name = "employeeShifts",
            joinColumns = @JoinColumn(name = "shiftId"),
            inverseJoinColumns = @JoinColumn(name = "employeeId")
    )
    private Set<Employee> employeesOnShifts = new HashSet<>();

    public Shift(String department, LocalDateTime startTime, LocalDateTime endTime, Boolean areThereEnoughEmployees, Long amountOfEmployeesNeeded) {
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfEmployeesNeeded = amountOfEmployeesNeeded;
    }

    public Shift(String department, LocalDateTime startTime, LocalDateTime endTime, Long amountOfEmployeesNeeded, Set<Employee> employeesOnShifts) {
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfEmployeesNeeded = amountOfEmployeesNeeded;
        this.employeesOnShifts = employeesOnShifts;
    }

    public Set<Long> getEmployeeIds() {
        Set<Long> employeeIds = new HashSet<>();
        Set<Employee> employees = this.employeesOnShifts;

        for (Employee employee : employees) {
            employeeIds.add(employee.getId());
        }
        return employeeIds;
    }


    public void addEmployeeToShift(Employee employee){
        this.employeesOnShifts.add(employee);
        areThereEnoughEmployees();
    }


    public boolean areThereEnoughEmployees(){
        if(this.amountOfEmployeesNeeded == employeesOnShifts.size()){
            return true;
        }else{
            return false;
        }
    }
}
