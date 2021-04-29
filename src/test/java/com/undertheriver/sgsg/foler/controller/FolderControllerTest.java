package com.undertheriver.sgsg.foler.controller;

import com.undertheriver.sgsg.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FolderControllerTest extends AbstractControllerTest {
    @Autowired
    private FolderController folderController;

    @Override
    protected Object controller() {
        return folderController;
    }

    @DisplayName("폴더를 읽어올 수 있다.")
    @Test
    public void read() throws Exception {
        mockMvc.perform(
                get("/api/v1/folders")
                        .queryParam("page", "1")
                        .queryParam("size", "20")
                        .queryParam("direction", "ASC")
                        .queryParam("orderBy", "createdAt")
        ).andExpect(status().isOk());
    }
}
