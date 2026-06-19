package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "faulty_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaultyRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "source_file")
    private String sourceFile;
    
    @Column(name = "raw_content", length = 2000)
    private String rawContent;
    
    @Column(name = "failure_reason", length = 1000)
    private String failureReason;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "line_number")
    private Long lineNumber;
}
