<%-- 
    Document   : index
    Created on : Mar 10, 2026, 3:19:46 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Main storefront page: shows hero banner, categories, search, product grid, and pagination. --%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>HE200133 Shop</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260414-1">
    </head>
    <body>
        <div class="site-shell">
            <header class="hero-card">
                <div class="hero-copy">
                    <span class="brand-mark">HE200133</span>
                    <h1>Electronic shop for everyday tech.</h1>
                    <p>Browse by category, search quickly, and explore devices, accessories, and home electronics in one place.</p>
                </div>
                <div class="hero-actions">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <span class="welcome-pill">Welcome ${sessionScope.user.fullName}</span>
                            <a class="btn btn-ghost" href="indexjsp?service=logOut" onclick="return confirm('Are you sure you want to log out');">Log out</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-ghost" href="indexjsp?service=login">Login</a>
                            <a class="btn btn-primary" href="indexjsp?service=register">Register</a>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty sessionScope.user and sessionScope.user.roleID == 1}">
                        <a class="btn btn-secondary" href="admin">Admin Dashboard</a>
                    </c:if>
                    <c:if test="${not empty sessionScope.user}">
                        <a class="btn btn-ghost" href="indexjsp?service=orderHistory">Order History</a>
                        <a class="btn btn-primary" href="cart?service=showCart">Show Cart</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty bestSellers}">
                <section class="panel feature-panel">
                    <div class="section-heading">
                        <div>
                            <h2>Best sellers</h2>
                            <p>Popular picks from customers.</p>
                        </div>
                    </div>
                    <div class="product-grid product-grid-compact">
                        <c:forEach var="product" items="${bestSellers}">
                            <c:set var="displayName" value="${empty product.productName ? 'Product' : product.productName}" />
                            <article class="product-card">
                                <a class="product-link" href="indexjsp?service=productDetail&pId=${product.productID}">
                                    <div class="product-visual">
                                        <div>
                                            <img class="product-image" src="${pageContext.request.contextPath}/images/${product.image}" alt="${displayName}" onerror="this.style.display='none'; this.nextElementSibling.style.display='grid';">
                                            <div class="product-initial product-fallback">${fn:substring(displayName, 0, 1)}</div>
                                        </div>
                                    </div>
                                </a>
                                <div>
                                    <h3><a class="product-title-link" href="indexjsp?service=productDetail&pId=${product.productID}">${displayName}</a></h3>
                                    <p class="product-meta">Sold: ${product.totalSold}</p>
                                </div>
                                <div class="product-footer">
                                    <div>
                                        <div class="product-price">$${product.price}</div>
                                        <p class="product-meta ${product.quantity <= 0 or product.status == 0 ? 'stock-sold-out' : ''}">
                                            <c:choose>
                                                <c:when test="${product.quantity <= 0 or product.status == 0}">Sold out.</c:when>
                                                <c:otherwise>Stock: ${product.quantity}</c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                    <div class="product-actions">
                                        <a class="btn btn-ghost" href="indexjsp?service=productDetail&pId=${product.productID}">View details</a>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <main class="store-layout" style="margin-top: 24px;">
                <aside class="category-panel sticky-sidebar">
                    <h2>Categories</h2>
                    <div class="category-list">
                        <a class="category-link ${empty currentCategoryId ? 'is-active' : ''}" href="indexjsp">All products</a>
                        <c:forEach var="category" items="${categories}">
                            <a class="category-link ${currentCategoryId eq category.categoryID ? 'is-active' : ''}" href="indexjsp?service=showItem&cId=${category.categoryID}">${category.categoryName}</a>
                        </c:forEach>
                    </div>
                </aside>

                <section class="content-stack">
                    <c:if test="${not empty cartMessage}">
                        <div class="message ${cartMessageType == 'success' ? 'message-success' : 'message-error'}">${cartMessage}</div>
                    </c:if>

                    <div class="search-panel sticky-search">
                        <h2>Find what you need</h2>
                        <form action="indexjsp" method="get" class="search-form">
                            <div class="field-group">
                                <input type="text" id="productName" name="productName" value="${currentSearchKeyword}" placeholder="Search for product">
                            </div>
                            <input type="submit" name="submit" value="Search">
                            <input type="hidden" name="service" value="listOfProduct">
                        </form>
                    </div>

                    <section class="panel">
                        <div class="section-heading">
                            <div>
                                <h2>Products</h2>
                                <p>${totalProductCount} item(s) available</p>
                            </div>
                        </div>

                        <c:choose>
                            <c:when test="${not empty products}">
                                <div class="product-grid">
                                    <c:forEach var="product" items="${products}">
                                        <c:set var="displayName" value="${empty product.productName ? 'Product' : product.productName}" />
                                        <article class="product-card">
                                            <a class="product-link" href="indexjsp?service=productDetail&pId=${product.productID}">
                                                <div class="product-visual">
                                                    <div>
                                                        <img class="product-image" src="${pageContext.request.contextPath}/images/${product.image}" alt="${displayName}" onerror="this.style.display='none'; this.nextElementSibling.style.display='grid';">
                                                        <div class="product-initial product-fallback">${fn:substring(displayName, 0, 1)}</div>
                                                    </div>
                                                </div>
                                            </a>
                                            <div>
                                                <h3><a class="product-title-link" href="indexjsp?service=productDetail&pId=${product.productID}">${displayName}</a></h3>
                                            </div>
                                            <div class="product-footer">
                                                <div>
                                                    <div class="product-price">$${product.price}</div>
                                                    <p class="product-meta ${product.quantity <= 0 or product.status == 0 ? 'stock-sold-out' : ''}">
                                                        <c:choose>
                                                            <c:when test="${product.quantity <= 0 or product.status == 0}">Sold out.</c:when>
                                                            <c:otherwise>Stock: ${product.quantity}</c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                                <div class="product-actions">
                                                    <a class="btn btn-ghost" href="indexjsp?service=productDetail&pId=${product.productID}">View details</a>
                                                    <c:choose>
                                                        <c:when test="${product.quantity <= 0 or product.status == 0}">
                                                            <span class="btn btn-disabled">Sold out</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="btn btn-primary" href="cart?service=add2Cart&pId=${product.productID}" onclick="return confirm('Add to cart?');">Add to cart</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </article>
                                    </c:forEach>
                                </div>

                                <c:if test="${totalPages > 1}">
                                    <div class="pagination">
                                        <c:if test="${currentPage > 1}">
                                            <c:url var="prevPageUrl" value="indexjsp">
                                                <c:param name="service" value="${currentService}" />
                                                <c:if test="${not empty currentCategoryId}">
                                                    <c:param name="cId" value="${currentCategoryId}" />
                                                </c:if>
                                                <c:if test="${not empty currentSearchKeyword}">
                                                    <c:param name="productName" value="${currentSearchKeyword}" />
                                                    <c:param name="submit" value="Search" />
                                                </c:if>
                                                <c:param name="page" value="${currentPage - 1}" />
                                            </c:url>
                                            <a class="page-link" href="${prevPageUrl}">Previous</a>
                                        </c:if>

                                        <c:forEach var="pageNumber" begin="1" end="${totalPages}">
                                            <c:url var="pageUrl" value="indexjsp">
                                                <c:param name="service" value="${currentService}" />
                                                <c:if test="${not empty currentCategoryId}">
                                                    <c:param name="cId" value="${currentCategoryId}" />
                                                </c:if>
                                                <c:if test="${not empty currentSearchKeyword}">
                                                    <c:param name="productName" value="${currentSearchKeyword}" />
                                                    <c:param name="submit" value="Search" />
                                                </c:if>
                                                <c:param name="page" value="${pageNumber}" />
                                            </c:url>
                                            <a class="page-link ${currentPage == pageNumber ? 'is-active' : ''}" href="${pageUrl}">${pageNumber}</a>
                                        </c:forEach>

                                        <c:if test="${currentPage < totalPages}">
                                            <c:url var="nextPageUrl" value="indexjsp">
                                                <c:param name="service" value="${currentService}" />
                                                <c:if test="${not empty currentCategoryId}">
                                                    <c:param name="cId" value="${currentCategoryId}" />
                                                </c:if>
                                                <c:if test="${not empty currentSearchKeyword}">
                                                    <c:param name="productName" value="${currentSearchKeyword}" />
                                                    <c:param name="submit" value="Search" />
                                                </c:if>
                                                <c:param name="page" value="${currentPage + 1}" />
                                            </c:url>
                                            <a class="page-link" href="${nextPageUrl}">Next</a>
                                        </c:if>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <h3>No products found</h3>
                                    <p>Try another keyword or switch to a different category.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </section>
                </section>
            </main>
        </div>
    </body>
</html>
