package codecvbackend.mapper;

import codecvbackend.Entity.Shift;
import codecvbackend.dto.ShiftDTO;

public class ShiftMapper {
    public static ShiftDTO mapToShiftDto(Shift shift){
        return new ShiftDTO(
                shift.getId(),
                shift.getDepartment(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getAmountOfEmployeesNeeded()
        );
    }

    public static Shift mapToShift(ShiftDTO shiftDto){
        return new Shift(
                shiftDto.getId(),
                shiftDto.getDepartment(),
                shiftDto.getStartTime(),
                shiftDto.getEndTime(),
                shiftDto.getAmountOfEmployeesNeeded()
        );
    }
}
