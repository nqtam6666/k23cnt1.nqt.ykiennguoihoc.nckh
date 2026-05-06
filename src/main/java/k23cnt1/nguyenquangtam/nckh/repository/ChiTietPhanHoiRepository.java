package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.ChiTietPhanHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietPhanHoiRepository extends JpaRepository<ChiTietPhanHoi, Long> {
    List<ChiTietPhanHoi> findByPhanHoi_PhanHoiId(Long phanHoiId);
    List<ChiTietPhanHoi> findByCauHoi_CauHoiId(Long cauHoiId);
}

