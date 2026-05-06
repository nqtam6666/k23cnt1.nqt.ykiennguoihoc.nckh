package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.Khoa;
import k23cnt1.nguyenquangtam.nckh.repository.KhoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhoaService {
    
    private final KhoaRepository khoaRepository;
    
    public List<Khoa> layTatCaKhoa() {
        return khoaRepository.findAll(Sort.by(Sort.Direction.ASC, "khoaId"));
    }
    
    public Khoa layKhoaTheoId(Long id) {
        return khoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
    }
}

