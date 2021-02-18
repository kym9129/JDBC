package stock.model.service;

import static common.JDBCTemplate.close;
import static common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import stock.model.dao.StockDao;
import stock.model.vo.ProductIO;
import stock.model.vo.ProductStock;

public class StockService {
	
	StockDao stockDao = new StockDao();

	public List<ProductStock> selectAll() {
		Connection conn = getConnection();
		List<ProductStock> sList = stockDao.selectAll(conn);
		close(conn);
		
		return sList;
	}

	public ProductStock selectById(String productId) {
		Connection conn = getConnection();
		ProductStock pStock = stockDao.selectById(conn, productId);
		close(conn);
		return pStock;
	}

	public List<ProductStock> selectByName(String pName) {
		Connection conn = getConnection();
		List<ProductStock> sList = stockDao.selectByName(conn, pName);
		close(conn);
		
		return sList;
	}

	public int addProduct(String productId, String pName, int price, String description, int stock) {
		int result = 0;
		Connection conn = getConnection();
		result = stockDao.addProduct(conn, productId, pName, price, description, stock);
		close(conn);
		return result;
	}

	public int updateStockInfo(String inputStr, String column, String productId) {
		int result = 0;
		Connection conn = getConnection();
		result = stockDao.updateStockInfo(conn, inputStr, column, productId);
		close(conn);
		return result;
	}
	
	public int updateStockInfo(int inputInt, String column, String productId) {
		int result = 0;
		Connection conn = getConnection();
		result = stockDao.updateStockInfo(conn, inputInt, column, productId);
		close(conn);
		return result;
	}

	public int deleteProduct(String table, String productId) {
		int result = 0;
		Connection conn = getConnection();
		result = stockDao.deleteProduct(conn, table, productId);
		close(conn);
		return result;
	}

	public int IO(String productId, int amount, String status) {
		int result = 0;
		Connection conn = getConnection();
		result = stockDao.IO(conn, productId, amount, status);
		close(conn);
		return result;
	}
	
	public List<ProductIO> selectIOAll() {
		Connection conn = getConnection();
		List<ProductIO> ioList = stockDao.selectIOAll(conn);
		close(conn);
		
		return ioList;
	}

}
