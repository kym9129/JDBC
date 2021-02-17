package member.controller;

import java.util.List;

import member.model.service.MemberService;
import member.model.vo.Member;

public class MemberController {
	
	private MemberService memberService = new MemberService();

	public List<Member> selectAll() {
		return memberService.selectAll();
	}
	
	public int insertMember(Member member) {
		return memberService.insertMember(member);
	}

	public Member selectById(String memberId) {
		return memberService.selectById(memberId);
	}

	public Member selectByName(String memberName) {
		return memberService.selectByName(memberName);
	}

	public int deleteMember(String memberId) {
		return memberService.deleteMember(memberId);
	}

	public int updateOneById(String column, String value, String memberId) {
		return memberService.updateOneById(column, value, memberId);
	}

}
