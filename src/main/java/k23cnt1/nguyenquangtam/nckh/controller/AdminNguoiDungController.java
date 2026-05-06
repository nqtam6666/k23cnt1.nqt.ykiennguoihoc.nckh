package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.ImportNguoiDungDTO;
import k23cnt1.nguyenquangtam.nckh.dto.NguoiDungDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/nguoi-dung")
@RequiredArgsConstructor
public class AdminNguoiDungController {
    
    private final NguoiDungService nguoiDungService;
    
    @GetMapping
    public String danhSachNguoiDung(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "vaiTro", required = false, defaultValue = "all") String vaiTro,
            @RequestParam(value = "trangThai", required = false) Boolean trangThai,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpSession session, 
            Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        // Chuẩn hóa trangThai: null = "all", true = "active", false = "inactive"
        Boolean trangThaiFilter = null;
        if (trangThai != null) {
            trangThaiFilter = trangThai;
        }
        
        var pageInfo = nguoiDungService.timKiemVaPhanTrang(keyword, vaiTro, trangThaiFilter, page, size);
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("vaiTro", vaiTro);
        model.addAttribute("trangThai", trangThai);
        return "admin/nguoi-dung/danh-sach";
    }
    
    @GetMapping("/them")
    public String hienThiFormThem(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("nguoiDungDTO", new NguoiDungDTO());
        return "admin/nguoi-dung/form";
    }
    
    @PostMapping("/them")
    public String themNguoiDung(@Valid @ModelAttribute NguoiDungDTO dto, 
                                BindingResult result, 
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            return "admin/nguoi-dung/form";
        }
        
        try {
            nguoiDungService.taoNguoiDung(dto);
            redirectAttributes.addFlashAttribute("success", "Thêm người dùng thành công!");
            return "redirect:/admin/nguoi-dung";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "admin/nguoi-dung/form";
        }
    }
    
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var nguoiDungEntity = nguoiDungService.layNguoiDungTheoId(id);
        NguoiDungDTO dto = new NguoiDungDTO();
        dto.setNguoiDungId(nguoiDungEntity.getNguoiDungId());
        dto.setTenDangNhap(nguoiDungEntity.getTenDangNhap());
        dto.setVaiTro(nguoiDungEntity.getVaiTro());
        dto.setTrangThai(nguoiDungEntity.getTrangThai());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("nguoiDungDTO", dto);
        return "admin/nguoi-dung/form";
    }
    
    @PostMapping("/sua/{id}")
    public String suaNguoiDung(@PathVariable Long id,
                               @Valid @ModelAttribute NguoiDungDTO dto,
                               BindingResult result,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            return "admin/nguoi-dung/form";
        }
        
        try {
            nguoiDungService.capNhatNguoiDung(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật người dùng thành công!");
            return "redirect:/admin/nguoi-dung";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "admin/nguoi-dung/form";
        }
    }
    
    @PostMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nguoiDungService.xoaNguoiDung(id);
            redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nguoi-dung";
    }
    
    @GetMapping("/them-hang-loat")
    public String hienThiFormThemHangLoat(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("importDTO", new ImportNguoiDungDTO());
        return "admin/nguoi-dung/import";
    }
    
    @PostMapping("/them-hang-loat")
    public String themHangLoat(@Valid @ModelAttribute ImportNguoiDungDTO dto,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            return "admin/nguoi-dung/import";
        }
        
        try {
            var ketQua = nguoiDungService.themHangLoat(
                dto.getDanhSachTaiKhoan(),
                dto.getMatKhauChung(),
                dto.getVaiTro()
            );
            
            int thanhCong = (Integer) ketQua.get("thanhCong");
            int thatBai = (Integer) ketQua.get("thatBai");
            int tongSo = (Integer) ketQua.get("tongSo");
            
            if (thanhCong > 0) {
                redirectAttributes.addFlashAttribute("success", 
                    String.format("Thêm thành công %d/%d tài khoản!", thanhCong, tongSo));
            }
            
            if (thatBai > 0) {
                @SuppressWarnings("unchecked")
                java.util.List<String> loiList = (java.util.List<String>) ketQua.get("loiList");
                redirectAttributes.addFlashAttribute("warning", 
                    String.format("Có %d tài khoản không thể thêm. Chi tiết: %s", 
                        thatBai, String.join("; ", loiList.subList(0, Math.min(5, loiList.size())))));
            }
            
            return "redirect:/admin/nguoi-dung";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "admin/nguoi-dung/import";
        }
    }
}

