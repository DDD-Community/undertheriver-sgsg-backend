package com.undertheriver.sgsg.foler.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUserId;
import com.undertheriver.sgsg.core.ApiResult;
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
    public ApiResult<?> save(
            @LoginUserId Long userId,
            @RequestBody FolderDto.CreateFolderReq dto) {
        try {
            Long id = folderService.save(userId, dto);
            URI location = new URI("/api/folders/" + id);
            return ApiResult.OK(location);
        } catch (IndexOutOfBoundsException e) {
            return ApiResult.ERROR(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return ApiResult.ERROR(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("폴더 조회")
    @GetMapping
    public ApiResult<List<FolderDto.ReadFolderRes>> read(@LoginUserId Long userId) {

        List<FolderDto.ReadFolderRes> folders = folderService.readAll(userId);

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
}
