package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.NguoiHocCapNhatDTO;
import k23cnt1.nguyenquangtam.nckh.dto.PhanHoiKhaoSatDTO;
import k23cnt1.nguyenquangtam.nckh.entity.CauHoi;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiHoc;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiHocRepository;
import k23cnt1.nguyenquangtam.nckh.service.CauHoiService;
import k23cnt1.nguyenquangtam.nckh.service.KhaoSatService;
import k23cnt1.nguyenquangtam.nckh.service.KhoaService;
import k23cnt1.nguyenquangtam.nckh.service.NguoiHocService;
import k23cnt1.nguyenquangtam.nckh.service.PhanHoiKhaoSatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/nguoi-hoc")
@RequiredArgsConstructor
public class NguoiHocController {
    
    private final NguoiHocRepository nguoiHocRepository;
    private final NguoiHocService nguoiHocService;
    private final KhaoSatService khaoSatService;
    private final CauHoiService cauHoiService;
    private final PhanHoiKhaoSatService phanHoiKhaoSatService;
    private final KhoaService khoaService;
    
    @GetMapping("/dashboard")
    @Transactional(readOnly = true)
    public String dashboard(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        
        if (nguoiDung == null) {
            return "redirect:/auth/login";
        }
        
        Optional<NguoiHoc> nguoiHocOpt = nguoiHocRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        
        model.addAttribute("nguoiDung", nguoiDung);
        
        if (nguoiHocOpt.isPresent()) {
            NguoiHoc nguoiHoc = nguoiHocOpt.get();
            model.addAttribute("nguoiHoc", nguoiHoc);
            // Lấy lịch sử phản hồi
            model.addAttribute("lichSuPhanHoi", phanHoiKhaoSatService.layLichSuPhanHoi(nguoiHoc.getNguoiHocId()));
        } else {
            model.addAttribute("nguoiHoc", null);
            model.addAttribute("lichSuPhanHoi", Collections.emptyList());
        }
        
        try {
            var danhSachKhaoSat = khaoSatService.layTatCaKhaoSat();
            model.addAttribute("danhSachKhaoSat", danhSachKhaoSat);
            
            // Tạo map để lưu trạng thái đã thực hiện của từng khảo sát
            int soLuongDaHoanThanh = 0;
            if (nguoiHocOpt.isPresent()) {
                java.util.Map<Long, Boolean> trangThaiKhaoSat = new java.util.HashMap<>();
                for (var khaoSat : danhSachKhaoSat) {
                    boolean daThucHien = phanHoiKhaoSatService.daThucHienKhaoSat(
                        nguoiHocOpt.get().getNguoiHocId(), 
                        khaoSat.getKhaoSatId()
                    );
                    trangThaiKhaoSat.put(khaoSat.getKhaoSatId(), daThucHien);
                    if (daThucHien) {
                        soLuongDaHoanThanh++;
                    }
                }
                model.addAttribute("trangThaiKhaoSat", trangThaiKhaoSat);
            }
            model.addAttribute("soLuongDaHoanThanh", soLuongDaHoanThanh);
            model.addAttribute("soLuongChuaHoanThanh", danhSachKhaoSat.size() - soLuongDaHoanThanh);
        } catch (Exception e) {
            model.addAttribute("danhSachKhaoSat", Collections.emptyList());
            model.addAttribute("trangThaiKhaoSat", Collections.emptyMap());
            model.addAttribute("soLuongDaHoanThanh", 0);
            model.addAttribute("soLuongChuaHoanThanh", 0);
        }
        
        return "nguoi-hoc/dashboard";
    }
    
