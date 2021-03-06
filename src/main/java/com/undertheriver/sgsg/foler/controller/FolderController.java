package com.undertheriver.sgsg.foler.controller;

import java.net.URI;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUserId;
import com.undertheriver.sgsg.core.ApiResult;
import com.undertheriver.sgsg.foler.domain.FolderOrderBy;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/folders")
@Api(value = "folder")
public class FolderController {
    private final FolderService folderService;

    @ApiOperation(value = "폴더 생성")
    @PostMapping
    public ApiResult<?> save(
        @LoginUserId Long userId,
        @RequestBody FolderDto.CreateFolderReq dto) {
        long id = folderService.save(userId, dto);
        URI location = URI.create("/v1/folders/" + id);
        return ApiResult.OK(location);
    }

    @ApiOperation("폴더 조회")
    @GetMapping
    public ApiResult<List<FolderDto.ReadFolderRes>> read(
        @LoginUserId Long userId, @RequestParam(required = false) FolderOrderBy orderBy) {
        List<FolderDto.ReadFolderRes> folders = folderService.readAll(userId, orderBy);
        return ApiResult.OK(folders);
    }

    @ApiOperation("폴더 이름 수정")
    @PutMapping("/{id}/title")
    public ApiResult<FolderDto.ReadFolderRes> update(
        @PathVariable Long id, @RequestBody FolderDto.UpdateFolderTitleReq body) {

        FolderDto.ReadFolderRes res = folderService.update(id, body);

        return ApiResult.OK(res);
    }

    @ApiOperation("다음 폴더 색상 조회")
    @GetMapping("/color")
    public ApiResult<FolderDto.GetNextFolderColorRes> getNextFolderColor(@LoginUserId Long userId) {

        FolderDto.GetNextFolderColorRes res = folderService.getNextColor(userId);

        return ApiResult.OK(res);
    }

    @ApiOperation("폴더 삭제")
    @DeleteMapping("/{folderId}")
    public ApiResult<?> delete(@PathVariable Long folderId) {
        folderService.delete(folderId);
        return ApiResult.OK();
    }

    @ApiOperation("폴더를 비밀 상태로 변경")
    @PostMapping("/{folderId}/secret")
    public ApiResult<?> secret(
        @LoginUserId Long userId,
        @PathVariable Long folderId
    ) {
        folderService.secret(userId, folderId);
        return ApiResult.OK();
    }

    @ApiOperation("폴더 비밀 상태 취소")
    @PostMapping("/{folderId}/unsecret")
    public ApiResult<?> unsecret(
        @LoginUserId Long userId,
        @PathVariable Long folderId,
        @RequestBody FolderDto.UnsecretReq request
    ) {
        folderService.unsecret(userId, folderId, request);
        return ApiResult.OK();
    }
}
