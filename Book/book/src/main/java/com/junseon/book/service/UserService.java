package com.junseon.book.service;

import com.junseon.book.domain.dto.user.*;
import com.junseon.book.domain.entity.User;
import com.junseon.book.domain.enums.LoginStatus;
import com.junseon.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(UserSaveDTO userSaveDTO) {
        User user = UserSaveDTO.toUser(userSaveDTO);
        userRepository.save(user);
    }

    public UserLoginResultDTO login(UserLoginDTO userSaveDTO) {
        Optional<User> byUserEmail = userRepository.findByEmail(userSaveDTO.getEmail());

        if (byUserEmail.isPresent()) {
            User user = byUserEmail.get();
            if (user.getPassword().equals(userSaveDTO.getPassword())) {
                return new UserLoginResultDTO(LoginStatus.SUCCESS, UserLoginDTO.toUserLoginDTO(user));
            } else {
                // 비밀번호 불일치
                return new UserLoginResultDTO(LoginStatus.PASSWORD_ERROR,null);
            }
        } else {
            // 이메일 불일치
            return new UserLoginResultDTO(LoginStatus.EMAIL_ERROR,null);
        }
    }

    public String findEmail(UserFindEmailDTO userFindEmailDTO) {
        return userRepository.findByNameAndPhone(
                userFindEmailDTO.getName(),
                userFindEmailDTO.getPhone()
        ).map(User::getEmail).orElse(null);
    }

    public String findPassword(UserFindPasswordDTO userFindPasswordDTO) {
        return userRepository.findByEmailAndNameAndPhone(
                userFindPasswordDTO.getEmail(),
                userFindPasswordDTO.getName(),
                userFindPasswordDTO.getPhone()
        ).map(User::getPassword).orElse(null);
    }

    public boolean isEmailDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public User userUpdate(Long userId,UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + userId));

        user.setName(userUpdateDTO.getName());
        user.setPassword(userUpdateDTO.getPassword());
        user.setPhone(userUpdateDTO.getPhone());

        return user;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
