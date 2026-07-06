<%-- 
    Document   : insertUser
    Created on : Mar 1, 2026, 10:20:07 AM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Registration page for creating a customer account before shopping. --%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260323-2">
    </head>
    <body>
        <div class="auth-wrapper">
            <section class="auth-card">
                <span class="brand-mark">HE200133</span>
                <h1>Create your account</h1>
                <p>Register once, then shop and manage orders more easily.</p>

                <c:if test="${not empty error}">
                    <div class="message message-error">${error}</div>
                </c:if>

                <form action="indexjsp" method="post" class="auth-form form-grid">
                    <div class="field-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${param.email}" placeholder="Enter your email address" required>
                    </div>

                    <div class="field-group">
                        <label for="fullName">Full name</label>
                        <input type="text" id="fullName" name="fullName" value="${param.fullName}" placeholder="Enter your full name" required>
                    </div>

                    <div class="field-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="8+ chars, 1 uppercase, 1 special" required minlength="8" title="Use at least 8 characters, including 1 uppercase letter and 1 special character">
                    </div>

                    <div class="field-group">
                        <label for="address">Address</label>
                        <input type="text" id="address" name="address" value="${param.address}" placeholder="Enter your address" required>
                    </div>

                    <div class="field-group">
                        <label for="phone">Phone</label>
                        <input type="text" id="phone" name="phone" value="${param.phone}" pattern="\d{9,11}" placeholder="Enter 9 to 11 digits" required>
                    </div>

                    <div class="page-actions">
                        <input type="submit" name="submit" value="Register">
                    </div>

                    <input type="hidden" name="service" value="register">
                </form>

                <div class="auth-links">
                    <a class="btn btn-secondary" href="indexjsp?service=login">Already have an account?</a>
                    <a class="btn btn-ghost" href="indexjsp">Back to shop</a>
                </div>
            </section>
        </div>
    </body>
</html>
