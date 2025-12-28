package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.LoaiPhanHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiPhanHoiRepository extends JpaRepository<LoaiPhanHoi, Long> {
    List<LoaiPhanHoi> findByTrangThai(Boolean trangThai);
}

