<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for creating a new user account. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add User</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260322-3">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Add user</h1>
                <p>Create a new account and assign role details for the admin system.</p>

                <form action="userjsp" method="post" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="userID">User ID</label>
                        <input type="text" name="userID" id="userID" placeholder="Enter user ID, for example U010" required>
                        </div>
                        <div class="admin-field">
                            <label for="fullName">Full name</label>
                        <input type="text" name="fullName" id="fullName" placeholder="Enter full name">
                        </div>
                        <div class="admin-field">
                            <label for="password">Password</label>
                        <input type="password" name="password" id="password" placeholder="Enter password">
                        </div>
                        <div class="admin-field">
                            <label for="roleID">Role ID</label>
                        <input type="text" name="roleID" id="roleID" placeholder="Enter role ID">
                        </div>
                        <div class="admin-field full">
                            <label for="address">Address</label>
                        <input type="text" name="address" id="address" placeholder="Enter address">
                        </div>
                        <div class="admin-field">
                            <label for="phone">Phone</label>
                        <input type="text" name="phone" id="phone" placeholder="Enter phone number">
                        </div>
                        <div class="admin-field">
                            <label for="email">Email</label>
                        <input type="text" name="email" id="email" placeholder="Enter email address">
                        </div>
                        <div class="admin-field full">
                            <span class="field-label">Account status</span>
                            <div class="radio-group">
                                <label><input type="radio" name="activate" value="true" checked> Active</label>
                                <label><input type="radio" name="activate" value="false"> Inactive</label>
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Add user">
                        <a class="btn btn-ghost" href="admin?service=userManager">Cancel</a>
                        <input type="hidden" name="service" value="addUser">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
