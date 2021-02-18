package member.controller;

import java.util.List;

import member.model.exception.MemberException;
import member.model.service.MemberService;
import member.model.vo.Member;
import member.model.vo.MemberDel;
import member.view.MemberMenu;

public class MemberController {
	
	private MemberService memberService = new MemberService();

	public List<Member> selectAll() {
		List<Member> list = null;
		try {
			list = memberService.selectAll();
			
		} catch(MemberException e) {
			//서버로깅
			//관리자 이메일 알림
			displayError(e);
		}
		return list;
	}
	
	public int insertMember(Member member) {
		int result = 0;
		try {
			result = memberService.insertMember(member);
		}catch(MemberException e){
			displayError(e);		
			}
		return result;
	}

	public Member selectById(String memberId) {
		Member member = null;
		try {
			member = memberService.selectById(memberId);
		}catch(MemberException e) {
			displayError(e);
		}
		return member;
	}

	public Member selectByName(String memberName) {
		Member member = null;
		try {
			member = memberService.selectByName(memberName);
		}catch(MemberException e) {
			displayError(e);
		}
		return member;
	}

	public int deleteMember(String memberId) {
		int result = 0;
		try {
			result = memberService.deleteMember(memberId);
		}catch(MemberException e) {
			displayError(e);
		}
		return result;
	}

	public int updateOneById(String column, String value, String memberId) {
		int result = 0;
		try {
			result = memberService.updateOneById(column, value, memberId);
		}catch(MemberException e){
			displayError(e);
		}
		return result;
	}
	
	private void displayError(MemberException e) {
		new MemberMenu().displayError(e.getMessage() + ": 관리자에게 문의하세요.");
		
	}

	public List<MemberDel> selectAllDel() {
		List<MemberDel> dList = null;
		try {
			dList = memberService.selectAllDel();
		}catch(MemberException e) {
			displayError(e);
		}
		return dList;
	}

}
