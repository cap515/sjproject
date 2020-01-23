package com.kh.sjproject.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.sjproject.member.model.service.MemberService;
import com.kh.sjproject.member.model.vo.Member;

/**
 * Servlet implementation class LoginServlet
 */

// 서버 구동 시 web.xml에
// 현재 Servlet 클래스 명으로 <servlet>이 등록되고
// 매개변수에 작성된 url로 <servlet-mapping> 작성이 됨.
@WebServlet("/member/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// POST 방식 전송 시 문자 인코딩 처리
		request.setCharacterEncoding("UTF-8");
		
		// 요청 데이터를 변수에 기록
		String memberId = request.getParameter("memberId");
		String memberPwd = request.getParameter("memberPwd");
		
		// Member 객체에 id, pwd를 저장
		Member member = new Member(memberId, memberPwd);
		
		try {
			// 비즈니스 로직 처리를 위한 MemberService 클래스 생성
			// login 기능을 수행할 메소드 호출 + 결과값 반환받기
			Member loginMember = new MemberService().loginMember(member);

			// 로그인 동작 여부 확인 Test
			System.out.println("loginMember : " + loginMember);
			
			// 응답 데이터 문자 인코딩 처리
			response.setContentType("text/html; charset=UTF-8");
			
			// 서비스 요청에 해당하는 결과를 가지고
			// 성공/실패에 대한 View(화면) 처리
			
			// 1) 서비스 요청 성공 시
			// 세션(Session) 객체를 생성하여 로그인된 정보를 담음
			
			HttpSession session = request.getSession();
			
			if(loginMember != null) {
				session.setMaxInactiveInterval(600);
				// 10분 뒤 session 만료(매개변수로 시간을 입력, 초단위로 적용)
				
				// 로그인이 성공한 경우
				session.setAttribute("loginMember", loginMember);
				
				/* 
				 * Session은 관련 자원을 모두 Server에서 관리하고
				 * Session ID만 Cookie를 통해 Browser로 전달
				 * 
				 * Cookie는 Server에 의해서 생성이 되지만
				 * 관리는 Browser에서 진행함.(Cookie 최대 크기 5kb)
				 */
				
				// C1) 아이디 저장 체크박스 값 가져오기
				String save = request.getParameter("save");
				
				// -> 체크박스에 별도의 value가 없을 경우
				// 체크시 on / 미 체크시 null 반환 
				
				// C2) javax.servlet.http.Cookie 이용하여 쿠키 생성
				Cookie cookie = new Cookie("saveId", memberId);
				
				// C3) 아이디 저장이 체크된 경우
				if(save != null) {
					cookie.setMaxAge(60*60*24*7);
				}else {
					cookie.setMaxAge(0);
				}
				
				// C4) 쿠키가 사용될 수 있는 유효한 디렉토리 설정
				cookie.setPath("/");	//해당 도메인 전역에서 사용 가능
				
				// C5) response 객체에 쿠키를 담아 클라이언트(브라우저) 전송
				response.addCookie(cookie);
				// -> 이후 쿠키 관리는 브라우저가 맡아서 진행
			}else {
				// 2) 로그인 서비스 요청 실패
				// 메인 페이지(index.jsp)로 이동하여
				// 경고창에 "로그인 정보가 유효하지 않습니다." 출력
				session.setAttribute("msg", "로그인 정보가 유효하지 않습니다.");
			}
			
			// 메인페이지로 이동
			// sendRedirect() <-> forward()
			// sendRedirect는 주소가 지정한 url로 변환되고
			// --> 갱신이 되었다는 의미
			// --> 갱신이 되면 request, response 객체 새로 생성됨
			
			// 2) 체크박스에 별도의 value가 없을 경우 체크시 on을 반환하며 미 체크시에는 null 반환
			
			// C3) 아이디 저장이 체크된 경우
			
			//response.sendRedirect(request.getContextPath()); 	// 메인으로
			
			response.sendRedirect(request.getHeader("referer"));
			
			// referer : 웹 브라우저가 방문한 사이트의 흔적을 저장하고 있음
			// request.getHeader("referer") : 바로 이전 페이지 uri 반환
			
		}catch(Exception e) {
			request.setAttribute("errorMsg", "로그인 과정에서 오류가 발생하였습니다.");
			e.printStackTrace();
			
			String path = "/WEB-INF/views/common/errorPage.jsp";
			RequestDispatcher view = request.getRequestDispatcher(path);
			view.forward(request,response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
