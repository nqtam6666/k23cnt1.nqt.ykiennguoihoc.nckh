package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.HocPhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HocPhanRepository extends JpaRepository<HocPhan, Long> {
    List<HocPhan> findByKhoa_KhoaId(Long khoaId);
    
    // Tìm kiếm và lọc
    @Query("SELECT h FROM HocPhan h LEFT JOIN FETCH h.khoa WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(h.tenHocPhan) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:khoaId IS NULL OR h.khoa.khoaId = :khoaId) " +
           "ORDER BY h.hocPhanId ASC")
    List<HocPhan> timKiemVaLoc(@Param("keyword") String keyword, @Param("khoaId") Long khoaId);
    
    @Query("SELECT COUNT(h) FROM HocPhan h WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(h.tenHocPhan) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:khoaId IS NULL OR h.khoa.khoaId = :khoaId)")
    long demTimKiemVaLoc(@Param("keyword") String keyword, @Param("khoaId") Long khoaId);
}

