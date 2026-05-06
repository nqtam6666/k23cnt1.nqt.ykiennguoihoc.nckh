package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.PhanHoiKhaoSatDTO;
import k23cnt1.nguyenquangtam.nckh.entity.*;
import k23cnt1.nguyenquangtam.nckh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PhanHoiKhaoSatService {
    
    private final PhanHoiRepository phanHoiRepository;
    private final ChiTietPhanHoiRepository chiTietPhanHoiRepository;
    private final NguoiHocRepository nguoiHocRepository;
    private final KhaoSatRepository khaoSatRepository;
    private final CauHoiRepository cauHoiRepository;
    
    @Transactional
    public PhanHoi thucHienKhaoSat(Long nguoiHocId, PhanHoiKhaoSatDTO dto) {
        // Kiểm tra xem đã thực hiện khảo sát chưa
        if (daThucHienKhaoSat(nguoiHocId, dto.getKhaoSatId())) {
            throw new RuntimeException("Bạn đã thực hiện khảo sát này rồi!");
        }
        
        // Lấy người học
        NguoiHoc nguoiHoc = nguoiHocRepository.findById(nguoiHocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người học"));
        
        // Lấy khảo sát
        KhaoSat khaoSat = khaoSatRepository.findById(dto.getKhaoSatId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát"));
        
        // Tạo phản hồi
        PhanHoi phanHoi = new PhanHoi();
        phanHoi.setNguoiHoc(nguoiHoc);
        phanHoi.setKhaoSat(khaoSat);
        phanHoi = phanHoiRepository.save(phanHoi);
        
        // Tạo chi tiết phản hồi cho từng câu hỏi
        for (Map.Entry<Long, Integer> entry : dto.getTraLoi().entrySet()) {
            Long cauHoiId = entry.getKey();
            Integer diemDanhGia = entry.getValue();
            
            CauHoi cauHoi = cauHoiRepository.findById(cauHoiId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi"));
            
            ChiTietPhanHoi chiTiet = new ChiTietPhanHoi();
            chiTiet.setPhanHoi(phanHoi);
            chiTiet.setCauHoi(cauHoi);
            chiTiet.setDiemDanhGia(diemDanhGia);
            
            // Thêm ý kiến khác nếu có
            if (dto.getYKienKhac() != null && dto.getYKienKhac().containsKey(cauHoiId)) {
                String yKien = dto.getYKienKhac().get(cauHoiId);
                // Chỉ lưu nếu không rỗng sau khi trim
                if (yKien != null && !yKien.trim().isEmpty()) {
                    String trimmedYKien = yKien.trim();
                    chiTiet.setYKienKhac(trimmedYKien);
                    System.out.println("DEBUG - Service: Lưu yKienKhac cho câu hỏi " + cauHoiId + ": '" + trimmedYKien + "'");
                } else {
                    System.out.println("DEBUG - Service: yKienKhac rỗng hoặc null cho câu hỏi " + cauHoiId);
                }
            } else {
                System.out.println("DEBUG - Service: Không có yKienKhac trong map cho câu hỏi " + cauHoiId + ". Map: " + (dto.getYKienKhac() != null ? dto.getYKienKhac().keySet() : "null"));
            }
            
            // Lưu chi tiết
            ChiTietPhanHoi savedChiTiet = chiTietPhanHoiRepository.save(chiTiet);
            System.out.println("DEBUG - Service: Đã lưu ChiTietPhanHoi với ID: " + savedChiTiet.getChiTietId() + ", yKienKhac: '" + savedChiTiet.getYKienKhac() + "'");
            
            chiTietPhanHoiRepository.save(chiTiet);
        }
        
        return phanHoi;
    }
    
    public List<PhanHoi> layLichSuPhanHoi(Long nguoiHocId) {
        return phanHoiRepository.findByNguoiHoc_NguoiHocId(nguoiHocId);
    }
    
    public boolean daThucHienKhaoSat(Long nguoiHocId, Long khaoSatId) {
        Boolean result = phanHoiRepository.daThucHienKhaoSat(nguoiHocId, khaoSatId);
        return result != null && result;
    }
    
    public java.util.Optional<PhanHoi> layPhanHoiTheoNguoiHocVaKhaoSat(Long nguoiHocId, Long khaoSatId) {
        return phanHoiRepository.findByNguoiHocAndKhaoSat(nguoiHocId, khaoSatId);
    }
}

