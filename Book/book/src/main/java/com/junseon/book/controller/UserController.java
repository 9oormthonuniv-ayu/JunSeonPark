package com.junseon.book.controller;

import com.junseon.book.domain.dto.user.*;
import com.junseon.book.domain.entity.User;
import com.junseon.book.domain.enums.LoginStatus;
import com.junseon.book.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/save")
    public String saveForm() {
        return "user/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute UserSaveDTO userSaveDTO) {
        userService.save(userSaveDTO);
        System.out.println("UserController.save");
        System.out.println("userSaveDTO = " + userSaveDTO);
        return "user/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    // 로그인 컨트롤러
    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginDTO userLoginDTO, HttpSession session, Model model) {
        UserLoginResultDTO loginResultDTO = userService.login(userLoginDTO);

        if (loginResultDTO.getStatus() == LoginStatus.SUCCESS){
//            session.setAttribute("loginUser", userLoginDTO);
            Long userId = loginResultDTO.getUserLoginDTO().getUserId();
            User user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
            session.setAttribute("loginUser", user);
            return "redirect:/user/" + userId + "/dashboard";
        } else {
            if (loginResultDTO.getStatus() == LoginStatus.EMAIL_ERROR){
                model.addAttribute("loginError", "이메일이 일치하지 않습니다.");
            } else if (loginResultDTO.getStatus() == LoginStatus.PASSWORD_ERROR) {
                model.addAttribute("loginError", "비밀번호가 일치하지 않습니다.");
            }
            return "user/login";
        }
    }

    @GetMapping("/{userId}/dashboard")
    public String userDashboard(@PathVariable("userId") Long userId, Model model, HttpSession session) {
        // 1. 세션에서 로그인 사용자 정보 가져오기
        User user = (User) session.getAttribute("loginUser");

        // 2. 세션이 없거나 userId가 일치하지 않으면 로그인 페이지로 리다이렉트
        if (user == null || !user.getUserId().equals(userId)) {
            return "redirect:/user/login";
        }

        //3. 디버깅용 로그
        System.out.println("로그인 유저 ID: " + user.getUserId());
        System.out.println("로그인 유저 이름: " + user.getName());
        System.out.println("로그인 유저 이메일: " + user.getEmail());

        model.addAttribute("user", user);
        return "user/dashboard"; // templates/user/dashboard.html
    }

    @GetMapping("/find-email")
    public String findEmailForm() {
        return "user/findEmail";
    }

    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "user/findPassword";
    }

    @PostMapping("/find-email")
    public String findEmail(@ModelAttribute UserFindEmailDTO userFindEmailDTO, Model model) {
    // 이름, 전화번호 받아와서 일치하면 email 값 전달하기
        String email = userService.findEmail(userFindEmailDTO);

        if (email != null) {
            model.addAttribute("foundEmail", "가입된 이메일: " + email);
        } else {
            model.addAttribute("findEmailError", "일치하는 회원 정보를 찾을 수 없습니다.");
        }
        return "user/findEmail";
    }

    @PostMapping("find-password")
    public String findPassword(@ModelAttribute UserFindPasswordDTO userFindPasswordDTO, Model model){
        String password = userService.findPassword(userFindPasswordDTO);

        if(password != null) {
            model.addAttribute("foundPassword", "가입된 비밀번호: " + password);
        } else {
            model.addAttribute("findPasswordError", "일치하는 회원 정보를 찾을 수 없습니다.");
        }
        return "user/findPassword";

    }

    @GetMapping("/check-id")
    @ResponseBody
    public String checkDuplicateId(@RequestParam("email") String email) {
        // userRepository의 existsByEmail 메서드를 통해 이메일이 존재하는 확인
        boolean isDuplicate = userService.isEmailDuplicated(email);
        return isDuplicate ? "duplicate" : "ok";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/user/login";
    }

    @GetMapping("/{userId}/myPage")
    public String showMyPage(@PathVariable("userId") Long userId, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || userId == null || !userId.equals(loginUser.getUserId())) {
            return "redirect:/user/login";
        }

        UserMyPageDTO dto = UserMyPageDTO.from(loginUser);
        model.addAttribute("user", dto);
        return "user/myPage";
    }


    @PostMapping("/{userId}/myPage")
    public String userUpdate(@PathVariable ("userId") Long userId, @ModelAttribute  UserUpdateDTO userUpdateDTO, HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !userId.equals(loginUser.getUserId())) {
            return "redirect:/user/login";
        }

        User updateUser = userService.userUpdate(userId, userUpdateDTO);
        session.setAttribute("loginUser",updateUser);

        return "redirect:/user/" + userId + "/myPage"; // GET으로 리다이렉트
    }

    @PostMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !userId.equals(loginUser.getUserId())) {
            return "redirect:/user/login";
        }

        userService.deleteUser(userId);
        session.invalidate(); // 세션 제거

        return "redirect:/"; // 홈 or 로그인 페이지로
    }



}
