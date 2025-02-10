package com.ispace.practical_assignment.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {


    private Long recordIdentity;
    @NotBlank(message = "Record type cannot be null or blank")
    private String recordType;

    @NotBlank(message = "Device ID cannot be null or blank")
    @Pattern(regexp = "^[0-9]+$", message = "Device id type must contain only numeric letter")
    private String deviceId;


    @NotBlank(message = "Event time  cannot be null or blank")
    private String eventDateTime;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity cannot exceed 10")
    private int quantity;

    @NotBlank(message = "Device name cannot be null or blank")
    private String deviceName;

    @DecimalMin(value = "100.00", message = "Device price must be at least 100.00")
    private Double devicePrice;

}
