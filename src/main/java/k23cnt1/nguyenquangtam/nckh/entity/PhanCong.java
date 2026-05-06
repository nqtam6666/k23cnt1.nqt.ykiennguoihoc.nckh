package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phan_cong")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanCong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phan_cong_id")
    private Long phanCongId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "giang_vien_id", nullable = false)
    private GiangVien giangVien;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hoc_phan_id", nullable = false)
    private HocPhan hocPhan;
    
    @Column(name = "hoc_ky", length = 20)
    private String hocKy;
    
    @Column(name = "nam_hoc", length = 20)
    private String namHoc;
}

