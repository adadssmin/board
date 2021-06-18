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
	
	var i = 0;
	$("#fileAddBtn").click(function() {
		var fileTag = "<div>"
					+ "<input type='file' name='file"+i+"' id='file"+i+"' accept='image/*' onchange='fncFileCheck(this)'>"
					+ "<input type='button' name='fileDelete' id='fileDelete' value='삭제'>"
					+ "<div id='file"+i+"'></div>"
					+ "</div>";
		$("#fileDv").append(fileTag);
		i++;
	})
	
	$(document).on("click", "[name='fileDelete']", function(e){
		$(this).parent().remove();
	})
})
	function fncFileCheck(file) {
	    var ext = $(file).val().split(".").pop().toLowerCase();
	    
	    if($.inArray(ext,["gif","jpg","jpeg","png","bmp"]) == -1) {
	        alert("gif, jpg, jpeg, png, bmp 파일만 업로드 해주세요.");
	        $(file).val("");
	        return;
	    } else {
		    var file1  = file.files[0];
		    var _URL = window.URL || window.webkitURL;
		    var img = new Image();
		    
		    img.src = _URL.createObjectURL(file1);
		    img.onload = function() {
		        if(img.width > 500 || img.height > 500) {
		            alert("이미지 가로 500px, 세로 500px로 맞춰서 올려주세요.");
		            $(file).val("");
		            $(file).parent().remove();
		        }
		    }
	    }
	    var reader = new FileReader(); 
		reader.onload = function(event) {
			var img = document.createElement("img"); 
			img.setAttribute("src", event.target.result); 
			document.querySelector("div#"+$(file).attr('id')).appendChild(img); 
		}; 
		reader.readAsDataURL(event.target.files[0]);
	}
	</script>
</head>
<body>
	<div class="write">
		<form name = "frm" id = "frm" enctype="multipart/form-data">
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
				<br>
				<input type="button" name="fileAddBtn" id="fileAddBtn" value="파일추가">
				<input type="button" name="uploadBtn" id="uploadBtn" value="업로드">
				<br>
				<br>
				<div id="fileDv">
				</div>
				<br>
				<button name="regBtn" id="regBtn">등록</button>
			</c:if>
			<c:if test="${ not empty view }">
				<c:forEach items="${ fileDown }" var="file">
					<input type="hidden" name="fileSeq" id="fileSeq">
					<div>
					<a href="fileDownload?saveName=${ file.saveName }&realName=${ file.realName }" id="${ file.fileseq }">
					<img src="${pageContext.request.contextPath}/resources/image/${file.saveName}">
					${ file.realName }</a>
					</div>
				</c:forEach>
				<br>
				<button name="modBtn" id="modBtn">수정</button>
				<input type="hidden" name="seq" id="seq" value="${ view.seq }">
			</c:if>
			<input type="button" name="back" id="back" onclick="history.go(-1)" value="취소">
		</form>
	</div>
</body>
</html>