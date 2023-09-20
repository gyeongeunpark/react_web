package kr.or.iei.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.PageInfo;
import kr.or.iei.Pagination;
import kr.or.iei.board.model.dao.BoardDao;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private Pagination pagination;

	public Map boardList(int reqPage) {
		//게시물 조회, 페이징에 필요한 데이터를 취합(페이징 만드는건 react)
		//페이징 하기 전에 무조건 필요한 데이터를 얻기위한 query문을 미리 만들어야함
		int numPerPage = 12; //한페이지 당 게시물 수
		int pageNaviSize = 5;//페이지 네비게이션 길이(페이지 개수)
		int totalCount = boardDao.totalCount();
		//페이징 조회 및 페이지 네비 제작에 필요한 데이터를 객체로 받아옴
		PageInfo pi = pagination.getPageInfo(reqPage, numPerPage, pageNaviSize, totalCount);
		List boardList = boardDao.selectBoardList(pi);
		//java에서는 return이 하나밖에 안되므로 map을 이용해서 보내주기
		//List와 객체를 둘 다 보내야하므로 제일 조상인 Object형태로 보내줌
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("boardList", boardList);
		map.put("pi",pi);
		return map;
	}
	
}
