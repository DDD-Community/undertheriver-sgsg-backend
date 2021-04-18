package com.undertheriver.sgsg.foler.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUser;
import com.undertheriver.sgsg.common.dto.CurrentUser;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.service.FolderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/folders")
@Api(value = "folder")
public class FolderController {
	private final FolderService folderService;

	@ApiOperation(value = "폴더 생성")
	@PostMapping
	public ResponseEntity<Object> save(
		@LoginUser CurrentUser currentUser,
		@RequestBody FolderDto.CreateFolderReq dto) {
		try {
			dto.setUser(currentUser.toUserIdDto());
			Long id = folderService.save(dto);
			URI location = new URI("/api/folders/" + id);
			return ResponseEntity.status(201).location(location).build();
		} catch (IndexOutOfBoundsException e) {
			return ResponseEntity.status(406).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@ApiOperation("폴더 조회")
	@GetMapping
	public ResponseEntity<List<FolderDto.ReadFolderRes>> read(@LoginUser CurrentUser currentUser) {
		return new ResponseEntity<>(
			folderService.read(currentUser.toUserIdDto()), HttpStatus.valueOf(200));
	}

	@ApiOperation("폴더 수정")
	@PutMapping
	public ResponseEntity<List<FolderDto.ReadFolderRes>> update(@RequestBody List<FolderDto.UpdateFolderReq> body) {
		return new ResponseEntity<>(
			folderService.update(body), HttpStatus.valueOf(200));
	}

}
