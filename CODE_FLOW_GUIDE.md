# Code Flow Guide

This document explains how the current Java backend is structured, how requests move through the servlet layer, and which parts can be restructured later with the lowest risk.

## 1. Current Layer Structure

The project is currently organized in 4 main Java layers:

1. `controllers`
   Handles HTTP requests, session checks, request parameters, redirects, and forwards to JSP pages.
2. `dal`
   Handles database access using JDBC.
3. `model`
   Represents application data such as `Product`, `User`, `Order`, and `Cart`.
4. `web/jsp`
   Renders the UI using JSTL/EL.

Current Java packages:

- `src/java/controllers`
- `src/java/dal`
- `src/java/model`

## 2. Controller Responsibilities

### `IndexServlet`
File: `src/java/controllers/IndexServlet.java`

This is the main customer-facing controller.

It handles:

- storefront display
- login
- logout
- register
- product search
- category filtering
- pagination
- product detail
- order history
- cancel order

Current request flow:

1. Read `service`
2. Default to `display` if no service is provided
3. Dispatch to a dedicated handler method

Main handlers:

- `handleDisplay(...)`
  Loads all categories, products, and best sellers, then prepares pagination for the homepage.
- `handleProductSearch(...)`
  Searches products by name, then paginates the filtered result.
- `handleCategoryFilter(...)`
  Filters products by category, then paginates the filtered result.
- `handleProductDetail(...)`
  Loads one product, its category, and random recommendation products.
- `handleLogin(...)`
  Shows login page first, then checks email/password.
- `handleRegister(...)`
  Shows register page first, validates data, generates a user ID, then inserts the user.
- `handleOrderHistory(...)`
  Requires a logged-in user, then loads only that user's orders.
- `handleCancelOrder(...)`
  Cancels only a pending order owned by the logged-in user and restores stock.

Why this structure is better than before:

- the controller is still large, but the main flow is now split into handler methods instead of one long `if/else` chain
- storefront, auth, and order-history actions are easier to locate

### `CartServlet`
File: `src/java/controllers/CartServlet.java`

This controller manages the session cart and checkout flow.

It handles:

- show cart
- add to cart
- update quantity
- remove one item
- remove all items
- checkout

Important helper logic:

- `getLoggedInUser(...)`
  Reads the current session user.
- `requiresLoggedInUser(...)`
  Defines which cart actions need login.
- `getCartItems(...)`
  Collects all `Cart` objects currently stored in session.
- `moveCartFlashToRequest(...)`
  Moves one-time cart messages from session to request before forwarding to JSP.

Checkout flow:

1. Read cart items from session
2. Recheck current stock from database
3. Build an `Order` object with `Pending` status
4. Call `OrderDAO.checkoutOrder(...)`
5. If the transaction succeeds, remove cart items from session
6. Redirect back to cart with success message

The cart is session-based, not database-cart-based.

### `AdminGuardServlet`
File: `src/java/controllers/AdminGuardServlet.java`

This is a shared base servlet for admin-only controllers.

Its main job:

- check if there is a logged-in user
- check if that user is an admin (`roleID == 1`)

If not:

- guest users are redirected to `indexjsp?service=login`
- non-admin users are redirected to `indexjsp`

This reduces duplicated access-check code across admin servlets.

### `AdminServlet`
File: `src/java/controllers/AdminServlet.java`

This is the main admin dashboard controller.

It does not perform CRUD directly.
It loads manager data and forwards to the single dashboard page:

- product manager
- order manager
- user manager
- role manager
- category manager

Main flow:

1. Require admin user through `AdminGuardServlet`
2. Read `service`
3. Load the correct manager dataset
4. Set `selectedManager`
5. Forward to `jsp/admin/adminIndex.jsp`

Manager loaders:

- `loadProducts(...)`
- `loadOrders(...)`
- `loadUsers(...)`
- `loadRoles(...)`
- `loadCategories(...)`

`loadOrders(...)` also loads:

- selected order
- order detail lines
- related products for detail lines
- selected customer

### Admin CRUD Servlets

These servlets handle create/update/delete actions and redirect back to the dashboard.

#### `ProductJSP`
File: `src/java/controllers/ProductJSP.java`

Responsibilities:

- add product
- update product
- delete/deactivate product
- upload product images

Important note:

- product image upload is handled here, not in JSP
- quantity controls product status automatically
  - `quantity > 0` => active
  - `quantity <= 0` => inactive

#### `UserJSP`
File: `src/java/controllers/UserJSP.java`

Responsibilities:

- update user
- toggle active/inactive user status

Current behavior:

- add-user flow is intentionally disabled because customer registration already exists on the storefront
- user status is toggled from the manager board instead of using delete

#### `OrderJSP`
File: `src/java/controllers/OrderJSP.java`

