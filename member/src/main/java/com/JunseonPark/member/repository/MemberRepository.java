package com.JunseonPark.member.repository;

import com.JunseonPark.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    Optional<MemberEntity> findByMemberNameAndMemberTelAndMemberBirth(String name, String tel, String birth);
    Optional<MemberEntity> findByMemberEmailAndMemberTel(String memberEmail, String memberTel);

}
