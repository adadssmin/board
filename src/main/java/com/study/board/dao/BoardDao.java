package com.study.board.dao;

import java.util.List;
import java.util.Map;

public interface BoardDao {
	List<Map<String, Object>> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int insert(Map<String, Object> map);

	Map<String, Object> view(int seq);

	int update(Map<String, Object> map);

	void updateCnt(int seq);

	int delete(List<Integer> list);
}
