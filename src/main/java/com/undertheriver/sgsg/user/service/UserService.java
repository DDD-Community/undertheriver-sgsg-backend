package com.undertheriver.sgsg.user.service;

import static com.undertheriver.sgsg.user.exception.PasswordValidationException.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.user.controller.dto.FolderPasswordRequest;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;
import com.undertheriver.sgsg.user.exception.PasswordValidationException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long id) {
        return getOne(id);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(ModelNotFoundException::new);
        user.delete();
    }

    @Transactional
    public void createFolderPassword(Long userId, FolderPasswordRequest.CreateRequest request) {
        User user = getOne(userId);

        String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        user.createFolderPassword(encryptedPassword);
    }

    public void updateFolderPassword(Long userId, FolderPasswordRequest.UpdateRequest request) {
        User user = getOne(userId);

        if (!user.hasFolderPassword()) {
            throw new PasswordValidationException(NO_PASSWORD);
        }
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getFolderPassword())) {
            throw new PasswordValidationException(PASSWORD_NOT_MATCH);
        }

        user.updateFolderPassword(request.getNewPassword());
    }

    private User getOne(Long id) {
        return userRepository.findById(id)
            .orElseThrow(ModelNotFoundException::new);
    }
}
