package stock.controller;

import java.util.List;

import stock.model.service.StockService;
import stock.model.vo.ProductIO;
import stock.model.vo.ProductStock;

public class StockController {
	
	StockService stockService = new StockService();

	public List<ProductStock> selectAll() {
		List<ProductStock> sList = null;
		sList = stockService.selectAll();
		return sList;
	}

	public ProductStock selectById(String productId) {
		ProductStock pStock = stockService.selectById(productId);
		return pStock;
	}

	public List<ProductStock> selectByName(String pName) {
		List<ProductStock> sList = null;
		sList = stockService.selectByName(pName);
		return sList;
	}

	public int addProduct(String productId, String pName, int price, String description, int stock) {
		int result = 0;
		result = stockService.addProduct(productId, pName, price, description, stock);
		
		return result;
	}

	public int updateStockInfo(String inputStr, String column, String productId) {
		int result = 0;
		result = stockService.updateStockInfo(inputStr, column, productId);
		return result;
	}
	
	public int updateStockInfo(int inputInt, String column, String productId) {
		int result = 0;
		result = stockService.updateStockInfo(inputInt, column, productId);
		return result;
	}

	public int deleteProduct(String table, String productId) {
		int result = 0;
		result = stockService.deleteProduct(table, productId);
		return result;
	}
	
	public List<ProductIO> selectIOAll() {
		List<ProductIO> ioList = null;
		ioList = stockService.selectIOAll();
		return ioList;
	}

	public int IO(String productId, int amount, String status) {
		int result = 0;
		result = stockService.IO(productId, amount, status);
		return result;
	}

}
