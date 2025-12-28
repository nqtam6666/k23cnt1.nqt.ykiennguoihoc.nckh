package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dich_vu_dao_tao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DichVuDaoTao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String tenDichVu;
    
    @Column(length = 500)
    private String moTa;
    
    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true: đang hoạt động, false: ngừng hoạt động
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    @OneToMany(mappedBy = "dichVuDaoTao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

