package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.CauHoiDTO;
import k23cnt1.nguyenquangtam.nckh.entity.CauHoi;
import k23cnt1.nguyenquangtam.nckh.entity.KhaoSat;
import k23cnt1.nguyenquangtam.nckh.entity.NhomDichVu;
import k23cnt1.nguyenquangtam.nckh.repository.CauHoiRepository;
import k23cnt1.nguyenquangtam.nckh.repository.KhaoSatRepository;
import k23cnt1.nguyenquangtam.nckh.repository.NhomDichVuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CauHoiService {
    
    private final CauHoiRepository cauHoiRepository;
    private final KhaoSatRepository khaoSatRepository;
    private final NhomDichVuRepository nhomDichVuRepository;
    
    public List<CauHoi> layTatCaCauHoi() {
        return cauHoiRepository.findAll(Sort.by(Sort.Direction.ASC, "cauHoiId"));
    }
    
    @Transactional(readOnly = true)
    public List<CauHoi> layCauHoiTheoKhaoSat(Long khaoSatId) {
        // Sử dụng query tối ưu với JOIN FETCH để tránh N+1 problem
        return cauHoiRepository.findByKhaoSatIdWithNhomDichVu(khaoSatId);
    }
    
    public CauHoi layCauHoiTheoId(Long id) {
        return cauHoiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi"));
    }
    
    @Transactional
    public CauHoi taoCauHoi(CauHoiDTO dto) {
        KhaoSat khaoSat = khaoSatRepository.findById(dto.getKhaoSatId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát"));
        
        NhomDichVu nhomDichVu = nhomDichVuRepository.findById(dto.getNhomDichVuId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm dịch vụ"));
        
        CauHoi cauHoi = new CauHoi();
        cauHoi.setKhaoSat(khaoSat);
        cauHoi.setNhomDichVu(nhomDichVu);
        cauHoi.setNoiDung(dto.getNoiDung());
        cauHoi.setLoaiThangDo(dto.getLoaiThangDo());
        
        return cauHoiRepository.save(cauHoi);
    }
    
    @Transactional
    public CauHoi capNhatCauHoi(Long id, CauHoiDTO dto) {
        CauHoi cauHoi = cauHoiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi"));
        
        cauHoi.setNoiDung(dto.getNoiDung());
        cauHoi.setLoaiThangDo(dto.getLoaiThangDo());
        
        if (dto.getKhaoSatId() != null) {
            KhaoSat khaoSat = khaoSatRepository.findById(dto.getKhaoSatId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát"));
            cauHoi.setKhaoSat(khaoSat);
        }
        
        if (dto.getNhomDichVuId() != null) {
            NhomDichVu nhomDichVu = nhomDichVuRepository.findById(dto.getNhomDichVuId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm dịch vụ"));
            cauHoi.setNhomDichVu(nhomDichVu);
        }
        
        return cauHoiRepository.save(cauHoi);
    }
    
    @Transactional
    public void xoaCauHoi(Long id) {
        cauHoiRepository.deleteById(id);
    }
}

