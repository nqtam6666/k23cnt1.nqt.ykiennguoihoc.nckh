package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.KhaoSatService;
import k23cnt1.nguyenquangtam.nckh.service.PhanHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/phan-hoi")
@RequiredArgsConstructor
public class AdminPhanHoiController {
    
    private final PhanHoiService phanHoiService;
    private final KhaoSatService khaoSatService;
    
    @GetMapping
    @Transactional(readOnly = true)
    public String danhSachPhanHoi(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "khaoSatId", required = false) Long khaoSatId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpSession session, 
            Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var pageInfo = phanHoiService.timKiemVaPhanTrang(keyword, khaoSatId, page, size);
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("danhSachKhaoSat", khaoSatService.layTatCaKhaoSat());
        model.addAttribute("keyword", keyword);
        model.addAttribute("khaoSatId", khaoSatId);
        return "admin/phan-hoi/danh-sach";
    }
    
    @GetMapping("/khao-sat/{khaoSatId}")
    @Transactional(readOnly = true)
    public String phanHoiTheoKhaoSat(@PathVariable Long khaoSatId, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("danhSachPhanHoi", phanHoiService.layPhanHoiTheoKhaoSat(khaoSatId));
        model.addAttribute("khaoSatId", khaoSatId);
        return "admin/phan-hoi/danh-sach";
    }
    
    @GetMapping("/chi-tiet/{id}")
    @Transactional(readOnly = true)
    public String chiTietPhanHoi(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        try {
            var phanHoi = phanHoiService.layPhanHoiTheoId(id);
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("phanHoi", phanHoi);
            return "admin/phan-hoi/chi-tiet";
        } catch (RuntimeException e) {
            return "redirect:/admin/phan-hoi?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/xoa/{id}")
    @Transactional
    public String xoaPhanHoi(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        try {
            phanHoiService.xoaPhanHoi(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phản hồi thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa phản hồi: " + e.getMessage());
        }
        
        return "redirect:/admin/phan-hoi";
    }
}

