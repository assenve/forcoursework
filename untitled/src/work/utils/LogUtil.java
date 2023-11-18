package work.utils;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class LogUtil {
    public static int CreatLog(String cid, String pid, String store, String active)
            throws IOException, ServletException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id = -1;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO log (customer, product,store, time, active) VALUES (?, ?, ?,?, ?)";
            ps = conn.prepareStatement(sql,1);
            ps.setString(1, String.valueOf(cid));
            ps.setString(2, String.valueOf(pid));
            ps.setString(3, store);
            ps.setTimestamp(4, Timestamp.valueOf(localDateTime));
            ps.setString(5, active);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return id;
    }

    public static void ChangeLogActive(int id, String newActive)
            throws IOException, ServletException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "update log set active = ?, time = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newActive);
            ps.setTimestamp(2, Timestamp.valueOf(localDateTime));
            ps.setInt(3, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }
}