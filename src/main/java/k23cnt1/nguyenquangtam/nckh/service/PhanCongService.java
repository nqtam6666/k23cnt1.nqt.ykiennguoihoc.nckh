package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.PhanCongDTO;
import k23cnt1.nguyenquangtam.nckh.entity.GiangVien;
import k23cnt1.nguyenquangtam.nckh.entity.HocPhan;
import k23cnt1.nguyenquangtam.nckh.entity.PhanCong;
import k23cnt1.nguyenquangtam.nckh.repository.GiangVienRepository;
import k23cnt1.nguyenquangtam.nckh.repository.HocPhanRepository;
import k23cnt1.nguyenquangtam.nckh.repository.PhanCongRepository;
import k23cnt1.nguyenquangtam.nckh.util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhanCongService {
    
    private final PhanCongRepository phanCongRepository;
    private final GiangVienRepository giangVienRepository;
    private final HocPhanRepository hocPhanRepository;
    
    public List<PhanCong> layTatCaPhanCong() {
        return phanCongRepository.findAll(Sort.by(Sort.Direction.ASC, "phanCongId"));
    }
    
    @Transactional(readOnly = true)
    public PageInfo<PhanCong> timKiemVaPhanTrang(String keyword, Long giangVienId, Long hocPhanId, String hocKy, String namHoc, int page, int size) {
        String keywordNormalized = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        Long giangVienIdNormalized = (giangVienId != null && giangVienId > 0) ? giangVienId : null;
        Long hocPhanIdNormalized = (hocPhanId != null && hocPhanId > 0) ? hocPhanId : null;
        String hocKyNormalized = (hocKy != null && !hocKy.trim().isEmpty() && !hocKy.equals("all")) ? hocKy.trim() : null;
        String namHocNormalized = (namHoc != null && !namHoc.trim().isEmpty() && !namHoc.equals("all")) ? namHoc.trim() : null;
        
        long total = phanCongRepository.demTimKiemVaLoc(keywordNormalized, giangVienIdNormalized, hocPhanIdNormalized, hocKyNormalized, namHocNormalized);
        List<PhanCong> allResults = phanCongRepository.timKiemVaLoc(keywordNormalized, giangVienIdNormalized, hocPhanIdNormalized, hocKyNormalized, namHocNormalized);
        
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        List<PhanCong> pagedResults = (start < allResults.size()) ? allResults.subList(start, end) : java.util.Collections.emptyList();
        
        return PageInfo.of(pagedResults, page, size, total);
    }
    
    public PhanCong layPhanCongTheoId(Long id) {
        return phanCongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân công"));
    }
    
    @Transactional
    public PhanCong taoPhanCong(PhanCongDTO dto) {
        GiangVien giangVien = giangVienRepository.findById(dto.getGiangVienId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
        
        HocPhan hocPhan = hocPhanRepository.findById(dto.getHocPhanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
        
        PhanCong phanCong = new PhanCong();
        phanCong.setGiangVien(giangVien);
        phanCong.setHocPhan(hocPhan);
        phanCong.setHocKy(dto.getHocKy());
        phanCong.setNamHoc(dto.getNamHoc());
        
        return phanCongRepository.save(phanCong);
    }
    
    @Transactional
    public PhanCong capNhatPhanCong(Long id, PhanCongDTO dto) {
        PhanCong phanCong = phanCongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân công"));
        
        phanCong.setHocKy(dto.getHocKy());
        phanCong.setNamHoc(dto.getNamHoc());
        
        if (dto.getGiangVienId() != null) {
            GiangVien giangVien = giangVienRepository.findById(dto.getGiangVienId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
            phanCong.setGiangVien(giangVien);
        }
        
        if (dto.getHocPhanId() != null) {
            HocPhan hocPhan = hocPhanRepository.findById(dto.getHocPhanId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
            phanCong.setHocPhan(hocPhan);
        }
        
        return phanCongRepository.save(phanCong);
    }
    
    @Transactional
    public void xoaPhanCong(Long id) {
        phanCongRepository.deleteById(id);
    }
}

