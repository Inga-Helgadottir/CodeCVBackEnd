package com.CodeCV.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    private Long id;
    private String department;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private Long amount_of_employees_needed;
    private Long how_many_employees_are_missing;

    @Override
    public String toString() {
        return "ShiftDTO{" +
                "\n     id=" + id +
                "\n     department=" + department +
                "\n     start_time=" + start_time +
                "\n     end_time=" + end_time +
                "\n     amount_of_employees_needed=" + amount_of_employees_needed +
                "\n     how_many_employees_are_missing= " + how_many_employees_are_missing +
                "\n}";
    }
}