package com.JunseonPark.member.dto;

import com.JunseonPark.member.enums.LoginStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultDTO {
    private LoginStatus status;
    private MemberDTO memberDTO;
}
