package kr.or.iei;

import org.springframework.stereotype.Component;

@Component
public class Pagination {//service에서 받은 데이터를 받아서 페이지네비게이션 만들때 필요한 데이터를 리턴
	public PageInfo getPageInfo(int reqPage, int numPerPage, int pageNaviSize, int totalCount) {
		int end = reqPage*numPerPage;
		int start = end-numPerPage+1;
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		PageInfo pi = new PageInfo(start, end, pageNo, pageNaviSize, totalPage);
		return pi;
	}
}
