package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.GiangVienCapNhatDTO;
import k23cnt1.nguyenquangtam.nckh.dto.GiangVienDTO;
import k23cnt1.nguyenquangtam.nckh.entity.GiangVien;
import k23cnt1.nguyenquangtam.nckh.entity.Khoa;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.repository.GiangVienRepository;
import k23cnt1.nguyenquangtam.nckh.repository.KhoaRepository;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiDungRepository;
import k23cnt1.nguyenquangtam.nckh.util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiangVienService {
    
    private final GiangVienRepository giangVienRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final KhoaRepository khoaRepository;
    private final AuthService authService;
    
    public List<GiangVien> layTatCaGiangVien() {
        return giangVienRepository.findAll(Sort.by(Sort.Direction.ASC, "giangVienId"));
    }
    
    @Transactional(readOnly = true)
    public PageInfo<GiangVien> timKiemVaPhanTrang(String keyword, Long khoaId, String hocVi, int page, int size) {
        String keywordNormalized = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        Long khoaIdNormalized = (khoaId != null && khoaId > 0) ? khoaId : null;
        String hocViNormalized = (hocVi != null && !hocVi.trim().isEmpty() && !hocVi.equals("all")) ? hocVi.trim() : null;
        
        long total = giangVienRepository.demTimKiemVaLoc(keywordNormalized, khoaIdNormalized, hocViNormalized);
        List<GiangVien> allResults = giangVienRepository.timKiemVaLoc(keywordNormalized, khoaIdNormalized, hocViNormalized);
        
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        List<GiangVien> pagedResults = (start < allResults.size()) ? allResults.subList(start, end) : java.util.Collections.emptyList();
        
        return PageInfo.of(pagedResults, page, size, total);
    }
    
    public GiangVien layGiangVienTheoId(Long id) {
        return giangVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
    }
    
    @Transactional
    public GiangVien taoGiangVien(GiangVienDTO dto) {
        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(dto.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        // Tạo người dùng
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(dto.getTenDangNhap());
        nguoiDung.setMatKhau(authService.hashPassword(dto.getMatKhau()));
        nguoiDung.setVaiTro("giang_vien");
        nguoiDung.setTrangThai(true);
        nguoiDung = nguoiDungRepository.save(nguoiDung);
        
        // Lấy khoa
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        
        // Tạo giảng viên
        GiangVien giangVien = new GiangVien();
        giangVien.setNguoiDung(nguoiDung);
        giangVien.setHoTen(dto.getHoTen());
        giangVien.setHocVi(dto.getHocVi());
        giangVien.setKhoa(khoa);
        giangVien.setSoNamKinhNghiem(dto.getSoNamKinhNghiem());
        giangVien.setNamBatDau(dto.getNamBatDau());
        
        return giangVienRepository.save(giangVien);
    }
    
    @Transactional
    public GiangVien capNhatGiangVien(Long id, GiangVienDTO dto) {
        GiangVien giangVien = giangVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
        
        giangVien.setHoTen(dto.getHoTen());
        giangVien.setHocVi(dto.getHocVi());
        giangVien.setSoNamKinhNghiem(dto.getSoNamKinhNghiem());
        giangVien.setNamBatDau(dto.getNamBatDau());
        
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        giangVien.setKhoa(khoa);
        
        // Cập nhật mật khẩu nếu có
        if (dto.getMatKhau() != null && !dto.getMatKhau().isEmpty()) {
            giangVien.getNguoiDung().setMatKhau(authService.hashPassword(dto.getMatKhau()));
        }
        
        return giangVienRepository.save(giangVien);
    }
    
    @Transactional
    public GiangVien capNhatThongTinGiangVien(Long id, GiangVienCapNhatDTO dto) {
        GiangVien giangVien = giangVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
        
        giangVien.setHoTen(dto.getHoTen());
        giangVien.setHocVi(dto.getHocVi());
        giangVien.setSoNamKinhNghiem(dto.getSoNamKinhNghiem());
        giangVien.setNamBatDau(dto.getNamBatDau());
        
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        giangVien.setKhoa(khoa);
        
        // Cập nhật mật khẩu nếu có
        if (dto.getMatKhau() != null && !dto.getMatKhau().trim().isEmpty()) {
            giangVien.getNguoiDung().setMatKhau(authService.hashPassword(dto.getMatKhau()));
        }
        
        return giangVienRepository.save(giangVien);
    }
    
    @Transactional
    public void xoaGiangVien(Long id) {
        GiangVien giangVien = giangVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
        
        // Khóa tài khoản thay vì xóa
        giangVien.getNguoiDung().setTrangThai(false);
        nguoiDungRepository.save(giangVien.getNguoiDung());
    }
}

