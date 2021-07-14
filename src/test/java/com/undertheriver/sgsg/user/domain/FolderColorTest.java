package com.undertheriver.sgsg.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.undertheriver.sgsg.foler.domain.FolderColor;

public class FolderColorTest {
    @DisplayName("다음 생성할 폴더 색상을 알려준다.")
    @Test
    public void nextColorTestCase() {
        // given
        FolderColor[] colors = FolderColor.values();
        FolderColor expectedColor = colors[0];

        // when
        FolderColor actualColor = FolderColor.nextColorFrom(colors.length);

        // then
        assertEquals(actualColor, expectedColor);
    }
}
