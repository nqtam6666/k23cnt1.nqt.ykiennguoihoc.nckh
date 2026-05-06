package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.PhanCong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanCongRepository extends JpaRepository<PhanCong, Long> {
    List<PhanCong> findByGiangVien_GiangVienId(Long giangVienId);
    List<PhanCong> findByHocPhan_HocPhanId(Long hocPhanId);
    
    // Tìm kiếm và lọc
    @Query("SELECT p FROM PhanCong p LEFT JOIN FETCH p.giangVien gv LEFT JOIN FETCH gv.nguoiDung LEFT JOIN FETCH p.hocPhan WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(gv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.hocPhan.tenHocPhan) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:giangVienId IS NULL OR p.giangVien.giangVienId = :giangVienId) AND " +
           "(:hocPhanId IS NULL OR p.hocPhan.hocPhanId = :hocPhanId) AND " +
           "(:hocKy IS NULL OR :hocKy = '' OR p.hocKy = :hocKy) AND " +
           "(:namHoc IS NULL OR :namHoc = '' OR p.namHoc = :namHoc) " +
           "ORDER BY p.phanCongId ASC")
    List<PhanCong> timKiemVaLoc(@Param("keyword") String keyword, 
                                 @Param("giangVienId") Long giangVienId, 
                                 @Param("hocPhanId") Long hocPhanId,
                                 @Param("hocKy") String hocKy,
                                 @Param("namHoc") String namHoc);
    
    @Query("SELECT COUNT(p) FROM PhanCong p LEFT JOIN p.giangVien gv LEFT JOIN p.hocPhan WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(gv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.hocPhan.tenHocPhan) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:giangVienId IS NULL OR p.giangVien.giangVienId = :giangVienId) AND " +
           "(:hocPhanId IS NULL OR p.hocPhan.hocPhanId = :hocPhanId) AND " +
           "(:hocKy IS NULL OR :hocKy = '' OR p.hocKy = :hocKy) AND " +
           "(:namHoc IS NULL OR :namHoc = '' OR p.namHoc = :namHoc)")
    long demTimKiemVaLoc(@Param("keyword") String keyword, 
                         @Param("giangVienId") Long giangVienId, 
                         @Param("hocPhanId") Long hocPhanId,
                         @Param("hocKy") String hocKy,
                         @Param("namHoc") String namHoc);
}

