<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<head>
    <title>Error!</title>
</head>
<body>
    <h1>Error while navigating through SecurIA.com</h1>
    <p>
        <h4><% switch (request.getParameter("error")){
            case "login":
                out.println("Login error. Email or password may be incorrect. Try again.");
                break;
            default:
                out.println("Unknown error");
                break;
        }; %></h4>
    </p>
</body>
</html>