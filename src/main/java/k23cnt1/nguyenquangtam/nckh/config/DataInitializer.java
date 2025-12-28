package k23cnt1.nguyenquangtam.nckh.config;

import k23cnt1.nguyenquangtam.nckh.entity.DichVuDaoTao;
import k23cnt1.nguyenquangtam.nckh.entity.LoaiPhanHoi;
import k23cnt1.nguyenquangtam.nckh.repository.DichVuDaoTaoRepository;
import k23cnt1.nguyenquangtam.nckh.repository.LoaiPhanHoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final DichVuDaoTaoRepository dichVuDaoTaoRepository;
    private final LoaiPhanHoiRepository loaiPhanHoiRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Khởi tạo dữ liệu mẫu cho Dịch vụ Đào tạo
        if (dichVuDaoTaoRepository.count() == 0) {
            DichVuDaoTao dv1 = new DichVuDaoTao();
            dv1.setTenDichVu("Đào tạo Chuyên ngành");
            dv1.setMoTa("Chương trình đào tạo các chuyên ngành theo chuẩn quốc tế");
            dv1.setTrangThai(true);
            dichVuDaoTaoRepository.save(dv1);
            
            DichVuDaoTao dv2 = new DichVuDaoTao();
            dv2.setTenDichVu("Thư viện và Tài liệu học tập");
            dv2.setMoTa("Dịch vụ thư viện, tài liệu điện tử và không gian học tập");
            dv2.setTrangThai(true);
            dichVuDaoTaoRepository.save(dv2);
            
            DichVuDaoTao dv3 = new DichVuDaoTao();
            dv3.setTenDichVu("Cơ sở vật chất và Phòng học");
            dv3.setMoTa("Phòng học, phòng thực hành, phòng máy tính");
            dv3.setTrangThai(true);
            dichVuDaoTaoRepository.save(dv3);
            
            DichVuDaoTao dv4 = new DichVuDaoTao();
            dv4.setTenDichVu("Hỗ trợ Sinh viên");
            dv4.setMoTa("Dịch vụ tư vấn, hỗ trợ học tập và đời sống sinh viên");
            dv4.setTrangThai(true);
            dichVuDaoTaoRepository.save(dv4);
            
            DichVuDaoTao dv5 = new DichVuDaoTao();
            dv5.setTenDichVu("Hoạt động Ngoại khóa");
            dv5.setMoTa("Các hoạt động ngoại khóa, câu lạc bộ, sự kiện");
            dv5.setTrangThai(true);
            dichVuDaoTaoRepository.save(dv5);
        }
        
        // Khởi tạo dữ liệu mẫu cho Loại Phản hồi
        if (loaiPhanHoiRepository.count() == 0) {
            LoaiPhanHoi loai1 = new LoaiPhanHoi();
            loai1.setTenLoai("Góp ý");
            loai1.setMoTa("Đóng góp ý kiến để cải thiện dịch vụ");
            loai1.setTrangThai(true);
            loaiPhanHoiRepository.save(loai1);
            
            LoaiPhanHoi loai2 = new LoaiPhanHoi();
            loai2.setTenLoai("Khiếu nại");
            loai2.setMoTa("Khiếu nại về chất lượng dịch vụ");
            loai2.setTrangThai(true);
            loaiPhanHoiRepository.save(loai2);
            
            LoaiPhanHoi loai3 = new LoaiPhanHoi();
            loai3.setTenLoai("Đề xuất");
            loai3.setMoTa("Đề xuất cải tiến hoặc tính năng mới");
            loai3.setTrangThai(true);
            loaiPhanHoiRepository.save(loai3);
            
            LoaiPhanHoi loai4 = new LoaiPhanHoi();
            loai4.setTenLoai("Khen ngợi");
            loai4.setMoTa("Ghi nhận những điểm tích cực của dịch vụ");
            loai4.setTrangThai(true);
            loaiPhanHoiRepository.save(loai4);
        }
    }
}

