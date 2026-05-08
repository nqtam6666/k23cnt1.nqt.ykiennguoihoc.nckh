package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quyen_id")
    private Long quyenId;

    @Column(name = "ma_quyen", nullable = false, unique = true, length = 100)
    private String maQuyen;

    @Column(name = "ten_quyen", length = 200)
    private String tenQuyen;

    @Column(name = "nhom", length = 50)
    private String nhom;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
}
