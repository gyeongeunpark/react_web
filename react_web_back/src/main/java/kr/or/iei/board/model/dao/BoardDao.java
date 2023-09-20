package kr.or.iei.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.iei.PageInfo;

@Mapper
public interface BoardDao {

	int totalCount();

	List selectBoardList(PageInfo pi);

}
