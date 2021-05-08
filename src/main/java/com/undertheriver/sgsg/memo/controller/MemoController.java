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

    @ApiOperation(value = "메모 저장")
    @PostMapping
    public ApiResult<?> save(
            @LoginUserId Long userId,
            @RequestBody MemoDto.CreateMemoReq body) {
        try {
            Long id = memoService.save(userId, body).getId();
            URI location = new URI("/api/v1/memos/" + id);
            return ApiResult.OK(location);
        } catch (IndexOutOfBoundsException e) {
            return ApiResult.ERROR(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return ApiResult.ERROR(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
