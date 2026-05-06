package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "khoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Khoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khoa_id")
    private Long khoaId;
    
    @Column(name = "ten_khoa", nullable = false, length = 200)
    private String tenKhoa;
    
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    
    @OneToMany(mappedBy = "khoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiangVien> danhSachGiangVien;
    
    @OneToMany(mappedBy = "khoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NguoiHoc> danhSachNguoiHoc;
    
    @OneToMany(mappedBy = "khoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HocPhan> danhSachHocPhan;
}

