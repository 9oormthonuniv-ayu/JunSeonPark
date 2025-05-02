package com.JunseonPark.member.controller;

import com.JunseonPark.member.dto.LoginResultDTO;
import com.JunseonPark.member.dto.MemberDTO;
import com.JunseonPark.member.enums.LoginStatus;
import com.JunseonPark.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

   @PostMapping("/save")
   public String save(@ModelAttribute MemberDTO memberDTO) {
       System.out.println("MemberController.save");
       System.out.println("memberDTO = " + memberDTO);
       memberService.save(memberDTO);
        return "login";
   }

    @GetMapping("/login")
    public String loginFrom() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model){
        LoginResultDTO loginResultDTO = memberService.login(memberDTO);
        if (loginResultDTO.getStatus() == LoginStatus.SUCCESS) {
            // 로그인 성공
            session.setAttribute("loginEmail", loginResultDTO.getMemberDTO().getMemberEmail());
            return "main";
        } else {
            if (loginResultDTO.getStatus() == LoginStatus.EMAIL_ERROR) {
                model.addAttribute("loginError", "이메일이 일치하지 않습니다.");
            }
            else if (loginResultDTO.getStatus() == LoginStatus.PASSWORD_ERROR) {
                model.addAttribute("loginError", "비밀번호가 일치하지 않습니다.");
            }
            return "login";
        }
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList",memberDTOList);
        return "list";
    }

    @GetMapping("/{id}") // ex : /member/9
    public String findById(@PathVariable("id") Long id ,Model model) {
       MemberDTO memberDTO = memberService.findById(id);
       model.addAttribute("member", memberDTO);
       return "detail";

    }

    // 회원 정보 수정하기
    // 회원 정보는 로그인된 상태, 즉 세션값을 참조해서 회원 정보 페이지를 렌더링
    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate(); // httpSession 세션 제거!!
        return "index";
    }

    @GetMapping("/find-email")
    public String findEmailForm() {
        return "findEmail";
    }

    @GetMapping("/find-password")
    public String findPass() {
        return "findPassword";
    }

    @PostMapping("/find-email")
    public String findEmail(@ModelAttribute MemberDTO memberDTO, Model model) {
        // 이름, 전화번호, 생년월일을 받아와서 찾고 일치하면 email값 전달하기
         String email = memberService.findEmail(memberDTO);

         if (email != null) {
             model.addAttribute("foundEmail", "가입된 이메일: " + email);
         } else {
             model.addAttribute("findEmailError", "일치하는 회원 정보를 찾을 수 없습니다.");
         }
         return "findEmail";
    }

    @PostMapping("/find-password")
    public String findPassword(@ModelAttribute MemberDTO memberDTO, Model model) {
        // 이메일 입력, 전화번호 일치하면 패스워드값 전달하기 나중에 전화번호 인증기능 도입 예정
        String password = memberService.findPassword(memberDTO);

        if (password != null) {
            model.addAttribute("foundPassword", "가입된 비밀번호: " + password);
        } else {
            model.addAttribute("findPasswordError", "일치하는 회원 정보를 찾을 수 없습니다.");
        }
        return "findPassword";
    }
}
