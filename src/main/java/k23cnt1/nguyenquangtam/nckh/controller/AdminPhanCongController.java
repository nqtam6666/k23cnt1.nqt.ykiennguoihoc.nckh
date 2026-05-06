package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.PhanCongDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.GiangVienService;
import k23cnt1.nguyenquangtam.nckh.service.HocPhanService;
import k23cnt1.nguyenquangtam.nckh.service.PhanCongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/phan-cong")
@RequiredArgsConstructor
public class AdminPhanCongController {
    
    private final PhanCongService phanCongService;
    private final GiangVienService giangVienService;
    private final HocPhanService hocPhanService;
    
    @GetMapping
    public String danhSachPhanCong(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "giangVienId", required = false) Long giangVienId,
            @RequestParam(value = "hocPhanId", required = false) Long hocPhanId,
            @RequestParam(value = "hocKy", required = false, defaultValue = "all") String hocKy,
            @RequestParam(value = "namHoc", required = false, defaultValue = "all") String namHoc,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpSession session, 
            Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var pageInfo = phanCongService.timKiemVaPhanTrang(keyword, giangVienId, hocPhanId, hocKy, namHoc, page, size);
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
        model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
        model.addAttribute("keyword", keyword);
        model.addAttribute("giangVienId", giangVienId);
        model.addAttribute("hocPhanId", hocPhanId);
        model.addAttribute("hocKy", hocKy);
        model.addAttribute("namHoc", namHoc);
        return "admin/phan-cong/danh-sach";
    }
    
    @GetMapping("/them")
    public String hienThiFormThem(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("phanCongDTO", new PhanCongDTO());
        model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
        model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
        return "admin/phan-cong/form";
    }
    
    @PostMapping("/them")
    public String themPhanCong(@Valid @ModelAttribute PhanCongDTO dto,
                               BindingResult result,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
            model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
            return "admin/phan-cong/form";
        }
        
        try {
            phanCongService.taoPhanCong(dto);
            redirectAttributes.addFlashAttribute("success", "Thêm phân công thành công!");
            return "redirect:/admin/phan-cong";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
            model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
            return "admin/phan-cong/form";
        }
    }
    
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var phanCong = phanCongService.layPhanCongTheoId(id);
        PhanCongDTO dto = new PhanCongDTO();
        dto.setPhanCongId(phanCong.getPhanCongId());
        dto.setGiangVienId(phanCong.getGiangVien().getGiangVienId());
        dto.setHocPhanId(phanCong.getHocPhan().getHocPhanId());
        dto.setHocKy(phanCong.getHocKy());
        dto.setNamHoc(phanCong.getNamHoc());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("phanCongDTO", dto);
        model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
        model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
        return "admin/phan-cong/form";
    }
    
    @PostMapping("/sua/{id}")
    public String suaPhanCong(@PathVariable Long id,
                              @Valid @ModelAttribute PhanCongDTO dto,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
            model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
            return "admin/phan-cong/form";
        }
        
        try {
            phanCongService.capNhatPhanCong(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phân công thành công!");
            return "redirect:/admin/phan-cong";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachGiangVien", giangVienService.layTatCaGiangVien());
            model.addAttribute("danhSachHocPhan", hocPhanService.layTatCaHocPhan());
            return "admin/phan-cong/form";
        }
    }
    
    @PostMapping("/xoa/{id}")
    public String xoaPhanCong(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            phanCongService.xoaPhanCong(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phân công thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/phan-cong";
    }
}

