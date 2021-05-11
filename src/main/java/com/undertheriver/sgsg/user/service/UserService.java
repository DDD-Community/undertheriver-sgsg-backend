package com.undertheriver.sgsg.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.user.controller.dto.FolderPasswordRequest;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

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

    private User getOne(Long id) {
        return userRepository.findById(id)
            .orElseThrow(ModelNotFoundException::new);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
            .map(User::delete)
            .orElseThrow(ModelNotFoundException::new);
    }

    @Transactional
    public void createFolderPassword(Long userId, FolderPasswordRequest request) {
        User user = getOne(userId);

        String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        user.createFolderPassword(encryptedPassword);
    }
}
