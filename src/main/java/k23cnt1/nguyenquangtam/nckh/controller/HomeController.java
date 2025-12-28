package k23cnt1.nguyenquangtam.nckh.controller;

import k23cnt1.nguyenquangtam.nckh.service.DichVuDaoTaoService;
import k23cnt1.nguyenquangtam.nckh.service.PhanHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final DichVuDaoTaoService dichVuDaoTaoService;
    private final PhanHoiService phanHoiService;
    
    @GetMapping("/")
    public String trangChu(Model model) {
        model.addAttribute("danhSachDichVu", dichVuDaoTaoService.layDichVuDangHoatDong());
        return "index";
    }
    
    @GetMapping("/thong-ke")
    public String thongKe(Model model) {
        model.addAttribute("danhSachDichVu", dichVuDaoTaoService.layDichVuDangHoatDong());
        return "thong-ke";
    }
}

