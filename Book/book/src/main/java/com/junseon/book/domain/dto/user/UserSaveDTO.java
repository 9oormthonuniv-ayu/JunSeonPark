package com.junseon.book.domain.dto.user;


import com.junseon.book.domain.entity.User;
import com.junseon.book.domain.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSaveDTO {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private Role role = Role.USER;


    public static UserSaveDTO toUserSaveDTO(User user){
        UserSaveDTO userSaveDTO = new UserSaveDTO();

        userSaveDTO.setUserId(user.getUserId());
        userSaveDTO.setEmail(user.getEmail());
        userSaveDTO.setPassword(user.getPassword());
        userSaveDTO.setName(user.getName());
        userSaveDTO.setPhone(user.getPhone());
        userSaveDTO.setCreatedAt(user.getCreatedAt());

        return userSaveDTO;
    }

    public static User toUser(UserSaveDTO dto){
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setCreatedAt(dto.getCreatedAt());

        return user;
    }
}
