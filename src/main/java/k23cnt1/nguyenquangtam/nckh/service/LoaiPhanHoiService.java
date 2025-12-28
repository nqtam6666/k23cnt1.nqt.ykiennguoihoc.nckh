package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.entity.LoaiPhanHoi;
import k23cnt1.nguyenquangtam.nckh.repository.LoaiPhanHoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaiPhanHoiService {
    
    private final LoaiPhanHoiRepository loaiPhanHoiRepository;
    
    public List<LoaiPhanHoi> layTatCaLoaiPhanHoi() {
        return loaiPhanHoiRepository.findAll();
    }
    
    public List<LoaiPhanHoi> layLoaiPhanHoiDangHoatDong() {
        return loaiPhanHoiRepository.findByTrangThai(true);
    }
}

