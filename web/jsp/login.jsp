<%-- 
    Document   : login
    Created on : Mar 10, 2026, 10:38:25 AM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Login page for storefront users. Error message only appears when credentials are incorrect. --%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260323-2">
    </head>
    <body>
        <div class="auth-wrapper">
            <section class="auth-card">
                <span class="brand-mark">HE200133</span>
                <h1>Welcome back</h1>
                <p>Sign in to continue shopping and manage your cart.</p>

                <form action="indexjsp" method="post" class="auth-form">
                    <div class="field-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${param.email}" placeholder="Enter your email address" required>
                    </div>

                    <div class="field-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    </div>

                    <input type="submit" value="Login">
                    <input type="hidden" name="service" value="login">
                </form>

                <c:if test="${not empty mess}">
                    <div class="message message-error">${mess}</div>
                </c:if>

                <div class="auth-links">
                    <a class="btn btn-secondary" href="indexjsp?service=register">Create account</a>
                    <a class="btn btn-ghost" href="indexjsp">Back to shop</a>
                </div>
            </section>
        </div>
    </body>
</html>
