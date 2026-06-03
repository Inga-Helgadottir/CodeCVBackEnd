package codecvbackend.dto;

import codecvbackend.Entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    private Long id;
    private String department;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long amountOfEmployeesNeeded;
    private List<Long> employeeIds = new ArrayList<>();

    public ShiftDTO(Shift shift){
        if(shift != null){
            this.id = shift.getId();
            this.department = shift.getDepartment();
            this.startTime = shift.getStartTime();
            this.endTime = shift.getEndTime();
            this.amountOfEmployeesNeeded = shift.getAmountOfEmployeesNeeded();
            this.employeeIds = shift.getEmployeeIds();
        }
    }
}