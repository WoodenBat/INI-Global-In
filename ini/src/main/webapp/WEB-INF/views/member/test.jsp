<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style type="text/css">
#main {
	width: 1440px;
	height: 100%;
	background-color: green;
	margin: auto;
}
</style>
<%@include file="../header.jsp"%>
<body>
	<div id="main">
		<p>${members.user_id}
		<p>${members}
	</div>
</body>
<%@include file="../footer.jsp"%>
</html>