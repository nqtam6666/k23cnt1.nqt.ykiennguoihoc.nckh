package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.entity.VaiTro;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiDungRepository;
import k23cnt1.nguyenquangtam.nckh.repository.VaiTroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    @Transactional
    public NguoiDung dangKy(String tenDangNhap, String matKhau) {
        if (nguoiDungRepository.existsByTenDangNhap(tenDangNhap)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(tenDangNhap);
        nguoiDung.setMatKhau(hashPassword(matKhau));
        nguoiDung.setTrangThai(true);
        
        // Gán vai trò mặc định ROLE_NGUOI_HOC
        VaiTro vaiTroNguoiHoc = vaiTroRepository.findByTenVaiTro("ROLE_NGUOI_HOC")
            .orElseThrow(() -> new RuntimeException("Lỗi cấu hình: Không tìm thấy vai trò người học."));
        nguoiDung.getDanhSachVaiTro().add(vaiTroNguoiHoc);
        
        return nguoiDungRepository.save(nguoiDung);
    }
}

