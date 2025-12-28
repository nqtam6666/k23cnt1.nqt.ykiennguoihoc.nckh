package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loai_phan_hoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiPhanHoi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String tenLoai;
    
    @Column(length = 500)
    private String moTa;
    
    @Column(name = "trang_thai")
    private Boolean trangThai = true;
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    @OneToMany(mappedBy = "loaiPhanHoi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

