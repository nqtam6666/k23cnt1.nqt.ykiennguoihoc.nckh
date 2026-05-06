package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "giang_vien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiangVien {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giang_vien_id")
    private Long giangVienId;
    
    @OneToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false, unique = true)
    private NguoiDung nguoiDung;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "hoc_vi", length = 50)
    private String hocVi;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;
    
    @Column(name = "so_nam_kinh_nghiem")
    private Integer soNamKinhNghiem;
    
    @Column(name = "nam_bat_dau")
    private Integer namBatDau; // Năm bắt đầu làm việc
    
    @OneToMany(mappedBy = "giangVien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanCong> danhSachPhanCong;
}

