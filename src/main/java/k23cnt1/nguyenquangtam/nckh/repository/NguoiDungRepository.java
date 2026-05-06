package k23cnt1.nguyenquangtam.nckh.repository;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);
    boolean existsByTenDangNhap(String tenDangNhap);
    Optional<NguoiDung> findByTenDangNhapAndTrangThai(String tenDangNhap, Boolean trangThai);
    
    // Tìm kiếm và lọc với phân trang
    @Query("SELECT n FROM NguoiDung n WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(n.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:vaiTro IS NULL OR :vaiTro = '' OR n.vaiTro = :vaiTro) AND " +
           "(:trangThai IS NULL OR n.trangThai = :trangThai) " +
           "ORDER BY n.nguoiDungId ASC")
    List<NguoiDung> timKiemVaLoc(@Param("keyword") String keyword, 
                                  @Param("vaiTro") String vaiTro, 
                                  @Param("trangThai") Boolean trangThai);
    
    @Query("SELECT COUNT(n) FROM NguoiDung n WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(n.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:vaiTro IS NULL OR :vaiTro = '' OR n.vaiTro = :vaiTro) AND " +
           "(:trangThai IS NULL OR n.trangThai = :trangThai)")
    long demTimKiemVaLoc(@Param("keyword") String keyword, 
                         @Param("vaiTro") String vaiTro, 
                         @Param("trangThai") Boolean trangThai);
}

