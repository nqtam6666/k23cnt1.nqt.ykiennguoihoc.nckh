package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.CauHoiDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.CauHoiService;
import k23cnt1.nguyenquangtam.nckh.service.KhaoSatService;
import k23cnt1.nguyenquangtam.nckh.service.NhomDichVuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cau-hoi")
@RequiredArgsConstructor
public class AdminCauHoiController {
    
    private final CauHoiService cauHoiService;
    private final KhaoSatService khaoSatService;
    private final NhomDichVuService nhomDichVuService;
    
    @GetMapping("/khao-sat/{khaoSatId}")
    public String danhSachCauHoi(@PathVariable Long khaoSatId, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("khaoSat", khaoSatService.layKhaoSatTheoId(khaoSatId));
        model.addAttribute("danhSachCauHoi", cauHoiService.layCauHoiTheoKhaoSat(khaoSatId));
        return "admin/cau-hoi/danh-sach";
    }
    
    @GetMapping("/them")
    public String hienThiFormThem(@RequestParam Long khaoSatId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        try {
            CauHoiDTO dto = new CauHoiDTO();
            dto.setKhaoSatId(khaoSatId);
            
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("cauHoiDTO", dto);
            model.addAttribute("khaoSat", khaoSatService.layKhaoSatTheoId(khaoSatId));
            model.addAttribute("danhSachNhomDichVu", nhomDichVuService.layTatCaNhomDichVu());
            return "admin/cau-hoi/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khảo sát với ID: " + khaoSatId);
            return "redirect:/admin/khao-sat";
        }
    }
    
    @PostMapping("/them")
    public String themCauHoi(@Valid @ModelAttribute CauHoiDTO dto,
                             BindingResult result,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("khaoSat", khaoSatService.layKhaoSatTheoId(dto.getKhaoSatId()));
            model.addAttribute("danhSachNhomDichVu", nhomDichVuService.layTatCaNhomDichVu());
            return "admin/cau-hoi/form";
        }
        
        try {
            cauHoiService.taoCauHoi(dto);
            redirectAttributes.addFlashAttribute("success", "Thêm câu hỏi thành công!");
            return "redirect:/admin/cau-hoi/khao-sat/" + dto.getKhaoSatId();
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("khaoSat", khaoSatService.layKhaoSatTheoId(dto.getKhaoSatId()));
            model.addAttribute("danhSachNhomDichVu", nhomDichVuService.layTatCaNhomDichVu());
            return "admin/cau-hoi/form";
        }
    }
    
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        var cauHoi = cauHoiService.layCauHoiTheoId(id);
        CauHoiDTO dto = new CauHoiDTO();
        dto.setCauHoiId(cauHoi.getCauHoiId());
        dto.setKhaoSatId(cauHoi.getKhaoSat().getKhaoSatId());
        dto.setNhomDichVuId(cauHoi.getNhomDichVu().getNhomDichVuId());
        dto.setNoiDung(cauHoi.getNoiDung());
        dto.setLoaiThangDo(cauHoi.getLoaiThangDo());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("cauHoiDTO", dto);
        model.addAttribute("khaoSat", cauHoi.getKhaoSat());
        model.addAttribute("danhSachNhomDichVu", nhomDichVuService.layTatCaNhomDichVu());
        return "admin/cau-hoi/form";
    }
    
    @PostMapping("/sua/{id}")
    public String suaCauHoi(@PathVariable Long id,
                           @Valid @ModelAttribute CauHoiDTO dto,
                           BindingResult result,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("khaoSat", khaoSatService.layKhaoSatTheoId(dto.getKhaoSatId()));
            model.addAttribute("danhSachNhomDichVu", nhomDichVuService.layTatCaNhomDichVu());
            return "admin/cau-hoi/form";
        }
        
        try {
            cauHoiService.capNhatCauHoi(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật câu hỏi thành công!");
            return "redirect:/admin/cau-hoi/khao-sat/" + dto.getKhaoSatId();
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("khaoSat", khaoSatService.layKhaoSatTheoId(dto.getKhaoSatId()));
            model.addAttribute("danhSachNhomDichVu", nhomDichVuService.layTatCaNhomDichVu());
            return "admin/cau-hoi/form";
        }
    }
    
    @PostMapping("/xoa/{id}")
    public String xoaCauHoi(@PathVariable Long id, 
                            @RequestParam Long khaoSatId,
                            RedirectAttributes redirectAttributes) {
        try {
            cauHoiService.xoaCauHoi(id);
            redirectAttributes.addFlashAttribute("success", "Xóa câu hỏi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/cau-hoi/khao-sat/" + khaoSatId;
    }
}

