package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cau_hoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CauHoi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cau_hoi_id")
    private Long cauHoiId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khao_sat_id", nullable = false)
    private KhaoSat khaoSat;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nhom_dich_vu_id", nullable = false)
    private NhomDichVu nhomDichVu;
    
    @Column(name = "noi_dung", columnDefinition = "TEXT", nullable = false)
    private String noiDung;
    
    @Column(name = "loai_thang_do", length = 50)
    private String loaiThangDo; // Likert 1-5
    
    @OneToMany(mappedBy = "cauHoi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietPhanHoi> danhSachChiTiet;
}

