package member.controller;

import java.util.List;

import member.model.dao.MemberDao;
import member.model.vo.Member;

public class MemberController {
	
	/*
	 * MVC패턴의 시작점이자 전체 흐름을 제어.
	 * 
	 * view단으로부터 요청을 받아서 dao에 다시 요청
	 * 그 결과를 view단에 다시 전달.(쿼리같은 역할)
	 */
	
	private MemberDao memberDao = new MemberDao();

	public int insertMember(Member member) {
		return memberDao.insertMember(member);
	}

	public List<Member> selectAll() {
		return memberDao.selectAll();
	}

	public Member selectOne(String memberId) {
		return memberDao.selectOne(memberId);
	}

	public Member selectName(String memberName) {
		return memberDao.selectName(memberName);
	}

	public int updateMember(Member member) {
		return memberDao.updateMember(member);
	}

	public int deleteMember(String memberId) {
		return memberDao.deleteMember(memberId);
	}

}
