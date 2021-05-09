package com.undertheriver.sgsg.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(ModelNotFoundException::new);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
            .map(User::delete)
            .orElseThrow(ModelNotFoundException::new);
    }
}
