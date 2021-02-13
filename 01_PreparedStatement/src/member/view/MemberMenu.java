package member.view;

import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;

public class MemberMenu {
	
	//컨트롤러 객체를 필드로 선언
	private MemberController memberController = new MemberController();
	private Scanner sc = new Scanner(System.in);

	public void mainMenu() {
		String menu = "========== 회원 관리 프로그램 ==========\n"
				+ "1. 회원 전체조회\n"
				+ "2. 회원 아이디조회\n"
				+ "3. 회원 이름조회\n"
				+ "4. 회원 가입\n"
				+ "5. 회원 정보변경\n"
				+ "6. 회원 탈퇴\n"
				+ "0. 프로그램 끝내기\n"
				+ "-----------------------------------------\n"
				+ "선택 : ";
		
		while(true) {
			System.out.print(menu);
			int choice = sc.nextInt();
			Member member = null;
			int result = 0;
			String msg = null;
			List<Member> list = null;
			String memberId = null;
			String memberName = null;
			
			switch(choice) {
				case 1: 
					//회원 전체조회
					list = memberController.selectAll();
					displayMemberList(list);
					break;
				case 2: 
					//회원 아이디 조회
					memberId = inputMemberId();
					member = memberController.selectOne(memberId);
					displayMember(member);
					break;
				case 3: 
					//회원 이름조회
					//이름 일부를 입력해도 조회가 될 수 있도록 한다.
					memberName = inputMemberName();
					member = memberController.selectName(memberName);
					displayMember(member);
					
					break;
				case 4: 
					//1. 신규회원정보 입력 -> Member객체
					member = inputMember();
					System.out.println("신규회원 확인: " + member);
					//2. controller에 회원가입 요청(메소드호출) -> int리턴(처리된 행의 개)
					result = memberController.insertMember(member);
					//3. int에 따른 분기처리
					msg = result > 0? "회원가입 성공!" : "회원가입 실패!";
					displayMsg(msg); //사용자 피드백을 보내는 메소드
					
					break;
				case 5: 
					//회원정보 변경
					//암호, 이메일, 전화번호, 주소, 취미를 일괄변경하도록 한다.
					
					//id, 암호, 메일 ,전번 ,주소, 취미 입력값 받기
					memberId = inputMemberId();
					String newPassword = inputUpdatePassword();
					String newEmail = inputUpdateEmail();
					String newPhone = inputUpdatePhone();
					sc.nextLine(); //개행날림용
					String newAddress = inputUpdateAddress();
					String newHobby = inputUpdateHobby();
					//얘네를 좀더 깔끔하게 받을 방법 없나.. member객체로 리턴하자니 없는 값도 있구 킁
					//파라미터로 받자니 리턴값이 여러개고 흠흠~
					
					//update문으로 사용자 입력값 대입하기
					result = memberController.updateMember(newPassword, newEmail, newPhone, newAddress, newHobby, memberId);
					
					//사용자 피드백 보내기 "회원정보 변경 성공 or 실패"
					msg = result > 0? "회원정보 변경 성공!" : "회원정보 변경 실패!";
					displayMsg(msg);
	
					break;
				case 6: 
					//회원 탈퇴
					//delete처리한다.
					
					//id입력받기
					memberId = inputMemberId();
					result = memberController.deleteMember(memberId);
					
					//사용자 피드백 보내기
					msg = result > 0 ? "회원 탈퇴 성공!" : "회원 탈퇴 실패!";
					displayMsg(msg);
					
					break;
				case 0: 
					//프로그램 끝내기
					System.out.print("정말로 끝내시겠습니까? (y/n) : ");
					if(sc.next().charAt(0) == 'y')
						return; //현재 메소드(mainMenu)를 호출한 곳 -> Run.main()
					break;
				default: 
					System.out.println("잘못 입력하셨습니다.");
			}
		}
		
	}

	private String inputUpdateHobby() {
		System.out.println("변경할 취미 입력 (,로 나열할 것): ");
		return sc.nextLine();
	}

	private String inputUpdateAddress() {
		System.out.println("변경할 주소 입력: ");
		return sc.nextLine();
	}

	private String inputUpdatePhone() {
		System.out.println("변경할 전화번호 입력: ");
		return sc.next();
	}

	private String inputUpdateEmail() {
		System.out.println("변경할 이메일 입력: ");
		return sc.next();
	}

	private String inputUpdatePassword() {
		System.out.println("변경할 비밀번호 입력: ");
		return sc.next();
	}

	//DB에서 조회한 1명의 회원 출력 (0명일 수도 있음)
	private void displayMember(Member member) {
		if(member == null)
			System.out.println(">>>> 조회된 회원이 없습니다.");
		else {
			System.out.println("**********************************");
			System.out.println(member);
			System.out.println("**********************************");
		}
		
	}

	//조회할 회원 아이디 입력
	private String inputMemberId() {
		System.out.println("조회할 아이디 입력: ");
		return sc.next();
	}
	
	//조회할 회원 이름 입력
	private String inputMemberName() {
		System.out.println("조회할 이름 입력: ");
		return sc.next();
	}

	//DB에서 조회된 회원객체 n개를 출력
	private void displayMemberList(List<Member> list) {
		if(list == null || list.isEmpty()) {
			//null이나 0행이 리턴되는걸 분기처리
			System.out.println(">>>> 조회된 행이 없습니다.");
			
		}else {
			System.out.println("***************************");
			for(Member m : list) {
				System.out.println(m);
			}
			System.out.println("***************************");
		}
		
	}

	//DML 처리결과 통보용
	private void displayMsg(String msg) {
		System.out.println(">>> 처리결과 : " + msg);
		
	}

	/*
	 * 신규회원정보 입력
	 */
	private Member inputMember() {
		System.out.println("새로운 회원정보를 입력하세요.");
		Member member = new Member();
		System.out.print("아이디: ");
		member.setMemberId(sc.next()); //next() 공백이 없는 문자열
//		System.out.println(member.getMemberId());
		System.out.println("이름: ");
		member.setMemberName(sc.next());
//		System.out.println(member.getMemberName());
		System.out.println("비밀번호: ");
		member.setPassword(sc.next());
		System.out.println("나이: ");
		member.setAge(sc.nextInt());
		System.out.println("성별(M/F): ");
		member.setGender(String.valueOf(sc.next().toUpperCase().charAt(0)));
		System.out.println("이메일: ");
		member.setEmail(sc.next());
		System.out.println("전화번호(-빼고 입력): ");
		member.setPhone(sc.next());
		sc.nextLine();
		System.out.println("주소: ");
		member.setAddress(sc.nextLine());
		System.out.println("취미(,로 나열할 것): ");
		member.setHobby(sc.nextLine());
		
		return	member;
	}

}
