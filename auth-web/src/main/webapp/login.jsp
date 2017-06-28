<html>
<head>
    <title>LGOIN</title>
    <script>
        window.onload=function(){
            document.loginForm.action += window.location.search;
        }
    </script>
</head>
<body>
<center>
    <form action="login.jsp" method="post" name="loginForm">
        username: <input type="text" name="username" /><br/>
        password: <input type="password" name="password"><br/>
        auto login: <input type="checkbox" name="rememberMe" value="true"><br/>
        <input type="submit" value="OK">
    </form>
</center>
</body>
</html>