package work.web.action;

import work.bean.Customer;
import work.bean.Merchant;
import work.bean.Store;
import work.utils.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet({"/login", "/register", "/logout", "/quit","/customer/details","/customer/save"})
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/login".equals(servletPath)) {
            doLogin(request, response);
        } else if ("/register".equals(servletPath)) {
            doRegister(request, response);
        } else if ("/logout".equals(servletPath)) {
            doLogout(request, response);
        } else if ("/quit".equals(servletPath)) {
            doQuit(request, response);
        }else if ("/customer/save".equals(servletPath)) {
            doCSave(request, response);
        }else if ("/customer/details".equals(servletPath)) {
            doDetails(request, response);
        }
    }

    private void doDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        int customerId=customer.getId();

        Customer newCustomer=new Customer();

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        try {
            conn=DBUtil.getConnection();
            String sql = "select * from customer where id =?";
            ps=conn.prepareStatement(sql);
            ps.setInt(1,customerId);

            rs=ps.executeQuery();
            while (rs.next()){
                newCustomer.setId(rs.getInt("id"));
                newCustomer.setAccount(rs.getString("account"));
                newCustomer.setPassword(rs.getString("password"));
                newCustomer.setEmail(rs.getString("email"));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(conn,ps,rs);
        }

        session.setAttribute("customer",newCustomer);
        response.sendRedirect(request.getContextPath()+"/customer.jsp");
    }

    private void doCSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if(password.length()<6)
        {
            String message="密码至少为6位";
            showErrorMessage(response,message);
        }
        if(!isValidEmail(email)){
            String message="邮箱格式有误";
            showErrorMessage(response,message);
        }

        Connection conn=null;
        PreparedStatement ps=null;
        int count;
        try {
            conn=DBUtil.getConnection();
            String sql = "update customer set password = ?, email = ? where account = ?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,password);
            ps.setString(2,email);
            ps.setString(3,customer.getAccount());

            count=ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(conn,ps,null);
        }

        if(count==1){
            response.sendRedirect(request.getContextPath()+"/customer/details");
        }
    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean check = false;
        int count = 0;
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        if (account.length() == 0 || email.length() == 0 || password.length() < 6 || !isValidEmail(email)) {
            String message;
            if (account.length() == 0) {
                message = "请输入账号";
            } else if (password.length() < 6) {
                message = "密码至少为6位";
            } else {
                message = "邮箱格式不正确";
            }
            showErrorMessage(response, message);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sqlCheck = "select * from " + role + " where account = ?";
            ps = conn.prepareStatement(sqlCheck);
            ps.setString(1, account);

            rs = ps.executeQuery();

            if (!rs.next()) {

                String sqlRegister = "insert into " + role + "(account,password,email) values(?,?,?)";
                ps = conn.prepareStatement(sqlRegister);
                ps.setString(1, account);
                ps.setString(2, password);
                ps.setString(3, email);

                count = ps.executeUpdate();
                check = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        if (check) {
//            账户未存在
            if (count == 1) {
//                账户创建成功
                String message = "账户创建成功！请登录";
                request.setAttribute("Message", message);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
            } else if (count == 0) {
//                账户创建失败
                String message = "账户创建失败！请联系管理员";
                showErrorMessage(response, message);
            }
        } else {
//            账户已存在
            String message = "账户已存在！请更换账号或登录账号";
            showErrorMessage(response, message);
        }
    }

    private void Remove(HttpServletRequest request, HttpSession session, String role)
            throws ServletException, IOException {
        session.removeAttribute(role);
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("account".equals(name) || "password".equals(name)) {
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                }
            }
        }
    }

    private boolean isValidEmail(String email)
            throws ServletException, IOException {
        // 邮箱格式的正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
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

    private void doQuit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        String role = request.getParameter("role");
        String id = request.getParameter("id");
        if (session != null) {
            Remove(request, session, role);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count;

        String sql;

        try {
            conn = DBUtil.getConnection();

            if ("merchant".equals(role)) {
                sql = "select store from merchant where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String store = rs.getString("store");
                    sql = "delete from log where store = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, store);
                    ps.executeUpdate();

                    sql = "delete from store where id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, store);
                    ps.executeUpdate();
                }

            }


            sql = "delete from " + role + " where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            count = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        if (count == 1) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String role = request.getParameter("role");
        if (session != null) {
            Remove(request, session, role);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = false;
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String email = null;
        String store = null;
        int id = -1;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from " + role + " where account = ? and password = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, account);
            ps.setString(2, password);

            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
                id = rs.getInt("id");
                if ("merchant".equals(role)) {
                    store = rs.getString("store");
                }
                success = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        if (success) {
            HttpSession session = request.getSession();

            if ("customer".equals(role)) {
                Customer customer = new Customer(account, password, email, id);
                session.setAttribute("customer", customer);
            } else if ("merchant".equals(role)) {
                Merchant merchant = new Merchant(account, password, email, store, id);
                session.setAttribute("merchant", merchant);

                if (store != null) {
                    Store myStore = null;
                    try {
                        conn = DBUtil.getConnection();
                        String sql = "select * from store where id = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, store);
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            String name = rs.getString("name");
                            String owner = rs.getString("owner");
                            String details = rs.getString("details");
                            String contact = rs.getString("contact");

                            myStore = new Store(name, owner, details, contact, Integer.parseInt(store));
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } finally {
                        DBUtil.close(conn, ps, rs);
                    }
                    session.setAttribute("store", myStore);
                }

            }

            String flag = request.getParameter("flag");
            if ("1".equals(flag)) {
                Cookie cookieAccount = new Cookie("account", account);
                Cookie cookiePassword = new Cookie("password", password);
                Cookie cookieRole = new Cookie("role", role);

                cookieAccount.setMaxAge(60 * 60 * 24 * 10);
                cookiePassword.setMaxAge(60 * 60 * 24 * 10);
                cookieRole.setMaxAge(60 * 60 * 24 * 10);

                response.addCookie(cookieAccount);
                response.addCookie(cookiePassword);
                response.addCookie(cookieRole);
            }

            if (store == null && "merchant".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/creatStore.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/" + role + "/list");
            }

        } else {
            String message = "请输入";
            if (account.length() == 0) {
                message += "账号";
            }
            if (password.length() == 0) {
                message += "密码";
            }
            if (account.length() != 0 && password.length() != 0) {
                message = "账号、密码、身份存在错误，请重新输入或注册账号";
            }
            showErrorMessage(response, message);
        }

    }
}