    @GetMapping("/khao-sat/{id}")
    @Transactional
    public String hienThiKhaoSat(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        Optional<NguoiHoc> nguoiHocOpt = nguoiHocRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (nguoiHocOpt.isEmpty()) {
            // Tự động tạo NguoiHoc nếu user có vai trò nguoi_hoc nhưng chưa có record
            if ("nguoi_hoc".equals(nguoiDung.getVaiTro())) {
                System.out.println("DEBUG: Auto-creating NguoiHoc for user: " + nguoiDung.getTenDangNhap());
                nguoiHocOpt = Optional.of(nguoiHocService.taoNguoiHocTuDong(nguoiDung));
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người học");
                return "redirect:/nguoi-hoc/dashboard";
            }
        }
        
        try {
            System.out.println("DEBUG: Loading khaoSat with id: " + id);
            
            // Kiểm tra xem khảo sát có tồn tại không
            var khaoSatOpt = khaoSatService.layKhaoSatTheoIdOptional(id);
            if (khaoSatOpt.isEmpty()) {
                System.out.println("DEBUG: KhaoSat with id " + id + " not found");
                redirectAttributes.addFlashAttribute("error", "Khảo sát không tồn tại hoặc đã bị xóa.");
                return "redirect:/nguoi-hoc/dashboard";
            }
            
            var khaoSat = khaoSatOpt.get();
            System.out.println("DEBUG: KhaoSat loaded: " + khaoSat.getTenKhaoSat());
            
            System.out.println("DEBUG: Loading cauHoi for khaoSatId: " + id);
            var danhSachCauHoi = cauHoiService.layCauHoiTheoKhaoSat(id);
            System.out.println("DEBUG: Number of cauHoi loaded: " + (danhSachCauHoi != null ? danhSachCauHoi.size() : 0));
            
            // Nhóm câu hỏi theo NhomDichVu để hiển thị giống Google Docs
            // Và tính toán số thứ tự cho mỗi câu hỏi
            java.util.Map<Long, java.util.List<CauHoi>> cauHoiTheoNhom = new java.util.LinkedHashMap<>();
            java.util.Map<Long, Integer> soThuTuCauHoi = new java.util.HashMap<>(); // Map cauHoiId -> số thứ tự
            
            if (danhSachCauHoi != null && !danhSachCauHoi.isEmpty()) {
                System.out.println("DEBUG: Grouping cauHoi by NhomDichVu");
                cauHoiTheoNhom = danhSachCauHoi.stream()
                        .filter(cauHoi -> cauHoi.getNhomDichVu() != null) // Lọc bỏ câu hỏi không có nhóm
                        .collect(java.util.stream.Collectors.groupingBy(
                                cauHoi -> cauHoi.getNhomDichVu().getNhomDichVuId(),
                                java.util.LinkedHashMap::new,
                                java.util.stream.Collectors.toList()
                        ));
                System.out.println("DEBUG: Number of groups: " + cauHoiTheoNhom.size());
                
                // Tính số thứ tự liên tục cho tất cả câu hỏi
                int soThuTu = 1;
                for (java.util.List<CauHoi> nhomCauHoi : cauHoiTheoNhom.values()) {
                    for (CauHoi cauHoi : nhomCauHoi) {
                        soThuTuCauHoi.put(cauHoi.getCauHoiId(), soThuTu++);
                    }
                }
                System.out.println("DEBUG: Total questions numbered: " + soThuTuCauHoi.size());
            }
            
            // Kiểm tra xem đã thực hiện khảo sát chưa
            System.out.println("DEBUG: Checking if survey already completed");
            boolean daThucHien = phanHoiKhaoSatService.daThucHienKhaoSat(nguoiHocOpt.get().getNguoiHocId(), id);
            var phanHoiDaThucHien = phanHoiKhaoSatService.layPhanHoiTheoNguoiHocVaKhaoSat(nguoiHocOpt.get().getNguoiHocId(), id);
            System.out.println("DEBUG: Survey completed: " + daThucHien);
            
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("khaoSat", khaoSat);
            model.addAttribute("danhSachCauHoi", danhSachCauHoi != null ? danhSachCauHoi : java.util.Collections.emptyList()); // Giữ lại để tương thích
            model.addAttribute("cauHoiTheoNhom", cauHoiTheoNhom); // Map nhóm câu hỏi
            model.addAttribute("soThuTuCauHoi", soThuTuCauHoi); // Map cauHoiId -> số thứ tự
            model.addAttribute("phanHoiDTO", new PhanHoiKhaoSatDTO());
            model.addAttribute("daThucHien", daThucHien);
            model.addAttribute("phanHoiDaThucHien", phanHoiDaThucHien.orElse(null));
            
            System.out.println("DEBUG: All attributes set, returning view");
        } catch (Exception e) {
            System.err.println("ERROR in hienThiKhaoSat: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace(); // Log error for debugging
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khảo sát: " + e.getMessage());
            return "redirect:/nguoi-hoc/dashboard";
        }
        
        return "nguoi-hoc/khao-sat";
    }
    
    @PostMapping("/khao-sat/submit")
    @Transactional
    public String submitKhaoSat(@RequestParam(value = "khaoSatId", required = false) Long khaoSatId,
                               HttpServletRequest request,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        Optional<NguoiHoc> nguoiHocOpt = nguoiHocRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (nguoiHocOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người học");
            return "redirect:/nguoi-hoc/dashboard";
        }
        
        PhanHoiKhaoSatDTO dto = new PhanHoiKhaoSatDTO();
        Long finalKhaoSatId = khaoSatId;
        
        try {
            // Lấy tất cả parameters từ request
            Map<String, String[]> parameterMap = request.getParameterMap();
            
            // Debug: Log tất cả parameters
            System.out.println("DEBUG - All parameter names: " + parameterMap.keySet());
            
            // Tạo DTO từ request parameters
            java.util.Map<Long, Integer> traLoi = new java.util.HashMap<>();
            java.util.Map<Long, String> yKienKhac = new java.util.HashMap<>();
            
            // Parse tất cả parameters
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                String value = (values != null && values.length > 0) ? values[0] : null;
                
                System.out.println("DEBUG - Processing key: " + key + " = " + value);
                
                if (key.equals("khaoSatId")) {
                    try {
                        if (value != null) {
                            dto.setKhaoSatId(Long.parseLong(value));
                            finalKhaoSatId = dto.getKhaoSatId();
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid
                    }
                } else if (key.startsWith("traLoi[")) {
                    String cauHoiIdStr = key.substring(7, key.length() - 1);
                    try {
                        Long cauHoiId = Long.parseLong(cauHoiIdStr);
                        if (value != null) {
                            Integer diemDanhGia = Integer.parseInt(value);
                            traLoi.put(cauHoiId, diemDanhGia);
                            System.out.println("DEBUG - Added traLoi: " + cauHoiId + " = " + diemDanhGia);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("DEBUG - Error parsing traLoi: " + key + " = " + value);
                    }
                } else if (key.startsWith("yKienKhac[")) {
                    try {
                        // Extract cauHoiId from key like "yKienKhac[1]"
                        // Tìm vị trí dấu [ và ]
                        int openBracket = key.indexOf('[');
                        int closeBracket = key.indexOf(']', openBracket);
                        
                        if (openBracket >= 0 && closeBracket > openBracket) {
                            String cauHoiIdStr = key.substring(openBracket + 1, closeBracket);
                            System.out.println("DEBUG - Extracted cauHoiIdStr from '" + key + "': '" + cauHoiIdStr + "' (openBracket: " + openBracket + ", closeBracket: " + closeBracket + ")");
                            
                            if (!cauHoiIdStr.isEmpty()) {
                                Long cauHoiId = Long.parseLong(cauHoiIdStr);
                                
                                // Lưu giá trị nếu có (trim để loại bỏ khoảng trắng thừa)
                                // Lưu cả khi giá trị là số hoặc text
                                if (value != null) {
                                    String trimmedValue = value.trim();
                                    if (!trimmedValue.isEmpty()) {
                                        yKienKhac.put(cauHoiId, trimmedValue);
                                        System.out.println("DEBUG - Added yKienKhac: " + cauHoiId + " = '" + trimmedValue + "'");
                                    } else {
                                        System.out.println("DEBUG - yKienKhac is empty (whitespace only) for cauHoiId: " + cauHoiId);
                                    }
                                } else {
                                    System.out.println("DEBUG - yKienKhac value is null for cauHoiId: " + cauHoiId);
                                }
                            } else {
                                System.out.println("DEBUG - cauHoiIdStr is empty from key: " + key);
                            }
                        } else {
                            System.out.println("DEBUG - Invalid yKienKhac key format: " + key + ", openBracket: " + openBracket + ", closeBracket: " + closeBracket);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("DEBUG - NumberFormatException parsing yKienKhac cauHoiId from key: " + key + ", Exception: " + e.getMessage());
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("DEBUG - General Exception parsing yKienKhac: " + key + " = " + value + ", Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            
            // Nếu không có khaoSatId từ form, lấy từ parameter
            if (dto.getKhaoSatId() == null && khaoSatId != null) {
                dto.setKhaoSatId(khaoSatId);
                finalKhaoSatId = khaoSatId;
            }
            
            if (dto.getKhaoSatId() == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin khảo sát");
                return "redirect:/nguoi-hoc/dashboard";
            }
            
            if (traLoi.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng trả lời ít nhất một câu hỏi");
                return "redirect:/nguoi-hoc/khao-sat/" + dto.getKhaoSatId();
            }
            
            dto.setTraLoi(traLoi);
            dto.setYKienKhac(yKienKhac);
            
            // Debug: Log yKienKhac để kiểm tra
            System.out.println("DEBUG - yKienKhac map: " + yKienKhac);
            
            phanHoiKhaoSatService.thucHienKhaoSat(nguoiHocOpt.get().getNguoiHocId(), dto);
            redirectAttributes.addFlashAttribute("success", "Gửi khảo sát thành công! Cảm ơn bạn đã tham gia.");
            return "redirect:/nguoi-hoc/dashboard";
        } catch (Exception e) {
            e.printStackTrace(); // Log error for debugging
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            if (finalKhaoSatId == null) {
                return "redirect:/nguoi-hoc/dashboard";
            }
            return "redirect:/nguoi-hoc/khao-sat/" + finalKhaoSatId;
        }
    }
    
    @GetMapping("/thong-tin")
    @Transactional(readOnly = true)
    public String hienThiThongTin(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        Optional<NguoiHoc> nguoiHocOpt = nguoiHocRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (nguoiHocOpt.isEmpty()) {
            // Tự động tạo NguoiHoc nếu user có vai trò nguoi_hoc nhưng chưa có record
            if ("nguoi_hoc".equals(nguoiDung.getVaiTro())) {
                nguoiHocOpt = Optional.of(nguoiHocService.taoNguoiHocTuDong(nguoiDung));
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người học");
                return "redirect:/nguoi-hoc/dashboard";
            }
        }
        
        NguoiHoc nguoiHoc = nguoiHocOpt.get();
        
        // Tạo DTO từ entity
        NguoiHocCapNhatDTO dto = new NguoiHocCapNhatDTO();
        dto.setNguoiHocId(nguoiHoc.getNguoiHocId());
        dto.setGioiTinh(nguoiHoc.getGioiTinh());
        dto.setNamSinh(nguoiHoc.getNamSinh());
        dto.setKhoaId(nguoiHoc.getKhoa() != null ? nguoiHoc.getKhoa().getKhoaId() : null);
        dto.setNganhHoc(nguoiHoc.getNganhHoc());
        dto.setKhoaHoc(nguoiHoc.getKhoaHoc());
        
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("nguoiHoc", nguoiHoc);
        model.addAttribute("dto", dto);
        model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
        
        return "nguoi-hoc/thong-tin";
    }
    
    @PostMapping("/thong-tin")
    @Transactional
    public String capNhatThongTin(@Valid @ModelAttribute("dto") NguoiHocCapNhatDTO dto,
                                  BindingResult result,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        
        Optional<NguoiHoc> nguoiHocOpt = nguoiHocRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (nguoiHocOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người học");
            return "redirect:/nguoi-hoc/dashboard";
        }
        
        // Bỏ qua validation cho matKhau khi để trống
        if (dto.getMatKhau() != null && dto.getMatKhau().trim().isEmpty()) {
            dto.setMatKhau(null);
        }
        
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("nguoiHoc", nguoiHocOpt.get());
            model.addAttribute("dto", dto);
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "nguoi-hoc/thong-tin";
        }
        
        try {
            nguoiHocService.capNhatThongTinNguoiHoc(nguoiHocOpt.get().getNguoiHocId(), dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
            return "redirect:/nguoi-hoc/dashboard";
        } catch (Exception e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("nguoiHoc", nguoiHocOpt.get());
            model.addAttribute("dto", dto);
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("danhSachKhoa", khoaService.layTatCaKhoa());
            return "nguoi-hoc/thong-tin";
        }
    }
}

