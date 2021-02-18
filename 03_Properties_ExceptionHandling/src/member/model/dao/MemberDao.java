package member.model.dao;

import static common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import member.model.exception.MemberException;
import member.model.vo.Member;
import member.model.vo.MemberDel;

public class MemberDao {
	
	private Properties prop = new Properties();
	/*
	 * 1. MemberDao객체 생성 시(최초1회),
	 * 	  member-query.properties의 내용을 읽어 prop에 저장한다.
	 * 
	 * 2. dao메소드 호출 시마다 prop으로부터 query를 가져다 사용한다.
	 */
	public MemberDao() {
		String filename = "resources/member-query.properties";
		try {
			prop.load(new FileReader(filename));	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*
	 * Dao
	 * 3.PreparedStatement객체 생성 (미완성쿼리 : 물음표가 있는 쿼리)
	 * 	3.1 물음표에 값대입
	 * 4. 실행과 동시에 리턴값 받음. DML(executeUpdate) -> int, DQL(executeQuery) -> ResultSet
	 * 	4.1 ResultSet -> Java객체로 옮겨담기
	 * 5. 자원반납 (객체 생성 역순으로. rset - pstmt)
	 * */

	public List<Member> selectAll(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAll");
		List<Member> list = null;
		
		try {
//		 * 3.PreparedStatement객체 생성 (미완성쿼리 : 물음표가 있는 쿼리)
			pstmt = conn.prepareStatement(sql);
//		 * 	3.1 물음표에 값대입
//		 * 4. 실행과 동시에 리턴값 받음. DML(executeUpdate) -> int, DQL(executeQuery) -> ResultSet
			rset = pstmt.executeQuery();
//		 * 	4.1 ResultSet -> Java객체로 옮겨담기
			list = new ArrayList<>();
			while(rset.next()) {
				String memberId = rset.getString("member_id"); //컬럼명
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
			
		} catch (SQLException e) {
			//사용자에게 안보이는 로그
			//예외를 전환 : RuntimeException(unchecked), 의미분명한 커스텀 예외객체로 전환
			throw new MemberException("회원 전체 조회", e);
		}finally {
//		 * 5. 자원반납 (객체 생성 역순으로. rset - pstmt)
			close(rset);
			close(pstmt);
		}
		return list;
	}

	public int insertMember(Connection conn, Member member) {
		String sql = prop.getProperty("insertMember");
		int result = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender());
			pstmt.setInt(5, member.getAge());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getHobby());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
//			e.printStackTrace();
			throw new MemberException("회원가입", e);
		}
		
		return result;
	}

	public Member selectById(Connection conn, String memberId) {
		String sql = prop.getProperty("selectById");
		ResultSet rset = null;
		Member member = null;
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				memberId = rset.getString("member_id");
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
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MemberException("아이디로 조회", e);
		}
		return member;
	}

	public Member selectByName(Connection conn, String memberName) {
		String sql = prop.getProperty("selectByName");
		ResultSet rset = null;
		Member member = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ("%" + memberName + "%"));
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
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
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MemberException("이름으로 조회", e);
		}
		
		
		return member;
	}

	public int deleteMember(Connection conn, String memberId) {
		int result = 0;
		String sql = prop.getProperty("deleteMember");
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MemberException("회원 탈퇴", e);
		}
		return result;
	}
	
	public int updateOneById(Connection conn, String column, String value, String memberId) {
		int result = 0;
		String sql = prop.getProperty("updateOneById").replace("#", column);
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, value);
			pstmt.setString(2, memberId);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
//			e.printStackTrace();
			throw new MemberException("회원정보 수정", e);
		}
		
		return result;
	}

	public List<MemberDel> selectAllDel(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAllDel");
		List<MemberDel> dList = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			//ResultSet -> Java Object
			dList = new ArrayList<>();
			while(rset.next()) {
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
				Date delDate = rset.getDate("del_date");
				
				MemberDel dMember = new MemberDel(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate, delDate);
				dList.add(dMember);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MemberException("탈퇴 회원 조회", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return dList;
	}

}
