package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nguoi_hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiHoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String maSinhVien;
    
    @Column(nullable = false, length = 100)
    private String hoTen;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 15)
    private String soDienThoai;
    
    @Column(length = 50)
    private String lop;
    
    @Column(length = 50)
    private String khoa;
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    @OneToMany(mappedBy = "nguoiHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanHoi> danhSachPhanHoi;
    
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}

