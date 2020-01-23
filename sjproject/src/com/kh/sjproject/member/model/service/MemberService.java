package com.kh.sjproject.member.model.service;

import static com.kh.sjproject.common.JDBCTemplate.*;

import java.sql.Connection;

import com.kh.sjproject.member.model.dao.MemberDao;
import com.kh.sjproject.member.model.vo.Member;

public class MemberService {

	/** 로그인용 Service
	 * @param member
	 * @return loginMember
	 * @throws Exception
	 */
	public Member loginMember(Member member) throws Exception{
		Connection conn = getConnection();
		
		Member loginMember = new MemberDao().loginMember(conn, member);
		
		return loginMember;
	}

	/** 아이디 중복 확인용 Service
	 * @param id
	 * @return result
	 * @throws Exception
	 */ 
	public int idDupCheck(String id) throws Exception {

		Connection conn = getConnection();
		
		return new MemberDao().ipDupCheck(conn, id);
	}
	
	public int signUp(Member member) throws Exception{
		
		Connection conn = getConnection();
		
		int result = new MemberDao().signUp(conn,member);

		if(result > 0)
			commit(conn);
		else
			rollback(conn);
		
		return result;
	}

	
	/**
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public Member selectMember(String memberId) throws Exception {
		Connection conn = getConnection();
		return new MemberDao().selectMember(conn,memberId);
	}

	public int updateMember(Member member) throws Exception{
		Connection conn = getConnection();
		
		int result = new MemberDao().updateMember(conn,member);
		
		if(result > 0)
			commit(conn);
		else
			rollback(conn);
		
		return result;
	}

	/** 비밀번호 수정용 서비스
	 * @param loginMember
	 * @param newPwd
	 * @return result
	 * @throws Exception
	 */
	public int updatePwd(Member loginMember, String newPwd) throws Exception{
		
		Connection conn = getConnection();
		
		MemberDao memberDao = new MemberDao();
		
		// 현재 비밀번호 일치 여부 확인
		// SELECT COUNT(*) FROM MEMBER WHERE MEMBER_ID = ? AND MEMBER_PWD = ?
		int result = memberDao.checkPwd(conn, loginMember);
		
		if(result > 0) {
			loginMember.setMemberPwd(newPwd);
			result = memberDao.updatePwd(conn, loginMember);
			if(result > 0)
				commit(conn);
			else
				rollback(conn);
			return result;
		} else {
			// 현재 비밀번호 불일치
			return -1;
		}
	}

	public int deleteMember(Member loginMember, String inputPwd) throws Exception{
		
		Connection conn = getConnection();
		
		MemberDao memberDao = new MemberDao();
		
		// 현재 비밀번호 일치 여부 확인
		// SELECT COUNT(*) FROM MEMBER WHERE MEMBER_ID = ? AND MEMBER_PWD = ?
		loginMember.setMemberPwd(inputPwd);
		System.out.println("service : " + inputPwd);
		int result = memberDao.checkPwd(conn, loginMember);
		
		if(result > 0) {
			result = memberDao.deleteMember(conn, loginMember);
			if(result > 0)
				commit(conn);
			else
				rollback(conn);
			return result;
		} else {
			// 현재 비밀번호 불일치
			return -1;
		}
	}
}
