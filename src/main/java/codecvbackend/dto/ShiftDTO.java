package codecvbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    private Long id;
    private String department;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime amountOfEmployeesNeeded;
}
