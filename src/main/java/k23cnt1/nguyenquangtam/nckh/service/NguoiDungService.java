package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.NguoiDungDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiDungRepository;
import k23cnt1.nguyenquangtam.nckh.util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import k23cnt1.nguyenquangtam.nckh.repository.VaiTroRepository;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    
    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final AuthService authService;
    
    public List<NguoiDung> layTatCaNguoiDung() {
        return nguoiDungRepository.findAll(Sort.by(Sort.Direction.ASC, "nguoiDungId"));
    }
    
    @Transactional(readOnly = true)
    public PageInfo<NguoiDung> timKiemVaPhanTrang(String keyword, String vaiTro, Boolean trangThai, int page, int size) {
        // Chuẩn hóa parameters
        String keywordNormalized = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String vaiTroNormalized = (vaiTro != null && !vaiTro.trim().isEmpty() && !vaiTro.equals("all")) ? vaiTro.trim() : null;
        if (vaiTroNormalized != null) {
            vaiTroNormalized = mapToRoleName(vaiTroNormalized);
        }
        Boolean trangThaiNormalized = trangThai != null ? trangThai : null;
        
        // Lấy tổng số records
        long total = nguoiDungRepository.demTimKiemVaLoc(keywordNormalized, vaiTroNormalized, trangThaiNormalized);
        
        // Lấy tất cả records (sẽ phân trang trong controller)
        List<NguoiDung> allResults = nguoiDungRepository.timKiemVaLoc(keywordNormalized, vaiTroNormalized, trangThaiNormalized);
        
        // Phân trang thủ công
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        List<NguoiDung> pagedResults = (start < allResults.size()) ? allResults.subList(start, end) : java.util.Collections.emptyList();
        
        return PageInfo.of(pagedResults, page, size, total);
    }
    
    public NguoiDung layNguoiDungTheoId(Long id) {
        return nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }
    
    @Transactional
    public NguoiDung taoNguoiDung(NguoiDungDTO dto) {
        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(dto.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(dto.getTenDangNhap());
        nguoiDung.setMatKhau(authService.hashPassword(dto.getMatKhau()));
        if (dto.getVaiTro() != null && !dto.getVaiTro().isEmpty()) {
            String roleName = mapToRoleName(dto.getVaiTro());
            k23cnt1.nguyenquangtam.nckh.entity.VaiTro vt = vaiTroRepository.findByTenVaiTro(roleName)
                .orElseThrow(() -> new RuntimeException("Vai trò không hợp lệ: " + roleName));
            nguoiDung.getDanhSachVaiTro().add(vt);
        }
        nguoiDung.setTrangThai(dto.getTrangThai());
        
        return nguoiDungRepository.save(nguoiDung);
    }
    
    @Transactional
    public NguoiDung capNhatNguoiDung(Long id, NguoiDungDTO dto) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        if (dto.getVaiTro() != null && !dto.getVaiTro().isEmpty()) {
            String roleName = mapToRoleName(dto.getVaiTro());
            k23cnt1.nguyenquangtam.nckh.entity.VaiTro vt = vaiTroRepository.findByTenVaiTro(roleName)
                .orElseThrow(() -> new RuntimeException("Vai trò không hợp lệ: " + roleName));
            nguoiDung.getDanhSachVaiTro().clear();
            nguoiDung.getDanhSachVaiTro().add(vt);
        }
        
        nguoiDung.setTrangThai(dto.getTrangThai());
        
        // Cập nhật mật khẩu nếu có
        if (dto.getMatKhau() != null && !dto.getMatKhau().isEmpty()) {
            nguoiDung.setMatKhau(authService.hashPassword(dto.getMatKhau()));
        }
        
        return nguoiDungRepository.save(nguoiDung);
    }
    
    @Transactional
    public void xoaNguoiDung(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        // Khóa tài khoản thay vì xóa
        nguoiDung.setTrangThai(false);
        nguoiDungRepository.save(nguoiDung);
    }
    
    @Transactional
    public java.util.Map<String, Object> themHangLoat(String danhSachTaiKhoan, String matKhauChung, String vaiTro) {
        java.util.List<String> taiKhoanList = java.util.Arrays.stream(danhSachTaiKhoan.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        
        if (taiKhoanList.isEmpty()) {
            throw new RuntimeException("Danh sách tài khoản không được rỗng");
        }
        
        int thanhCong = 0;
        int thatBai = 0;
        java.util.List<String> loiList = new java.util.ArrayList<>();
        
        String matKhauHash = authService.hashPassword(matKhauChung);
        
        for (String tenDangNhap : taiKhoanList) {
            try {
                // Kiểm tra tên đăng nhập đã tồn tại chưa
                if (nguoiDungRepository.existsByTenDangNhap(tenDangNhap)) {
                    thatBai++;
                    loiList.add("Tài khoản '" + tenDangNhap + "' đã tồn tại");
                    continue;
                }
                
                // Tạo người dùng mới
                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.setTenDangNhap(tenDangNhap);
                nguoiDung.setMatKhau(matKhauHash);
                String vaiTroStr = vaiTro != null && !vaiTro.isEmpty() ? vaiTro : "nguoi_hoc";
                String roleName = mapToRoleName(vaiTroStr);
                k23cnt1.nguyenquangtam.nckh.entity.VaiTro vt = vaiTroRepository.findByTenVaiTro(roleName)
                    .orElseThrow(() -> new RuntimeException("Vai trò không hợp lệ: " + roleName));
                nguoiDung.getDanhSachVaiTro().add(vt);
                
                nguoiDung.setTrangThai(true);
                
                nguoiDungRepository.save(nguoiDung);
                thanhCong++;
            } catch (Exception e) {
                thatBai++;
                loiList.add("Lỗi khi tạo tài khoản '" + tenDangNhap + "': " + e.getMessage());
            }
        }
        
        java.util.Map<String, Object> ketQua = new java.util.HashMap<>();
        ketQua.put("thanhCong", thanhCong);
        ketQua.put("thatBai", thatBai);
        ketQua.put("tongSo", taiKhoanList.size());
        ketQua.put("loiList", loiList);
        
        return ketQua;
    }
    
    private String mapToRoleName(String frontendRole) {
        if ("admin".equalsIgnoreCase(frontendRole) || "ROLE_ADMIN".equalsIgnoreCase(frontendRole)) return "ROLE_ADMIN";
        if ("giang_vien".equalsIgnoreCase(frontendRole) || "ROLE_GIANG_VIEN".equalsIgnoreCase(frontendRole)) return "ROLE_GIANG_VIEN";
        return "ROLE_NGUOI_HOC";
    }
}

