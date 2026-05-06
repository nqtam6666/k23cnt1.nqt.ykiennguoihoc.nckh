package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "nguoi_hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiHoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nguoi_hoc_id")
    private Long nguoiHocId;
    
    @OneToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false, unique = true)
    private NguoiDung nguoiDung;
    
    @Column(name = "gioi_tinh")
    private Boolean gioiTinh; // true: nam, false: nữ
    
    @Column(name = "nam_sinh")
    private Integer namSinh;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;
    
    @Column(name = "nganh_hoc", length = 100)
    private String nganhHoc;
    
    @Column(name = "khoa_hoc", length = 50)
    private String khoaHoc;
    
    @OneToMany(mappedBy = "nguoiHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanHoi> danhSachPhanHoi;
}

