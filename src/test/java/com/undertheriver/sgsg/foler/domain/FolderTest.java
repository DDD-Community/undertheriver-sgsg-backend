package com.undertheriver.sgsg.foler.domain;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.foler.service.FolderService;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.repository.MemoRepository;
import com.undertheriver.sgsg.memo.service.MemoService;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class FolderTest {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void memo() {
        // given
        String expectedMemoContent = "테스트2";
        int expectedMemoSize = 1;

        Folder folder = Folder.builder()
                .title("테스트")
                .color(FolderColor.BLUE)
                .build();
        folder = folderRepository.save(folder);

        Memo memo = Memo.builder()
                .content(expectedMemoContent)
                .build();
        memo = memoRepository.save(memo);
        folder.addMemo(memo);

        // when
        memo.updateContent(expectedMemoContent);
        folder.addMemo(memo);

        // then
        List<Memo> joinedMemos = new ArrayList<>(folder.getMemos());
        int actualMemoSize = joinedMemos.size();
        String actualMemoContent = joinedMemos.get(0).getContent();

        assertEquals(actualMemoSize, expectedMemoSize);
        assertEquals(actualMemoContent, expectedMemoContent);
    }
}
