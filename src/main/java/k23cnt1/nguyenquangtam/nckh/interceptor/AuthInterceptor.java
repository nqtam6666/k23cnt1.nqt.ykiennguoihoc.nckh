package k23cnt1.nguyenquangtam.nckh.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("nguoiDung") == null) {
            response.sendRedirect("/auth/login");
            return false;
        }
        
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        String requestPath = request.getRequestURI();
        
        // Kiểm tra quyền truy cập
        if (requestPath.startsWith("/admin/") && !"admin".equals(nguoiDung.getVaiTro())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
            return false;
        }
        
        if (requestPath.startsWith("/giang-vien/") && !"giang_vien".equals(nguoiDung.getVaiTro())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
            return false;
        }
        
        if (requestPath.startsWith("/nguoi-hoc/") && !"nguoi_hoc".equals(nguoiDung.getVaiTro())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
            return false;
        }
        
        return true;
    }
}

