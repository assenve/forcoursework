package work.web.action;

import work.bean.*;
import work.utils.DBUtil;
import work.utils.EMailUtil;
import work.utils.LogUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;


@WebServlet({"/creat/save", "/merchant/list", "/modify", "/modify/save", "/product/delete",
        "/product/save", "/store/save", "/store/details", "/order"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)
public class MerchantServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/creat/save".equals(servletPath)) {
            doCSave(request, response);
        } else if ("/merchant/list".equals(servletPath)) {
            doMerchant(request, response);
        } else if ("/modify".equals(servletPath)) {
            doModify(request, response);
        } else if ("/modify/save".equals(servletPath)) {
            doMSave(request, response);
        } else if ("/product/delete".equals(servletPath)) {
            doDel(request, response);
        } else if ("/product/save".equals(servletPath)) {
            doPSave(request, response);
        } else if ("/store/save".equals(servletPath)) {
            doSave(request, response);
        } else if ("/store/details".equals(servletPath)) {
            doDetails(request, response);
        } else if ("/order".equals(servletPath)) {
            doOrder(request, response);
        }
    }

    private void doCSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Merchant merchant = (Merchant) session.getAttribute("merchant");

        String name = request.getParameter("name");
        String details = request.getParameter("details");
        String contact = request.getParameter("contact");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = DBUtil.getConnection();
            sql = "select name from store";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String name1 = rs.getString("name");
                String message;
                if (name == null || "".equals(name)) {
                    message = "店铺名称不能为空";
                    showErrorMessage(response, message);
                    return;
                }
                if (name1.equals(name)) {
                    message = "已经存在重名店铺";
                    showErrorMessage(response, message);
                    return;
                }
            }
            sql = "insert into store(name,owner,details,contact) values(?,?,?,?)";
            ps = conn.prepareStatement(sql, 1);
            ps.setString(1, name);
            ps.setString(2, String.valueOf(merchant.getId()));
            ps.setString(3, details);
            ps.setString(4, contact);

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            while (rs.next()) {
                int id = rs.getInt(1);
                Store store = new Store(name, String.valueOf(merchant.getId()), details, contact, id);
                session.setAttribute("store", store);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        String message = "创建店铺成功！";
        request.setAttribute("Message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/merchant/list");
        dispatcher.forward(request, response);
    }

    private void doOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Store store = (Store) session.getAttribute("store");

        String logId = request.getParameter("logId");
        String customerId = null, productId = null, email = null, account = null, name = null;
        String sql;
        LocalDateTime time, now;
        now = LocalDateTime.now();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            sql = "select * from log where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, logId);

            rs = ps.executeQuery();
            while (rs.next()) {
                customerId = rs.getString("customer");
                productId = rs.getString("product");
            }

            sql = "select account,email from customer where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, customerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                email = rs.getString("email");
                account = rs.getString("account");
            }

            sql = "select name from product where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        String flag = request.getParameter("flag");
        if ("shipping".equals(flag)) {
            LogUtil.ChangeLogActive(Integer.parseInt(logId), "已付款，已发货");
            String message = "您好 " + account + " ，您于 " + now + " 在店铺 " + store.getName() + " 的购买物 " + name
                    + " 已经发货，请注意查收！" + " 具体信息可以与店铺联系，联系方式：" + store.getContact();
            try {
                EMailUtil.send(message, email);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if ("cancel".equals(flag)) {
            LogUtil.ChangeLogActive(Integer.parseInt(logId), "已取消");
        }
        response.sendRedirect(request.getContextPath() + "/store/details");
    }

    private void doDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        ArrayList<Log> logs = new ArrayList<>();


        ArrayList<Log> BPLogs = new ArrayList<>();
        ArrayList<Log> unpaid = new ArrayList<>();
        ArrayList<Log> notShipped = new ArrayList<>();
        ArrayList<Order> finished = new ArrayList<>();

        ArrayList<Product> onSale = new ArrayList<>();
        ArrayList<Product> soldOut = new ArrayList<>();

        float revenue = 0;
        int total = 0;


        HttpSession session = request.getSession();
        Store store = (Store) session.getAttribute("store");


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from log where store = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, store.getId());
            rs = ps.executeQuery();

            while (rs.next()) {
                String customer = rs.getString("customer");
                String product = rs.getString("product");
                Timestamp time = rs.getTimestamp("time");
                String active = rs.getString("active");
                int id = rs.getInt("id");

                Log log = new Log(customer, product, time, active, id);
                logs.add(log);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        conn = null;
        ps = null;
        rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from product where store = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, store.getId());
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String price = rs.getString("price");
                String picture = rs.getString("picture");
                String details = rs.getString("details");
                String quantity = rs.getString("quantity");
                int id = rs.getInt("id");

                for (Log log : logs) {
                    if (String.valueOf(id).equals(log.getProduct()) && "已付款，已发货".equals(log.getActive())) {
                        boolean create = true;
                        for (Order order : finished) {
                            if (order.getId().equals(log.getProduct())) {
                                create = false;
                                float p = order.getPrice();
                                int num = order.getQuantity();
                                float income = order.getIncome();
                                income += p;
                                order.setQuantity(num + 1);
                                order.setIncome(income);
                                break;
                            }
                        }
                        if (create) {
                            Order order = new Order(picture, name, String.valueOf(id), Float.parseFloat(price), 1, Float.parseFloat(price));
                            finished.add(order);
                        }
                    }
                }
                Product product = new Product(name, price, picture, details, quantity, String.valueOf(store.getId()), id, -1);

                if (Integer.parseInt(quantity) > 0) {
                    onSale.add(product);
                } else {
                    soldOut.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);

        }

        conn = null;
        ps = null;
        rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql;

            for (Log log : logs) {

                String customer = log.getCustomer();
                String product = log.getProduct();
                String active = log.getActive();

                sql = "select * from customer where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, customer);
                rs = ps.executeQuery();
                while (rs.next()) {
                    customer = rs.getString("account");
                }

                sql = "select * from product where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, product);
                rs = ps.executeQuery();
                while (rs.next()) {
                    product = rs.getString("name");
                }

                log.setCustomer(customer);
                log.setProduct(product);

                if ("浏览".equals(active) || "已付款，已发货".equals(active)) {
                    BPLogs.add(log);
                }
                if ("待付款".equals(active)) {
                    unpaid.add(log);
                }
                if ("已付款，未发货".equals(active)) {
                    notShipped.add(log);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        for (Order order : finished) {
            revenue += order.getIncome();
            total += order.getQuantity();
        }


        session.setAttribute("BPLogs", BPLogs);
        session.setAttribute("unpaid", unpaid);
        session.setAttribute("notShipped", notShipped);
        session.setAttribute("finished", finished);

        session.setAttribute("onSale", onSale);
        session.setAttribute("soldOut", soldOut);

        session.setAttribute("revenue", revenue);
        session.setAttribute("total", total);

        response.sendRedirect(request.getContextPath() + "/storeDetails.jsp");
    }

    private boolean isValidNum(String num, String regex) {
        return !num.matches(regex);
    }

    private void showErrorMessage(HttpServletResponse response, String message)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("window.history.back();");
        out.println("</script>");
    }

    private boolean checkNum(HttpServletResponse response, String price, String quantity)
            throws ServletException, IOException {
        if (isValidNum(price, "\\d+(\\.\\d+)?")) {
            String message = "商品价格必须为数字";
            showErrorMessage(response, message);
            return true;
        }

        if (isValidNum(quantity, "\\d+")) {
            String message = "商品数量必须为整数";
            showErrorMessage(response, message);
            return true;
        }
        return false;
    }

    private void SavePicture(String fileName, InputStream pictureInputStream)
            throws ServletException, IOException {
        String relativePath = "/images";
        String savePath = getServletContext().getRealPath(relativePath); // 获取绝对路径
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs(); // 创建目录
        }
        String filePath = savePath + File.separator + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = pictureInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();
        pictureInputStream.close();
    }

    private void doSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Store store = (Store) session.getAttribute("store");

        String name = request.getParameter("name");
        String details = request.getParameter("details");
        String contact = request.getParameter("contact");

        if (!"".equals(name)) {
            store.setName(name);
        }
        store.setDetails(details);
        if (!"".equals(contact)) {
            store.setContact(contact);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        int count;

        try {
            conn = DBUtil.getConnection();
            String sql = "update store set name = ?, details = ?, contact = ? where owner = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, store.getName());
            ps.setString(2, store.getDetails());
            ps.setString(3, store.getContact());
            ps.setString(4, store.getOwner());

            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, null);
        }
        if (count == 1) {
            String message = "修改成功！";
            request.setAttribute("Message", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/merchant/list");
            dispatcher.forward(request, response);
        }
    }

    private void doMSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Product product = (Product) session.getAttribute("product");

        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String details = request.getParameter("details");
        String quantity = request.getParameter("quantity");

        // 处理上传的图片文件
        Part picturePart = request.getPart("picture");
        String fileName = picturePart.getSubmittedFileName();
        if (!"".equals(name)) {
            product.setName(name);
        }
        if (!"".equals(price)) {
            product.setPrice(price);
        }

        product.setDetails(details);

        if (!"".equals(quantity)) {
            product.setQuantity(quantity);
        }

        if (checkNum(response, price, quantity)) return;

        if (fileName != null && !"".equals(fileName)) {

            InputStream pictureInputStream = picturePart.getInputStream();
            product.setPicture(fileName);

            // 保存图片文件到相对路径的指定目录
            SavePicture(fileName, pictureInputStream);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        int count;

        try {
            conn = DBUtil.getConnection();
            String sql = "update product set name = ?, price = ?, picture = ?, details = ?, quantity = ?, store = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getPrice());
            ps.setString(3, product.getPicture());
            ps.setString(4, product.getDetails());
            ps.setString(5, product.getQuantity());
            ps.setString(6, product.getStore());
            ps.setInt(7, product.getId());

            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, null);
        }

        if (count == 1) {
            String message = "修改成功！";
            request.setAttribute("Message", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/merchant/list");
            dispatcher.forward(request, response);
        }

    }

    private void doPSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Merchant merchant = (Merchant) session.getAttribute("merchant");

        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String details = request.getParameter("details");
        String quantity = request.getParameter("quantity");
        if ("".equals(name) || name == null) {
            String message = "商品必须有名称";
            showErrorMessage(response, message);
        }
        if ("".equals(price) || price == null) {
            String message = "商品必须有价格";
            showErrorMessage(response, message);
        }
        if ("".equals(quantity) || quantity == null) {
            String message = "商品必须设置数量";
            showErrorMessage(response, message);
        }

        if (checkNum(response, price, quantity)) return;

        // 处理上传的图片文件
        Part picturePart = request.getPart("picture");
        String fileName = picturePart.getSubmittedFileName();

        if (fileName != null && !"".equals(fileName)) {
            InputStream pictureInputStream = picturePart.getInputStream();
            // 保存图片文件到相对路径的指定目录
            SavePicture(fileName, pictureInputStream);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        int count;

        try {
            conn = DBUtil.getConnection();
            String sql = "insert into product(name,price,picture,details,quantity,store) values(?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, price);
            ps.setString(3, fileName);
            ps.setString(4, details);
            ps.setString(5, quantity);
            ps.setString(6, merchant.getStore());

            count = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, null);
        }
        if (count == 1) {
            String message = "上架成功！";
            request.setAttribute("Message", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/merchant/list");
            dispatcher.forward(request, response);
        }
    }

    private void doDel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String pid = request.getParameter("pid");

        Connection conn = null;
        PreparedStatement ps = null;
        int count;
        try {
            conn = DBUtil.getConnection();
            String sql = "delete from product where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, pid);

            count = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, null);
        }

        if (count == 1) {
            response.sendRedirect(request.getContextPath() + "/merchant/list");
        }
    }

    private void doModify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String pid = request.getParameter("pid");
        boolean success = false;
        Product product = null;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from product where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, pid);

            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String price = rs.getString("price");
                String details = rs.getString("details");
                String quantity = rs.getString("quantity");
                String store = rs.getString("store");
                String picture = rs.getString("picture");
                int id = rs.getInt("id");

                product = new Product(name, price, picture, details, quantity, store, id, -1);
                success = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        if (success) {
            session.setAttribute("product", product);
            response.sendRedirect(request.getContextPath() + "/modify.jsp");
        }
    }

    private void doMerchant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Store store = (Store) session.getAttribute("store");
        Merchant merchant = (Merchant) session.getAttribute("merchant");

        if (store == null) {
            response.sendRedirect(request.getContextPath() + "/createStore.jsp");
        } else {

            ArrayList<Product> products = new ArrayList<>();
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = DBUtil.getConnection();
                String sql = "select * from product where store = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, merchant.getStore());
                rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String price = rs.getString("price");
                    String picture = rs.getString("picture");
                    String details = rs.getString("details");
                    String quantity = rs.getString("quantity");

                    Product product = new Product(name, price, picture, details, quantity, merchant.getStore(), id, -1);
                    products.add(product);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBUtil.close(conn, ps, rs);
            }
            session.setAttribute("products", products);
            response.sendRedirect(request.getContextPath() + "/store.jsp");
        }
    }
}
