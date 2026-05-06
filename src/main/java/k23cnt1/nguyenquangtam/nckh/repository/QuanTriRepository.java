package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.QuanTri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuanTriRepository extends JpaRepository<QuanTri, Long> {
    Optional<QuanTri> findByNguoiDung_NguoiDungId(Long nguoiDungId);
}

