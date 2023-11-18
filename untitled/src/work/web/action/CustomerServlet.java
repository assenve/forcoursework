package work.web.action;

import work.bean.Customer;
import work.bean.Product;
import work.utils.DBUtil;
import work.utils.LogUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet({"/customer/list", "/basket/list", "/basket/add", "/basket/delete", "/detail", "/pay"})
public class CustomerServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/customer/list".equals(servletPath)) {
            doCustomer(request, response);
        } else if ("/basket/list".equals(servletPath)) {
            doBasket(request, response);
        } else if ("/basket/add".equals(servletPath)) {
            doAdd(request, response);
        } else if ("/basket/delete".equals(servletPath)) {
            doDel(request, response);
        } else if ("/detail".equals(servletPath)) {
            doDetail(request, response);
        } else if ("/pay".equals(servletPath)) {
            doPay(request, response);
        }
    }

    private void doPay(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        Customer customer = (Customer) session.getAttribute("customer");
        ArrayList<Product> basketProducts = (ArrayList<Product>) session.getAttribute("basketProducts");

        if (basketProducts == null) {
            basketProducts = new ArrayList<>();
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql;
            Iterator<Product> iterator = basketProducts.iterator();
            while (iterator.hasNext()) {
                Product basketProduct = iterator.next();
                int count, num = 0;

                sql = "select quantity from product where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, String.valueOf(basketProduct.getId()));
                rs = ps.executeQuery();

                while (rs.next()) {
                    String quantity = rs.getString("quantity");
                    num = Integer.parseInt(quantity);
                    if (num <= 0) {
                        String message = "存在商品存货不足！请移除后再继续购买";
                        request.setAttribute("Message", message);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/payment.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                }

                sql = "update product set quantity = ? where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, String.valueOf(num - 1));
                ps.setString(2, String.valueOf(basketProduct.getId()));
                count = ps.executeUpdate();

                if (count == 1) {
                    LogUtil.ChangeLogActive(basketProduct.getOrder(), "已付款，未发货");
                    iterator.remove(); // 使用迭代器的remove方法删除元素
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        session.setAttribute("basketProducts", basketProducts);
        response.sendRedirect(request.getContextPath() + "/payment.jsp");
    }

    private void doDel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pid = request.getParameter("pid");
        HttpSession session = request.getSession();

        ArrayList<String> basketIds = (ArrayList<String>) session.getAttribute("basketIds");
        if (basketIds != null) {
            basketIds.remove(pid);
            session.setAttribute("basketIds", basketIds);
        }
        response.sendRedirect(request.getContextPath() + "/basket/list");
    }

    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        String AddId = request.getParameter("AddId");
        ArrayList<Product> basketProducts = (ArrayList<Product>) session.getAttribute("basketProducts");

        if (basketProducts == null) {
            basketProducts = new ArrayList<Product>();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from product where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, AddId);
            rs = ps.executeQuery();

            while (rs.next()) {
                String picture = rs.getString("picture");
                String name = rs.getString("name");
                String store = rs.getString("store");
                String price = rs.getString("price");
                String details = rs.getString("details");
                String quantity = rs.getString("quantity");

                int num = Integer.parseInt(quantity);
                if (num <= 0) {
                    String message = "该商品已售罄！";
                    request.setAttribute("Message", message);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/productDetails.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                int id = LogUtil.CreatLog(String.valueOf(customer.getId()), AddId, store, "待付款");
                Product product = new Product(name, price, picture, details, quantity, store, Integer.parseInt(AddId), id);
                basketProducts.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        session.setAttribute("basketProducts", basketProducts);
        response.sendRedirect(request.getContextPath() + "/productDetails.jsp");
    }

    private void doDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pid = request.getParameter("pid");
        String sid = request.getParameter("sid");

        // 从会话中获取 products 列表
        HttpSession session = request.getSession();
        ArrayList<Product> products = (ArrayList<Product>) session.getAttribute("products");
        Customer customer = (Customer) session.getAttribute("customer");

        LogUtil.CreatLog(String.valueOf(customer.getId()), pid, sid, "浏览");

        Product productDetail = null;
        for (Product product : products) {
            if (product.getId() == Integer.parseInt(pid)) {
                productDetail = new Product(product.getName(), product.getPrice(), product.getPicture(), product.getDetails(), product.getQuantity(), product.getStore(), product.getId(), -1);
                String storeId = productDetail.getStore();
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                String name = null;
                String contact = null;

                try {
                    conn = DBUtil.getConnection();
                    String sql = "select name,contact from store where id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, storeId);

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        name = rs.getString("name");
                        contact = rs.getString("contact");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    DBUtil.close(conn, ps, rs);
                }

                productDetail.setStore(name);
                session.setAttribute("contact", contact);
                break;
            }
        }
        session.setAttribute("productDetail", productDetail);

        response.sendRedirect(request.getContextPath() + "/productDetails.jsp");
    }

    private void doBasket(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        ArrayList<Product> basketProducts = (ArrayList<Product>) session.getAttribute("basketProducts");

        if (basketProducts == null) {
            basketProducts = new ArrayList<>();
        }

        int totalPrice = 0;
        for (Product basketProduct : basketProducts) {
            String price = basketProduct.getPrice();
            float priceValue = Float.parseFloat(price);
            totalPrice += priceValue;
        }

        session.setAttribute("totalPrice", totalPrice);
        response.sendRedirect(request.getContextPath() + "/basket.jsp");
    }

    private void doCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from product";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String price = rs.getString("price");
                String picture = rs.getString("picture");
                String details = rs.getString("details");
                String quantity = rs.getString("quantity");
                String store = rs.getString("store");
                int id = rs.getInt("id");

                Product product = new Product(name, price, picture, details, quantity, store, id, -1);

                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        HttpSession session = request.getSession();
        session.setAttribute("products", products);

        request.getRequestDispatcher("/product.jsp").forward(request, response);
    }

}
