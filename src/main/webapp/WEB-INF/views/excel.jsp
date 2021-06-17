<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta http-equiv="Content-Type"	content="application/vnd.ms-excel;charset=UTF-8">
<%
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	response.setHeader("Content-Disposition", "attachment;filename=" + sdf.format(cal.getTime()) + "_excel_file.xls");
	response.setHeader("Content-Description", "JSP Generated Data");
%>
</head>
<body>
	<table border="1">
		<tr>
			<th></th>
			<th>글번호</th>
			<th>작성자(ID)</th>
			<th>제목</th>
			<th>작성일</th>
			<th>수정일</th>
			<th>조회수</th>
		</tr>
		<c:forEach items="${ list }" var="list" varStatus="status">
			<tr>
				<td><input type="checkbox" name="chk" id="chk${ status.index }" value="${ list.seq }"></td>
				<td>${ list.seq }</td>
				<td>${ list.memName }(${ list.memId })</td>
				<td><a id="title" href="view?seq=${ list.seq }">${ list.boardSubject }</a></td>
				<td>${ list.regDate }</td>
				<td>${ list.uptDate }</td>
				<td>${ list.viewCnt }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>