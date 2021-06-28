package com.undertheriver.sgsg.foler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.foler.domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query(value = "SELECT f FROM Folder f "
        + "JOIN f.memos "
        + "WHERE f.user.id = :userId "
        + "GROUP BY f.id "
        + "ORDER BY count(f.id) DESC")
    List<Folder> findAllOrderByMemos(Long userId);

    List<Folder> findAllByUserId(Long userId, Sort sort);

    Integer countByUserId(Long userId);

    Optional<Folder> findFirstByUserIdAndTitle(Long userId, String title);
}
