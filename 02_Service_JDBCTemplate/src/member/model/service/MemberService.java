package member.model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import member.model.dao.MemberDao;
import member.model.vo.Member;

import static common.JDBCTemplate.*; //메소드명까지 호출

/*

 * 
 * 
 * Dao
 *  * 3.PreparedStatement객체 생성 (미완성쿼리 : 물음표가 있는 쿼리)
 * 	3.1 물음표에 값대입
 * 4. 실행과 동시에 리턴값 받음. DML(executeUpdate) -> int, DQL(executeQuery) -> ResultSet
 * 	4.1 ResultSet -> Java객체로 옮겨담기
 * 5. 자원반납 (객체 생성 역순으로. rset - pstmt)
 * 
 * 
 * */

public class MemberService {
	
	private MemberDao memberDao = new MemberDao();
	
	//JDBCTemplate을 사용한 메소드
	public List<Member> selectAll(){
		Connection conn = getConnection(); //static import하면 클래스명 안써도 됨
		List<Member> list = memberDao.selectAll(conn);
		close(conn);
		
		return list;
	}
	
	/*
	 * Service
	 * 기존의 DAO클래스가 했던 일
	 * 1. DriverClass등록 (최초1회)
	 * 2. Connection 객체 생성 (url, user, password)
	 * 	2.1 자동커밋 false설정
	 * ------- Dao 요청 -----------
	 * 6. 트랜잭션처리(DML) commit or rollback
	 * 7. 자원반납 (conn)
	*/
	
	

	public List<Member> selectAll_() {
		String driverClass = "oracle.jdbc.OracleDriver"; //ojdbc에서 제공하는 클래스
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "student";
		String password = "student";
		Connection conn = null;
		List<Member> list = null;
		
		try {
//		 * 1. DriverClass등록 (최초1회)
			Class.forName(driverClass);
//		 * 2. Connection 객체 생성 (url, user, password)
			conn = DriverManager.getConnection(url, user, password);
			//DriverManager: java sql패키지의 클래스
//		 * 	2.1 자동커밋 false설정
			conn.setAutoCommit(false); //false라고 해도 close하면 자동커밋된다고함.
			//true하면 실행하는 족족 커밋해버린다고함
			
//		 * ------- Dao 요청 -----------
			//Connection객체 전달 (pstmt객체 만들어야하니까)
			list = memberDao.selectAll(conn);
			
//		 * 6. 트랜잭션처리(DML) commit or rollback
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//		 * 7. 자원반납 (conn)
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return list; //꼭바꾸십쇼 
	}

	public int insertMember(Member member) {
		Connection conn = getConnection();
		int result = memberDao.insertMember(conn, member);
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		
		return result;
	}

	public Member selectById(String memberId) {
		Connection conn = getConnection();
		Member member = memberDao.selectById(conn, memberId);
		close(conn);
		return member;
	}

	public Member selectByName(String memberName) {
		Connection conn = getConnection();
		Member member = memberDao.selectByName(conn, memberName);
		close(conn);
		return member;
	}

	public int deleteMember(String memberId) {
		Connection conn = getConnection();
		int result = memberDao.deleteMember(conn, memberId);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public int updateOneById(String column, String value, String memberId) {
		Connection conn = getConnection();
		int result = memberDao.updateOneById(conn, column, value, memberId);
		if(result > 0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

}
