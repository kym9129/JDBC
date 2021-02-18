package stock.view;

import java.util.List;
import java.util.Scanner;

import stock.controller.StockController;
import stock.model.vo.ProductIO;
import stock.model.vo.ProductStock;

public class MainMenu {
	Scanner sc = new Scanner(System.in);
	StockController stockController = new StockController();

	public void mainMenu() {
		while(true) {
			displayMainMenu();
			String choice = sc.next();
			List<ProductStock> sList  = null;
			ProductStock pStock = null;
			int result = 0;
			String productId = null;
			
			switch(choice) {
			case "1": //전체상품조회
				String table = "product_stock";
				sList = stockController.selectAll();
				displayList(sList);
				break;
			case "2": //상품 아이디 검색
				productId = inputProductId();
				pStock = stockController.selectById(productId);
				displayOne(pStock);
				break;
			case "3": //상품명 검색 %%
				String pName = inputPName(false);
				sList = stockController.selectByName(pName);
				displayList(sList);
				break;
			case "4": //상품추가 addProduct
				String title = "상품 추가";
				productId = inputProductId();
				pName = inputPName(false);
				int price = inputPrice(false);
				String description = inputDescription(false);
				sc.nextLine();
				int stock = inputStock();
				result = stockController.addProduct(productId, pName, price, description, stock);
				
				displayMsg(result, title);
				break;
			case "5": 
				updateStockInfo();
				break;
			case "6": 
				title = "상품 삭제";
				productId = inputProductId();
				
				//product_stock
				table = "product_stock";
				result = stockController.deleteProduct(table, productId);
				
				//product_io
				table = "product_io";
				stockController.deleteProduct(table, productId);
				
				displayMsg(result, title);
				break;
			case "7":
				IOMenu();
				break;
			case "9": 
				System.out.println("정말 종료하시겠습니까? (y/n) : ");
				if(sc.next().toUpperCase().charAt(0) == 'Y')
					return;
				break;
			default: System.out.println("잘못 입력하셨습니다."); break;
			}
			
			
		}
		
	}

	private void IOMenu() {
		String IOMenu = "***** 상품입출고메뉴*****\n" + 
				"1. 전체입출고내역조회\n" + 
				"2. 상품입고\n" + 
				"3. 상품출고\n" + 
				"9. 메인메뉴로 돌아가기\n"
				+ "--------------------\n"
				+ "선택 : ";
		while(true) {
			List<ProductIO> ioList = null;
			System.out.println(IOMenu);
			String choice = sc.next();
			switch(choice) {
			case "1": 
				ioList = stockController.selectIOAll();
				displayIOList(ioList);
				break;
			case "2": //상품 입고
				String title = "상품 입고";
				String productId = inputProductId();
				int amount = inputAmount(title);
				String status = "I";
				int result = stockController.IO(productId, amount, status);
				displayMsg(result, title);
				break;
			case "3": //상품 출고
				title = "상품 출고";
				productId = inputProductId();
				amount = inputAmount(title);
				status = "O";
				result = stockController.IO(productId, amount, status);
				displayMsg(result, title);
				break;
			case "9": 
				System.out.println("메인메뉴로 돌아가시겠습니까? (y/n) : ");
				if(sc.next().toUpperCase().charAt(0) == 'Y')
					return;
			default : System.out.println("잘못 입력했습니다.") ;break;
			}
		}
		
	}

	private int inputAmount(String title) {
		System.out.println(title + " 수량 입력 : ");
		return sc.nextInt();
	}

	private void updateStockInfo() {
		String subMenu = "***** 상품정보변경메뉴 *****\n" + 
				"1.상품명변경\n" + 
				"2.가격변경\n" + 
				"3.설명변경\n" + 
				"9.메인메뉴로 돌아가기\n"
				+ "--------------------\n"
				+ "선택 : ";
		while(true) {
			System.out.println(subMenu);
			String choice = sc.next();
			switch(choice) {
			case "1" : //상품명 변경
				String title = "상품명 변경";
				//id입력받기
				String productId = inputProductId();
				
				//상품명 입력받기
				String pName = inputPName(true);
				String column = "p_name"; //상품명 컬럼
				int result = stockController.updateStockInfo(pName, column, productId);
	
				//사용자 피드백 출력
				displayMsg(result, title);
				break;
			case "2" : //가격 변경
				title = "가격 변경";
				//id입력받기
				productId = inputProductId();
				
				//가격 입력받기
				int price = inputPrice(true);
				column = "price";
				result = stockController.updateStockInfo(price, column, productId);
	
				//사용자 피드백 출력
				displayMsg(result, title);
				break;
			case "3" :
				title = "설명 변경";
				productId = inputProductId();
				String description = inputDescription(true);
				column = "description";
				result = stockController.updateStockInfo(description, column, productId);
				displayMsg(result, title);
				break;
			case "9" : 
				System.out.println("메인메뉴로 돌아가시겠습니까? (y/n) : ");
				if(sc.next().toUpperCase().charAt(0) == 'Y')
					return;
			default : System.out.println("잘못 입력했습니다."); break;
			}
		}
		
	}

	private void displayMsg(int result, String title) {
		String msg = result > 0? (title + " 성공!") : (title + " 실패!");
		System.out.println(msg);
		
	}

	private int inputStock() {
		System.out.println("상품 재고 입력 : ");
		return sc.nextInt();
	}

	private String inputDescription(boolean isUpdate) {
		if(!isUpdate) {
			System.out.println("상품 설명 입력 : ");
			
		}
		else {
			System.out.println("변경할 상품 설명 입력 : ");
			
		}
		return sc.nextLine();
	}

	private int inputPrice(boolean isUpdate) {
		if(!isUpdate) {
			System.out.println("상품 가격 입력 : ");
			
		}else {
			
			System.out.println("변경할 상품 가격 입력 : ");
		}
		return sc.nextInt();
	}

	private String inputPName(boolean isUpdate) {
		if(!isUpdate) {
			System.out.println("상품명 입력 : ");
		}
		else {
			System.out.println("변경할 상품명 입력 : ");
		}
		return sc.next();
	}

	private String inputProductId() {
		System.out.println("상품 아이디 입력 : ");
		return sc.next();
	}

	private void displayOne(ProductStock pStock) {
		System.out.println("=================================");
		if(pStock != null) {
			System.out.println(pStock);
		}
		System.out.println("=================================");
		
	}

	private void displayList(List<ProductStock> sList) {
		System.out.println("=================================");
		if(sList != null && !(sList.isEmpty())) {
			for(int i = 0 ; i < sList.size(); i++) {
				System.out.println((i + 1) + " : " + sList.get(i));
			}
		}
		System.out.println("=================================");
		
	}
	
	private void displayIOList(List<ProductIO> ioList) {
		System.out.println("=================================");
		if(ioList != null && !(ioList.isEmpty())) {
			for(int i = 0 ; i < ioList.size(); i++) {
				System.out.println((i + 1) + " : " + ioList.get(i));
			}
		}
		System.out.println("=================================");
		
	}
	
	

	private void displayMainMenu() {
		String menu = "***** 상품재고관리프로그램 *****\n"
				+ "1. 전체상품조회\n" + 
				"2. 상품아이디검색\n" + 
				"3. 상품명검색\n" + 
				"4. 상품추가\n" + 
				"5. 상품정보변경\n" + 
				"6. 상품삭제\n" + 
				"7. 상품입/출고 메뉴\n" + 
				"9. 프로그램종료\n"
				+ "----------------------------\n"
				+ "선택 : ";
		System.out.println(menu);
		
	}

}
