package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.GiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiangVienRepository extends JpaRepository<GiangVien, Long> {
    Optional<GiangVien> findByNguoiDung_NguoiDungId(Long nguoiDungId);
    List<GiangVien> findByKhoa_KhoaId(Long khoaId);
    
    @Query("SELECT gv FROM GiangVien gv LEFT JOIN FETCH gv.danhSachPhanCong pc LEFT JOIN FETCH pc.hocPhan WHERE gv.nguoiDung.nguoiDungId = :nguoiDungId")
    Optional<GiangVien> findByNguoiDung_NguoiDungIdWithPhanCong(@Param("nguoiDungId") Long nguoiDungId);
    
    // Tìm kiếm và lọc
    @Query("SELECT gv FROM GiangVien gv LEFT JOIN FETCH gv.nguoiDung LEFT JOIN FETCH gv.khoa WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(gv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(gv.nguoiDung.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:khoaId IS NULL OR gv.khoa.khoaId = :khoaId) AND " +
           "(:hocVi IS NULL OR :hocVi = '' OR gv.hocVi = :hocVi) " +
           "ORDER BY gv.giangVienId ASC")
    List<GiangVien> timKiemVaLoc(@Param("keyword") String keyword, 
                                  @Param("khoaId") Long khoaId, 
                                  @Param("hocVi") String hocVi);
    
    @Query("SELECT COUNT(gv) FROM GiangVien gv WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(gv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(gv.nguoiDung.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:khoaId IS NULL OR gv.khoa.khoaId = :khoaId) AND " +
           "(:hocVi IS NULL OR :hocVi = '' OR gv.hocVi = :hocVi)")
    long demTimKiemVaLoc(@Param("keyword") String keyword, 
                         @Param("khoaId") Long khoaId, 
                         @Param("hocVi") String hocVi);
}

