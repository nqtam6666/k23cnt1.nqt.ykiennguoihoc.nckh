package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.PhanHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanHoiRepository extends JpaRepository<PhanHoi, Long> {
    List<PhanHoi> findByNguoiHocId(Long nguoiHocId);
    List<PhanHoi> findByDichVuDaoTaoId(Long dichVuDaoTaoId);
    List<PhanHoi> findByLoaiPhanHoiId(Long loaiPhanHoiId);
    List<PhanHoi> findByTrangThai(Boolean trangThai);
    
    @Query("SELECT AVG(p.diemDanhGia) FROM PhanHoi p WHERE p.dichVuDaoTao.id = :dichVuId AND p.trangThai = true")
    Double tinhDiemTrungBinhTheoDichVu(@Param("dichVuId") Long dichVuId);
    
    @Query("SELECT COUNT(p) FROM PhanHoi p WHERE p.dichVuDaoTao.id = :dichVuId AND p.trangThai = true")
    Long demSoLuongPhanHoiTheoDichVu(@Param("dichVuId") Long dichVuId);
}

