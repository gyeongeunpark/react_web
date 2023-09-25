package kr.or.iei.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.JwtUtil;
import kr.or.iei.PageInfo;
import kr.or.iei.Pagination;
import kr.or.iei.member.model.dao.MemberDao;
import kr.or.iei.member.model.vo.Member;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JwtUtil jswUtil;
	@Value("${jwt.secret}")
	private String secretKey;
	private long expiredMs;
	@Autowired
	private Pagination pagination;

	public MemberService() {
		super();
		expiredMs = 1000 * 60 * 60;
	}

	public Member selectOneMember(String memberId) {
		return memberDao.selectOneMember(memberId);
	}

	@Transactional
	public int insertMember(Member member) {
		return memberDao.insertMember(member);
	}

	@Transactional
	public String login(Member member) { // 로그인은 입력한 아이디,비밀번호와 DB에 저장되어있는 정보가 일치해야한다
		Member m = selectOneMember(member.getMemberId()); // 아이디는 이미 구했었음
		if (m != null && bCryptPasswordEncoder.matches(member.getMemberPw(), m.getMemberPw())) {

			return jswUtil.createToken(member.getMemberId(), secretKey, expiredMs);
		} else {
			return "실패";
		}
	}

	@Transactional
	public int changePhone(Member member) {
		return memberDao.changePhone(member);
	}

	@Transactional
	public int delete(String memberId) {
		return memberDao.delete(memberId);
	}

	public int pwCheck(Member member) {
		Member m = memberDao.selectOneMember(member.getMemberId());
		if(m!=null && bCryptPasswordEncoder.matches(member.getMemberPw(), m.getMemberPw())) {
			return 1;
		}
		return 0;
	}

	@Transactional
	public int changePwMember(Member member) {
		return memberDao.changePwMember(member);
	}

	public Map memberList(int reqPage) {
		int totalCount = memberDao.totalCount();
		int numPerPage = 10;
		int pageNaviSize = 5;
		PageInfo pi = pagination.getPageInfo(reqPage, numPerPage, pageNaviSize, totalCount);
		List memberList = memberDao.memberList(pi);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", memberList);
		map.put("pi", pi);
		return map;
	}

	@Transactional
	public int changeType(Member member) {
		return memberDao.changeType(member);
	}
	
}
