package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/save") //회원가입페이지요청
    public String saveForm() {
        return "save"; //회원가입페이지이동
    }

    @PostMapping("/member/save") //회원가입정보입력
    public String save(@ModelAttribute MemberDTO memberDTO) { //입력값자동DTO매핑
        System.out.println("MemberController.save"); //로그출력
        System.out.println("memberDTO = " + memberDTO); //입력값확인
        memberService.save(memberDTO);
        return "login";
    }

    @GetMapping("/member/login") //로그인페이지요청
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login") //로그인정보입력
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) { //DTO매핑(회원정보), null 세션(로그인유지용임시정보)
        MemberDTO loginResult = memberService.login(memberDTO); //로그인처리

        if (loginResult != null) { //로그인성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail()); //세션저장
            return "main";
        } else { //실패
            return "login";
        }
    }

    @GetMapping("/member/") //전체회원목록요청
    public String findAll(Model model) { //데이터전달용Model주입
        List<MemberDTO> memberDTOList = memberService.findAll(); //전체회원조회처리
        model.addAttribute("memberList", memberDTOList); //목록데이터전달
        return "list";
    }

    @GetMapping("/member/{id}") //회원상세정보요청
    public String findById(@PathVariable Long id, Model model) { //경로변수추출 데이터전달용Model주입
        MemberDTO memberDTO = memberService.findById(id); //회원정보조회처리
        model.addAttribute("member", memberDTO); //상세정보전달
        return "detail";
    }

    @GetMapping("/member/update") //회원정보수정페이지요청 (∴ 기존 정보 제공)
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail"); //이메일추출
        MemberDTO memberDTO = memberService.updateForm(myEmail); //db정보에 있는지 확인 및 회원정보 추출
        model.addAttribute("updateMember", memberDTO); //회원정보제공
        return "update";
    }

    @PostMapping("/member/update") //회원정보수정입력
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO); //회원정보수정처리
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/member/delete/{id}") //회원삭제요청
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("/member/logout") //로그아웃요청
    public String logout(HttpSession session) {
        session.invalidate(); //세션삭제
        return "index"; //메인페이지이동
    }

    @PostMapping("/member/email-check") //이메일중복검사요청
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) { //JSON응답반환 파라미터추출
        System.out.println("memberEmail = " + memberEmail); //입력값확인
        String checkResult = memberService.emailCheck(memberEmail); //중복검사처리
        return checkResult;
    }
}