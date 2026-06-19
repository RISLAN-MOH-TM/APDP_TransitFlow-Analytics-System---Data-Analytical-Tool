package com.transitflow.repository;

import com.transitflow.model.FaultyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaultyRecordRepository extends JpaRepository<FaultyRecord, Long> {
    
    List<FaultyRecord> findBySourceFile(String sourceFile);
    
    @Query("SELECT f.sourceFile, COUNT(f) FROM FaultyRecord f GROUP BY f.sourceFile")
    List<Object[]> countBySourceFile();
}
