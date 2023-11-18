package work.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();

        HttpSession session = request.getSession(false);
        boolean isAllowed = "/index.jsp".equals(servletPath) ||
                "/register.jsp".equals(servletPath) ||
                "/welcome".equals(servletPath) ||
                "/login".equals(servletPath) ||
                "/register".equals(servletPath) ||
                "/logout".equals(servletPath) ||
                "/quit".equals(servletPath) ||
                (session != null && session.getAttribute("customer") != null) ||
                (session != null && session.getAttribute("merchant") != null);

        if (isAllowed) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
