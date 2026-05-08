package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuyenRepository extends JpaRepository<Quyen, Long> {
    Optional<Quyen> findByMaQuyen(String maQuyen);
}
