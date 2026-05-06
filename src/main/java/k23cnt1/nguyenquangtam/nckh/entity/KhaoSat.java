package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "khao_sat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhaoSat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khao_sat_id")
    private Long khaoSatId;
    
    @Column(name = "ten_khao_sat", nullable = false, length = 200)
    private String tenKhaoSat;
    
    @Column(name = "hoc_ky", length = 20)
    private String hocKy;
    
    @Column(name = "nam_hoc", length = 20)
    private String namHoc;
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @OneToMany(mappedBy = "khaoSat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CauHoi> danhSachCauHoi;
    
    @OneToMany(mappedBy = "khaoSat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanHoi> danhSachPhanHoi;
    
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}

