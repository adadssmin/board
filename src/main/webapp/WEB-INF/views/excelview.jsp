<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%	
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	response.setHeader("Content-Disposition", "attachment;filename="
						+ sdf.format(cal.getTime())
						+ "_All_excel_file.xls");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>excel</title>
<style type="text/css">
.date {
	mso-number-format:"yyyy\-mm\-dd";
}
.subcon {
	mso-number-format: "@"; /* String */
	mso-width-source:auto;
}
</style>
</head>
<body>
<table border="1">
	<tr>
		<th></th>
		<th>글번호</th>
		<th>작성자(ID)</th>
		<th>제목</th>
		<th>내용</th>
		<th>작성일</th>
		<th>수정일</th>
		<th>조회수</th>
	</tr>
	<c:forEach items="${ list }" var="list" varStatus="status">
		<tr>
			<td></td>
			<td>${ list.seq }</td>
			<td>${ list.memName }(${ list.memId })</td>
			<td class="subcon">${ list.boardSubject }</td>
			<td class="subcon">${ list.boardContent }</td>
			<td class="date">${ list.regDate }</td>
			<td class="date">${ list.uptDate }</td>
			<td>${ list.viewCnt }</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>