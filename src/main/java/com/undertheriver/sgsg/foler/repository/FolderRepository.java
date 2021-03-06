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

    List<Folder> findAllByUser(Long userId, Sort sort);

    Integer countByUser(Long userId);

    Optional<Folder> findFirstByUserAndTitle(Long userId, String title);
}
