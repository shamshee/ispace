package com.ispace.practical_assignment.repository;

import com.ispace.practical_assignment.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity,Long> {
}
