package kr.or.iei.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.or.iei.member.model.vo.Member;

@Mapper
public interface MemberDao {

	Member selectOneMember(String memberId);

	int insertMember(Member member);

	int changePhone(Member member);

	int delete(String memberId);

	int changePwMember(Member member);



}
