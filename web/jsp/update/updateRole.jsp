<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for editing an existing role. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Role</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260322-3">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Update role</h1>
                <p>Edit the selected role entry.</p>

                <form action="rolejsp" method="post" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="roleID">Role ID</label>
                        <input type="text" name="roleID" id="roleID" value="${p.roleID}" placeholder="Enter role ID">
                        </div>
                        <div class="admin-field">
                            <label for="roleName">Role name</label>
                        <input type="text" name="roleName" id="roleName" value="${p.roleName}" placeholder="Enter role name">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Update role">
                        <a class="btn btn-ghost" href="admin?service=roleManager">Cancel</a>
                        <input type="hidden" name="service" value="updateRole">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
