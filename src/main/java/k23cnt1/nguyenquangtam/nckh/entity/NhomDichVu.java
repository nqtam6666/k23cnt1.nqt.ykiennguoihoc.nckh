package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "nhom_dich_vu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhomDichVu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhom_dich_vu_id")
    private Long nhomDichVuId;
    
    @Column(name = "ten_dich_vu", nullable = false, length = 200)
    private String tenDichVu;
    
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    
    @OneToMany(mappedBy = "nhomDichVu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CauHoi> danhSachCauHoi;
}

