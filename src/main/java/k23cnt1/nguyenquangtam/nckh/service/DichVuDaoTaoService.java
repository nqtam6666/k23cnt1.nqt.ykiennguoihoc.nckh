package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.DichVuDaoTao;
import k23cnt1.nguyenquangtam.nckh.repository.DichVuDaoTaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DichVuDaoTaoService {
    
    private final DichVuDaoTaoRepository dichVuDaoTaoRepository;
    
    public List<DichVuDaoTao> layTatCaDichVu() {
        return dichVuDaoTaoRepository.findAll();
    }
    
    public List<DichVuDaoTao> layDichVuDangHoatDong() {
        return dichVuDaoTaoRepository.findByTrangThai(true);
    }
    
    public DichVuDaoTao layDichVuTheoId(Long id) {
        return dichVuDaoTaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ đào tạo"));
    }
}

