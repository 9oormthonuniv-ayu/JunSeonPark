package com.JunseonPark.member.service;

import com.JunseonPark.member.dto.LoginResultDTO;
import com.JunseonPark.member.dto.MemberDTO;
import com.JunseonPark.member.entity.MemberEntity;
import com.JunseonPark.member.enums.LoginStatus;
import com.JunseonPark.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }

        return memberDTOList;
    }

    public LoginResultDTO login(MemberDTO memberDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            // Email값이 있을 때
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                //비밀번호도 일치한다면
                return new LoginResultDTO(LoginStatus.SUCCESS,MemberDTO.toMemberDTO(memberEntity));
            } else {
                // 비밀번호 불일치
                return new LoginResultDTO(LoginStatus.PASSWORD_ERROR,null);
            }
        } else {
            // 이메일 값없을 때
            return new LoginResultDTO(LoginStatus.EMAIL_ERROR, null);
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity= memberRepository.findByMemberEmail(myEmail);
        if (optionalMemberEntity.isPresent()){
            // 존재하면
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    // 아이디 찾기
    // 존재하면 email을, 존재안하면 null값을
    public String findEmail(MemberDTO memberDTO) {
        // 이름, 전화번호, 생년월일 비교
        return memberRepository.findByMemberNameAndMemberTelAndMemberBirth(
                memberDTO.getMemberName(),
                memberDTO.getMemberTel(),
                memberDTO.getMemberBirth()
        ).map(MemberEntity::getMemberEmail)
                .orElse(null);
    }

    public String findPassword(MemberDTO memberDTO) {

        return memberRepository.findByMemberEmailAndMemberTel(
                memberDTO.getMemberEmail(),
                memberDTO.getMemberTel()
        ).map(MemberEntity::getMemberPassword)
                .orElse(null);
    }
}
