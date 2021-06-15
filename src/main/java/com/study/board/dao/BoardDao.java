package com.study.board.dao;

import java.util.List;
import java.util.Map;

public interface BoardDao {
	int insert(Map<String, Object> map);

	Map<String, Object> view(int seq);

	int update(Map<String, Object> map);

	void updateCnt(int seq);

	int delete(List<Integer> list);

	int totalCnt();

	List<Map<String, Object>> listPage(Map<String, Object> map);
	
	int count(Map<String, Object> map);
}
