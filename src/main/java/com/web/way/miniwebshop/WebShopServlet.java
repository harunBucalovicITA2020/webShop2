package com.web.way.miniwebshop;

import com.web.way.miniwebshop.model.Product;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Harun
 */
public class WebShopServlet extends HttpServlet {

    static String PRODUCTS_KEY = "products";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = getServletContext();
        String realPath = servletContext.getRealPath("products.txt");
        try (Scanner scanner = new Scanner(new FileReader(realPath))) {
            List<Product> products = new ArrayList<>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(line, ";");
                int id = Integer.parseInt(stringTokenizer.nextToken());
                String name = stringTokenizer.nextToken();
                BigDecimal price = new BigDecimal(stringTokenizer.nextToken());
                Product product = new Product(id, name, price);
                products.add(product);
            }
            servletContext.setAttribute(PRODUCTS_KEY, products);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Products</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Proizvodi u WEB SHOPU</h1>");
            List<Product> products = (List<Product>) getServletContext().getAttribute(PRODUCTS_KEY);
            if (products != null && !products.isEmpty()) {
                out.println("<h3>Dostupni proizvodi</h3>");
                out.println("<table cellspacing='0' cellpadding='3' border = '1'>");
                out.println("<tr bgcolor='lightblue'>"
                        + "<th>Naziv artikla</th>"
                        + "<th>Cijena</th>"
                        + "<th>Dodaj u korpu</th>"
                        + "</tr>");
                for (Product product : products) {
                    out.println("<tr>");
                    out.println("<td>" + product.getProductName() + "</td>");
                    out.println("<td>" + product.getUnitPrice().toPlainString() + "</td>");
                    out.println("<td>");
                    out.println("<form method = 'GET' action='shoppingCart'>"
                            + "<input type = 'number' name='quantity' size = '3'/>"
                            + "<input type = 'hidden' value='" + product.getId() + "' name='productId'/>");
                    out.println("</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("<input type = 'submit' value = 'Dodaj'/></form>");
            } else {
                out.println("<h4>Trenutno nemamo artikala na stanju. Pokušajte kasnije.</h4>");
            }

            out.println("</body>");
            out.println("</html>");
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
