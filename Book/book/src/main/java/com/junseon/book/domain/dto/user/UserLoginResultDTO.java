package com.junseon.book.domain.dto.user;

import com.junseon.book.domain.enums.LoginStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResultDTO {
    private LoginStatus status;
    private UserLoginDTO userLoginDTO;
}
