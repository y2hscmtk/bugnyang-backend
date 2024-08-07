package com.winner_cat.domain.scream.repository;

import com.winner_cat.domain.scream.entity.Scream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreamRepository extends JpaRepository<Scream, Long> {
    @Query("SELECT s FROM Scream s WHERE s.updatedAt BETWEEN :startOfDay AND :endOfDay ORDER BY s.updatedAt DESC")
    List<Scream> findAllByUpdatedAtBetween(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}


