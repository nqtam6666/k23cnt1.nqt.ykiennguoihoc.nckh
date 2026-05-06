package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauHoiRepository extends JpaRepository<CauHoi, Long> {
    List<CauHoi> findByKhaoSat_KhaoSatId(Long khaoSatId);
    List<CauHoi> findByNhomDichVu_NhomDichVuId(Long nhomDichVuId);
    
    // Tối ưu query với JOIN FETCH để tránh N+1 problem
    @Query("SELECT DISTINCT c FROM CauHoi c " +
           "LEFT JOIN FETCH c.nhomDichVu " +
           "LEFT JOIN FETCH c.khaoSat " +
           "WHERE c.khaoSat.khaoSatId = :khaoSatId " +
           "ORDER BY c.cauHoiId")
    List<CauHoi> findByKhaoSatIdWithNhomDichVu(@Param("khaoSatId") Long khaoSatId);
}

