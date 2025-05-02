package com.junseon.book.domain.dto.user;

import com.junseon.book.domain.entity.User;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserFindPasswordDTO {
    private String email;
    private String name;
    private String phone;

    public static UserFindPasswordDTO toUserFindPassword(User user){
        UserFindPasswordDTO userFindPasswordDTO = new UserFindPasswordDTO();

        userFindPasswordDTO.setEmail(user.getEmail());
        userFindPasswordDTO.setName(user.getName());
        userFindPasswordDTO.setPhone(user.getPhone());
        return userFindPasswordDTO;
    }

    public static User toUser(UserFindPasswordDTO userFindPasswordDTO){
        User user = new User();

        user.setEmail(userFindPasswordDTO.getEmail());
        user.setName(userFindPasswordDTO.getName());
        user.setPhone(userFindPasswordDTO.getPhone());

        return user;
    }
}
