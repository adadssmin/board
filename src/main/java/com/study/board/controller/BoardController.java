package com.study.board.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.nexacro17.xapi.data.DataSet;
import com.nexacro17.xapi.data.PlatformData;
import com.nexacro17.xapi.tx.HttpPlatformResponse;
import com.nexacro17.xapi.tx.PlatformException;
import com.nexacro17.xapi.tx.PlatformType;
import com.study.board.service.BoardService;
import com.tobesoft.platform.PlatformRequest;
import com.tobesoft.platform.PlatformResponse;
import com.tobesoft.platform.data.ColumnInfo;
import com.tobesoft.platform.data.Dataset;
import com.tobesoft.platform.data.DatasetList;
import com.tobesoft.platform.data.VariableList;

@Controller
public class BoardController {
	@Resource(name = "service") // 인터페이스로 연결
	private BoardService boardService;

	@RequestMapping("list")
	public String list(@RequestParam Map<String, Object> map, Model model) {
		List<Map<String, Object>> list = boardService.list(map);
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
	
	private static final String FILEPATH = "C:\\Users\\dev\\Desktop\\ssm\\java\\workspaceSTS\\board\\src\\main\\webapp\\resources\\image\\";
	@RequestMapping("insert")
	public String insert(@RequestParam Map<String, Object> map
						, MultipartHttpServletRequest mRequest) throws IllegalStateException, IOException {
		int seq = boardService.seq();
		map.put("seq", seq);
		
		File dir = new File(FILEPATH);
		if(dir.exists() == false){
			dir.mkdirs();
		}
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
	public String excel(@RequestParam Map<String, Object> map, HttpServletResponse response, Model model) throws IOException, ParseException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = boardService.excelList(map);

		model.addAttribute("list", list);
		return "excelview";
	}
	
	@RequestMapping("connMi")
	public void connect(Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		List<Map<String, Object>> list = boardService.excelList(map);
		
		Dataset javaDs = new Dataset("javaDs");
		
		javaDs.addColumn("seq", ColumnInfo.COLUMN_TYPE_INT, 500);
		javaDs.addColumn("memName", ColumnInfo.COLUMN_TYPE_STRING, 500);
		javaDs.addColumn("memId", ColumnInfo.COLUMN_TYPE_STRING, 500);
		javaDs.addColumn("boardSubject", ColumnInfo.COLUMN_TYPE_STRING, 500);
		javaDs.addColumn("boardContent", ColumnInfo.COLUMN_TYPE_STRING, 500);
		javaDs.addColumn("regDate", ColumnInfo.COLUMN_TYPE_DATE, 500);
		javaDs.addColumn("uptDate", ColumnInfo.COLUMN_TYPE_DATE, 500);
		javaDs.addColumn("viewCnt", ColumnInfo.COLUMN_TYPE_INT, 500);
		
		for (int i = 0; i < list.size(); i++) {
			int row = javaDs.appendRow();
			javaDs.setColumn(row, "seq", list.get(i).get("seq").toString());
			javaDs.setColumn(row, "memName", list.get(i).get("memName").toString());
			javaDs.setColumn(row, "memId", list.get(i).get("memId").toString());
			javaDs.setColumn(row, "boardSubject", list.get(i).get("boardSubject").toString());
			if (list.get(i).get("boardContent") == null) {
				javaDs.setColumn(row, "boardContent", "");
			} else {
				javaDs.setColumn(row, "boardContent", list.get(i).get("boardContent").toString());
			}
			javaDs.setColumn(row, "regDate", list.get(i).get("regDate").toString());
			if (list.get(i).get("uptDate") == null) {
				javaDs.setColumn(row, "uptDate", "");
			} else {
				javaDs.setColumn(row, "uptDate", list.get(i).get("uptDate").toString());
			}
			javaDs.setColumn(row, "viewCnt", list.get(i).get("viewCnt").toString());
		}
		
		DatasetList dsl = new DatasetList();
		VariableList vl = new VariableList();
		
		dsl.add(javaDs);
		
		PlatformResponse pRes = new PlatformResponse(response, PlatformRequest.XML, "UTF-8");
		pRes.sendData(vl, dsl);
	}
	
	@RequestMapping("connNex")
	public void connectNex(Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) throws IOException, PlatformException {
		List<Map<String, Object>> list = boardService.excelList(map);
		
		PlatformData pData = new PlatformData();
		DataSet ds = new DataSet("ds");
		ds.addColumn("seq", ColumnInfo.COLUMN_TYPE_INT, 500);
		ds.addColumn("memName", ColumnInfo.COLUMN_TYPE_STRING, 500);
		ds.addColumn("memId", ColumnInfo.COLUMN_TYPE_STRING, 500);
		ds.addColumn("boardSubject", ColumnInfo.COLUMN_TYPE_STRING, 500);
		ds.addColumn("boardContent", ColumnInfo.COLUMN_TYPE_STRING, 500);
		ds.addColumn("regDate", ColumnInfo.COLUMN_TYPE_STRING, 500);
		ds.addColumn("uptDate", ColumnInfo.COLUMN_TYPE_STRING, 500);
		ds.addColumn("viewCnt", ColumnInfo.COLUMN_TYPE_INT, 500);
		
		for (int i = 0; i < list.size(); i++) {
			int row = ds.newRow();
			ds.set(row, "seq", list.get(i).get("seq").toString());
			ds.set(row, "memName", list.get(i).get("memName").toString());
			ds.set(row, "memId", list.get(i).get("memId").toString());
			ds.set(row, "boardSubject", list.get(i).get("boardSubject").toString());
			if (list.get(i).get("boardContent") == null) {
				ds.set(row, "boardContent", "");
			} else {
				ds.set(row, "boardContent", list.get(i).get("boardContent").toString());
			}
			ds.set(row, "regDate", list.get(i).get("regDate").toString());
			if (list.get(i).get("uptDate") == null) {
				ds.set(row, "uptDate", "");
			} else {
				ds.set(row, "uptDate", list.get(i).get("uptDate").toString());
			}
			ds.set(row, "viewCnt", list.get(i).get("viewCnt").toString());
		}
		pData.addDataSet(ds);
		
		HttpPlatformResponse res = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML,"UTF-8");
		res.setData(pData);
		res.sendData();
	}
}