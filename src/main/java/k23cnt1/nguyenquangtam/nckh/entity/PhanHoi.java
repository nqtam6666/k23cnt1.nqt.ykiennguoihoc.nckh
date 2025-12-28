package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "phan_hoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanHoi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nguoi_hoc_id", nullable = false)
    private NguoiHoc nguoiHoc;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dich_vu_dao_tao_id", nullable = false)
    private DichVuDaoTao dichVuDaoTao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loai_phan_hoi_id", nullable = false)
    private LoaiPhanHoi loaiPhanHoi;
    
    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;
    
    @Column(name = "diem_danh_gia")
    private Integer diemDanhGia; // 1-5 hoặc 1-10
    
    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true: đã duyệt, false: chờ duyệt
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    @Column(name = "ghi_chu", length = 500)
    private String ghiChu; // Ghi chú từ admin
    
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

