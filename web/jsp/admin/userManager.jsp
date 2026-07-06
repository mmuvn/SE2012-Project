<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Admin user manager fragment: lists user accounts with search and update/delete actions. --%>

<div class="content-stack">
    <div class="section-heading">
        <div>
            <h2>User manager</h2>
            <p>${fn:length(users)} user(s) shown</p>
        </div>
    </div>

    <section class="search-panel">
        <h2>Search users</h2>
        <form action="admin#manager-panel" method="get" class="search-form admin-search-form">
            <div class="field-group">
                <input type="text" name="fullName" value="${param.fullName}" placeholder="Search by full name">
            </div>
            <input type="submit" value="Search">
            <input type="hidden" name="service" value="userManager">
        </form>
    </section>

    <div class="admin-table-wrap admin-table-scroll">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Full name</th>
                    <th>Password</th>
                    <th>Role</th>
                    <th>Address</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Num of Orders</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td class="inline-code">${user.userID}</td>
                        <td>${user.fullName}</td>
                        <td>
                            <div class="password-toggle-row admin-password-cell">
                                <input type="password" class="table-password-input" value="${user.password}" readonly>
                                <button type="button" class="btn btn-ghost btn-small password-toggle" onclick="toggleTablePassword(this)">Show</button>
                            </div>
                        </td>
                        <td>
                            <c:forEach var="role" items="${roles}">
                                <c:if test="${role.roleID == user.roleID}">
                                    ${role.roleName}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td class="admin-text-cell">${user.address}</td>
                        <td>${user.phone}</td>
                        <td>${user.email}</td>
                        <td>${empty orderCounts[user.userID] ? 0 : orderCounts[user.userID]}</td>
                        <td>
                            <span class="status-pill ${user.activate ? 'status-active' : 'status-inactive'}">
                                ${user.activate ? 'Active' : 'Inactive'}
                            </span>
                        </td>
                        <td>
                            <div class="admin-action-group">
                                <a class="btn ${user.activate ? 'btn-danger' : 'btn-primary'} btn-small" href="userjsp?service=toggleUserStatus&pId=${user.userID}">
                                    ${user.activate ? 'Deactivate' : 'Activate'}
                                </a>
                                <a class="btn btn-secondary btn-small" href="userjsp?service=updateUser&pId=${user.userID}">Update</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    function toggleTablePassword(button) {
        const input = button.parentElement.querySelector(".table-password-input");
        const isHidden = input.type === "password";
        input.type = isHidden ? "text" : "password";
        button.textContent = isHidden ? "Hide" : "Show";
    }
</script>
