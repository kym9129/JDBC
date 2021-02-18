package common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*
 * Service, Dao클래스의 공통부분을 static 메소드로 제공
 * 예외처리를 공통부분에서 작성하므로, 사용(호출)하는 쪽의 코드를 간결히 할 수 있다.
 */

public class JDBCTemplate {
	
	static String driverClass;
	static String url;
	static String user;
	static String password;
	
	//클래스가 사용될 때 처음 1회 실행되는 초기화 블럭
	static {
		//data-source.properties의 내용을 읽어서 변수에 대입
		
		Properties prop = new Properties();
		String filename = "resources/data-source.properties";
		try {
			prop.load(new FileReader(filename));
//			System.out.println("prop = " + prop);
			driverClass = prop.getProperty("driverClass");
			url = prop.getProperty("url");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
//		 * 1. DriverClass등록 (최초1회)
			try {
				Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		try {

//		 * 2. Connection 객체 생성 (url, user, password)
			conn = DriverManager.getConnection(url, user, password);
			//DriverManager: java sql패키지의 클래스
//		 * 	2.1 자동커밋 false설정
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void close(Connection conn) {
//		 * 7. 자원반납 (conn)
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void close(ResultSet rset) {
		try {
			if(rset != null)
				rset.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void commit(Connection conn) {
		try {
			if(conn != null)
				conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null)
				conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
