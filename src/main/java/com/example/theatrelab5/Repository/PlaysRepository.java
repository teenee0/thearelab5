package com.example.theatrelab5.Repository;

import com.example.theatrelab5.Models.Plays;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlaysRepository extends JpaRepository<Plays, Long> {
    @Query("SELECT f FROM Plays f " +
            "WHERE (:title IS NULL OR LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:studio IS NULL OR LOWER(f.studio) LIKE LOWER(CONCAT('%', :studio, '%'))) " +
            "AND (:start_date IS NULL OR f.sessionDateTime >= :start_date)" +
            "AND (:end_date is null or f.sessionDateTime <= :end_date)" +
            "AND (:ticketCount IS NULL OR f.ticketCount = :ticketCount)")
    List<Plays> findByParams(
            @Param("title") String title,
            @Param("studio") String studio,
            @Param("start_date") LocalDateTime start_date,
            @Param("end_date") LocalDateTime end_date,
            @Param("ticketCount") Integer ticketCount,
            Sort sort);
    @Query("SELECT DATE(f.sessionDateTime), COUNT(f) FROM Plays f GROUP BY DATE(f.sessionDateTime)")
    List<Object[]> findPlayIssueStats();

}
