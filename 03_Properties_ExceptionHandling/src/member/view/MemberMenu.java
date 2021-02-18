package member.view;

import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;
import member.model.vo.MemberDel;

public class MemberMenu {
	
	private Scanner sc = new Scanner(System.in);
	private MemberController memberController = new MemberController();
	
	public void mainMenu() {
		
		String menu = "========== 회원 관리 프로그램 ==========\n"
				+ "1. 회원 전체조회\n"
				+ "2. 회원 아이디조회\n"
				+ "3. 회원 이름조회\n"
				+ "4. 회원 가입\n"
				+ "5. 회원 정보변경\n"
				+ "6. 회원 탈퇴\n"
				+ "7. 탈퇴 회원 조회\n"
				+ "0. 프로그램 끝내기\n"
				+ "-----------------------------------------\n"
				+ "선택 : ";
		
		do {
			System.out.println(menu);
			String choice = sc.nextLine(); //문자로 하면 잘못 입력했을 때 오류는 안나지.
			List<Member> list = null;
			String memberId = null;
			Member member = null;
			String memberName = null;
			int result = 0;
			String msg = null;
			String title = null;
			String column = null;
			List<MemberDel> dList = null;
			
			switch(choice) {
			case "1": 
				list = memberController.selectAll();
				displayMemberList(list);
				break;
			case "2": 
				memberId = inputMemberId();
				member = memberController.selectById(memberId);
				displayMember(member);
				break;
			case "3": //회원 이름조회
				memberName = inputMemberName();
				member = memberController.selectByName(memberName);
				displayMember(member);
				break;
			case "4": //회원가입
				//1. 신규 회원정보 입력받고 member객체에 담기
				member = inputMember();
				System.out.println("신규회원 확인 : " + member);
				
				//2. controller에 
				result = memberController.insertMember(member);
				
				
				break;
			case "5": //회원정보 변경 (서브메뉴)
//				요구사항
//				* 메인메뉴에서 회원정보변경 메뉴 선택시, 수정할 회원아이디를 입력받고, 해당 회원정보(회원아이디조회)를 우선 보여준 후 서브메뉴출력
//				* 회원정보 수정은 선택된 필드를 각각 처리할 것.
//				* 회원정보변경 수정완료 시 수정된 회원정보를 출력후, 서브메뉴를 다시 출력.
//				* 수정할 회원아이디를 조회해서 해당하는 회원이 없다면, 메인메뉴를 다시 출력할 것.
//				접기
				
				//수정할 회원아이디를 입력
				memberId = inputMemberId();
				
				member = memberController.selectById(memberId);
				if(member != null ) {
					//회원정보 표시
					displayMember(member);
					boolean isTrue = true;
					while(isTrue) {
					
					String subMenu = "****** 회원 정보 변경 메뉴******\n" + 
							"1. 암호변경\n" + 
							"2. 이메일변경\n" + 
							"3. 전화번호변경\n" + 
							"4. 주소 변경\n" + 
							"9. 메인메뉴 돌아가기\n"
							+ "----------------------------\n"
							+ "선택 : ";
					
					System.out.println(subMenu);
					String subChoice = sc.nextLine();

						//서브메뉴 출력
						switch(subChoice) {
						case "1": //암호변경
							title = "암호 변경";
							String password = inputOneToUpdate("암호");
							column = "password";
							result = memberController.updateOneById(column, password, memberId);
							displayMsg(result, msg, title);
							
							//수정 완료 후 수정된 회원정보 출력
							member = memberController.selectById(memberId);
							displayMember(member);
							
							break;
						case "2": //이메일변경
							title = "이메일 변경";
							column = "email";
							String email = inputOneToUpdate("이메일");
							result = memberController.updateOneById(column, email, memberId);
							
							displayMsg(result, msg, title);
							member = memberController.selectById(memberId);
							displayMember(member);
							
							break;
						case "3": //전화번호변경
							title = "전화번호 변경";
							column = "phone";
							String phone = inputOneToUpdate("전화번호");
							result = memberController.updateOneById(column, phone, memberId);
							
							displayMsg(result, msg, title);
							member = memberController.selectById(memberId);
							displayMember(member);
							
							break;
						case "4": //주소변경
							title = "주소 변경";
							column = "address";
							String address = inputOneToUpdate("주소");
							result = memberController.updateOneById(column, address, memberId);
							
							displayMsg(result, msg, title);
							member = memberController.selectById(memberId);
							displayMember(member);
							
							break;
						case "9": //메인메뉴 돌아가기
							System.out.println("메인메뉴로 돌아가겠습니까? (y/n) : ");
							if(sc.next().toUpperCase().charAt(0) == 'Y') {
								isTrue = false; 
								break;
							}
						default: System.out.println("질못 입력하셨습니다."); break;
						}
					}
					
					break;
				} else {
					System.out.println("존재하지 않는 아이디입니다. 메인메뉴로 돌아갑니다.");
					break;
				}
				
				
			case "6": //회원 탈퇴
				//id입력받고 그 행 delete
				title = "회원 탈퇴";
				memberId = inputMemberId();
				result = memberController.deleteMember(memberId);
				
				//사용자 피드백 보내기
				displayMsg(result, msg, title);
				
				break;
			case "7": //탈퇴 회원 조회
				//탈퇴 회원 모두 조회 (member_del테이블)
				dList = memberController.selectAllDel();
				//탈퇴 회원 표시
				displayDelMemberList(dList);
				
				break;
			case "0": 
				System.out.println("정말 끝내시겠습니까? (y/n) : ");
				if(sc.next().toUpperCase().charAt(0) == 'Y')
					return;
				break;
			default: 
				System.out.println("잘못 입력하셨습니다.");
				break;
			}	
		} while(true);
		
	}
	
