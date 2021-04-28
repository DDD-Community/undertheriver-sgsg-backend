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

import com.undertheriver.sgsg.common.annotation.LoginUserId;
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
		@LoginUserId Long userId,
		@RequestBody FolderDto.CreateFolderReq dto) {
		try {
			Long id = folderService.save(userId, dto).getId();
			URI location = new URI("/api/folders/" + id);
			return ResponseEntity.created(location)
				.build();
		} catch (IndexOutOfBoundsException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
				.body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest()
				.body(e.getMessage());
		}
	}

	@ApiOperation("폴더 조회")
	@GetMapping
	public ResponseEntity<List<FolderDto.ReadFolderRes>> read(@LoginUserId Long userId) {
		List<FolderDto.ReadFolderRes> folders = folderService.readAll(userId);
		return ResponseEntity.ok(folders);
	}

	@ApiOperation("폴더 수정")
	@PutMapping("/{id}")
	public ResponseEntity<FolderDto.ReadFolderRes> update(
		@PathVariable Long id, @RequestBody FolderDto.UpdateFolderReq body) {
		FolderDto.ReadFolderRes res = folderService.update(id, body);
		return ResponseEntity.ok(res);
	}

	@ApiOperation("다음 폴더 색상 조회")
	@GetMapping("/color")
	public ResponseEntity<FolderDto.GetNextFolderColorRes> getNextFolderColor(
		@LoginUserId Long userId) {
		FolderDto.GetNextFolderColorRes res = folderService.getNextColor(userId);
		return ResponseEntity.ok(res);
	}
}
