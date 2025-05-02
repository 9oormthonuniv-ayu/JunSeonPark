package com.junseon.book.domain.dto.user;

import com.junseon.book.domain.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDTO {
    // 비밀번호, 이름, 전화번호 변경 가능
    private String name;
    private String password;
    private String phone;

    public static User toUser(UserUpdateDTO userUpdateDTO){
        User user = new User();

        user.setPassword(userUpdateDTO.getPassword());
        user.setName(userUpdateDTO.getName());
        user.setPhone(userUpdateDTO.getPhone());

        return user;
    }
}
