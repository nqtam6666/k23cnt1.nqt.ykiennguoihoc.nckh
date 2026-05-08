package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.NguoiHocCapNhatDTO;
import k23cnt1.nguyenquangtam.nckh.dto.NguoiHocDTO;
import k23cnt1.nguyenquangtam.nckh.entity.Khoa;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiHoc;
import k23cnt1.nguyenquangtam.nckh.repository.KhoaRepository;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiDungRepository;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiHocRepository;
import k23cnt1.nguyenquangtam.nckh.repository.VaiTroRepository;
import k23cnt1.nguyenquangtam.nckh.entity.VaiTro;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiHocService {
    
    private final NguoiHocRepository nguoiHocRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final KhoaRepository khoaRepository;
    private final AuthService authService;
    private final VaiTroRepository vaiTroRepository;
    
    public List<NguoiHoc> layTatCaNguoiHoc() {
        return nguoiHocRepository.findAll(Sort.by(Sort.Direction.ASC, "nguoiHocId"));
    }
    
    public NguoiHoc layNguoiHocTheoId(Long id) {
        return nguoiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người học"));
    }
    
    @Transactional
    public NguoiHoc taoNguoiHoc(NguoiHocDTO dto) {
        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(dto.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        // Tạo người dùng
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(dto.getTenDangNhap());
        nguoiDung.setMatKhau(authService.hashPassword(dto.getMatKhau()));
        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro("ROLE_NGUOI_HOC")
            .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò người học."));
        nguoiDung.getDanhSachVaiTro().add(vaiTro);
        nguoiDung.setTrangThai(true);
        nguoiDung = nguoiDungRepository.save(nguoiDung);
        
        // Lấy khoa
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        
        // Tạo người học
        NguoiHoc nguoiHoc = new NguoiHoc();
        nguoiHoc.setNguoiDung(nguoiDung);
        nguoiHoc.setGioiTinh(dto.getGioiTinh());
        nguoiHoc.setNamSinh(dto.getNamSinh());
        nguoiHoc.setKhoa(khoa);
        nguoiHoc.setNganhHoc(dto.getNganhHoc());
        nguoiHoc.setKhoaHoc(dto.getKhoaHoc());
        
        return nguoiHocRepository.save(nguoiHoc);
    }
    
    @Transactional
    public NguoiHoc capNhatNguoiHoc(Long id, NguoiHocDTO dto) {
        NguoiHoc nguoiHoc = nguoiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người học"));
        
        nguoiHoc.setGioiTinh(dto.getGioiTinh());
        nguoiHoc.setNamSinh(dto.getNamSinh());
        nguoiHoc.setNganhHoc(dto.getNganhHoc());
        nguoiHoc.setKhoaHoc(dto.getKhoaHoc());
        
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        nguoiHoc.setKhoa(khoa);
        
        // Cập nhật mật khẩu nếu có
        if (dto.getMatKhau() != null && !dto.getMatKhau().isEmpty()) {
            nguoiHoc.getNguoiDung().setMatKhau(authService.hashPassword(dto.getMatKhau()));
        }
        
        return nguoiHocRepository.save(nguoiHoc);
    }
    
    @Transactional
    public NguoiHoc capNhatThongTinNguoiHoc(Long id, NguoiHocCapNhatDTO dto) {
        NguoiHoc nguoiHoc = nguoiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người học"));
        
        nguoiHoc.setGioiTinh(dto.getGioiTinh());
        nguoiHoc.setNamSinh(dto.getNamSinh());
        nguoiHoc.setNganhHoc(dto.getNganhHoc());
        nguoiHoc.setKhoaHoc(dto.getKhoaHoc());
        
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        nguoiHoc.setKhoa(khoa);
        
        // Cập nhật mật khẩu nếu có
        if (dto.getMatKhau() != null && !dto.getMatKhau().trim().isEmpty()) {
            nguoiHoc.getNguoiDung().setMatKhau(authService.hashPassword(dto.getMatKhau()));
        }
        
        return nguoiHocRepository.save(nguoiHoc);
    }
    
    @Transactional
    public void xoaNguoiHoc(Long id) {
        NguoiHoc nguoiHoc = nguoiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người học"));
        
        // Khóa tài khoản thay vì xóa
        nguoiHoc.getNguoiDung().setTrangThai(false);
        nguoiDungRepository.save(nguoiHoc.getNguoiDung());
    }
    
    /**
     * Tự động tạo NguoiHoc cho user có vai trò nguoi_hoc nhưng chưa có record
     * Sử dụng khoa đầu tiên trong database làm mặc định
     */
    @Transactional
    public NguoiHoc taoNguoiHocTuDong(NguoiDung nguoiDung) {
        // Kiểm tra đã có NguoiHoc chưa
        Optional<NguoiHoc> existing = nguoiHocRepository.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Lấy khoa đầu tiên làm mặc định (hoặc tạo khoa mặc định nếu chưa có)
        List<Khoa> danhSachKhoa = khoaRepository.findAll();
        Khoa khoaMacDinh;
        if (danhSachKhoa.isEmpty()) {
            // Tạo khoa mặc định nếu chưa có
            khoaMacDinh = new Khoa();
            khoaMacDinh.setTenKhoa("Khoa Mặc định");
            khoaMacDinh.setMoTa("Khoa mặc định được tạo tự động");
            khoaMacDinh = khoaRepository.save(khoaMacDinh);
        } else {
            khoaMacDinh = danhSachKhoa.get(0);
        }
        
        // Tạo NguoiHoc với thông tin mặc định
        NguoiHoc nguoiHoc = new NguoiHoc();
        nguoiHoc.setNguoiDung(nguoiDung);
        nguoiHoc.setGioiTinh(true); // Mặc định là nam
        nguoiHoc.setNamSinh(2000); // Mặc định năm sinh
        nguoiHoc.setKhoa(khoaMacDinh);
        nguoiHoc.setNganhHoc("Chưa cập nhật");
        nguoiHoc.setKhoaHoc("Chưa cập nhật");
        
        return nguoiHocRepository.save(nguoiHoc);
    }
}

