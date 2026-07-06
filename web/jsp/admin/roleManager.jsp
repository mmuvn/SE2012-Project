<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Admin role manager fragment: maintains the available role records used by user accounts. --%>

<div class="content-stack">
    <div class="section-heading">
        <div>
            <h2>Role manager</h2>
            <p>${fn:length(roles)} role record(s) shown</p>
        </div>
        <a class="btn btn-primary" href="rolejsp?service=addRole">Add role</a>
    </div>

    <section class="search-panel">
        <h2>Search roles</h2>
        <form action="admin#manager-panel" method="get" class="search-form admin-search-form">
            <div class="field-group">
                <input type="text" name="roleName" value="${param.roleName}" placeholder="Search by role name">
            </div>
            <input type="submit" value="Search">
            <input type="hidden" name="service" value="roleManager">
        </form>
    </section>

    <div class="admin-table-wrap admin-table-scroll">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Role ID</th>
                    <th>Role name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="role" items="${roles}">
                    <tr>
                        <td class="inline-code">${role.roleID}</td>
                        <td>${role.roleName}</td>
                        <td>
                            <div class="admin-action-group">
                                <a class="btn btn-danger btn-small" href="rolejsp?service=deleteRole&pId=${role.roleID}">Delete</a>
                                <a class="btn btn-secondary btn-small" href="rolejsp?service=updateRole&pId=${role.roleID}">Update</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
