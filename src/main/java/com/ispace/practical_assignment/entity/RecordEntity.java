package com.ispace.practical_assignment.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "records_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordIdentity;

    private String recordType;
    private String deviceId;
    private String eventDateTime;
    private int quantity;
    private String deviceName;
    private Double devicePrice;

}
