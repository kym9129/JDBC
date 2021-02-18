package stock.model.dao;

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

import stock.model.vo.ProductIO;
import stock.model.vo.ProductStock;

public class StockDao {
	Properties prop = new Properties();
	
	public StockDao() {
		String filename = "resources/stock-query.properties";
		try {
			prop.load(new FileReader(filename));
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ProductStock> selectAll(Connection conn) {
		List<ProductStock> sList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAll");
//		System.out.println("sql = " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				String productId = rset.getString("product_id");
				String pName = rset.getString("p_name");
				int price = rset.getInt("price");
				String description = rset.getString("description");
				int stock = rset.getInt("stock");
				ProductStock pStock = new ProductStock(productId, pName, price, description, stock);
				sList.add(pStock);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		
		return sList;
	}

	public ProductStock selectById(Connection conn, String productId) {
		ProductStock pStock = null;
		String sql = prop.getProperty("selectById");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				productId = rset.getString("product_id");
				String pName = rset.getString("p_name");
				int price = rset.getInt("price");
				String description = rset.getString("description");
				int stock = rset.getInt("stock");
				pStock = new ProductStock(productId, pName, price, description, stock);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return pStock;
	}

	public List<ProductStock> selectByName(Connection conn, String pName) {
		List<ProductStock> sList = new ArrayList<>();
		String sql = prop.getProperty("selectByName");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ("%" + pName + "%"));
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				String productId = rset.getString("product_id");
				pName = rset.getString("p_name");
				int price = rset.getInt("price");
				String description = rset.getString("description");
				int stock = rset.getInt("stock");
				ProductStock pStock = new ProductStock(productId, pName, price, description, stock);
				sList.add(pStock);
//				System.out.println("sList = " + sList);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return sList;
	}

	public int addProduct(Connection conn, String productId, String pName, int price, String description, int stock) {
		int result = 0;
		String sql = prop.getProperty("addProduct");
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			pstmt.setString(2, pName);
			pstmt.setInt(3, price);
			pstmt.setString(4, description);
			pstmt.setInt(5, stock);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateStockInfo(Connection conn, String inputStr, String column, String productId) {
		int result = 0;
		String sql = prop.getProperty("updateStockInfo").replace("#", column);
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputStr);
			pstmt.setString(2, productId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return result;
	}
	
	public int updateStockInfo(Connection conn, int inputInt, String column, String productId) {
		int result = 0;
		String sql = prop.getProperty("updateStockInfo").replace("#", column);
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inputInt);
			pstmt.setString(2, productId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return result;
	}

	public int deleteProduct(Connection conn, String table, String productId) {
		int result = 0;
		String sql = prop.getProperty("deleteProduct").replace("#", table);
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	public List<ProductIO> selectIOAll(Connection conn) {
		List<ProductIO> ioList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectIOAll");
//		System.out.println("sql = " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				int ioNo = rset.getInt("io_no");
				String productId = rset.getString("product_id");
				Date ioDate = rset.getDate("iodate");
				int amount = rset.getInt("amount");
				String status = rset.getString("status");
				ProductIO pStock = new ProductIO(ioNo, productId, ioDate, amount, status);
				ioList.add(pStock);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		
		return ioList;
	}

	public int IO(Connection conn, String productId, int amount, String status) {
		int result = 0;
		String sql = prop.getProperty("IO");
		PreparedStatement pstmt = null;
		
		try {
			System.out.println("amount = " + amount);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			pstmt.setInt(2, amount);
			pstmt.setString(3, status);
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

}
