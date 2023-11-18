package work.web.action;

import work.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = null;
        String password = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("account".equals(name)) {
                    account = cookie.getValue();
                } else if ("password".equals(name)) {
                    password = cookie.getValue();
                } else if ("role".equals(name)) {
                    role = cookie.getValue();
                }
            }
        }
        if (password != null && account != null) {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean check = false;

            try {
                conn = DBUtil.getConnection();
                String sql = "select * from " + role + " where account = ? and password = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, account);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if (rs.next()) {
                    check = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBUtil.close(conn, ps, rs);
            }

            if (check) {
                HttpSession session = request.getSession();
                session.setAttribute("account", account);
                session.setAttribute("password", password);
                session.setAttribute("role", role);
                response.sendRedirect(request.getContextPath() + "/" + role + "/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
