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
	public int seq() {
		return sqlSession.selectOne("mapper.seq");
	}
	
	@Override
	public int insert(Map<String, Object> map) {
		return sqlSession.insert("mapper.insert", map);
	}
	
	@Override
	public int fileInsert(Map<String, Object> fileMap) {
		return sqlSession.insert("mapper.fileInsert", fileMap);
	}
	
	@Override
	public List<Map<String, Object>> fileRead(int seq) {
		return sqlSession.selectList("mapper.fileRead", seq);
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
		sqlSession.delete("mapper.deletefile", list);
		return sqlSession.delete("mapper.delete", list);
	}

	@Override
	public List<Map<String, Object>> excelList(Map<String, Object> map) {
		return sqlSession.selectList("mapper.excelList", map);
	}
}
