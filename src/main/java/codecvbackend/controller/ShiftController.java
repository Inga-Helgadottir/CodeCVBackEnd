package codecvbackend.controller;

import codecvbackend.dto.ShiftDTO;
import codecvbackend.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    private ShiftService ShiftService;

    // Build Add Shift REST API
    @PostMapping
    public ResponseEntity<ShiftDTO> createShift(@RequestBody ShiftDTO shiftDTO){
        ShiftDTO savedShift = ShiftService.createShift(shiftDTO);
        return new ResponseEntity<>(savedShift, HttpStatus.CREATED);
    }

    // Build get Shift REST API
    @GetMapping("{id}")
    public ResponseEntity<ShiftDTO> getShiftById(@PathVariable("id") Long shiftId){
        ShiftDTO Shift = ShiftService.getShiftById(shiftId);
        return ResponseEntity.ok(Shift);
    }

    // Build get all Shifts REST API
    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShifts(){
        List<ShiftDTO> shifts = ShiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }










    // Build get all Shifts REST API
    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShiftsWithMissingEmployees(){
        List<ShiftDTO> missingEmployeeShifts = ShiftService.getAllShiftsWithMissingEmployees();
        return ResponseEntity.ok(missingEmployeeShifts);
    }









    // Build update Shift REST API
    @PutMapping("{id}")
    public ResponseEntity<ShiftDTO> updateShift(@PathVariable("id") Long shiftId, @RequestBody ShiftDTO updatedShift){
        ShiftDTO ShiftDTO = ShiftService.updateShift(shiftId, updatedShift);
        return ResponseEntity.ok(ShiftDTO);
    }

    // Build delete Shift REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteShift(@PathVariable("id") Long shiftId){
        ShiftService.deleteShift(shiftId);
        return ResponseEntity.ok("Shift deleted successfully!");
    }
}
