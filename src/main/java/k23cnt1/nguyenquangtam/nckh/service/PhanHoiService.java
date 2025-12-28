package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.PhanHoiDTO;
import k23cnt1.nguyenquangtam.nckh.entity.*;
import k23cnt1.nguyenquangtam.nckh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhanHoiService {
    
    private final PhanHoiRepository phanHoiRepository;
    private final NguoiHocRepository nguoiHocRepository;
    private final DichVuDaoTaoRepository dichVuDaoTaoRepository;
    private final LoaiPhanHoiRepository loaiPhanHoiRepository;
    
    @Transactional
    public PhanHoi taoPhanHoi(PhanHoiDTO phanHoiDTO) {
        // Tìm hoặc tạo người học
        NguoiHoc nguoiHoc = nguoiHocRepository.findByMaSinhVien(phanHoiDTO.getMaSinhVien())
                .orElseGet(() -> {
                    NguoiHoc newNguoiHoc = new NguoiHoc();
                    newNguoiHoc.setMaSinhVien(phanHoiDTO.getMaSinhVien());
                    newNguoiHoc.setHoTen(phanHoiDTO.getHoTen());
                    newNguoiHoc.setEmail(phanHoiDTO.getEmail());
                    newNguoiHoc.setSoDienThoai(phanHoiDTO.getSoDienThoai());
                    return nguoiHocRepository.save(newNguoiHoc);
                });
        
        // Lấy dịch vụ đào tạo
        DichVuDaoTao dichVuDaoTao = dichVuDaoTaoRepository.findById(phanHoiDTO.getDichVuDaoTaoId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ đào tạo"));
        
        // Lấy loại phản hồi
        LoaiPhanHoi loaiPhanHoi = loaiPhanHoiRepository.findById(phanHoiDTO.getLoaiPhanHoiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phản hồi"));
        
        // Tạo phản hồi
        PhanHoi phanHoi = new PhanHoi();
        phanHoi.setNguoiHoc(nguoiHoc);
        phanHoi.setDichVuDaoTao(dichVuDaoTao);
        phanHoi.setLoaiPhanHoi(loaiPhanHoi);
        phanHoi.setNoiDung(phanHoiDTO.getNoiDung());
        phanHoi.setDiemDanhGia(phanHoiDTO.getDiemDanhGia());
        phanHoi.setTrangThai(false); // Mặc định chờ duyệt
        
        return phanHoiRepository.save(phanHoi);
    }
    
    public List<PhanHoi> layTatCaPhanHoi() {
        return phanHoiRepository.findAll();
    }
    
    public List<PhanHoi> layPhanHoiTheoDichVu(Long dichVuId) {
        return phanHoiRepository.findByDichVuDaoTaoId(dichVuId);
    }
    
    public List<PhanHoi> layPhanHoiDaDuyet() {
        return phanHoiRepository.findByTrangThai(true);
    }
    
    public List<PhanHoi> layPhanHoiChoDuyet() {
        return phanHoiRepository.findByTrangThai(false);
    }
    
    @Transactional
    public PhanHoi duyetPhanHoi(Long id, String ghiChu) {
        PhanHoi phanHoi = phanHoiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi"));
        phanHoi.setTrangThai(true);
        if (ghiChu != null && !ghiChu.isEmpty()) {
            phanHoi.setGhiChu(ghiChu);
        }
        return phanHoiRepository.save(phanHoi);
    }
    
    @Transactional
    public void xoaPhanHoi(Long id) {
        phanHoiRepository.deleteById(id);
    }
    
    public Double tinhDiemTrungBinhTheoDichVu(Long dichVuId) {
        return phanHoiRepository.tinhDiemTrungBinhTheoDichVu(dichVuId);
    }
    
    public Long demSoLuongPhanHoiTheoDichVu(Long dichVuId) {
        return phanHoiRepository.demSoLuongPhanHoiTheoDichVu(dichVuId);
    }
    
    public PhanHoi layPhanHoiTheoId(Long id) {
        return phanHoiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi"));
    }
}

