package com.undertheriver.sgsg.foler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.user.domain.User;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
	List<Folder> findAllByDeletedFalseOrDeletedNull();

	List<Folder> findFirst20ByUserAndDeletedFalseOrDeletedNull(User user);
}
