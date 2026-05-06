package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quan_tri")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuanTri {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quan_tri_id")
    private Long quanTriId;
    
    @OneToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false, unique = true)
    private NguoiDung nguoiDung;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}