Responsibilities:

- update order
- delete order
- approve pending order
- redirect to order detail view inside admin dashboard

Important status rule:

- only pending orders can be approved
- approved orders are locked from status change in normal flow

#### `CategoryJSP`
File: `src/java/controllers/CategoryJSP.java`

Responsibilities:

- add category
- update category
- delete category

#### `RoleJSP`
File: `src/java/controllers/RoleJSP.java`

Responsibilities:

- add role
- update role
- delete role

Note:

- the role manager controller still exists, but the role manager card is hidden from the admin dashboard UI because it is not essential for the assignment flow

## 3. DAO Responsibilities

### `DBContext`
File: `src/java/dal/DBContext.java`

Purpose:

- create and hold the JDBC connection used by DAO classes

### `ProductDAO`
File: `src/java/dal/ProductDAO.java`

Purpose:

- product queries
- insert/update/delete product
- stock updates
- storefront recommendation queries

Important non-CRUD behavior:

- `getBestSellingProducts(...)`
  Loads top-selling products using approved orders only.
- `getRandomProducts(...)`
  Loads random recommended products using `ORDER BY NEWID()`.
- `reduceStockAfterCheckout(...)`
  Deducts stock and can deactivate a product at zero quantity.
- `restoreStockAfterCancellation(...)`
  Returns stock after a pending order is cancelled and reactivates product if needed.

### `OrderDAO`
File: `src/java/dal/OrderDAO.java`

Purpose:

- order queries
- insert/update/delete order
- order status changes
- checkout transaction

Important non-CRUD behavior:

- `getAllOrdersSortedByLatest()`
  Returns orders sorted by newest date first.
- `searchOrdersByUserId(...)`
  Used by admin order search.
- `getOrdersByUser(...)`
  Used by customer order history.
- `updateOrderStatus(...)`
  Used for approve/cancel status changes.
- `checkoutOrder(...)`
  This is the most important method in the DAO layer.
  It wraps:
  - insert order
  - reduce stock
  - insert order details
  in one transaction.

Order status mapping:

- `0` = Pending
- `1` = Cancelled
- `2` = Approved

### `OrderDetailDAO`
File: `src/java/dal/OrderDetailDAO.java`

Purpose:

- load order detail lines for one order

Used by:

- admin order detail panel
- user cancel-order flow

### `UserDAO`
File: `src/java/dal/UserDAO.java`

Purpose:

- user lookup
- login support
- register support
- user update
- user status update

Important non-CRUD behavior:

- `checkLogin(...)`
  Validates email/password login.
- `searchUserByEmail(...)`
  Used in register validation.
- `generateNextUserId(...)` or equivalent ID helper logic
  Supports auto-generated user IDs for register flow.
- `updateUserStatus(...)`
  Supports admin activate/deactivate action.

### `CategoryDAO`
File: `src/java/dal/CategoryDAO.java`

Purpose:

- category CRUD
- category list/search

### `RoleDAO`
File: `src/java/dal/RoleDAO.java`

Purpose:

- role CRUD
- role list/search

### `CartDAO`
File: `src/java/dal/CartDAO.java`

Purpose:

- build `Cart` objects from `productID`

The cart itself is still stored in session, so this DAO is lightweight.

## 4. Model Responsibilities

### `User`
- account data
- role ID
- address, phone, email
- active status

### `Product`
- product data
- category ID
- description
- image file name
- quantity
- active/inactive status

### `Order`
- order header
- order date
- total
- user ID
- status

### `OrderDetail`
- line item of an order
- price at purchase time
- quantity
- product ID

### `Cart`
- temporary session cart item
- product ID
- product name
- price
- quantity

### `Category`
- category ID
- category name
- description

### `Role`
- role ID
- role name

## 5. Main End-to-End Request Flows

### A. Homepage Flow

1. Request goes to `indexjsp`
2. `IndexServlet` defaults `service` to `display`
3. Products, categories, and best sellers are loaded
4. Pagination is calculated
5. Request forwards to `jsp/index.jsp`

### B. Login Flow

1. User opens `indexjsp?service=login`
2. `IndexServlet.handleLogin(...)` forwards to `jsp/login.jsp`
3. On submit, email/password are checked in `UserDAO`
4. If valid, session stores `user`
5. Redirect back to `indexjsp`

### C. Add To Cart Flow

1. User clicks add to cart
2. Request goes to `cart?service=add2Cart&pId=...`
3. `CartServlet` checks login
4. Product is loaded from DB
5. If active and in stock, session cart is created or incremented
6. Redirect to `cart`

### D. Checkout Flow

1. User clicks checkout
2. `CartServlet` rechecks stock against current DB state
3. `Order` object is built with `Pending` status
4. `OrderDAO.checkoutOrder(...)` runs one transaction
5. If success, session cart is cleared
6. User stays on cart page and sees success message

