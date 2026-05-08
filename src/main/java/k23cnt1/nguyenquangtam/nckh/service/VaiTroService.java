package k23cnt1.nguyenquangtam.nckh.service;

import k23cnt1.nguyenquangtam.nckh.dto.VaiTroDTO;
import k23cnt1.nguyenquangtam.nckh.entity.Quyen;
import k23cnt1.nguyenquangtam.nckh.entity.VaiTro;
import k23cnt1.nguyenquangtam.nckh.repository.QuyenRepository;
import k23cnt1.nguyenquangtam.nckh.repository.VaiTroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VaiTroService {

    private final VaiTroRepository vaiTroRepository;
    private final QuyenRepository quyenRepository;

    public List<VaiTro> layTatCaVaiTro() {
        return vaiTroRepository.findAll();
    }

    public List<Quyen> layTatCaQuyen() {
        return quyenRepository.findAll();
    }

    public VaiTro layVaiTroTheoId(Long id) {
        return vaiTroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
    }

    @Transactional
    public VaiTro taoVaiTro(VaiTroDTO dto) {
        if (vaiTroRepository.findByTenVaiTro(dto.getTenVaiTro()).isPresent()) {
            throw new RuntimeException("Mã vai trò đã tồn tại");
        }

        VaiTro vaiTro = new VaiTro();
        vaiTro.setTenVaiTro(dto.getTenVaiTro());
        vaiTro.setMoTa(dto.getMoTa());
        vaiTro.setMacDinh(dto.getMacDinh() != null ? dto.getMacDinh() : false);

        if (vaiTro.getMacDinh()) {
            // Nếu set role này làm mặc định, phải bỏ mặc định của các role khác
            boMacDinhCacRoleKhac();
        }

        Set<Quyen> danhSachQuyen = new HashSet<>();
        if (dto.getQuyenIds() != null && !dto.getQuyenIds().isEmpty()) {
            List<Quyen> quyens = quyenRepository.findAllById(dto.getQuyenIds());
            danhSachQuyen.addAll(quyens);
        }
        vaiTro.setDanhSachQuyen(danhSachQuyen);

        return vaiTroRepository.save(vaiTro);
    }

    @Transactional
    public VaiTro capNhatVaiTro(Long id, VaiTroDTO dto) {
        VaiTro vaiTro = layVaiTroTheoId(id);

        if (!vaiTro.getTenVaiTro().equals(dto.getTenVaiTro()) && 
            vaiTroRepository.findByTenVaiTro(dto.getTenVaiTro()).isPresent()) {
            throw new RuntimeException("Mã vai trò đã tồn tại");
        }

        vaiTro.setTenVaiTro(dto.getTenVaiTro());
        vaiTro.setMoTa(dto.getMoTa());
        vaiTro.setMacDinh(dto.getMacDinh() != null ? dto.getMacDinh() : false);

        if (vaiTro.getMacDinh()) {
            boMacDinhCacRoleKhac(id);
        }

        Set<Quyen> danhSachQuyen = new HashSet<>();
        if (dto.getQuyenIds() != null && !dto.getQuyenIds().isEmpty()) {
            List<Quyen> quyens = quyenRepository.findAllById(dto.getQuyenIds());
            danhSachQuyen.addAll(quyens);
        }
        
        vaiTro.getDanhSachQuyen().clear();
        vaiTro.getDanhSachQuyen().addAll(danhSachQuyen);

        return vaiTroRepository.save(vaiTro);
    }

    @Transactional
    public void xoaVaiTro(Long id) {
        VaiTro vaiTro = layVaiTroTheoId(id);
        if ("ROLE_ADMIN".equals(vaiTro.getTenVaiTro())) {
            throw new RuntimeException("Không thể xóa vai trò quản trị viên hệ thống");
        }
        vaiTroRepository.delete(vaiTro);
    }

    private void boMacDinhCacRoleKhac() {
        boMacDinhCacRoleKhac(null);
    }

    private void boMacDinhCacRoleKhac(Long truIdNay) {
        List<VaiTro> tatCaVaiTro = vaiTroRepository.findAll();
        for (VaiTro vt : tatCaVaiTro) {
            if (vt.getMacDinh() && (truIdNay == null || !vt.getVaiTroId().equals(truIdNay))) {
                vt.setMacDinh(false);
                vaiTroRepository.save(vt);
            }
        }
    }
}
