package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.GiangVienCapNhatDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.entity.GiangVien;
import k23cnt1.nguyenquangtam.nckh.repository.GiangVienRepository;
import k23cnt1.nguyenquangtam.nckh.repository.PhanCongRepository;
import k23cnt1.nguyenquangtam.nckh.service.GiangVienService;
import k23cnt1.nguyenquangtam.nckh.service.KhoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/giang-vien")
@RequiredArgsConstructor
public class GiangVienController {
    
    private final GiangVienRepository giangVienRepository;
    private final PhanCongRepository phanCongRepository;
    private final GiangVienService giangVienService;
    private final KhoaService khoaService;
    
    @GetMapping("/dashboard")
    @Transactional(readOnly = true)
    public String dashboard(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        
        if (nguoiDung == null) {
            return "redirect:/auth/login";
        }
        
        Optional<GiangVien> giangVienOpt = giangVienRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        
        model.addAttribute("nguoiDung", nguoiDung);
        
        if (giangVienOpt.isPresent()) {
            GiangVien giangVien = giangVienOpt.get();
            model.addAttribute("giangVien", giangVien);
            // Lấy phân công trực tiếp từ repository để tránh lazy loading
            try {
                var danhSachPhanCong = phanCongRepository.findByGiangVien_GiangVienId(giangVien.getGiangVienId());
                model.addAttribute("danhSachPhanCong", danhSachPhanCong);
                // Tính số học phần unique
                long soHocPhan = danhSachPhanCong != null ? 
                    danhSachPhanCong.stream()
                        .filter(pc -> pc.getHocPhan() != null)
                        .map(pc -> pc.getHocPhan().getHocPhanId())
                        .distinct()
                        .count() : 0;
                model.addAttribute("soHocPhan", soHocPhan);
            } catch (Exception e) {
                model.addAttribute("danhSachPhanCong", Collections.emptyList());
                model.addAttribute("soHocPhan", 0);
            }
        } else {
            model.addAttribute("giangVien", null);
            model.addAttribute("danhSachPhanCong", Collections.emptyList());
            model.addAttribute("soHocPhan", 0);
        }
        
        return "giang-vien/dashboard";
    }
    
    @GetMapping("/thong-tin")
    @Transactional(readOnly = true)
    public String hienThiThongTin(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        Optional<GiangVien> giangVienOpt = giangVienRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (giangVienOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin giảng viên");
            return "redirect:/giang-vien/dashboard";
        }
        
        GiangVien giangVien = giangVienOpt.get();
        
        // Tạo DTO từ entity
        GiangVienCapNhatDTO dto = new GiangVienCapNhatDTO();
        dto.setGiangVienId(giangVien.getGiangVienId());
        dto.setHoTen(giangVien.getHoTen());
        dto.setHocVi(giangVien.getHocVi());
        dto.setKhoaId(giangVien.getKhoa() != null ? giangVien.getKhoa().getKhoaId() : null);
        dto.setSoNamKinhNghiem(giangVien.getSoNamKinhNghiem());
        dto.setNamBatDau(giangVien.getNamBatDau());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("giangVien", giangVien);
        model.addAttribute("dto", dto);
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        
        return "giang-vien/thong-tin";
    }
    
    @PostMapping("/thong-tin")
    @Transactional
    public String capNhatThongTin(@Valid @ModelAttribute("dto") GiangVienCapNhatDTO dto,
                                  BindingResult result,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        Optional<GiangVien> giangVienOpt = giangVienRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (giangVienOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin giảng viên");
            return "redirect:/giang-vien/dashboard";
        }
        
        // Bỏ qua validation cho matKhau khi để trống
        if (dto.getMatKhau() != null && dto.getMatKhau().trim().isEmpty()) {
            dto.setMatKhau(null);
        }
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("giangVien", giangVienOpt.get());
            model.addAttribute("dto", dto);
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "giang-vien/thong-tin";
        }
        
        try {
            giangVienService.capNhatThongTinGiangVien(giangVienOpt.get().getGiangVienId(), dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
            return "redirect:/giang-vien/dashboard";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("giangVien", giangVienOpt.get());
            model.addAttribute("dto", dto);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "giang-vien/thong-tin";
        }
    }
}

