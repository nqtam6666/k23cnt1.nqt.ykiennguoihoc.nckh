package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.HocPhanDTO;
import k23cnt1.nguyenquangtam.nckh.entity.HocPhan;
import k23cnt1.nguyenquangtam.nckh.entity.Khoa;
import k23cnt1.nguyenquangtam.nckh.repository.HocPhanRepository;
import k23cnt1.nguyenquangtam.nckh.repository.KhoaRepository;
import k23cnt1.nguyenquangtam.nckh.util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HocPhanService {
    
    private final HocPhanRepository hocPhanRepository;
    private final KhoaRepository khoaRepository;
    
    public List<HocPhan> layTatCaHocPhan() {
        return hocPhanRepository.findAll(Sort.by(Sort.Direction.ASC, "hocPhanId"));
    }
    
    @Transactional(readOnly = true)
    public PageInfo<HocPhan> timKiemVaPhanTrang(String keyword, Long khoaId, int page, int size) {
        String keywordNormalized = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        Long khoaIdNormalized = (khoaId != null && khoaId > 0) ? khoaId : null;
        
        long total = hocPhanRepository.demTimKiemVaLoc(keywordNormalized, khoaIdNormalized);
        List<HocPhan> allResults = hocPhanRepository.timKiemVaLoc(keywordNormalized, khoaIdNormalized);
        
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        List<HocPhan> pagedResults = (start < allResults.size()) ? allResults.subList(start, end) : java.util.Collections.emptyList();
        
        return PageInfo.of(pagedResults, page, size, total);
    }
    
    public HocPhan layHocPhanTheoId(Long id) {
        return hocPhanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
    }
    
    @Transactional
    public HocPhan taoHocPhan(HocPhanDTO dto) {
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        
        HocPhan hocPhan = new HocPhan();
        hocPhan.setTenHocPhan(dto.getTenHocPhan());
        hocPhan.setSoTinChi(dto.getSoTinChi());
        hocPhan.setKhoa(khoa);
        
        return hocPhanRepository.save(hocPhan);
    }
    
    @Transactional
    public HocPhan capNhatHocPhan(Long id, HocPhanDTO dto) {
        HocPhan hocPhan = hocPhanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
        
        hocPhan.setTenHocPhan(dto.getTenHocPhan());
        hocPhan.setSoTinChi(dto.getSoTinChi());
        
        Khoa khoa = khoaRepository.findById(dto.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        hocPhan.setKhoa(khoa);
        
        return hocPhanRepository.save(hocPhan);
    }
    
    @Transactional
    public void xoaHocPhan(Long id) {
        hocPhanRepository.deleteById(id);
    }
}

