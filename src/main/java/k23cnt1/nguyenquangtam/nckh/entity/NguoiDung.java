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
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "nguoi_dung_vai_tro",
        joinColumns = @JoinColumn(name = "nguoi_dung_id"),
        inverseJoinColumns = @JoinColumn(name = "vai_tro_id")
    )
    private java.util.Set<VaiTro> danhSachVaiTro = new java.util.HashSet<>();
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = true; // true: hoạt động, false: khóa
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
    
    public String getVaiTro() {
        if (danhSachVaiTro == null || danhSachVaiTro.isEmpty()) return "nguoi_hoc";
        String role = danhSachVaiTro.iterator().next().getTenVaiTro();
        if ("ROLE_ADMIN".equals(role)) return "admin";
        if ("ROLE_GIANG_VIEN".equals(role)) return "giang_vien";
        return "nguoi_hoc";
    }
}

