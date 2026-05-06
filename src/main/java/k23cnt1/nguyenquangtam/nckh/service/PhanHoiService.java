package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.PhanHoi;
import k23cnt1.nguyenquangtam.nckh.repository.ChiTietPhanHoiRepository;
import k23cnt1.nguyenquangtam.nckh.repository.PhanHoiRepository;
import k23cnt1.nguyenquangtam.nckh.util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhanHoiService {
    
    private final PhanHoiRepository phanHoiRepository;
    private final ChiTietPhanHoiRepository chiTietPhanHoiRepository;
    
    @Transactional(readOnly = true)
    public List<PhanHoi> layTatCaPhanHoi() {
        return phanHoiRepository.findAllWithChiTiet();
    }
    
    @Transactional(readOnly = true)
    public PageInfo<PhanHoi> timKiemVaPhanTrang(String keyword, Long khaoSatId, int page, int size) {
        String keywordNormalized = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        Long khaoSatIdNormalized = (khaoSatId != null && khaoSatId > 0) ? khaoSatId : null;
        
        long total = phanHoiRepository.demTimKiemVaLoc(keywordNormalized, khaoSatIdNormalized);
        List<PhanHoi> allResults = phanHoiRepository.timKiemVaLoc(keywordNormalized, khaoSatIdNormalized);
        
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        List<PhanHoi> pagedResults = (start < allResults.size()) ? allResults.subList(start, end) : java.util.Collections.emptyList();
        
        return PageInfo.of(pagedResults, page, size, total);
    }
    
    @Transactional(readOnly = true)
    public List<PhanHoi> layPhanHoiTheoKhaoSat(Long khaoSatId) {
        return phanHoiRepository.findByKhaoSat_KhaoSatIdWithChiTiet(khaoSatId);
    }
    
    @Transactional(readOnly = true)
    public PhanHoi layPhanHoiTheoId(Long phanHoiId) {
        return phanHoiRepository.findByIdWithChiTiet(phanHoiId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi với ID: " + phanHoiId));
    }
    
    @Transactional
    public void xoaPhanHoi(Long phanHoiId) {
        // Load phản hồi cùng với chi tiết để tránh lazy loading exception
        PhanHoi phanHoi = phanHoiRepository.findByIdWithChiTiet(phanHoiId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi với ID: " + phanHoiId));
        
        // Xóa tất cả chi tiết phản hồi trước để tránh lỗi foreign key
        if (phanHoi.getDanhSachChiTiet() != null && !phanHoi.getDanhSachChiTiet().isEmpty()) {
            chiTietPhanHoiRepository.deleteAll(phanHoi.getDanhSachChiTiet());
            chiTietPhanHoiRepository.flush(); // Đảm bảo xóa chi tiết trước
        }
        
        // Sau đó xóa phản hồi
        phanHoiRepository.delete(phanHoi);
    }
}
