package codecvbackend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "amountOfEmployeesNeeded")
    private Long amountOfEmployeesNeeded;
    @ManyToMany
    @JoinTable(name = "employeesOnShifts",
            joinColumns = @JoinColumn(name = "shiftId"),
            inverseJoinColumns = @JoinColumn(name = "employeeId")
    )
    private List<Employee> employeesOnShifts = new ArrayList<>();

    public Shift(String department, LocalDateTime startTime, LocalDateTime endTime, Long amountOfEmployeesNeeded) {
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfEmployeesNeeded = amountOfEmployeesNeeded;
    }

    public Shift(String department, LocalDateTime startTime, LocalDateTime endTime, Long amountOfEmployeesNeeded, List<Employee> employeesOnShifts) {
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfEmployeesNeeded = amountOfEmployeesNeeded;
        this.employeesOnShifts = employeesOnShifts;
    }

    public List<Long> getEmployeeIds() {
        List<Long> employeeIds = new ArrayList<>();
        List<Employee> employees = this.employeesOnShifts;

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
        return this.amountOfEmployeesNeeded == employeesOnShifts.size();
    }
}
