package com.undertheriver.sgsg.memo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.memo.domain.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query(value = "SELECT m FROM Memo m "
        + "JOIN Folder f ON f.user.id = :userId "
        + "WHERE f.id = m.folder.id AND (m.deleted = null OR m.deleted = false) "
        + "ORDER BY m.favorite DESC")
    List<Memo> findAllByUserId(Long userId);
}
