package org.delcom.todos.repositories;

import java.util.List;
import java.util.UUID;

import org.delcom.todos.entities.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, UUID> {

    @Query("SELECT t FROM CashFlow t WHERE LOWER(t.source) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.label) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CashFlow> findByKeyword(String keyword);

    @Query("SELECT DISTINCT t.label FROM CashFlow t")
    List<String> findDistinctLabels();
}