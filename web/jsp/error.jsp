<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Something went wrong</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260323-1">
    </head>
    <body>
        <div class="auth-wrapper">
            <section class="auth-card">
                <span class="brand-mark">ERROR</span>
                <h1>Something went wrong</h1>
                <p>The request could not be completed. Please go back and try again.</p>

                <c:if test="${not empty pageContext.exception}">
                    <div class="message message-error">
                        ${pageContext.exception.message}
                    </div>
                </c:if>

                <div class="auth-links">
                    <a class="btn btn-primary" href="javascript:history.back()">Go back</a>
                    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/indexjsp">Back to shop</a>
                </div>
            </section>
        </div>
    </body>
</html>
