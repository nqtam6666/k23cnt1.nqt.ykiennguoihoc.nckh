package k23cnt1.nguyenquangtam.nckh.config;

import k23cnt1.nguyenquangtam.nckh.entity.*;
import k23cnt1.nguyenquangtam.nckh.repository.*;
import k23cnt1.nguyenquangtam.nckh.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final NguoiDungRepository nguoiDungRepository;
    private final KhoaRepository khoaRepository;
    private final QuanTriRepository quanTriRepository;
    private final GiangVienRepository giangVienRepository;
    private final NguoiHocRepository nguoiHocRepository;
    private final NhomDichVuRepository nhomDichVuRepository;
    private final VaiTroRepository vaiTroRepository;
    private final QuyenRepository quyenRepository;
    private final AuthService authService;
    
    @Override
    public void run(String... args) throws Exception {
        
        // Tạo Quyền
        Quyen qlKhaoSat = createQuyenIfNotFound("QUAN_LY_KHAO_SAT", "Quản lý khảo sát", "KHAO_SAT", "Mở đợt, chỉnh sửa, xóa khảo sát");
        Quyen qlNguoiDung = createQuyenIfNotFound("QUAN_LY_NGUOI_DUNG", "Quản lý người dùng", "HE_THONG", "Thêm sửa xóa người dùng");
        Quyen thucHienKhaoSat = createQuyenIfNotFound("THUC_HIEN_KHAO_SAT", "Tham gia khảo sát", "KHAO_SAT", "Gửi phản hồi khảo sát");
        Quyen xemPhanCong = createQuyenIfNotFound("XEM_PHAN_CONG", "Xem phân công giảng dạy", "DAO_TAO", "Xem các học phần được phân công");
        Quyen qlVaiTro = createQuyenIfNotFound("QUAN_LY_VAI_TRO", "Quản lý vai trò", "HE_THONG", "Thêm sửa xóa vai trò và phân quyền");

        // Tạo Vai Trò
        VaiTro roleAdmin = createVaiTroIfNotFound("ROLE_ADMIN", "Quản trị viên", false, qlKhaoSat, qlNguoiDung, qlVaiTro);
        VaiTro roleGiangVien = createVaiTroIfNotFound("ROLE_GIANG_VIEN", "Giảng viên", false, xemPhanCong);
        VaiTro roleNguoiHoc = createVaiTroIfNotFound("ROLE_NGUOI_HOC", "Người học", true, thucHienKhaoSat);

        // Tạo Khoa
        Khoa khoaCNTT = null;
        if (khoaRepository.count() == 0) {
            khoaCNTT = new Khoa();
            khoaCNTT.setTenKhoa("Khoa Công nghệ Thông tin");
            khoaCNTT.setMoTa("Khoa đào tạo về Công nghệ Thông tin");
            khoaCNTT = khoaRepository.save(khoaCNTT);
        } else {
            khoaCNTT = khoaRepository.findAll().get(0);
        }
        
        // Tạo Admin
        if (!nguoiDungRepository.existsByTenDangNhap("admin")) {
            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap("admin");
            admin.setMatKhau(authService.hashPassword("admin123"));
            admin.setTrangThai(true);
            admin.getDanhSachVaiTro().add(roleAdmin);
            admin = nguoiDungRepository.save(admin);
            
            QuanTri quanTri = new QuanTri();
            quanTri.setNguoiDung(admin);
            quanTri.setHoTen("Quản trị viên");
            quanTri.setGhiChu("Admin hệ thống");
            quanTriRepository.save(quanTri);
        }
            
        if (!nguoiDungRepository.existsByTenDangNhap("giangvien")) {
            // Tạo Giảng viên
            NguoiDung gv = new NguoiDung();
            gv.setTenDangNhap("giangvien");
            gv.setMatKhau(authService.hashPassword("gv123"));
            gv.setTrangThai(true);
            gv.getDanhSachVaiTro().add(roleGiangVien);
            gv = nguoiDungRepository.save(gv);
            
            GiangVien giangVien = new GiangVien();
            giangVien.setNguoiDung(gv);
            giangVien.setHoTen("Nguyễn Văn A");
            giangVien.setHocVi("Thạc sĩ");
            giangVien.setKhoa(khoaCNTT);
            giangVien.setSoNamKinhNghiem(5);
            giangVienRepository.save(giangVien);
        }
            
        if (!nguoiDungRepository.existsByTenDangNhap("sinhvien")) {
            // Tạo Người học
            NguoiDung nh = new NguoiDung();
            nh.setTenDangNhap("sinhvien");
            nh.setMatKhau(authService.hashPassword("sv123"));
            nh.setTrangThai(true);
            nh.getDanhSachVaiTro().add(roleNguoiHoc);
            nh = nguoiDungRepository.save(nh);
            
            NguoiHoc nguoiHoc = new NguoiHoc();
            nguoiHoc.setNguoiDung(nh);
            nguoiHoc.setGioiTinh(true);
            nguoiHoc.setNamSinh(2003);
            nguoiHoc.setKhoa(khoaCNTT);
            nguoiHoc.setNganhHoc("Công nghệ Thông tin");
            nguoiHoc.setKhoaHoc("K23");
            nguoiHocRepository.save(nguoiHoc);
        }
        
        // Tạo Nhóm Dịch vụ
        if (nhomDichVuRepository.count() == 0) {
            NhomDichVu dv1 = new NhomDichVu();
            dv1.setTenDichVu("Giảng dạy");
            dv1.setMoTa("Đánh giá về chất lượng giảng dạy");
            nhomDichVuRepository.save(dv1);
            
            NhomDichVu dv2 = new NhomDichVu();
            dv2.setTenDichVu("Chương trình đào tạo");
            dv2.setMoTa("Đánh giá về chương trình đào tạo");
            nhomDichVuRepository.save(dv2);
            
            NhomDichVu dv3 = new NhomDichVu();
            dv3.setTenDichVu("Cơ sở vật chất");
            dv3.setMoTa("Đánh giá về cơ sở vật chất");
            nhomDichVuRepository.save(dv3);
            
            NhomDichVu dv4 = new NhomDichVu();
            dv4.setTenDichVu("Hỗ trợ sinh viên");
            dv4.setMoTa("Đánh giá về dịch vụ hỗ trợ sinh viên");
            nhomDichVuRepository.save(dv4);
        }
    }

    private Quyen createQuyenIfNotFound(String maQuyen, String tenQuyen, String nhom, String moTa) {
        return quyenRepository.findByMaQuyen(maQuyen).orElseGet(() -> {
            Quyen quyen = new Quyen();
            quyen.setMaQuyen(maQuyen);
            quyen.setTenQuyen(tenQuyen);
            quyen.setNhom(nhom);
            quyen.setMoTa(moTa);
            return quyenRepository.save(quyen);
        });
    }

    private VaiTro createVaiTroIfNotFound(String tenVaiTro, String moTa, Boolean macDinh, Quyen... quyen) {
        return vaiTroRepository.findByTenVaiTro(tenVaiTro).orElseGet(() -> {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setTenVaiTro(tenVaiTro);
            vaiTro.setMoTa(moTa);
            vaiTro.setMacDinh(macDinh);
            if (quyen != null) {
                for (Quyen q : quyen) {
                    vaiTro.getDanhSachQuyen().add(q);
                }
            }
            return vaiTroRepository.save(vaiTro);
        });
    }
}

