package com.undertheriver.sgsg.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.memo.domain.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
}
