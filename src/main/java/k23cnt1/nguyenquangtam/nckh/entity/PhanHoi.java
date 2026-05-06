package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "phan_hoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanHoi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phan_hoi_id")
    private Long phanHoiId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nguoi_hoc_id", nullable = false)
    private NguoiHoc nguoiHoc;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khao_sat_id", nullable = false)
    private KhaoSat khaoSat;
    
    @Column(name = "thoi_gian_gui")
    private LocalDateTime thoiGianGui;
    
    @OneToMany(mappedBy = "phanHoi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietPhanHoi> danhSachChiTiet;
    
    @PrePersist
    protected void onCreate() {
        thoiGianGui = LocalDateTime.now();
    }
}

