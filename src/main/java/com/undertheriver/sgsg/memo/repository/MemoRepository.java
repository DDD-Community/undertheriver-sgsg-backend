package com.undertheriver.sgsg.memo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.memo.domain.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query(value = "SELECT m FROM Memo m "
        + "JOIN Folder f ON f.user = :userId "
        + "WHERE f.id = m.folder.id "
        + "ORDER BY m.favorite DESC, m.createdAt DESC ")
    List<Memo> findAllByUser(Long userId);

    @Query(value = "SELECT m FROM Memo m "
        + "WHERE m.folder.id = :folderId "
        + "ORDER BY m.favorite DESC, m.createdAt DESC")
    List<Memo> findAllByFolder(Long folderId);
}
