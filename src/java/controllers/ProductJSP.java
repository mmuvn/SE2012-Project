/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.CategoryDAO;
import dal.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Vector;
import model.Category;
import model.Product;
import model.User;

/**
 *
 * @author admin
 */
@WebServlet(name="ProductJSP", urlPatterns={"/productjsp"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
// Handles admin-side product CRUD forms and older product list/search routes.
public class ProductJSP extends AdminGuardServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = requireAdminUser(request, response);
        if (user == null) {
            return;
        }

        CategoryDAO cDao = new CategoryDAO();
        ProductDAO dao = new ProductDAO();

        Vector<Category> cVector = cDao.getAllCategories();

        String service = request.getParameter("service");

        if (service == null || service.equals("listOfProduct")) {
            response.sendRedirect("admin?service=productManager");
            return;
        }

        // Delete one product, or deactivate it if the DAO blocks hard delete.
        if (service.equals("deleteProduct")) {

            int pId = Integer.parseInt(request.getParameter("pId"));
            Product product = dao.searchProduct(pId);
            int deleted = dao.deleteProduct(pId);
            if (deleted > 0) {
                session.setAttribute("adminMessage", "Product deleted successfully.");
                session.setAttribute("adminMessageType", "success");
            } else if (product != null) {
                session.setAttribute("adminMessage", "Product removal processed. If the product was used in orders, it was deactivated instead.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Product could not be deleted.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=productManager");
            return;
        }

        // Show the update form first, then save the edited product.
        if (service.equals("updateProduct")) {

            String submit = request.getParameter("submit");

            if (submit == null) {

                int pId = Integer.parseInt(request.getParameter("pId"));

                Product p = dao.searchProduct(pId);

                request.setAttribute("p", p);
                request.setAttribute("cVector", cVector);

                request.getRequestDispatcher("jsp/update/updateProduct.jsp")
                        .forward(request, response);
                return;
            }

            int pId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            String image = request.getParameter("currentImage");
            String uploadedImage = saveUploadedImage(request, "imageFile");
            if (uploadedImage != null) {
                image = uploadedImage;
            }
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String categoryID = request.getParameter("categoryID");
            String description = request.getParameter("description");
            int status = quantity > 0 ? 1 : 0;

            Product p = new Product(pId, productName, image, price, quantity,
                    categoryID, description, status);

            int updated = dao.updateProduct(p);
            if (updated > 0) {
                session.setAttribute("adminMessage", "Updated successfully.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Update failed.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=productManager");
            return;
        }

        // Show the insert form first, then create a new product.
        if (service.equals("addProduct")) {

            String submit = request.getParameter("submit");

            if (submit == null) {

                request.setAttribute("cVector", cVector);

                request.getRequestDispatcher("jsp/insert/insertProduct.jsp")
                        .forward(request, response);
                return;
            }

            String productName = request.getParameter("productName");
            String image = saveUploadedImage(request, "imageFile");
            if (image == null) {
                request.setAttribute("cVector", cVector);
                request.setAttribute("error", "Please choose an image file before creating the product.");
                request.getRequestDispatcher("jsp/insert/insertProduct.jsp")
                        .forward(request, response);
                return;
            }
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String categoryID = request.getParameter("categoryID");
            String description = request.getParameter("description");
            int status = quantity > 0 ? 1 : 0;

            Product p = new Product(productName, image, price, quantity,
                    categoryID, description, status);

            int inserted = dao.insertProduct(p);
            if (inserted > 0) {
                session.setAttribute("adminMessage", "Product added successfully.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Product could not be added.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=productManager");
            return;
        }

        response.sendRedirect("admin?service=productManager");
        return;
    }

    private String saveUploadedImage(HttpServletRequest request, String fieldName)
            throws IOException, ServletException {
        // Saves the uploaded product image and returns the stored file name.
        Part imagePart = request.getPart(fieldName);
        if (imagePart == null || imagePart.getSize() == 0) {
            return null;
        }

        String fileName = sanitizeFileName(imagePart.getSubmittedFileName());
        Path imagesDir = resolveImagesDirectory();
        String uniqueFileName = ensureUniqueFileName(imagesDir, fileName);
        Path savedFile = imagesDir.resolve(uniqueFileName);

        try (InputStream inputStream = imagePart.getInputStream()) {
            Files.copy(inputStream, savedFile, StandardCopyOption.REPLACE_EXISTING);
        }
        mirrorToProjectImages(savedFile);

        return uniqueFileName;
    }

    private Path resolveImagesDirectory() throws IOException {
        // Resolves the runtime images folder, with the project folder as fallback.
        String runtimeImagesPath = getServletContext().getRealPath("/images");
        if (runtimeImagesPath != null) {
            Path runtimeDir = Path.of(runtimeImagesPath);
            Files.createDirectories(runtimeDir);
            return runtimeDir;
        }

        Path projectImagesDir = Path.of(System.getProperty("user.dir"), "web", "images");
        Files.createDirectories(projectImagesDir);
        return projectImagesDir;
    }

    private void mirrorToProjectImages(Path savedFile) throws IOException {
        // Copies the uploaded image into the project web/images folder for local consistency.
        Path projectWebDir = Path.of(System.getProperty("user.dir"), "web");
        if (!Files.isDirectory(projectWebDir)) {
            return;
        }

        Path projectImagesDir = projectWebDir.resolve("images").toAbsolutePath().normalize();
        Path sourceDir = savedFile.getParent().toAbsolutePath().normalize();
        if (projectImagesDir.equals(sourceDir)) {
            return;
        }

        Files.createDirectories(projectImagesDir);
        Files.copy(savedFile, projectImagesDir.resolve(savedFile.getFileName()),
                StandardCopyOption.REPLACE_EXISTING);
    }

    private String sanitizeFileName(String submittedFileName) {
        // Removes risky characters so uploaded image names stay filesystem-safe.
        String fileName = submittedFileName == null ? "" : submittedFileName.replace("\\", "/");
        int lastSlash = fileName.lastIndexOf('/');
        if (lastSlash >= 0) {
            fileName = fileName.substring(lastSlash + 1);
        }

        fileName = fileName.trim().replaceAll("\\s+", "-");
        fileName = fileName.replaceAll("[^A-Za-z0-9._-]", "");

        if (fileName.isEmpty() || ".".equals(fileName) || "..".equals(fileName)) {
            return "product-image";
        }
        return fileName;
    }

    private String ensureUniqueFileName(Path imagesDir, String fileName) throws IOException {
        // Adds a numeric suffix when the same image file name already exists.
        String baseName = fileName;
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }

        String candidate = fileName;
        int counter = 1;
        while (Files.exists(imagesDir.resolve(candidate))) {
            candidate = baseName + "-" + counter + extension;
            counter++;
        }
        return candidate;
    }

    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Product Controller";
    }// </editor-fold>

}
