package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final NguoiDungRepository nguoiDungRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public Optional<NguoiDung> authenticate(String tenDangNhap, String matKhau) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByTenDangNhapAndTrangThai(tenDangNhap, true);
        
        if (nguoiDung.isPresent()) {
            // BCrypt tự động so sánh password với hash đã lưu
            if (passwordEncoder.matches(matKhau, nguoiDung.get().getMatKhau())) {
                return nguoiDung;
            }
        }
        
        return Optional.empty();
    }
    
    public String hashPassword(String password) {
        // BCrypt tự động tạo salt và hash password
        return passwordEncoder.encode(password);
    }
    
    public boolean hasRole(NguoiDung nguoiDung, String role) {
        return nguoiDung.getVaiTro().equals(role);
    }
    
    public boolean isAdmin(NguoiDung nguoiDung) {
        return hasRole(nguoiDung, "admin");
    }
    
    public boolean isGiangVien(NguoiDung nguoiDung) {
        return hasRole(nguoiDung, "giang_vien");
    }
    
    public boolean isNguoiHoc(NguoiDung nguoiDung) {
        return hasRole(nguoiDung, "nguoi_hoc");
    }
    
    @org.springframework.transaction.annotation.Transactional
    public NguoiDung dangKy(String tenDangNhap, String matKhau) {
        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(tenDangNhap)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        // Tạo người dùng mới với vai trò mặc định là nguoi_hoc
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(tenDangNhap);
        nguoiDung.setMatKhau(hashPassword(matKhau));
        nguoiDung.setVaiTro("nguoi_hoc"); // Mặc định là sinh viên
        nguoiDung.setTrangThai(true); // Mặc định là hoạt động
        
        return nguoiDungRepository.save(nguoiDung);
    }
}

