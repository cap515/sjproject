package com.kh.sjproject.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.kh.sjproject.wrapper.EncryptWrapper;

@WebFilter(urlPatterns = {"/member/login.do","/member/signUp.do","/member/updatePwd.do","/member/deleteMember.do"})
public class EncryptFilter implements Filter {

    public EncryptFilter() {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest h_request = (HttpServletRequest)request;
		
		// Encrypt Wrapper 객체 생성
		EncryptWrapper encWrapper = new EncryptWrapper(h_request);
		
		chain.doFilter(encWrapper, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
