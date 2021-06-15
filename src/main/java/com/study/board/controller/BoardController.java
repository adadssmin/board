package com.study.board.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.board.service.BoardService;

@Controller
public class BoardController {
	@Resource(name="service") //인터페이스로 연결
	private BoardService boardService;
	
	@RequestMapping("list")
	public String list(@RequestParam Map<String, Object> map, Model model) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(); 
		list = boardService.list(map);
		Map<String, Object> pageMap = boardService.pageService(map);
		
		model.addAttribute("list", list);
		model.addAttribute("pageMap", pageMap);
		model.addAttribute("map", map);
		return "board/list";
	}
	
	@RequestMapping("write")
	public String write() {
		return "board/write";
	}
	
	@RequestMapping("insert")
	public String insert(@RequestParam Map<String, Object> map) {
		int insert = boardService.insert(map);
		System.out.println(map);
		return "redirect:list";
	}
	//forward는 그 페이지 이름 그대로 넘어감(insert로 넘어감)
	
	@RequestMapping("view")
	public String view(@RequestParam("seq") int seq, Model model) {
		boardService.updateCnt(seq); // 조회수는 페이지에서 새로고침하면 계속먹힘! 이걸 처리해줘야함!!
		
		Map<String, Object> view = boardService.view(seq);
		model.addAttribute("view", view);
		return "board/write";
	}
	//map은 무조건 @RequestParam 붙여야함
	//VO는 @ModelAttribute VO vo 로 사용한다
	
	@RequestMapping("update")
	public String update(@RequestParam Map<String, Object> map) {
		int update = boardService.update(map);
		return "redirect:list";
	}
	
	@RequestMapping("delete")
	public String delete(Integer[] chk) {
		List<Integer> list = Arrays.asList(chk);
		System.out.println("확인용: " + list);
		int delete = boardService.delete(list);
		return "redirect:list";
	}
}