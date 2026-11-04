package com.CodeCV.BackEnd.service.impl;

import com.CodeCV.BackEnd.dto.ShiftDTO;
import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.entity.Shift;
import com.CodeCV.BackEnd.exceptions.ResourceNotFoundException;
import com.CodeCV.BackEnd.mapper.ShiftMapper;
import com.CodeCV.BackEnd.repository.ShiftRepository;
import com.CodeCV.BackEnd.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private ShiftRepository shiftRepository;
    private ShiftMapper shiftMapper;

    @Override
    public ShiftDTO createShift(ShiftDTO shiftDto) {
        Shift shift = shiftMapper.mapToShift(shiftDto);
        Shift savedShift = shiftRepository.save(shift);

        return shiftMapper.mapToShiftDto(savedShift);
    }

    @Override
    public ShiftDTO getShiftById(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift does not exist with the id: " + shiftId));

        return shiftMapper.mapToShiftDto(shift);
    }

    @Override
    public List<ShiftDTO> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();

        if(shifts.isEmpty()){
            return new ArrayList<>();
        }

        return shiftMapper.mapToShiftDtos(shifts);
    }

    @Override
    public List<ShiftDTO> getAllShiftsWithMissingEmployees() {
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftDTO> shiftDTOs = new ArrayList<>();

        for (Shift currentShift : shifts) {
            if (!currentShift.areThereEnoughEmployees()) {
                shiftDTOs.add(shiftMapper.mapToShiftDto(currentShift));
            }
        }

        return shiftDTOs;
    }

    @Override
    public ShiftDTO updateShift(Long shiftId, ShiftDTO updatedShift) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift not found with id: " + shiftId)
        );

        shift.setDepartment(updatedShift.getDepartment());
        shift.setStart_time(updatedShift.getStart_time());
        shift.setEnd_time(updatedShift.getEnd_time());
        shift.setAmount_of_employees_needed(updatedShift.getAmount_of_employees_needed());

        Shift updatedShiftObj = shiftRepository.save(shift);

        return shiftMapper.mapToShiftDto(updatedShiftObj);
    }

    @Override
    public String deleteShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift not found with id: " + shiftId)
        );

        if(shift.getEmployees() != null){
            for (Employee employee : shift.getEmployees()) {
                employee.getShifts().remove(shift);
            }
            shift.getEmployees().clear();
        }
        shiftRepository.delete(shift);
        return "The Shift with the id " + shiftId + " has been deleted!";
    }
}
