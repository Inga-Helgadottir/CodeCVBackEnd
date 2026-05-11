package codecvbackend.service.impl;

import codecvbackend.Entity.Shift;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.exception.ResourceNotFoundException;
import codecvbackend.mapper.ShiftMapper;
import codecvbackend.repository.ShiftRepository;
import codecvbackend.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    private ShiftRepository shiftRepository;

    @Override
    public ShiftDTO createShift(ShiftDTO shiftDto) {
        Shift shift = ShiftMapper.mapToShift(shiftDto);
        Shift savedShift = shiftRepository.save(shift);

        return ShiftMapper.mapToShiftDto(savedShift);
    }

    @Override
    public ShiftDTO getShiftById(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(()-> new ResourceNotFoundException("Shift does not exist with the id: " + shiftId));

        return ShiftMapper.mapToShiftDto(shift);
    }

    @Override
    public List<ShiftDTO> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();

        return shifts.stream().map((shift) -> ShiftMapper.mapToShiftDto(shift)).collect(Collectors.toList());
    }

    @Override
    public ShiftDTO updateShift(Long shiftId, ShiftDTO updatedShift) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift not found with id: " + shiftId)
        );

        shift.setDepartment(updatedShift.getDepartment());
        shift.setStartTime(updatedShift.getStartTime());
        shift.setEndTime(updatedShift.getEndTime());
        shift.setAmountOfEmployeesNeeded(updatedShift.getAmountOfEmployeesNeeded());

        Shift updatedShiftObj = shiftRepository.save(shift);

        return ShiftMapper.mapToShiftDto(updatedShiftObj);
    }

    @Override
    public void deleteShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift not found with id: " + shiftId)
        );

        shiftRepository.deleteById(shiftId);
    }
}
