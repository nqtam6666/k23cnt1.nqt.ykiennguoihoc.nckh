package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.GiangVienDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.GiangVienService;
import k23cnt1.nguyenquangtam.nckh.service.KhoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/giang-vien")
@RequiredArgsConstructor
public class AdminGiangVienController {
    
    private final GiangVienService giangVienService;
    private final KhoaService khoaService;
    
    @GetMapping
    public String danhSachGiangVien(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "khoaId", required = false) Long khoaId,
            @RequestParam(value = "hocVi", required = false, defaultValue = "all") String hocVi,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpSession session, 
            Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var pageInfo = giangVienService.timKiemVaPhanTrang(keyword, khoaId, hocVi, page, size);
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        model.addAttribute("keyword", keyword);
        model.addAttribute("khoaId", khoaId);
        model.addAttribute("hocVi", hocVi);
        return "admin/giang-vien/danh-sach";
    }
    
    @GetMapping("/them")
    public String hienThiFormThem(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("giangVienDTO", new GiangVienDTO());
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        return "admin/giang-vien/form";
    }
    
    @PostMapping("/them")
    public String themGiangVien(@Valid @ModelAttribute GiangVienDTO dto, 
                                BindingResult result, 
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        // Kiểm tra mật khẩu khi tạo mới
        if (dto.getMatKhau() == null || dto.getMatKhau().trim().isEmpty()) {
            result.rejectValue("matKhau", "error.matKhau", "Mật khẩu không được để trống");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/giang-vien/form";
        }
        
        try {
            giangVienService.taoGiangVien(dto);
            redirectAttributes.addFlashAttribute("success", "Thêm giảng viên thành công!");
            return "redirect:/admin/giang-vien";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/giang-vien/form";
        }
    }
    
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var giangVien = giangVienService.layGiangVienTheoId(id);
        GiangVienDTO dto = new GiangVienDTO();
        dto.setGiangVienId(giangVien.getGiangVienId());
        dto.setTenDangNhap(giangVien.getNguoiDung().getTenDangNhap());
        dto.setHoTen(giangVien.getHoTen());
        dto.setHocVi(giangVien.getHocVi());
        dto.setKhoaId(giangVien.getKhoa().getKhoaId());
        dto.setSoNamKinhNghiem(giangVien.getSoNamKinhNghiem());
        dto.setNamBatDau(giangVien.getNamBatDau());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("giangVienDTO", dto);
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        return "admin/giang-vien/form";
    }
    
    @PostMapping("/sua/{id}")
    public String suaGiangVien(@PathVariable Long id,
                              @Valid @ModelAttribute("giangVienDTO") GiangVienDTO dto,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        // Đảm bảo giangVienId được set
        dto.setGiangVienId(id);
        
        // Bỏ qua validation cho matKhau khi sửa (có thể để trống)
        if (dto.getMatKhau() != null && dto.getMatKhau().trim().isEmpty()) {
            dto.setMatKhau(null);
        }
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("giangVienDTO", dto);
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/giang-vien/form";
        }
        
        try {
            giangVienService.capNhatGiangVien(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật giảng viên thành công!");
            return "redirect:/admin/giang-vien";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("giangVienDTO", dto);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/giang-vien/form";
        }
    }
    
    @PostMapping("/xoa/{id}")
    public String xoaGiangVien(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            giangVienService.xoaGiangVien(id);
            redirectAttributes.addFlashAttribute("success", "Xóa giảng viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/giang-vien";
    }
}

