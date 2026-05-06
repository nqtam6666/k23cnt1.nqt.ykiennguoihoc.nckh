package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.KhaoSatDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.CauHoiService;
import k23cnt1.nguyenquangtam.nckh.service.KhaoSatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/khao-sat")
@RequiredArgsConstructor
public class AdminKhaoSatController {
    
    private final KhaoSatService khaoSatService;
    private final CauHoiService cauHoiService;
    
    @GetMapping
    public String danhSachKhaoSat(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "hocKy", required = false, defaultValue = "all") String hocKy,
            @RequestParam(value = "namHoc", required = false, defaultValue = "all") String namHoc,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpSession session, 
            Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var pageInfo = khaoSatService.timKiemVaPhanTrang(keyword, hocKy, namHoc, page, size);
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("hocKy", hocKy);
        model.addAttribute("namHoc", namHoc);
        return "admin/khao-sat/danh-sach";
    }
    
    @GetMapping("/them")
    public String hienThiFormThem(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("khaoSatDTO", new KhaoSatDTO());
        return "admin/khao-sat/form";
    }
    
    @PostMapping("/them")
    public String themKhaoSat(@Valid @ModelAttribute KhaoSatDTO dto,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            return "admin/khao-sat/form";
        }
        
        try {
            khaoSatService.taoKhaoSat(dto);
            redirectAttributes.addFlashAttribute("success", "Tạo khảo sát thành công!");
            return "redirect:/admin/khao-sat";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/khao-sat/them";
        }
    }
    
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var khaoSat = khaoSatService.layKhaoSatTheoId(id);
        KhaoSatDTO dto = new KhaoSatDTO();
        dto.setKhaoSatId(khaoSat.getKhaoSatId());
        dto.setTenKhaoSat(khaoSat.getTenKhaoSat());
        dto.setHocKy(khaoSat.getHocKy());
        dto.setNamHoc(khaoSat.getNamHoc());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("khaoSatDTO", dto);
        return "admin/khao-sat/form";
    }
    
    @PostMapping("/sua/{id}")
    public String suaKhaoSat(@PathVariable Long id,
                             @Valid @ModelAttribute KhaoSatDTO dto,
                             BindingResult result,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            return "admin/khao-sat/form";
        }
        
        try {
            khaoSatService.capNhatKhaoSat(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khảo sát thành công!");
            return "redirect:/admin/khao-sat";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/khao-sat/sua/" + id;
        }
    }
    
    @GetMapping("/{id}/cau-hoi")
    public String quanLyCauHoi(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var khaoSat = khaoSatService.layKhaoSatTheoId(id);
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("khaoSat", khaoSat);
        model.addAttribute("danhSachCauHoi", cauHoiService.layCauHoiTheoKhaoSat(id));
        return "admin/khao-sat/cau-hoi";
    }
    
    @PostMapping("/xoa/{id}")
    public String xoaKhaoSat(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            khaoSatService.xoaKhaoSat(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khảo sát thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/khao-sat";
    }
}

