<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리스트</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
<script>
	$(function(){
		$( "#date1" ).datepicker({
			dateFormat: 'yy-mm-dd'
		})
		$( "#date2" ).datepicker({
			dateFormat: 'yy-mm-dd'
		})
		
		$("#searchType").val('${map.searchType}');
		$("#keyword").val('${map.keyword}');
		$("#date1").val('${map.date1}');
		$("#date2").val('${map.date2}');
		
		$("#searchBtn").click(function(){
// 			$('#searchFrm').attr("action", "list").attr("method", "post").submit();
// 			curPage=1&pageScale=10&searchType=&keyword=&date1=&date2=
			$.ajax({
				url: 'search',
				type: 'post',
				data: $("#searchFrm").serialize(),
				success : function(map){
	                $(".content").html(map);
	            },
				error: function(xhr, status, error){
	                alert(error);
	            }
			})
		})
		
		$("#title").click(function() {
			alert(this.text());
		})
		
		$("#excelBtn").click(function() {
			$('#searchFrm').attr("action", "excel").attr("method", "post").submit();
		})
	})
	
	function fncGoDelete() {
		var frm = document.listFrm;
		frm.action = "delete";
		frm.method = "post";
		frm.submit();
	}
	
	function list(num) {
		$("#curPage").val(num);
		$("#searchBtn").click();
	}
</script>
</head>
<body>
	<div class="nav">
		<span id="nav1">
		<button class="btn" name="regBtn" id="regBtn" onclick="location.href='write'">등록</button>
		<button class="btn" name="deletebtn" id="deleteBtn" onclick="fncGoDelete()">삭제</button>
		<button class="btn" name="excelBtn" id="excelBtn">엑셀 다운로드</button>
		</span>
		<form name="searchFrm" id="searchFrm">
		<input type="hidden" name="curPage" id="curPage" value="1">
		<input type="hidden" name="pageScale" id="pageScale" value="10">
			<span id="nav2">
					<select name="searchType" id="searchType">
						<option value="">선  택</option>
						<option value="title">제  목</option>
						<option value="content">내  용</option>
						<option value="title_content">제목+내용</option>
						<option value="writer">작성자</option>
					</select>
					<input type="text" name="keyword" id="keyword">
					<input type="text" name="date1" id="date1">
					~
					<input type="text" name="date2" id="date2">
			<input type="button" name="searchBtn" id="searchBtn" value="검색">
			</span>
		</form>
	</div>
	<div class="content">
		<form name="listFrm" id="listFrm">
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
				<tr>
					<td colspan="7">
						 <!-- **처음페이지로 이동 : 현재 페이지가 1보다 크면  [처음]하이퍼링크를 화면에 출력-->
		                <c:if test="${pageMap.curBlock > 1}">
		                    <a href="javascript:list('1')">[처음]</a>
		                </c:if>
		                
		                <!-- **이전페이지 블록으로 이동 : 현재 페이지 블럭이 1보다 크면 [이전]하이퍼링크를 화면에 출력 -->
		                <c:if test="${pageMap.curBlock > 1}">
		                    <a href="javascript:list('${pageMap.prevPage}')">[이전]</a>
		                </c:if>
		                <!-- **하나의 블럭에서 반복문 수행 시작페이지부터 끝페이지까지 -->
		                <c:forEach var="num" begin="${pageMap.blockBegin}" end="${pageMap.blockEnd}">
		                    <!-- **현재페이지이면 하이퍼링크 제거 -->
		                    <c:choose>
		                        <c:when test="${num == pageMap.curPage}">
		                            <span style="color: red">${num}</span>&nbsp;
		                        </c:when>
		                        <c:otherwise>
		                            <a href="javascript:list('${num}')">${num}</a>&nbsp;
		                        </c:otherwise>
		                    </c:choose>
		                </c:forEach>
		                
		                <!-- **다음페이지 블록으로 이동 : 현재 페이지 블럭이 전체 페이지 블럭보다 작거나 같으면 [다음]하이퍼링크를 화면에 출력 -->
		                <c:if test="${pageMap.curBlock <= pageMap.totBlock}">
		                    <a href="javascript:list('${pageMap.nextPage}')">[다음]</a>
		                </c:if>
		                
		                <!-- **끝페이지로 이동 : 현재 페이지가 전체 페이지보다 작거나 같으면 [끝]하이퍼링크를 화면에 출력 -->
		                <c:if test="${pageMap.curPage <= pageMap.totPage}">
		                    <a href="javascript:list('${pageMap.totPage}')">[끝]</a>
		                </c:if>
					</td>
				</tr>
			</table>
		</form>
 	</div>
</body>
</html>