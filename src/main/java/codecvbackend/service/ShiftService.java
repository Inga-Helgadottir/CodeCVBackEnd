package codecvbackend.service;

import codecvbackend.dto.ShiftDTO;

import java.util.List;

public interface ShiftService {
    ShiftDTO createShift(ShiftDTO shiftIdDto);

    ShiftDTO getShiftById(Long shiftId);

    List<ShiftDTO> getAllShifts();

    ShiftDTO updateShift(Long shiftId, ShiftDTO updatedShift);

    void deleteShift(Long employeeId);

    List<ShiftDTO> getAllShiftsWithMissingEmployees();

    List<ShiftDTO> getShiftsById(List<Long> shiftIds);
}
