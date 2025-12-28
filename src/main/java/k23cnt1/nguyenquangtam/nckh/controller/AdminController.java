package k23cnt1.nguyenquangtam.nckh.controller;

import k23cnt1.nguyenquangtam.nckh.service.PhanHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final PhanHoiService phanHoiService;
    
    @GetMapping("/phan-hoi/cho-duyet")
    public String danhSachPhanHoiChoDuyet(Model model) {
        model.addAttribute("danhSachPhanHoi", phanHoiService.layPhanHoiChoDuyet());
        return "admin/phan-hoi-cho-duyet";
    }
    
    @PostMapping("/phan-hoi/{id}/duyet")
    public String duyetPhanHoi(@PathVariable Long id, 
                               @RequestParam(required = false) String ghiChu,
                               RedirectAttributes redirectAttributes) {
        try {
            phanHoiService.duyetPhanHoi(id, ghiChu);
            redirectAttributes.addFlashAttribute("success", "Duyệt phản hồi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/phan-hoi/cho-duyet";
    }
    
    @PostMapping("/phan-hoi/{id}/xoa")
    public String xoaPhanHoi(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            phanHoiService.xoaPhanHoi(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phản hồi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/phan-hoi/cho-duyet";
    }
}

