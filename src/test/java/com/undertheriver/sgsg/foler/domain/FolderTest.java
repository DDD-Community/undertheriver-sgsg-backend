package com.undertheriver.sgsg.foler.domain;

import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.repository.MemoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class FolderTest {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private MemoRepository memoRepository;

    @DisplayName("Folder 엔티티에 동일한 Memo를 add 했을 때 기존의 Memo를 대체할 수 있다.")
    @Test
    public void addMemo() {
        // given
        int expectedMemoSize = 1;

        Folder folder = Folder.builder()
                .title("테스트")
                .color(FolderColor.BLUE)
                .build();
        folder = folderRepository.save(folder);

        Memo memo = Memo.builder()
                .content("안녕")
                .build();
        memo = memoRepository.save(memo);

        folder.addMemo(memo);
        folder.addMemo(memo);

        // when
        int actualMemoSize = folder.getMemos().size();

        // then
        assertEquals(actualMemoSize, expectedMemoSize);
    }
}
