package com.undertheriver.sgsg.memo.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUserId;
import com.undertheriver.sgsg.core.ApiResult;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.service.MemoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/memos")
@Api(value = "memo")
public class MemoController {

	private final MemoService memoService;

	@ApiOperation(value = "메모 전체 조회")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "메모 전체 조회", response = MemoDto.ReadMemoRes.class, responseContainer = "List")
	})
	@GetMapping
	public void readAll() {

	}

	@ApiOperation(value = "특정 폴더 메모 전체 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "folderId", value = "폴더 ID", required = true, dataType = "Long", paramType = "path variable")
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "노션 서버Wiki에 메모 read 부분 참고", response = MemoDto.ReadMemoRes.class, responseContainer = "List")
	})
	@GetMapping("/folders/{id}")
	public void readAllByFolder(@PathVariable Long folderId) {
	}

	@ApiOperation(value = "메모 단일 조회", response = MemoDto.ReadMemoRes.class)
	@GetMapping("/{id}")
	public void read(@PathVariable Long id) {

	}

	@ApiOperation(value = "메모 저장")
	@PostMapping
	public ApiResult<?> save(
		@LoginUserId Long userId,
		@RequestBody MemoDto.CreateMemoReq body) {
		long id = memoService.save(userId, body).getId();
		URI location = URI.create("/api/folders/" + id);
		return ApiResult.OK(location);
	}

	@ApiOperation(value = "메모 내용 수정")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "메모 ID", required = true, dataType = "Long", paramType = "path variable"),
		@ApiImplicitParam(name = "content", value = "메모 내용", required = true, dataType = "String", paramType = "body")
	})
	@PutMapping("/{id}/content")
	public void update(@PathVariable Long id, @RequestBody String content) {
	}

	@ApiOperation(value = "메모 상태 수정 (토글? 명시?)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "메모 ID", required = true, dataType = "Long", paramType = "path variable"),
		@ApiImplicitParam(name = "status", value = "비밀 여부", required = true, dataType = "Boolean", paramType = "body")
	})
	@PutMapping("/{id}/status")
	public void updateStatus(@PathVariable Long id, @RequestBody Boolean status) {
	}

}
