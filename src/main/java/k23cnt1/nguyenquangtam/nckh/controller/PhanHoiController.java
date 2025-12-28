package k23cnt1.nguyenquangtam.nckh.controller;

import k23cnt1.nguyenquangtam.nckh.dto.PhanHoiDTO;
import k23cnt1.nguyenquangtam.nckh.entity.PhanHoi;
import k23cnt1.nguyenquangtam.nckh.service.DichVuDaoTaoService;
import k23cnt1.nguyenquangtam.nckh.service.LoaiPhanHoiService;
import k23cnt1.nguyenquangtam.nckh.service.PhanHoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/phan-hoi")
@RequiredArgsConstructor
public class PhanHoiController {
    
    private final PhanHoiService phanHoiService;
    private final DichVuDaoTaoService dichVuDaoTaoService;
    private final LoaiPhanHoiService loaiPhanHoiService;
    
    @GetMapping("/form")
    public String hienThiFormPhanHoi(Model model) {
        model.addAttribute("phanHoiDTO", new PhanHoiDTO());
        model.addAttribute("danhSachDichVu", dichVuDaoTaoService.layDichVuDangHoatDong());
        model.addAttribute("danhSachLoaiPhanHoi", loaiPhanHoiService.layLoaiPhanHoiDangHoatDong());
        return "phan-hoi/form";
    }
    
    @PostMapping("/submit")
    public String guiPhanHoi(@Valid @ModelAttribute PhanHoiDTO phanHoiDTO, 
                             BindingResult result, 
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("danhSachDichVu", dichVuDaoTaoService.layDichVuDangHoatDong());
            model.addAttribute("danhSachLoaiPhanHoi", loaiPhanHoiService.layLoaiPhanHoiDangHoatDong());
            return "phan-hoi/form";
        }
        
        try {
            phanHoiService.taoPhanHoi(phanHoiDTO);
            redirectAttributes.addFlashAttribute("success", "Gửi phản hồi thành công! Cảm ơn bạn đã đóng góp ý kiến.");
            return "redirect:/phan-hoi/form";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("danhSachDichVu", dichVuDaoTaoService.layDichVuDangHoatDong());
            model.addAttribute("danhSachLoaiPhanHoi", loaiPhanHoiService.layLoaiPhanHoiDangHoatDong());
            return "phan-hoi/form";
        }
    }
    
    @GetMapping("/danh-sach")
    public String hienThiDanhSachPhanHoi(Model model) {
        List<PhanHoi> danhSachPhanHoi = phanHoiService.layPhanHoiDaDuyet();
        model.addAttribute("danhSachPhanHoi", danhSachPhanHoi);
        return "phan-hoi/danh-sach";
    }
    
    @GetMapping("/chi-tiet/{id}")
    public String hienThiChiTietPhanHoi(@PathVariable Long id, Model model) {
        PhanHoi phanHoi = phanHoiService.layPhanHoiTheoId(id);
        model.addAttribute("phanHoi", phanHoi);
        return "phan-hoi/chi-tiet";
    }
}

