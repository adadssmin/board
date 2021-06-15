<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>
$(function() {
	$("#regBtn").click(function() {
		$("#frm").attr("action", "insert").attr("method", "post").submit();
	})
	
	$("#modBtn").click(function() {
		$("#frm").attr("action", "update").attr("method", "post").submit();
	})
})
</script>
</head>
<body>
	<div class="write">
		<form name = "frm" id = "frm">
		<div>
		<span class="lbl">작성자</span><input type="text" name="writer" id="writer" value="${ view.memName }"> 
		</div>
		<div>
		<span class="lbl">ID</span><input type="text" name="ID" id="ID" value="${ view.memId }">
		</div>
		<div>
		<span class="lbl">제목</span><input type="text" name="title" id="title" value="${ view.boardSubject }">
		</div>
		<div>
		<span class="lbl">내용</span><br>
		<textarea cols="30" rows="5" name="content" id="content">${ view.boardContent }</textarea>
		</div>
		
		<c:if test="${ empty view }">
			<button name="regBtn" id="regBtn">등록</button>
		</c:if>
		<c:if test="${ not empty view }">
			<button name="modBtn" id="modBtn">수정</button>
			<input type="hidden" name="seq" id="seq" value="${ view.seq }">
		</c:if>
		<input type="button" name="back" id="back" onclick="history.go(-1)" value="취소">
		</form>
	</div>
</body>
</html>