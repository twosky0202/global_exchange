package com.globalexchange.app.controller;

import com.globalexchange.app.domain.vo.FileProfileVO;
import com.globalexchange.app.domain.vo.MemberDTO;
import com.globalexchange.app.domain.vo.MemberVO;
import com.globalexchange.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    //회원가입 선택 페이지 이동
    @GetMapping("/member/join")
    public void join(){
    }


    //이메일 회원가입
//    @PostMapping("/member/joinForm")
    @RequestMapping(value = "/member/joinForm")
    public void joinForm(){
    }

    //회원가입 완료 ( 디비 등록 )
    @RequestMapping(value = "/member/joinFormOk")
    public String joinFormOk(MemberVO memberVO){

        memberService.joinForm(memberVO);
        return "/member/login";

         String teachingLang = memberVO.getMemberTeachingLang();
         switch (teachingLang) {
             case "ENGLISH" : memberVO.setMemberNationUrl("/images/nation/america.png");
                 break;
             case "SPANISH" : memberVO.setMemberNationUrl("/images/nation/spain.png");
                 break;
             case "FRENCH" : memberVO.setMemberNationUrl("/images/nation/france.png");
                 break;
             case "GERMAN" : memberVO.setMemberNationUrl("/images/nation/germany.png");
                 break;
             case "ITALIAN" : memberVO.setMemberNationUrl("/images/nation/italy.png");
                 break;
             case "PORTUGUESE" : memberVO.setMemberNationUrl("/images/nation/portugal.png");
                 break;
             case "RUSSIAN" : memberVO.setMemberNationUrl("/images/nation/russia.png");
                 break;
             case "JAPANESE" : memberVO.setMemberNationUrl("/images/nation/japan.png");
                 break;
             case "CHINESE" : memberVO.setMemberNationUrl("/images/nation/china.png");
                 break;
             case "KOREAN" : memberVO.setMemberNationUrl("/images/nation/southKorea.png");
                 break;
         }
         memberService.joinForm(memberVO);
//        Long currentJoinMemberNum = memberService.currentJoinMemberNum();
        FileProfileVO fileProfileVO = new FileProfileVO();

        log.info("" + memberVO.getMemberNumber());
        fileProfileVO.setFileImageCheck(false);
        fileProfileVO.setFileName("NUll");
        fileProfileVO.setFileSize(0L);
        fileProfileVO.setFileUploadPath("NULL");
        fileProfileVO.setFileUuid("NULL");
        fileProfileVO.setMemberNumber(memberVO.getMemberNumber());
        memberService.profileDefaultInsert(fileProfileVO);


        return "member/login";

    }

    //구글 회원가입
//    @GetMapping("/joinGoogle")
//    @RequestMapping(value = "/googleJoin")
//    public String joinGoogle(MemberVO memberVO){
//        memberService.googleJoin(memberVO);
//
//        return "/main/main";
//    }

    //로그인 선택 페이지 이동
    @GetMapping("/member/login")
    public void login(){
    }

//    이메일 로그인
    @PostMapping(value = "/member/emailLogin")
    public RedirectView emailLogin(MemberVO memberVO, HttpServletRequest request) {
        /*log.info(memberVO.getMemberId() + "들어옴"+ memberVO.getMemberPassword());*/

        HttpSession session = request.getSession();
        Long memberNumber = memberService.emailLogin(memberVO);

        /* 일치하는 회원 정보가 없는 경우 */
        if(memberNumber == null){
            return new RedirectView("/member/login");
        /* 일치하는 회원 정보가 있는 경우 회원 번호를 session에 넣는다. */
        }else {
            session.setAttribute("memberNumber" , memberNumber);
            return new RedirectView("/main/main");
        }
    }

//    @PostMapping("/logout")
//    public String logout(@RequestBody)

    //구글 로그인
