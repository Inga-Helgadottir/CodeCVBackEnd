package com.CodeCV.BackEnd.mapper;


import com.CodeCV.BackEnd.dto.ShiftDTO;
import com.CodeCV.BackEnd.entity.Shift;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftMapper {

    public ShiftDTO mapToShiftDto(Shift shift){
        return new ShiftDTO(
                shift.getId(),
                shift.getDepartment(),
                shift.getStart_time(),
                shift.getEnd_time(),
                shift.getAmount_of_employees_needed(),
                shift.howManyEmployeesAreMissing()
        );
    }

    public List<ShiftDTO> mapToShiftDtos(List<Shift> shifts){
        List<ShiftDTO> shiftDTOS = new ArrayList<>();

        if(shifts != null){
            if(!shifts.isEmpty()){
                for(Shift shift : shifts){
                    shiftDTOS.add(mapToShiftDto(shift));
                }
            }
        }

        return shiftDTOS;
    }

    public Shift mapToShift(ShiftDTO shiftDto){
        return new Shift(
                shiftDto.getDepartment(),
                shiftDto.getStart_time(),
                shiftDto.getEnd_time(),
                shiftDto.getAmount_of_employees_needed()
                );
    }
}
