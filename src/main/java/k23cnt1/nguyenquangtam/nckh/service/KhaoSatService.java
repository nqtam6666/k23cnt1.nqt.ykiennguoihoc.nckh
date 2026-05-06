package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.KhaoSatDTO;
import k23cnt1.nguyenquangtam.nckh.entity.KhaoSat;
import k23cnt1.nguyenquangtam.nckh.repository.KhaoSatRepository;
import k23cnt1.nguyenquangtam.nckh.util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KhaoSatService {
    
    private final KhaoSatRepository khaoSatRepository;
    
    public List<KhaoSat> layTatCaKhaoSat() {
        return khaoSatRepository.findAll(Sort.by(Sort.Direction.ASC, "khaoSatId"));
    }
    
    @Transactional(readOnly = true)
    public PageInfo<KhaoSat> timKiemVaPhanTrang(String keyword, String hocKy, String namHoc, int page, int size) {
        String keywordNormalized = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String hocKyNormalized = (hocKy != null && !hocKy.trim().isEmpty() && !hocKy.equals("all")) ? hocKy.trim() : null;
        String namHocNormalized = (namHoc != null && !namHoc.trim().isEmpty() && !namHoc.equals("all")) ? namHoc.trim() : null;
        
        long total = khaoSatRepository.demTimKiemVaLoc(keywordNormalized, hocKyNormalized, namHocNormalized);
        List<KhaoSat> allResults = khaoSatRepository.timKiemVaLoc(keywordNormalized, hocKyNormalized, namHocNormalized);
        
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        List<KhaoSat> pagedResults = (start < allResults.size()) ? allResults.subList(start, end) : java.util.Collections.emptyList();
        
        return PageInfo.of(pagedResults, page, size, total);
    }
    
    public KhaoSat layKhaoSatTheoId(Long id) {
        return khaoSatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát"));
    }
    
    public Optional<KhaoSat> layKhaoSatTheoIdOptional(Long id) {
        return khaoSatRepository.findById(id);
    }
    
    @Transactional
    public KhaoSat taoKhaoSat(KhaoSatDTO dto) {
        KhaoSat khaoSat = new KhaoSat();
        khaoSat.setTenKhaoSat(dto.getTenKhaoSat());
        khaoSat.setHocKy(dto.getHocKy());
        khaoSat.setNamHoc(dto.getNamHoc());
        
        return khaoSatRepository.save(khaoSat);
    }
    
    @Transactional
    public KhaoSat capNhatKhaoSat(Long id, KhaoSatDTO dto) {
        KhaoSat khaoSat = khaoSatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát"));
        
        khaoSat.setTenKhaoSat(dto.getTenKhaoSat());
        khaoSat.setHocKy(dto.getHocKy());
        khaoSat.setNamHoc(dto.getNamHoc());
        
        return khaoSatRepository.save(khaoSat);
    }
    
    @Transactional
    public void xoaKhaoSat(Long id) {
        khaoSatRepository.deleteById(id);
    }
}