	private String inputOneToUpdate(String title) {
		System.out.println("변경할 " +title+ "을(를) 입력하세요 : ");
		return sc.nextLine();
	}

	private void displayMsg(int result, String msg, String title) {
		msg = result > 0? title + " 성공!" : title + " 실패!";
		System.out.println(">>> 처리결과 : " + msg);
		
	}

	//신규 회원정보 입력
	private Member inputMember() {
		System.out.println("새로운 회원정보를 입력하세요.");
		Member member = new Member();
		System.out.print("아이디: ");
		member.setMemberId(sc.nextLine()); //nextLine() 공백이 없는 문자열
//		System.out.println(member.getMemberId());
		System.out.println("이름: ");
		member.setMemberName(sc.nextLine());
//		System.out.println(member.getMemberName());
		System.out.println("비밀번호: ");
		member.setPassword(sc.nextLine());
		System.out.println("나이: ");
		member.setAge(sc.nextInt());
		System.out.println("성별(M/F): ");
		member.setGender(String.valueOf(sc.next().toUpperCase().charAt(0)));
		sc.nextLine();
		System.out.println("이메일: ");
		member.setEmail(sc.nextLine());
		System.out.println("전화번호(-빼고 입력): ");
		member.setPhone(sc.nextLine());
		System.out.println("주소: ");
		member.setAddress(sc.nextLine());
		System.out.println("취미(,로 나열할 것): ");
		member.setHobby(sc.nextLine());
		
		return member;
	}


	private String inputMemberName() {
		System.out.println("이름 입력: ");
		return sc.nextLine();
	}


	private void displayMember(Member member) {
		if(member == null) {
			System.out.println(">>> 조회된 회원이 없습니다.");
		}else {
			System.out.println("=========================");
			System.out.println(member);
			System.out.println("=========================");
		}
		
	}


	private String inputMemberId() {
		System.out.println("아이디 입력: ");
		return sc.nextLine();
	}


	//n행의 회원정보 출력
	private void displayMemberList(List<Member> list) {
		if(list != null && !(list.isEmpty())){
			System.out.println("============================================");
			for(int i = 0; i < list.size(); i++) {
				System.out.println((i + 1) + " : " + list.get(i));
			}
			System.out.println("============================================");
		} else {
			System.out.println(">>> 조회된 회원 정보가 없습니다.");
		}
		
	}
	
	private void displayDelMemberList(List<MemberDel> dList) {
		if(dList != null && !(dList.isEmpty())) {
			System.out.println("============================================");
			for(int i = 0; i < dList.size(); i++) {
				System.out.println((i + 1) + " : " + dList.get(i));
			}
			System.out.println("============================================");
		} else {
			System.out.println(">>> 조회된 탈퇴회원 정보가 없습니다.");
		}
		
	}

	/**
	 * 사용자에게 오류메세지 출력하기
	 * @param errorMsg
	 */
	public void displayError(String errorMsg) {
		// TODO Auto-generated method stub
		System.err.println(errorMsg); //에러메세지 출력
		
	}

}
