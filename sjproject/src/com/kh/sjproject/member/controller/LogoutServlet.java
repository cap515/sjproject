package com.kh.sjproject.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/logout.do")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();	// 세션 무효화
		
		// 메인페이지로 화면 갱신 -> request, response 객체를 다시 생성하여
		//					 매개변수로 작성된 페이지로 이동(갱신)
		//				  -> 주소창에 표시되는 url이 매개변수에 작성된 url로 변경됨

		//response.sendRedirect(request.getContextPath()); 	// 메인으로
		
		response.sendRedirect(request.getHeader("referer"));
		
		// referer : 웹 브라우저가 방문한 사이트의 흔적을 저장하고 있음
		// request.getHeader("referer") : 바로 이전 페이지 uri 반환
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
