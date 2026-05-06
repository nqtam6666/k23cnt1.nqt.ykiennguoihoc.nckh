package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nguoi_dung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nguoi_dung_id")
    private Long nguoiDungId;
    
    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;
    
    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;
    
    @Column(name = "vai_tro", nullable = false, length = 20)
    private String vaiTro; // admin, giang_vien, nguoi_hoc
    
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = true; // true: hoạt động, false: khóa
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}

