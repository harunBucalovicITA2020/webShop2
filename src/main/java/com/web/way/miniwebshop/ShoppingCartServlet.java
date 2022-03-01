package com.web.way.miniwebshop;


import com.web.way.miniwebshop.model.Product;
import com.web.way.miniwebshop.model.ShoppingCart;
import com.web.way.miniwebshop.model.ShoppingCartItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Harun
 */
@WebServlet(name = "ShoppingCartServlet", urlPatterns = {"/shoppingCart"})
public class ShoppingCartServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Artikli u korpi</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Artikli u korpi</h1>");
            HttpSession session = request.getSession();
            ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("cart");
            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                session.setAttribute("cart", shoppingCart);
            }
            List<Product> products = (List<Product>) getServletContext().getAttribute(WebShopServlet.PRODUCTS_KEY);
            String[] quantityParamsArray = request.getParameterValues("quantity");
            String[] productsIdArray = request.getParameterValues("productId");
            if (quantityParamsArray!=null && quantityParamsArray.length != 0) {
                for (int i = 0; i < quantityParamsArray.length; i++) {
                    if (quantityParamsArray[i].isEmpty()) {
                        continue;
                    }
                    int quantity = Integer.parseInt(quantityParamsArray[i]);
                    int productId = Integer.parseInt(productsIdArray[i]);
                    Product product = findProduct(products, productId);
                    if (product != null) {
                        shoppingCart.addItem(product, quantity);
                    }
                }
            }
            String uklonjeniQuantityParam = request.getParameter("uklonjeniQuantity");
            if (uklonjeniQuantityParam != null) {
                int quantity = Integer.parseInt(request.getParameter("uklonjeniQuantity"));
                int productId = Integer.parseInt(request.getParameter("uklonjeniProductId"));
                Product product1 = findProduct(products, productId);
                if (product1 != null) {
                    shoppingCart.deleteItem(product1, quantity);
                }
            }

            if (shoppingCart.getShoppingCartItems().isEmpty()) {
                out.println("<h2>Nema artikala u koripi</h>");
            } else {
                out.println("<table cellspacing='0' cellpadding='3' border = '1'>");
                out.println("<tr bgcolor='lightblue'>"
                        + "<th>Naziv artikla</th>"
                        + "<th>Jedinična cijena</th>"
                        + "<th>Kolićina</th>"
                        + "<th>Cijena</th>"
                        + "<th>Ukloni iz korpe</th>"
                        + "</tr>");
                for (ShoppingCartItem item : shoppingCart.getShoppingCartItems()) {
                    out.println("<tr>");
                    out.println("<td>" + item.getProduct().getProductName() + "</td>");
                    out.println("<td>" + item.getProduct().getUnitPrice().toPlainString() + "</td>");
                    out.println("<td>" + item.getQuantity() + "</td>");
                    out.println("<td>" + item.getTotalPrice().toPlainString() + "</td>");
                    out.println("<td>");
                    out.println("<form method = 'GET' action=''>"
                            + "<input type = 'number' name='uklonjeniQuantity' size = '3'/>"
                            + "<input type = 'hidden' value='" + item.getProduct().getId() + "' name='uklonjeniProductId'/>"
                            + "<input type = 'submit' value = 'Ukloni'/>"
                            + "</form>");
                    out.println("</td>");
                    out.println("</tr>");

                }
                out.println("</table>");
//                out.println("<input type = 'submit' value = 'Obrisi'/></form>");

            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    private Product findProduct(List<Product> products, int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;

            }
        }
        return null;
    }

    private void addToShoppingCart(HttpServletRequest request, HttpServletResponse response, ShoppingCart shoppingCart) {
        // ime parametra mora biti isto kao name ='quantity' WebShopServleta jer on dobija te parametre iz klijentskog requesta
        List<Product> products = (List<Product>) getServletContext().getAttribute(WebShopServlet.PRODUCTS_KEY);
        String[] quantityParamsArray = request.getParameterValues("quantity");
        String[] productsIdArray = request.getParameterValues("productId");
        for (int i = 0; i < quantityParamsArray.length; i++) {
            if (quantityParamsArray[i].isEmpty()) {
                continue;
            }
            int quantity = Integer.parseInt(quantityParamsArray[i]);
            int productId = Integer.parseInt(productsIdArray[i]);
            Product product = findProduct(products, productId);
            if (product != null) {
                shoppingCart.addItem(product, quantity);
            }
        }
    }

    private void removeFromShoppingCart(HttpServletRequest request, HttpServletResponse response, ShoppingCart shoppingCart) {
        List<Product> products = (List<Product>) getServletContext().getAttribute(WebShopServlet.PRODUCTS_KEY);
        String uklonjeniQuantityParam = request.getParameter("uklonjeniQuantity");
        if (uklonjeniQuantityParam != null) {
            int quantity = Integer.parseInt(request.getParameter("uklonjeniQuantity"));
            int productId = Integer.parseInt(request.getParameter("uklonjeniProductId"));
            Product product1 = findProduct(products, productId);
            if (product1 != null) {
                shoppingCart.deleteItem(product1, quantity);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
