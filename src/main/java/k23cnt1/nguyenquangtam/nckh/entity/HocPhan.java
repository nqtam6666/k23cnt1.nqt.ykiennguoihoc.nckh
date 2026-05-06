package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "hoc_phan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocPhan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hoc_phan_id")
    private Long hocPhanId;
    
    @Column(name = "ten_hoc_phan", nullable = false, length = 200)
    private String tenHocPhan;
    
    @Column(name = "so_tin_chi", nullable = false)
    private Integer soTinChi;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;
    
    @OneToMany(mappedBy = "hocPhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanCong> danhSachPhanCong;
}

