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
    private final HocPhanRepository hocPhanRepository;
    private final NhomDichVuRepository nhomDichVuRepository;
    private final AuthService authService;
    
    @Override
    public void run(String... args) throws Exception {
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
        if (nguoiDungRepository.count() == 0) {
            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap("admin");
            admin.setMatKhau(authService.hashPassword("admin123"));
            admin.setVaiTro("admin");
            admin.setTrangThai(true);
            admin = nguoiDungRepository.save(admin);
            
            QuanTri quanTri = new QuanTri();
            quanTri.setNguoiDung(admin);
            quanTri.setHoTen("Quản trị viên");
            quanTri.setGhiChu("Admin hệ thống");
            quanTriRepository.save(quanTri);
            
            // Tạo Giảng viên
            NguoiDung gv = new NguoiDung();
            gv.setTenDangNhap("giangvien");
            gv.setMatKhau(authService.hashPassword("gv123"));
            gv.setVaiTro("giang_vien");
            gv.setTrangThai(true);
            gv = nguoiDungRepository.save(gv);
            
            GiangVien giangVien = new GiangVien();
            giangVien.setNguoiDung(gv);
            giangVien.setHoTen("Nguyễn Văn A");
            giangVien.setHocVi("Thạc sĩ");
            giangVien.setKhoa(khoaCNTT);
            giangVien.setSoNamKinhNghiem(5);
            giangVienRepository.save(giangVien);
            
            // Tạo Người học
            NguoiDung nh = new NguoiDung();
            nh.setTenDangNhap("sinhvien");
            nh.setMatKhau(authService.hashPassword("sv123"));
            nh.setVaiTro("nguoi_hoc");
            nh.setTrangThai(true);
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
}

