package com.kh.sjproject.notice.model.service;

import static com.kh.sjproject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import com.kh.sjproject.notice.model.dao.NoticeDao;
import com.kh.sjproject.notice.model.vo.Notice;

public class NoticeService {

	/** 공지사항 목록 조회용 서비스
	 * @return list
	 * @throws Exception
	 */
	public List<Notice> selectList() throws Exception{

		Connection conn = getConnection();
		
		List<Notice> list = new NoticeDao().selectList(conn);
		
		return list;
	}

	/** 공지사항 상세 조회용 Service
	 * @param no
	 * @return notice
	 * @throws Exception
	 */
	public Notice selectNotice(int no) throws Exception {

		Connection conn = getConnection();
		
		NoticeDao noticeDao = new NoticeDao();

		// 공지사항 상세조회
		Notice notice = new NoticeDao().selectNotice(conn, no);
		
		// 공지사항 상세조회 성공 시 조회수 증가
		if(notice != null) {
			int result = noticeDao.increaseCount(conn, no);
			
			if(result > 0) {
				commit(conn);
				
				// 반환되는 notice는 조회수 증가가 되어있지 않으므로
				// 리턴 시 조회수를 +1 시켜줌
				notice.setNoticeCount(notice.getNoticeCount()+1);
			} else {
				rollback(conn);
				notice = null;	// 조회수 증가 실패 시 조회되지 않게 만듦.
			}
		}
		
		close(conn);
		
		return notice;
	}

	public int insertNotice(Notice notice) throws Exception {
		
		Connection conn = getConnection();
		
		NoticeDao noticeDao = new NoticeDao();
		
		int result = 0;
		// 등록될 공지사항의 글 번호 생성
		// -> 공지사항 등록 성공 시 해당 글번호 상세조회를 위해서
		//	+ 파일 업로드 시 현재 등록된 글 번호를 외래키로 사용하기 위해서
		int no = noticeDao.selectNextNo(conn);
		

		if(no > 0) {	// 다음 번호가 생성이 됐을 경우
			
			// DB 저장 시 개행문자 \r\n을 <br>로 변경해야
			// 상세 조회 시 줄바꿈이 유지됨
			notice.setNoticeContent(
						notice.getNoticeContent().replace("\r\n", "<br>")
					);
			
			
			result = noticeDao.insertNotice(conn, notice, no);
			
			if(result > 0) {	// 공지사항 DB 삽입 성공시 
				commit(conn);
				
				result = no;
				// 다음 번호 조회 + 공지사항 등록 성공 시
				// 등록 완료 후 해당 작성 페이지로 이동하기 위해서
				// 조회된 번호를 반환시킴
			} else
				rollback(conn);
		}
		
		close(conn);
		
		return result;
	}

	/** 공지사항 수정 화면용 Service
	 * @param no
	 * @return notice
	 * @throws Exception
	 */
	public Notice updateForm(int no) throws Exception{
		
		Connection conn = getConnection();
		
		// 공지사항 상세조회
		Notice notice = new NoticeDao().selectNotice(conn, no);
		
		// DB에 저장된 내용을 textarea에 출력할 경우
		// <br>로 저장되어있는 부분을 \r\n으로 변경해야 함
		notice.setNoticeContent(
					notice.getNoticeContent().replace("<br>", "\r\n"));
		
		close(conn);
		
		return notice;
	}

	public int updateNotice(Notice notice) throws Exception{
		
		Connection conn = getConnection();
		
		// 수정된 내용이 DB에 저장될 경우 개행문자 변경 필요
		notice.setNoticeContent(
				notice.getNoticeContent().replace("\r\n", "<br>"));
		
		int result = new NoticeDao().updateNotice(conn, notice);
		
		if(result > 0)
			commit(conn);
		else
			rollback(conn);
		
		close(conn);
		
		return result;
	}

	public static int deleteNotice(int no) throws Exception{
		
		Connection conn = getConnection();
		
		int result = new NoticeDao().deleteNotice(conn, no);
		
		if(result > 0)
			commit(conn);
		else
			rollback(conn);
		
		return result;
	}

	/** 공지사항 검색용 Service
	 * @param searchKey
	 * @param searchValue
	 * @return list
	 * @throws Exception
	 */
	public List<Notice> searchNotice(String searchKey, String searchValue) throws Exception{

		Connection conn = getConnection();
		
		String condition = null;
		searchValue = "'%' || '"+searchValue+"' || '%'";
					// ex) '%공지%'
		switch(searchKey) {
		case "title" : 
			condition = " NOTICE_TITLE LIKE " + searchValue; break;
		case "content" : 
			condition = " NOTICE_CONTENT LIKE " + searchValue; break;
		case "titcont" :
			condition = " (NOTICE_TITLE LIKE " + searchValue +
				" OR " +" NOTICE_CONTENT LIKE " + searchValue + ") "; break;
		}
		
		List<Notice> list = new NoticeDao().searchNotice(conn, condition);
		
		return list;
	}
}
