package com.CodeCV.BackEnd.controller;

import com.CodeCV.BackEnd.dto.ShiftDTO;
import com.CodeCV.BackEnd.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/shifts")
public class ShiftController {
    private ShiftService shiftService;

    @PostMapping("/create")
    @RolesAllowed("ROLE_ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<ShiftDTO> createShift(@RequestBody ShiftDTO shiftDTO){
        ShiftDTO savedShift = shiftService.createShift(shiftDTO);
        return new ResponseEntity<>(savedShift, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<ShiftDTO> getShiftById(@PathVariable("id") Long shiftId){
        ShiftDTO shift = shiftService.getShiftById(shiftId);
        return ResponseEntity.ok(shift);
    }

    @GetMapping("/all")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ShiftDTO>> getAllShifts(){
        List<ShiftDTO> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/missingEmployees")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ShiftDTO>> getAllShiftsWithMissingEmployees(){
        List<ShiftDTO> shifts = shiftService.getAllShiftsWithMissingEmployees();
        return ResponseEntity.ok(shifts);
    }

    @PutMapping("/update/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<ShiftDTO> updateShift(@PathVariable("id") Long shiftId, @RequestBody ShiftDTO updatedShift){
        ShiftDTO shiftDTO = shiftService.updateShift(shiftId, updatedShift);
        return ResponseEntity.ok(shiftDTO);
    }

    @DeleteMapping("delete/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> deleteShift(@PathVariable("id") Long shiftId){
        String response = shiftService.deleteShift(shiftId);
        return ResponseEntity.ok(response);
    }
}
