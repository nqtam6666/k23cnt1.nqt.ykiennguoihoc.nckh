package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.PhanHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanHoiRepository extends JpaRepository<PhanHoi, Long> {
    List<PhanHoi> findByNguoiHoc_NguoiHocId(Long nguoiHocId);
    List<PhanHoi> findByKhaoSat_KhaoSatId(Long khaoSatId);
    
    @Query("SELECT DISTINCT p FROM PhanHoi p LEFT JOIN FETCH p.danhSachChiTiet WHERE p.nguoiHoc.nguoiHocId = :nguoiHocId ORDER BY p.phanHoiId ASC")
    List<PhanHoi> findByNguoiHoc_NguoiHocIdWithChiTiet(@Param("nguoiHocId") Long nguoiHocId);
    
    @Query("SELECT DISTINCT p FROM PhanHoi p LEFT JOIN FETCH p.danhSachChiTiet WHERE p.khaoSat.khaoSatId = :khaoSatId ORDER BY p.phanHoiId ASC")
    List<PhanHoi> findByKhaoSat_KhaoSatIdWithChiTiet(@Param("khaoSatId") Long khaoSatId);
    
    @Query("SELECT DISTINCT p FROM PhanHoi p LEFT JOIN FETCH p.danhSachChiTiet ORDER BY p.phanHoiId ASC")
    List<PhanHoi> findAllWithChiTiet();
    
    @Query("SELECT DISTINCT p FROM PhanHoi p LEFT JOIN FETCH p.danhSachChiTiet ct LEFT JOIN FETCH ct.cauHoi WHERE p.phanHoiId = :phanHoiId")
    java.util.Optional<PhanHoi> findByIdWithChiTiet(@Param("phanHoiId") Long phanHoiId);
    
    @Query("SELECT AVG(ct.diemDanhGia) FROM ChiTietPhanHoi ct WHERE ct.cauHoi.nhomDichVu.nhomDichVuId = :nhomDichVuId")
    Double tinhDiemTrungBinhTheoNhomDichVu(@Param("nhomDichVuId") Long nhomDichVuId);
    
    @Query("SELECT COUNT(p) FROM PhanHoi p WHERE p.khaoSat.khaoSatId = :khaoSatId")
    Long demSoLuongPhanHoiTheoKhaoSat(@Param("khaoSatId") Long khaoSatId);
    
    @Query("SELECT p FROM PhanHoi p WHERE p.nguoiHoc.nguoiHocId = :nguoiHocId AND p.khaoSat.khaoSatId = :khaoSatId")
    java.util.Optional<PhanHoi> findByNguoiHocAndKhaoSat(@Param("nguoiHocId") Long nguoiHocId, @Param("khaoSatId") Long khaoSatId);
    
    @Query("SELECT COUNT(p) > 0 FROM PhanHoi p WHERE p.nguoiHoc.nguoiHocId = :nguoiHocId AND p.khaoSat.khaoSatId = :khaoSatId")
    Boolean daThucHienKhaoSat(@Param("nguoiHocId") Long nguoiHocId, @Param("khaoSatId") Long khaoSatId);
    
    // Tìm kiếm và lọc (load luôn danhSachChiTiet để tránh LazyInitializationException khi đếm size trong view)
    @Query("SELECT DISTINCT p FROM PhanHoi p " +
           "LEFT JOIN FETCH p.nguoiHoc nh " +
           "LEFT JOIN FETCH nh.nguoiDung " +
           "LEFT JOIN FETCH p.khaoSat " +
           "LEFT JOIN FETCH p.danhSachChiTiet ct " +
           "WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(nh.nguoiDung.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.khaoSat.tenKhaoSat) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:khaoSatId IS NULL OR p.khaoSat.khaoSatId = :khaoSatId) " +
           "ORDER BY p.phanHoiId ASC")
    List<PhanHoi> timKiemVaLoc(@Param("keyword") String keyword, @Param("khaoSatId") Long khaoSatId);
    
    @Query("SELECT COUNT(DISTINCT p) FROM PhanHoi p LEFT JOIN p.nguoiHoc nh LEFT JOIN nh.nguoiDung LEFT JOIN p.khaoSat WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(nh.nguoiDung.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.khaoSat.tenKhaoSat) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:khaoSatId IS NULL OR p.khaoSat.khaoSatId = :khaoSatId)")
    long demTimKiemVaLoc(@Param("keyword") String keyword, @Param("khaoSatId") Long khaoSatId);
}

