<!-- 
Shivam Agarwal
shivamag@buffalo.edu
UBIT 50289132
 -->
<html>

<script type="text/javascript">
function purchase(){
	alert("in purchase");
	document.getElementById('login').submit();
}

window.onload = purchase;

</script>


<body>
	<form id="login" action="login"></form>
	<jsp:include page="/WEB-INF/jsp/buypage.jsp"></jsp:include>
</body>
</html>
