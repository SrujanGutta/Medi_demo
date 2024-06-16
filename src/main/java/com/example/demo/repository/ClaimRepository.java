package com.example.demo.repository;

import com.example.demo.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    @Query(value = "SELECT * from Claim WHERE claim_date > '1/1/2024';", nativeQuery = true)
    List<Claim> findByClaimDateAfter();

    @Query(value = "SELECT * FROM Claim WHERE status = 'SUBMITTED' GROUP BY id, member_id, medication, pharmacy_name;", nativeQuery = true)
    List<Claim> findGroupedSubmittedClaims();
}
