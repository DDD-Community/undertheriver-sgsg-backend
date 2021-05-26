package com.undertheriver.sgsg.memo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.memo.domain.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query(value = "SELECT m, f FROM Memo m "
        + "JOIN Folder f ON f.user.id = :userId AND (f.deleted = null OR f.deleted = false) "
        + "WHERE f.id = m.folder.id AND (m.deleted = null OR m.deleted = false) "
        + "ORDER BY m.favorite DESC")
    List<Memo> findAllByUser(Long userId);

    @Query(value = "SELECT m, f FROM Memo m "
        + "JOIN Folder f ON m.folder.id = :folderId AND (f.deleted = null OR f.deleted = false) "
        + "WHERE m.deleted = null OR m.deleted = false "
        + "ORDER BY m.favorite DESC")
    List<Memo> findAllByFolder(Long folderId);
}
