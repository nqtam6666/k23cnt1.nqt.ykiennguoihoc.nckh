package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.DichVuDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DichVuDaoTaoRepository extends JpaRepository<DichVuDaoTao, Long> {
    List<DichVuDaoTao> findByTrangThai(Boolean trangThai);
}