### E. Cancel Order Flow

1. Logged-in user opens order history
2. User cancels a pending order
3. `IndexServlet.handleCancelOrder(...)` verifies ownership and status
4. Order detail lines are loaded
5. Product stock is restored line by line
6. Order status changes from `Pending` to `Cancelled`
7. Redirect back to order history with message

### F. Admin Product Update Flow

1. Admin opens product manager from dashboard
2. `AdminServlet.loadProducts(...)` loads product list
3. Admin clicks update
4. Request goes to `productjsp?service=updateProduct&pId=...`
5. `ProductJSP` loads selected product and category list
6. JSP form submits updated data
7. `ProductDAO.updateProduct(...)` saves changes
8. Redirect back to `admin?service=productManager`

### G. Admin Order Approve Flow

1. Admin opens order manager
2. Orders are loaded newest first
3. Admin clicks approve on a pending order
4. Request goes to `orderjsp?service=approveOrder&pId=...`
5. `OrderJSP` verifies current status is pending
6. `OrderDAO.updateOrderStatus(...)` changes status to approved
7. Redirect back to order manager with message

## 6. Why The Current Structure Is Acceptable

For a course assignment, the current structure is reasonable because:

- controllers own request/response/session logic
- DAO classes own SQL and JDBC access
- JSP pages mostly render data instead of containing Java logic
- admin access checks are centralized
- checkout is transactional

This is not enterprise architecture, but it is coherent and defendable for the project scope.

## 7. If A Mentor Asks For Structural Changes

These are the safest changes to propose without rewriting the whole project.

### Option 1. Split `IndexServlet`

Current issue:

- `IndexServlet` still owns multiple concerns:
  - storefront
  - authentication
  - order history

Suggested split:

- `StorefrontServlet`
- `AuthServlet`
- `OrderHistoryServlet`

Why:

- each servlet gets one responsibility
- future changes are easier
- method count per controller drops

### Option 2. Add A Service Layer

Current issue:

- some business rules still live directly inside servlets

Suggested service classes:

- `AuthService`
- `CartService`
- `OrderService`
- `AdminProductService`

Why:

- moves business logic out of controllers
- makes testing and restructuring easier

### Option 3. Replace Remaining Generic DAO Patterns

Current issue:

- some DAO classes still expose generic methods like `getAllX(String sql)` or `getData(String sql)`

Suggested change:

- keep only named query methods

Examples:

- `getAllProducts()`
- `searchProductsByName(...)`
- `getAllOrdersSortedByLatest()`
- `searchOrdersByUserId(...)`

Why:

- better readability
- less risk from passing SQL strings around
- clearer DAO responsibility

### Option 4. Use A Filter For Auth

Current issue:

- admin protection is centralized better than before, but still servlet-based

Suggested change:

- use a servlet `Filter` for:
  - admin-only routes
  - login-required routes

Why:

- removes repeated guard calls
- moves access policy out of business controllers

### Option 5. Group Admin CRUD More Aggressively

Current issue:

- admin dashboard loading is in `AdminServlet`
- actual CRUD actions are split across multiple `*JSP` controllers

Possible alternative:

- keep one dashboard controller for page loading
- move each domain to:
  - `ProductAdminServlet`
  - `UserAdminServlet`
  - `OrderAdminServlet`
  - `CategoryAdminServlet`

This is optional. The current version is already acceptable if the mentor is not asking for deeper restructuring.

## 8. Practical Guidance If A Change Is Requested

If asked to change the structure, the safest order is:

1. keep JSP pages and URLs stable
2. extract or move one responsibility at a time
3. keep DAO method names stable during controller refactor
4. only then change routes if necessary

Recommended low-risk sequence:

1. extract service/helper classes
2. split `IndexServlet`
3. replace remaining generic DAO methods
4. add auth filter if needed

## 9. Current Risks To Mention Honestly

These are the main weaknesses if someone asks for limitations:

- passwords are still stored as plain text
- there is no dedicated service layer yet
- some DAO classes still contain generic SQL helper methods
- the project uses session cart storage instead of persistent cart storage

These do not prevent the project from functioning, but they are fair improvement points.

## 10. Short Summary

The current backend flow is:

- `IndexServlet` for customer/storefront flow
- `CartServlet` for session cart and checkout flow
- `AdminServlet` for embedded dashboard loading
- `ProductJSP`, `UserJSP`, `OrderJSP`, `CategoryJSP`, `RoleJSP` for admin CRUD actions
- DAO classes for JDBC/database access
- model classes for application data

If a mentor asks for restructuring, the most reasonable next steps are:

- split `IndexServlet`
- introduce a service layer
- remove the remaining generic DAO query methods
- optionally move auth checks into a servlet filter
