package com.kh.sjproject.member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.sjproject.member.model.service.MemberService;
import com.kh.sjproject.member.model.vo.Member;

@WebServlet("/member/signUp.do")
public class SignUpServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    public SignUpServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// POST방식 + 한글 데이터 포함 - > 인코딩
		// request.setCharacterEncoding("UTF-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd1");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone1") + "-" +
						request.getParameter("phone2") + "-" +
						request.getParameter("phone3");
		String email = request.getParameter("email");
		String addr = 	request.getParameter("post") + "," +
						request.getParameter("address1") + "," + 
						request.getParameter("address2");
		String[] interest = request.getParameterValues("memberInterest");
		
		String memberInterest = null;
		if(interest != null) 
			memberInterest = String.join(",", interest);
		else
			memberInterest = "";
		
		Member member = new Member(id, pwd, name, phone, email, addr, memberInterest);
		
		System.out.println(member);
		
		int result = 0;
		try {
			result = new MemberService().signUp(member);
			
			// 한번만 사용하기 때문에 변수선언 안함
			//HttpSession session = request.getSession();
			
			String msg = null;
			
			if(result > 0)
				msg = "가입 성공";
			else
				msg = "가입 실패";
			
			request.getSession().setAttribute("msg", msg);
			response.sendRedirect(request.getContextPath());
			
		}catch(Exception e) {
			request.setAttribute("errorMsg", "회원가입 과정에서 오류가 발생하였습니다.");
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
