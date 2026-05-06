package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final KhaoSatService khaoSatService;
    private final GiangVienService giangVienService;
    private final HocPhanService hocPhanService;
    private final PhanCongService phanCongService;
    private final PhanHoiService phanHoiService;
    private final NguoiDungService nguoiDungService;
    
    // Redirect từ /admin/nguoi-hoc sang /admin/nguoi-dung
    @GetMapping("/nguoi-hoc")
    public String redirectNguoiHoc() {
        return "redirect:/admin/nguoi-dung";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("soLuongKhaoSat", khaoSatService.layTatCaKhaoSat().size());
        model.addAttribute("soLuongGiangVien", giangVienService.layTatCaGiangVien().size());
        model.addAttribute("soLuongHocPhan", hocPhanService.layTatCaHocPhan().size());
        model.addAttribute("soLuongPhanCong", phanCongService.layTatCaPhanCong().size());
        model.addAttribute("soLuongPhanHoi", phanHoiService.layTatCaPhanHoi().size());
        model.addAttribute("soLuongNguoiDung", nguoiDungService.layTatCaNguoiDung().size());
        return "admin/dashboard";
    }
}
