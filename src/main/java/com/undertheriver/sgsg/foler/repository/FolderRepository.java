package com.undertheriver.sgsg.foler.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.foler.domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
	List<Folder> findByUserIdAndDeletedFalseOrDeletedNull(Long userId, Pageable page);

	Integer countByUserId(Long userId);

	Integer countByUserIdAndDeletedFalseOrDeletedNull(Long userId);
}
