package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    @GetMapping("/")
    public String trangChu(HttpSession session) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        
        if (nguoiDung == null) {
            return "redirect:/auth/login";
        }
        
        // Redirect theo vai trò
        String vaiTro = nguoiDung.getVaiTro();
        if ("admin".equals(vaiTro)) {
            return "redirect:/admin/dashboard";
        } else if ("giang_vien".equals(vaiTro)) {
            return "redirect:/giang-vien/dashboard";
        } else {
            return "redirect:/nguoi-hoc/dashboard";
        }
    }
}

