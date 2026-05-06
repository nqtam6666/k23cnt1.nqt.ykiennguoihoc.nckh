package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chi_tiet_phan_hoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhanHoi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chi_tiet_id")
    private Long chiTietId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phan_hoi_id", nullable = false)
    private PhanHoi phanHoi;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cau_hoi_id", nullable = false)
    private CauHoi cauHoi;
    
    @Column(name = "diem_danh_gia", nullable = false)
    private Integer diemDanhGia; // 1-5 (Likert scale)
    
    @Column(name = "y_kien_khac", columnDefinition = "TEXT")
    private String yKienKhac;
}

