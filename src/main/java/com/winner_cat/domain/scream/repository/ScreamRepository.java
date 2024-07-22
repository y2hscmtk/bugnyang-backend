package com.winner_cat.domain.scream.repository;

import com.winner_cat.domain.scream.entity.Scream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreamRepository extends JpaRepository<Scream, Long> {
}
