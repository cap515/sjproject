package com.kh.sjproject.wrapper;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncryptWrapper extends HttpServletRequestWrapper{

	// 상속받은 HttpServletRequestWrapper 클래스는 기본생성자가 없음
	// 반드시 명시적으로 HttpServletRequestWrapper를 매개변수로 하는 생성자 작성이 필수
	public EncryptWrapper(HttpServletRequest request) {
		super(request);
	}

	// ServletRequestWrapper의 getParameter() 오버라이딩
	@Override
	public String getParameter(String key) {
		
		// 요청 데이터의 key에 대응되는 value를 저장할 변수 선언 
		String value="";
		
		if(key != null && (key.equals("memberPwd") || key.equals("newPwd1") ||
			key.equals("pwd1") || key.equals("currentPwd"))) {
			// 암호화 진행 -> SHA-512 해시함수 암호화
			// 해시함수란?
			// 임의의 길이의 데이터를 고정된 길이의 데이터로 매핑하는 함수
			
			value = getSha512(super.getParameter(key));
		}else {
			value = super.getParameter(key);
		}
		
		return value;
	}
	
	/** SHA-512 해시함수를 이용하여 암호화
	 * @param pwd
	 * @return encPwd
	 */
	public static String getSha512(String pwd) {
		
		String encPwd = null;
		
		MessageDigest md = null;
		// -> 지정된 알고리즘에 따라 해쉬함수를 진행하는 클래스
		
		try {
			// MessageDigest.getInstance("알고리즘명")
			// 지정된 알고리즘을 사용해 MessageDigest 객체를 작성하여 반환
			md = MessageDigest.getInstance("SHA-512");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// 암호화를 진행하기 위해서는
		// 전달받은 문자열(비밀번호)를 바이트 배열로 변환해야 함
		byte[] bytes = pwd.getBytes(Charset.forName("UTF-8"));
		
		// md객체에 pwd 바이트 배열을 전달하여 갱신
		// -> 실제 암호화(해시함수 적용) 진행
		md.update(bytes);
		
		// java.util.Base64 인코더를 이용해서 
		// 암호화된 바이트 배열을 인코딩해서 문자열로 출력 
		encPwd = Base64.getEncoder().encodeToString(md.digest());
		
		System.out.println("암호화 전 : " + pwd);
		System.out.println("암호화 후 : " + encPwd);
		
		return encPwd;
	}
	
	
	

}
