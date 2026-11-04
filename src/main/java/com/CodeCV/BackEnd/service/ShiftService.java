package com.CodeCV.BackEnd.service;

import com.CodeCV.BackEnd.dto.ShiftDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ShiftService {
    ShiftDTO createShift(ShiftDTO shiftIdDto);

    ShiftDTO getShiftById(Long shiftId);

    List<ShiftDTO> getAllShifts();

    ShiftDTO updateShift(Long shiftId, ShiftDTO updatedShift);

    String deleteShift(Long employeeId);

    List<ShiftDTO> getAllShiftsWithMissingEmployees();

}
