package com.undertheriver.sgsg.memo.controller;

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
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.service.MemoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/memos")
@Api(value = "memo")
public class MemoController {

    private final MemoService memoService;

    @ApiOperation(value = "메모 저장", notes = "일치하는 폴더가 없을 시 바디에 folderId 필드를 제거해주세요.")
    @PostMapping
    public ApiResult<?> save(
        @LoginUserId Long userId, @RequestBody MemoDto.CreateMemoReq request
    ) {
        long id = memoService.save(userId, request);
        URI location = URI.create("/v1/memos" + id);
        return ApiResult.OK(location);
    }

    @ApiOperation(value = "메모 수정")
    @PutMapping("/{memoId}")
    public ApiResult<MemoDto.UpdateMemoRes> update(
        @PathVariable Long memoId, @RequestBody MemoDto.UpdateMemoReq request
    ) {
        MemoDto.UpdateMemoRes res = memoService.update(memoId, request);
        return ApiResult.OK(res);
    }

    @ApiOperation(value = "메모 조회")
    @GetMapping
    public ApiResult<List<MemoDto.ReadMemoRes>> readAll(
        @LoginUserId Long userId, @RequestParam(required = false) Long folderId
    ) {
        List<MemoDto.ReadMemoRes> res = memoService.readAll(userId, folderId);
        return ApiResult.OK(res);
    }

    @ApiOperation(value = "메모 삭제")
    @DeleteMapping("/{memoId}")
    public ApiResult<?> delete(@PathVariable Long memoId) {
        memoService.delete(memoId);
        return ApiResult.OK();
    }
    
    @ApiOperation(value = "메모 즐겨찾기")
    @PostMapping("/{memoId}/favorite")
    public ApiResult<?> favorite(
        @LoginUserId Long userId,
        @PathVariable Long memoId
    ) {
        memoService.favorite(userId, memoId);
        return ApiResult.OK();
    }

    @ApiOperation(value = "메모 즐겨찾기 취소")
    @PostMapping("/{memoId}/unfavorite")
    public ApiResult<?> unfavorite(
        @LoginUserId Long userId,
        @PathVariable Long memoId
    ) {
        memoService.unfavorite(userId, memoId);
        return ApiResult.OK();
    }
}
