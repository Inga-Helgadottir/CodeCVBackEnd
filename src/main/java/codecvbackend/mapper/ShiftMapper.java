package codecvbackend.mapper;

import codecvbackend.Entity.Shift;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.service.impl.EmployeeServiceImpl;
import codecvbackend.service.impl.ShiftServiceImpl;

public class ShiftMapper {

    private EmployeeServiceImpl employeeService;

    public ShiftMapper(EmployeeServiceImpl employeeService, ShiftServiceImpl shiftService) {
        this.employeeService = employeeService;
    }

    public ShiftDTO mapToShiftDto(Shift shift){
        return new ShiftDTO(
                shift.getId(),
                shift.getDepartment(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getAmountOfEmployeesNeeded(),
                shift.getEmployeeIds()
        );
    }

    public Shift mapToShift(ShiftDTO shiftDto){
        return new Shift(
                shiftDto.getDepartment(),
                shiftDto.getStartTime(),
                shiftDto.getEndTime(),
                shiftDto.getId(),
                employeeService.getEmployeesById(shiftDto.getEmployeeIds())
                );
    }
}
