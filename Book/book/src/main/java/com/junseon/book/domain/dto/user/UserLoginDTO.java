package com.junseon.book.domain.dto.user;


import com.junseon.book.domain.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDTO {

    private Long userId;
    private String email;
    private String password;


    public static UserLoginDTO toUserLoginDTO(User user){
        UserLoginDTO userLoginDTO = new UserLoginDTO();

        userLoginDTO.setUserId(user.getUserId());
        userLoginDTO.setEmail(user.getEmail());
        userLoginDTO.setPassword(user.getPassword());

        return userLoginDTO;
    }

    public static User toUser(UserLoginDTO userLoginDTO){
        User user = new User();

        user.setUserId(user.getUserId());
        user.setEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());

        return user;
    }
}
