package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.KhaoSat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhaoSatRepository extends JpaRepository<KhaoSat, Long> {
    List<KhaoSat> findByHocKyAndNamHoc(String hocKy, String namHoc);
    
    // Tìm kiếm và lọc
    @Query("SELECT k FROM KhaoSat k WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(k.tenKhaoSat) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:hocKy IS NULL OR :hocKy = '' OR k.hocKy = :hocKy) AND " +
           "(:namHoc IS NULL OR :namHoc = '' OR k.namHoc = :namHoc) " +
           "ORDER BY k.khaoSatId ASC")
    List<KhaoSat> timKiemVaLoc(@Param("keyword") String keyword, 
                                @Param("hocKy") String hocKy, 
                                @Param("namHoc") String namHoc);
    
    @Query("SELECT COUNT(k) FROM KhaoSat k WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(k.tenKhaoSat) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:hocKy IS NULL OR :hocKy = '' OR k.hocKy = :hocKy) AND " +
           "(:namHoc IS NULL OR :namHoc = '' OR k.namHoc = :namHoc)")
    long demTimKiemVaLoc(@Param("keyword") String keyword, 
                         @Param("hocKy") String hocKy, 
                         @Param("namHoc") String namHoc);
}

