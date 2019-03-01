<!-- 
Shivam Agarwal
shivamag@buffalo.edu
UBIT 50289132
 -->
<html>

<script type="text/javascript">
function login(){
	var accno = document.getElementById('accountNo').value; 
	var pass = document.getElementById('pass').value;
   	
	if(accno.trim() != "" && pass.trim() != ""){
   		document.getElementById('loginForm').submit();
   	}else{
   		alert('please enter valid data');
   	}
}

function createAccount(){
	var name = document.getElementById('name').value;
	var pass = document.getElementById('password').value;
	
	if(name.trim() != "" && pass.trim() != ""){
   		document.getElementById('createAcc').submit();
   	}else{
   		alert('please enter valid data');
   	}
}
 </script>

<body>

	<jsp:include page="/title.jsp"></jsp:include>
	<br>
	<br>
	
	<table width = 100%>
		<tr>
			<td>
				<form name="loginform" id="loginForm" action="login">
					<table width=100%>
						<thead>
							<tr>
								<h1>Login</h1>
							</tr>
							
						</thead>
						<tbody>
							<tr>
								<td>Account Number : </td>
								<td><input type="text" id="accountNo" name="accno" /></td>
							</tr>
							<tr>
								<td>Password : </td>
								<td><input type="text" id="pass" name="pass" /></td>
							</tr>
							<tr>
								<td>
									<input type="button" name="signIn" value="Sign In" onclick="javascript:login();" />
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
					<%-- <img src="<c:url value="${login.png}"/>"/> --%>
				</form>
			</td>
			<td>
				<form action="createAcc" id="createAcc" name="createAcc">
					<table width=100%>
						<thead>
							<tr>
								<h1>Create Account</h1>
							</tr>
							
						</thead>
						<tbody>
							<tr>
								<td>Name : </td>
								<td><input type="text" name="name" id="name" /></td>
							</tr>
							<tr>
								<td>Password : </td>
								<td><input type="text" name="password" id="password"/></td>
							</tr>
							<tr>
								<td>
									<input type="button" name="createAcc" value="Create Account" onclick="javascript:createAccount();" />
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</form>
			</td>
		</tr>
	</table>
</body>
</html>
