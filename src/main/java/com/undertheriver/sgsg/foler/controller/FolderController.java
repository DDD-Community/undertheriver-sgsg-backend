package com.undertheriver.sgsg.foler.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/folders")
@Api(value = "folder")
public class FolderController {

	@ApiOperation(value = "폴더 생성")
	@ApiImplicitParam(name = "title", value = "폴더 이름", required = true, dataType = "String", paramType = "body")
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Location: /api/folders/1"),
		@ApiResponse(code = 406, message = "폴더는 20개까지 생성 가능합니다")
	})
	@PostMapping
	public void save(@RequestBody String title){}

	@ApiOperation("폴더 제목 수정")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "폴더 ID", required = true, dataType = "Long", paramType = "path variable"),
		@ApiImplicitParam(name = "title", value = "폴더 이름", required = true, dataType = "String", paramType = "body")
	})
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @RequestBody String title){}

	@ApiOperation("폴더 순서 수정 (의논 필요)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "order", value = "전체 폴더 ID 리스트", required = true, dataType = "List<Long>", paramType = "body")
	})
	@PutMapping("/order")
	public void updateOrder(@RequestBody List<Long> ids) {}

	@ApiOperation("폴더 조회")
	@GetMapping
	@ApiResponses(
		@ApiResponse(code = 200, message = "[ { 'id' = 1, 'title' = '...' }, {'id' = 2, 'title' = '...' }, .... ] " )
	)
	public void read() {}
}
