package com.undertheriver.sgsg.memo.controller;

import java.net.URI;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @ApiOperation(value = "메모 저장")
    @PostMapping
    public ApiResult<?> save(@LoginUserId Long userId, @RequestBody MemoDto.CreateMemoReq body) {
        long id = memoService.save(userId, body);
        URI location = URI.create("/api/folders/" + id);
        return ApiResult.OK(location);
    }

    @ApiOperation(value = "메모 수정")
    @PutMapping("/{memoId}")
    public ApiResult<MemoDto.UpdateMemoRes> update(@PathVariable Long memoId, @RequestBody MemoDto.UpdateMemoReq body) {
        MemoDto.UpdateMemoRes res = memoService.update(memoId, body);
        return ApiResult.OK(res);
    }
}
