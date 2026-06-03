package codecvbackend.service.impl;

import codecvbackend.Entity.Shift;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.exception.ResourceNotFoundException;
import codecvbackend.mapper.ShiftMapper;
import codecvbackend.repository.ShiftRepository;
import codecvbackend.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        return shifts.stream().map(ShiftMapper::mapToShiftDto).collect(Collectors.toList());
    }

    @Override
    public List<ShiftDTO> getAllShiftsWithMissingEmployees() {
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftDTO> shiftDTOs = new ArrayList<>();

        for (Shift currentShift : shifts) {
            if (!currentShift.areThereEnoughEmployees()) {
                shiftDTOs.add(new ShiftDTO(currentShift));
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
        shift.setStartTime(updatedShift.getStartTime());
        shift.setEndTime(updatedShift.getEndTime());
        shift.setAmountOfEmployeesNeeded(updatedShift.getAmountOfEmployeesNeeded());

        Shift updatedShiftObj = shiftRepository.save(shift);

        return ShiftMapper.mapToShiftDto(updatedShiftObj);
    }

    @Override
    public void deleteShift(Long shiftId) {
        if (!shiftRepository.existsById(shiftId)) {
            throw new ResourceNotFoundException("Shift not found with id: " + shiftId);
        }
        shiftRepository.deleteById(shiftId);
    }

    @Override
    public List<ShiftDTO> getShiftsById(List<Long> shiftIds){
        List<Shift> shifts = shiftRepository.findAllById(shiftIds);
        return new ArrayList<>(ShiftMapper.mapToShiftDtos(shifts));
    }
}
