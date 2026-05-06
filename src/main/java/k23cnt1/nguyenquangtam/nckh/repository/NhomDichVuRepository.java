package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.NhomDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomDichVuRepository extends JpaRepository<NhomDichVu, Long> {
}

