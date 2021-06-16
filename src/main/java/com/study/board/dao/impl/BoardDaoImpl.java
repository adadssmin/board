package com.study.board.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.study.board.dao.BoardDao;

@Repository("dao")
public class BoardDaoImpl implements BoardDao{
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map<String, Object>> list(Map<String, Object> map) {
		return sqlSession.selectList("mapper.listPage", map);
	}
	
	@Override
	public int count(Map<String, Object> map) {
		return sqlSession.selectOne("mapper.count", map);
	}
	
	@Override
	public int insert(Map<String, Object> map) {
		return sqlSession.insert("mapper.write", map);
	}

	@Override
	public Map<String, Object> view(int seq) {
		return sqlSession.selectOne("mapper.view", seq);
	}

	@Override
	public int update(Map<String, Object> map) {
		return sqlSession.update("mapper.update", map);
	}

	@Override
	public void updateCnt(int seq) {
		sqlSession.update("mapper.cnt", seq);
	}

	@Override
	public int delete(List<Integer> list) {
		return sqlSession.delete("mapper.delete", list);
	}
}
