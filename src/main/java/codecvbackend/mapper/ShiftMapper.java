package codecvbackend.mapper;

import codecvbackend.Entity.Shift;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.service.impl.EmployeeServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ShiftMapper {

    private static EmployeeServiceImpl employeeService;

    public ShiftMapper(EmployeeServiceImpl employeeService) {
        ShiftMapper.employeeService = employeeService;
    }

    public static ShiftDTO mapToShiftDto(Shift shift){
        return new ShiftDTO(
                shift.getId(),
                shift.getDepartment(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getAmountOfEmployeesNeeded(),
                shift.getEmployeeIds()
        );
    }

    public static List<ShiftDTO> mapToShiftDtos(List<Shift> shifts){
        List<ShiftDTO> shiftDTOS = new ArrayList<>();
        for(Shift shift : shifts){
            shiftDTOS.add(mapToShiftDto(shift));
        }
        return shiftDTOS;
    }

    public static Shift mapToShift(ShiftDTO shiftDto){
        return new Shift(
                shiftDto.getDepartment(),
                shiftDto.getStartTime(),
                shiftDto.getEndTime(),
                shiftDto.getId(),
                employeeService.getEmployeesById(shiftDto.getEmployeeIds())
                );
    }
}
