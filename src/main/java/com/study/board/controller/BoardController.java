package com.study.board.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.study.board.service.BoardService;

@Controller
public class BoardController {
	@Resource(name = "service") // 인터페이스로 연결
	private BoardService boardService;

	@RequestMapping("list")
	public String list(@RequestParam Map<String, Object> map, Model model) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = boardService.list(map);
		Map<String, Object> pageMap = boardService.pageService(map);

		model.addAttribute("list", list);
		model.addAttribute("pageMap", pageMap);
		model.addAttribute("map", map);

		return "board/list";
	}

	@RequestMapping("search")
	public String search(@RequestParam Map<String, Object> map, Model model) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = boardService.list(map);
		Map<String, Object> pageMap = boardService.pageService(map);

		model.addAttribute("list", list);
		model.addAttribute("pageMap", pageMap);
		model.addAttribute("map", map);

		return "board/search";
	}

	@RequestMapping("write")
	public String write() {
		return "board/write";
	}
	
	private static final String FILEPATH = "C:\\Users\\dev\\Desktop\\ssm\\img\\";
	
	@RequestMapping("insert")
	public String insert(@RequestParam Map<String, Object> map
						, MultipartHttpServletRequest mRequest) throws IllegalStateException, IOException {
		int seq = boardService.seq();
		map.put("seq", seq);
		
		File dir = new File(FILEPATH);
		if(dir.exists() == false){
			dir.mkdirs();
		}
		
		//getFileNames(단일 파일)
		//getFiles(다중 파일)
		
		Iterator<String> iterator = mRequest.getFileNames();
		while(iterator.hasNext()){
			String realName;
			String saveName;
			
			MultipartFile mFile= mRequest.getFile(iterator.next());
			if(mFile.getSize() > 0) {
				UUID one = UUID.randomUUID();
				realName = mFile.getOriginalFilename();
				saveName = one.toString() + "_" + realName;
				mFile.transferTo(new File(FILEPATH + saveName));
				
				Map<String, Object> fileMap = new HashMap<String, Object>();
				fileMap.put("realName", realName);
				fileMap.put("saveName", saveName);
				fileMap.put("filePath", FILEPATH);
				fileMap.put("listSeq", seq);
				
				int fileInsert = boardService.fileInsert(fileMap);
			}
		}

		int insert = boardService.insert(map);
		
		return "redirect:list";
	}
	// forward는 그 페이지 이름 그대로 넘어감(insert로 넘어감)

	@RequestMapping("view")
	public String view(@RequestParam("seq") int seq, Model model) {
		boardService.updateCnt(seq); // 조회수는 페이지에서 새로고침하면 계속먹힘! 이걸 처리해줘야함!!
		
		Map<String, Object> view = boardService.view(seq);
		List<Map<String, Object>> fileDown = boardService.fileRead(seq);
		model.addAttribute("view", view);
		model.addAttribute("fileDown", fileDown);
		return "board/write";
	}
	// map은 무조건 @RequestParam 붙여야함
	// VO는 @ModelAttribute VO vo 로 사용한다
	
	@RequestMapping("fileDownload")
	public void fileDownload(@RequestParam String saveName
							, @RequestParam String realName
							, HttpServletResponse response) throws IOException {
		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File(FILEPATH + saveName));
		
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition",  "attachment; fileName=\""
							+URLEncoder.encode(realName, "UTF-8")+"\";");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	@RequestMapping("update")
	public String update(@RequestParam Map<String, Object> map) {
		int update = boardService.update(map);
		return "redirect:list";
	}

	@RequestMapping("delete")
	public String delete(Integer[] chk) {
		List<Integer> list = Arrays.asList(chk);
		int delete = boardService.delete(list);
		return "redirect:list";
	}
	
	@RequestMapping("excel")
	public void excel(@RequestParam Map<String, Object> map, HttpServletResponse response) throws IOException, ParseException {
		Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;
        List<Map<String, Object>> excelList = boardService.excelList(map);
        
        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("");
        cell = row.createCell(1);
        cell.setCellValue("글번호");
        cell = row.createCell(2);
        cell.setCellValue("작성자");
        cell = row.createCell(3);
        cell.setCellValue("제목");
        cell = row.createCell(4);
        cell.setCellValue("내용");
        cell = row.createCell(5);
        cell.setCellValue("작성일");
        cell = row.createCell(6);
        cell.setCellValue("수정일");
        cell = row.createCell(7);
        cell.setCellValue("조회수");
        
        // Body
        for (Map<String, Object> excel : excelList) {
        	String seq = excel.get("seq") + "";
        	String memName = excel.get("memName") + "";
        	String boardSubject = excel.get("boardSubject") + "";
        	String boardContent = excel.get("boardContent") + "";
        	String regDate = excel.get("regDate") + "";
        	String uptDate = excel.get("uptDate") + "";
        	String viewCnt = excel.get("viewCnt") + "";
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue("");
            cell = row.createCell(1);
            cell.setCellValue((String) seq);
            cell = row.createCell(2);
            cell.setCellValue((String) memName);
            cell = row.createCell(3);
            cell.setCellValue((String) boardSubject);
            cell = row.createCell(4);
            cell.setCellValue((String) boardContent);
            cell = row.createCell(5);
            cell.setCellValue((String) regDate);
            cell = row.createCell(6);
            cell.setCellValue((String) uptDate);
            cell = row.createCell(7);
            cell.setCellValue((String) viewCnt);
        }
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (map.get("keyword") != "") {
        	String keyword = map.get("keyword") + "";
        	response.setHeader("Content-Disposition", "attachment;filename=" 
        						+ sdf.format(cal.getTime()) 
        						+ "_keyword_" 
        						+ keyword
        						+ "_excel_file.xls");
        } else if (map.get("date1") != "" && map.get("date2") != "") {
        	String date1 = map.get("date1") + "";
        	String date2 = map.get("date2") + "";
        	response.setHeader("Content-Disposition", "attachment;filename=" 
					+ date1 + "-" + date2 
					+ "_excel_file.xls");
        } else {
        	response.setHeader("Content-Disposition", "attachment;filename=" 
					+ sdf.format(cal.getTime()) 
					+ "_All_excel_file.xls");
        }
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
	}
}