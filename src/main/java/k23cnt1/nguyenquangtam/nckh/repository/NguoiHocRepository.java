package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiHocRepository extends JpaRepository<NguoiHoc, Long> {
    Optional<NguoiHoc> findByMaSinhVien(String maSinhVien);
    Optional<NguoiHoc> findByEmail(String email);
    boolean existsByMaSinhVien(String maSinhVien);
    boolean existsByEmail(String email);
}

