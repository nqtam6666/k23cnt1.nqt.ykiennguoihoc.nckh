package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.NhomDichVu;
import k23cnt1.nguyenquangtam.nckh.repository.NhomDichVuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhomDichVuService {
    
    private final NhomDichVuRepository nhomDichVuRepository;
    
    public List<NhomDichVu> layTatCaNhomDichVu() {
        return nhomDichVuRepository.findAll(Sort.by(Sort.Direction.ASC, "nhomDichVuId"));
    }
    
    public NhomDichVu layNhomDichVuTheoId(Long id) {
        return nhomDichVuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm dịch vụ"));
    }
}

