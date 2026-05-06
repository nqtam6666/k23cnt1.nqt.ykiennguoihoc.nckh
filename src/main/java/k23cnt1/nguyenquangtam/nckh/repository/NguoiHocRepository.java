package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiHocRepository extends JpaRepository<NguoiHoc, Long> {
    Optional<NguoiHoc> findByNguoiDung_NguoiDungId(Long nguoiDungId);
    List<NguoiHoc> findByKhoa_KhoaId(Long khoaId);
}

