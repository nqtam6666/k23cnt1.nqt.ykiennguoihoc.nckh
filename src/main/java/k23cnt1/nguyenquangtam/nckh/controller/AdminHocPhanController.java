package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.HocPhanDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.HocPhanService;
import k23cnt1.nguyenquangtam.nckh.service.KhoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/hoc-phan")
@RequiredArgsConstructor
public class AdminHocPhanController {
    
    private final HocPhanService hocPhanService;
    private final KhoaService khoaService;
    
    @GetMapping
    public String danhSachHocPhan(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "khoaId", required = false) Long khoaId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpSession session, 
            Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var pageInfo = hocPhanService.timKiemVaPhanTrang(keyword, khoaId, page, size);
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        model.addAttribute("keyword", keyword);
        model.addAttribute("khoaId", khoaId);
        return "admin/hoc-phan/danh-sach";
    }
    
    @GetMapping("/them")
    public String hienThiFormThem(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("hocPhanDTO", new HocPhanDTO());
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        return "admin/hoc-phan/form";
    }
    
    @PostMapping("/them")
    public String themHocPhan(@Valid @ModelAttribute HocPhanDTO dto,
                             BindingResult result,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/hoc-phan/form";
        }
        
        try {
            hocPhanService.taoHocPhan(dto);
            redirectAttributes.addFlashAttribute("success", "Thêm học phần thành công!");
            return "redirect:/admin/hoc-phan";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/hoc-phan/form";
        }
    }
    
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var hocPhan = hocPhanService.layHocPhanTheoId(id);
        HocPhanDTO dto = new HocPhanDTO();
        dto.setHocPhanId(hocPhan.getHocPhanId());
        dto.setTenHocPhan(hocPhan.getTenHocPhan());
        dto.setSoTinChi(hocPhan.getSoTinChi());
        dto.setKhoaId(hocPhan.getKhoa().getKhoaId());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("hocPhanDTO", dto);
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        return "admin/hoc-phan/form";
    }
    
    @PostMapping("/sua/{id}")
    public String suaHocPhan(@PathVariable Long id,
                             @Valid @ModelAttribute HocPhanDTO dto,
                             BindingResult result,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/hoc-phan/form";
        }
        
        try {
            hocPhanService.capNhatHocPhan(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật học phần thành công!");
            return "redirect:/admin/hoc-phan";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "admin/hoc-phan/form";
        }
    }
    
    @PostMapping("/xoa/{id}")
    public String xoaHocPhan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            hocPhanService.xoaHocPhan(id);
            redirectAttributes.addFlashAttribute("success", "Xóa học phần thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/hoc-phan";
    }
}

