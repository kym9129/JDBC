package member.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.model.vo.Member;

/*
 * DAO
 * Data Access Object
 * DB에 접근하는 클래스
 * 
 * 1. 드라이버클래스 등록(최초1회)
 * 2. Connection객체 생성(url, user, password)
 * 3. 자동커밋여부 설정 true / false -> app에서 직접 트랜잭션 제어
 * 4. PreparedStatement 객생성(미완성 쿼리) 및 값대입
 * 5. Statement 객체 실행. 
 * 6. 응답처리 DML : int리턴, DQL : ResultSet리턴 -> 자바객체로 전환
 * 7. 트랜잭션처리(DML)
 * 8. 자원반납(생성의 역순)
 * 
 */

public class MemberDao {
	String driverClass = "oracle.jdbc.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "student";
	String password = "student";

	public int insertMember(Member member) {
		Connection conn = null;
		String sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, default)";
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
//		 * 1. 드라이버클래스 등록(최초1회)
			Class.forName(driverClass);
			
//		 * 2. Connection객체 생성(url, user, password)
			//:port:oracle version
			conn = DriverManager.getConnection(url, user, password);
			
//		 * 3. 자동커밋여부 설정(DML) true(기본값) / false -> app에서 직접 트랜잭션 제어
			conn.setAutoCommit(false);
			
//		 * 4. PreparedStatement 객체생성(미완성 쿼리) 및 값대입
			//값이 들어갈 자리를 물음표(?)로 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender());
			pstmt.setInt(5, member.getAge());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getHobby());
			
//		 * 5. Statement 객체 실행. 
//		 * 6. 응답처리 DML : int리턴, DQL : ResultSet리턴 -> 자바객체로 전환
			result = pstmt.executeUpdate(); //dml인 경우
			//정상적으로 리턴됐으면 1리턴 or 오류발생
			//executeQuery() : DQL인 경우
			
//		 * 7. 트랜잭션처리(DML)
			if(result > 0)
				conn.commit();
			else
				conn.rollback();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//		 * 8. 자원반납(생성의 역순)
			try {
				if(pstmt != null)
					pstmt.close(); //pstmt가 null이면 NullPointerException뜸
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//따로따로 try catch로 감싸야 안전한다.
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							
		}
		
		return result;
	}

	//DQL 실행
	public List<Member> selectAll() {
		Connection conn = null;
		String sql = "select * from member order by enroll_date desc";
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = null;
		
//		 * 1. 드라이버클래스 등록(최초1회)
		try {
			Class.forName(driverClass);			
			
//		 * 2. Connection객체 생성(url, user, password)
//		 * 3. 자동커밋여부 설정 true / false -> app에서 직접 트랜잭션 제어
			conn = DriverManager.getConnection(url, user, password);
			
//		 * 4. PreparedStatement 객생성(미완성 쿼리) 및 값대입
			pstmt = conn.prepareStatement(sql); //물음표가 없음. 값 대입 X
			
//		 * 5. Statement 객체 실행. 
			rset = pstmt.executeQuery();
			
//		 * 6. 응답처리 DML : int리턴, DQL : ResultSet리턴 -> 자바객체로 전환
			//다응햄 존재여부 리턴. 커서가 다음행에 접근
			list = new ArrayList<>();
			while(rset.next()) {
				//컬럼명은 대소문자를 구분하지 않는다.
				String memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				String gender = rset.getString("gender");
				int age = rset.getInt("age");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String address = rset.getString("address");
				String hobby = rset.getString("hobby");
				Date enrollDate = rset.getDate("enroll_date");
				
				Member member = new Member(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate);
				list.add(member);
			}
//		 * 7. 트랜잭션처리(DML) --

			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//		 * 8. 자원반납(생성의 역순)
			try {
				if(rset != null)
					rset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		return list; //중요중요!!! 빼먹지말고 리턴값 넣을 것
	}

	public Member selectOne(String memberId) {
		
		Connection conn = null;
		String sql = "select * from member where member_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		
//		 * 1. 드라이버클래스 등록(최초1회)
		try {
			Class.forName(driverClass);			
			
//		 * 2. Connection객체 생성(url, user, password)
//		 * 3. 자동커밋여부 설정 true / false -> app에서 직접 트랜잭션 제어
			conn = DriverManager.getConnection(url, user, password);
			
//		 * 4. PreparedStatement 객생성(미완성 쿼리) 및 값대입
			pstmt = conn.prepareStatement(sql); //물음표가 있음
			pstmt.setString(1, memberId);//첫번째 물음표에 memberId값을 채워주세요.
			
//		 * 5. Statement 객체 실행. 
			rset = pstmt.executeQuery();
			
//		 * 6. 응답처리 DML : int리턴, DQL : ResultSet리턴 -> 자바객체로 전환
			//다응햄 존재여부 리턴. 커서가 다음행에 접근
			
			while(rset.next()) {
				//컬럼명은 대소문자를 구분하지 않는다.
				memberId = rset.getString("member_id"); //파라미터 변수로 선언되어있음 
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				String gender = rset.getString("gender");
				int age = rset.getInt("age");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String address = rset.getString("address");
				String hobby = rset.getString("hobby");
				Date enrollDate = rset.getDate("enroll_date");
				
				member = new Member(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate);

			}
//		 * 7. 트랜잭션처리(DML) --

			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//		 * 8. 자원반납(생성의 역순)
			try {
				if(rset != null)
					rset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return member; //중요중요!!! 빼먹지말고 리턴값 넣을 것
	

	}

	public Member selectName(String memberName) {
		
		Connection conn = null;
		String sql = "select * from member where member_name like ?";
//		String keyword = "%" + memberName + "%";
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, password);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ("%" + memberName + "%"));
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				System.out.println("rset execute!");
				String memberId = rset.getString("member_id");
				String password = rset.getString("password");
				memberName = rset.getString("member_name");
				String gender = rset.getString("gender");
				int age = rset.getInt("age");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String address = rset.getString("address");
				String hobby = rset.getString("hobby");
				Date enrollDate = rset.getDate("enroll_date");
				
				member = new Member(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate);
				
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rset != null)
					rset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return member;
	}

	public int updateMember(Member member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//암호, 이메일, 전화번호, 주소, 취미, member_id
		String sql = "update member set age = ?, gender = ?, email = ?, phone = ?, address = ?, hobby = ? where member_id = ?";
		int result = 0;
		
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, member.getAge());
			pstmt.setString(2, member.getGender());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getPhone());
			pstmt.setString(5, member.getAddress());
			pstmt.setString(6, member.getHobby());
			pstmt.setString(7, member.getMemberId());
			
			result = pstmt.executeUpdate();
			
			if(result > 0)
				conn.commit();
			else
				conn.rollback();
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		return result;
	}

	public int deleteMember(String memberId) {
		int result = 0;
		String sql = "delete from member where member_id = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, memberId);
			
			result = pstmt.executeUpdate();
			
			if(result > 0)
				conn.commit();
			else
				conn.rollback();
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
