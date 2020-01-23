package com.kh.sjproject.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.sjproject.member.model.service.MemberService;
import com.kh.sjproject.member.model.vo.Member;

@WebServlet("/member/updateMember.do")
public class UpdateMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateMemberServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. POST방식 + 한글 데이터 포함 - > 인코딩
		request.setCharacterEncoding("UTF-8");
		
		// 2. 전달 받은 파라미터를 변수에 저장
		//	  + 로그인 ID를 얻어와 Member 객체에 저장
		
		// session에 저장된 ID 얻어오기
		HttpSession session = request.getSession();
		Member loginMember = (Member)session.getAttribute("loginMember");
		int No = loginMember.getMemberNo();

		// 파라미터 변수에 저장
		// 전화번호
		String phone = request.getParameter("phone1") + "-" +
						request.getParameter("phone2") + "-" +
						request.getParameter("phone3");
		
		// 이메일
		String email = request.getParameter("email");

		// 주소
		String addr = 	request.getParameter("post") + "," +
						request.getParameter("address1") + "," + 
						request.getParameter("address2");
		
		// 관심분야
		String[] interest = request.getParameterValues("memberInterest");
		System.out.println("NO : " + loginMember.getMemberNo());
		String memberInterest = null;
		if(interest != null) 
			memberInterest = String.join(",", interest);
		else
			memberInterest = "";
		
		// Member 객체에 저장
		Member member = new Member(phone, email, addr, memberInterest, No);
		
		// 3. 비즈니스 로직을 수행할 서비스 메소드 호출 후 결과 값 반환 받기
		int result = 0;
		try {
			result = new MemberService().updateMember(member);
			
			// 한번만 사용하기 때문에 변수선언 안함
			//HttpSession session = request.getSession();
			
			String msg = null;
			
			if(result > 0)
				msg = "수정 성공";
			else
				msg = "수정 실패";
			
			request.getSession().setAttribute("msg", msg);
			response.sendRedirect(request.getContextPath()+"/member/mypage.do");
			
		}catch(Exception e) {
			request.setAttribute("errorMsg", "회원정보 수정 과정에서 오류가 발생하였습니다.");
			e.printStackTrace();
			
			String path = "/WEB-INF/views/common/errorPage.jsp";
			RequestDispatcher view = request.getRequestDispatcher(path);
			view.forward(request,response);
		}
				
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