//    @RequestMapping(value = "/googleLogin")
//    public String googleLogin(MemberVO memberVO, HttpServletRequest request){
//        HttpSession session = request.getSession();
//
//        Long memberNumber = memberService.googleLogin(memberVO);
//
//        if(memberNumber == null){
//
//            return "member/login";
//
//        }else {
//            session.setAttribute("memberNumber" , memberNumber);
//
//            return "/main/main";
//        }
//    }

    // 마이페이지 메인

    @GetMapping("/member/mypage")
    public void mypage() {

//    @GetMapping("/mypage")
        @RequestMapping(value = "/mypage")
        public void mypage (HttpServletRequest request, Model model){
            HttpSession session = request.getSession();
            Long memberNumber = (Long) session.getAttribute("memberNumber");
            MemberDTO memberDTO = memberService.getMypageProfile(memberNumber);
            model.addAttribute("memberDTO", memberDTO);


            Long diaryTotalPost = memberService.diaryTotalPost(memberNumber);
            model.addAttribute("diaryTotalPost", diaryTotalPost);

            Long postTotal = memberService.postTotal(memberNumber);
            model.addAttribute("postTotal", postTotal);

            Long answerTotal = memberService.AnswerTotal(memberNumber);
            model.addAttribute("answerTotal", answerTotal);


            List<MemberDTO> partnerList = memberService.diaryPartnerList(memberNumber);
            model.addAttribute("partnerList", partnerList);
        }


        // 마이페이지 상세보기
        @GetMapping("/member/detail")
        public void detail () {

            // 마이페이지 교환일기 상대 끊기
//    @GetMapping("/deletePartner")
//    @RequestMapping(value = "/deletePartner")
//    public void deletePartner(){
//
//    }


            // 마이페이지 상세보기
            @RequestMapping(value = "/detail")
            public void detail (HttpServletRequest request, Model model){
                HttpSession session = request.getSession();
                Long memberNumber = (Long) session.getAttribute("memberNumber");
                MemberDTO memberDTO = memberService.myPageDetail(memberNumber);
                model.addAttribute("memberDTO", memberDTO);


                log.info("들어옴" + memberDTO);
                log.info("들어옴" + memberNumber);
//        memberService.getMypageProfile(memberNumber);
            }

            // 마이페이지 수정

            @GetMapping("/member/write")
            public void write () {

            }

            // 마이페이지 수정완료
            @GetMapping("/member/writeOk")
            public void writeOk () {

                @GetMapping("/write")
                public void write (HttpServletRequest request, Model model){
                    HttpSession session = request.getSession();
                    Long memberNumber = (Long) session.getAttribute("memberNumber");
                    MemberDTO memberDTO = memberService.myPageDetail(memberNumber);
                    model.addAttribute("memberDTO", memberDTO);
                }

                // 마이페이지 수정완료
                @PostMapping("/writeOk")
//    @RequestMapping(value = "/writeOk")
                public RedirectView writeOk (MemberDTO memberDTO){
                    MemberVO memberVO = new MemberVO();


                    memberVO.setMemberNumber(memberDTO.getMemberNumber());
                    memberVO.setMemberTeachingLang(memberDTO.getMemberTeachingLang());
                    memberVO.setMemberLearningLang(memberDTO.getMemberLearningLang());
                    memberVO.setMemberInteresting(memberDTO.getMemberInteresting());
                    memberVO.setMemberPhoneNum(memberDTO.getMemberPhoneNum());
                    memberVO.setMemberNickname(memberDTO.getMemberNickname());
                    memberVO.setMemberBirthdate(memberDTO.getMemberBirthdate());
                    memberVO.setMemberIntroduce(memberDTO.getMemberIntroduce());


                    // 마이페이지 교환일기 상대 끊기
                    @GetMapping("/member/partnerCancel")
                    public void partnerCancel () {

                        switch (memberDTO.getMemberTeachingLang()) {
                            case "ENGLISH":
                                memberVO.setMemberNationUrl("/images/nation/america.png");
                                break;
                            case "SPANISH":
                                memberVO.setMemberNationUrl("/images/nation/spain.png");
                                break;
                            case "FRENCH":
                                memberVO.setMemberNationUrl("/images/nation/france.png");
                                break;
                            case "GERMAN":
                                memberVO.setMemberNationUrl("/images/nation/germany.png");
                                break;
                            case "ITALIAN":
                                memberVO.setMemberNationUrl("/images/nation/italy.png");
                                break;
                            case "PORTUGUESE":
                                memberVO.setMemberNationUrl("/images/nation/portugal.png");
                                break;
                            case "RUSSIAN":
                                memberVO.setMemberNationUrl("/images/nation/russia.png");
                                break;
                            case "JAPANESE":
                                memberVO.setMemberNationUrl("/images/nation/japan.png");
                                break;
                            case "CHINESE":
                                memberVO.setMemberNationUrl("/images/nation/china.png");
                                break;
                            case "KOREAN":
                                memberVO.setMemberNationUrl("/images/nation/southKorea.png");
                                break;
                        }


                        fileProfileVO.setFileNumber(memberDTO.getFileProfileVO().getFileNumber());
                        fileProfileVO.setFileUuid(memberDTO.getFileProfileVO().getFileUuid());
                        fileProfileVO.setFileUploadPath(memberDTO.getFileProfileVO().getFileUploadPath());
                        fileProfileVO.setFileSize(memberDTO.getFileProfileVO().getFileSize());
                        fileProfileVO.setFileName(memberDTO.getFileProfileVO().getFileName());
                        fileProfileVO.setFileImageCheck(memberDTO.getFileProfileVO().isFileImageCheck());
                        fileProfileVO.setMemberNumber(memberDTO.getMemberNumber());


                        log.info(memberDTO.getMemberNickname() + "DTO");
                        log.info(memberVO.getMemberNickname() + "VO");
                        log.info(memberDTO + " 멤버DTO");
                        memberService.myPageImageUpdate(fileProfileVO);
                        memberService.myPageContentUpdate(memberVO);

                        return new RedirectView("/member/detail");
                    }

                }
            }
