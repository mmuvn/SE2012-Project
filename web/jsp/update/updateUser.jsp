<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for editing an existing user account. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update User</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260413-2">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Update user</h1>
                <p>Adjust account details for the selected user record.</p>

                <form action="userjsp" method="post" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="userID">User ID</label>
                        <input type="text" name="userID" id="userID" required value="${user.userID}" placeholder="Enter user ID" readonly>
                        </div>
                        <div class="admin-field">
                            <label for="fullName">Full name</label>
                        <input type="text" name="fullName" id="fullName" value="${user.fullName}" placeholder="Enter full name" readonly>
                        </div>
                        <div class="admin-field">
                            <label for="password">Password</label>
                            <div class="password-toggle-row">
                                <input type="password" name="password" id="password" value="${user.password}" placeholder="Enter password">
                                <button type="button" class="btn btn-ghost btn-small password-toggle" aria-label="Show password" aria-pressed="false" onclick="togglePassword()">
                                    Show
                                </button>
                            </div>
                        </div>
                        <div class="admin-field">
                            <label for="roleID">Role</label>
                            <c:choose>
                                <c:when test="${user.roleID == 1}">
                                    <select name="roleIDDisplay" id="roleID" disabled>
                                        <c:forEach var="role" items="${roles}">
                                            <option value="${role.roleID}" ${role.roleID == user.roleID ? 'selected' : ''}>${role.roleName}</option>
                                        </c:forEach>
                                    </select>
                                    <input type="hidden" name="roleID" value="${user.roleID}">
                                    <small class="form-hint">Admin accounts keep the admin role.</small>
                                </c:when>
                                <c:otherwise>
                                    <select name="roleID" id="roleID">
                                        <c:forEach var="role" items="${roles}">
                                            <option value="${role.roleID}" ${role.roleID == user.roleID ? 'selected' : ''}>${role.roleName}</option>
                                        </c:forEach>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="admin-field full">
                            <label for="address">Address</label>
                        <input type="text" name="address" id="address" value="${user.address}" placeholder="Enter address">
                        </div>
                        <div class="admin-field">
                            <label for="phone">Phone</label>
                        <input type="text" name="phone" id="phone" value="${user.phone}" placeholder="Enter phone number">
                        </div>
                        <div class="admin-field">
                            <label for="email">Email</label>
                        <input type="text" name="email" id="email" value="${user.email}" placeholder="Enter email address" readonly>
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Update user">
                        <a class="btn btn-ghost" href="admin?service=userManager">Cancel</a>
                        <input type="hidden" name="service" value="updateUser">
                    </div>
                </form>
            </section>
        </div>
        <script>
            function togglePassword() {
                const passwordInput = document.getElementById("password");
                const toggleButton = document.querySelector(".password-toggle");
                const showing = passwordInput.type === "text";

                passwordInput.type = showing ? "password" : "text";
                toggleButton.textContent = showing ? "Show" : "Hide";
                toggleButton.setAttribute("aria-label", showing ? "Show password" : "Hide password");
                toggleButton.setAttribute("aria-pressed", showing ? "false" : "true");
            }
        </script>
    </body>
</html>
