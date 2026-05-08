package k23cnt1.nguyenquangtam.nckh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vai_tro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vai_tro_id")
    private Long vaiTroId;

    @Column(name = "ten_vai_tro", nullable = false, unique = true, length = 50)
    private String tenVaiTro;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "mac_dinh", nullable = false)
    private Boolean macDinh = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "vai_tro_quyen",
        joinColumns = @JoinColumn(name = "vai_tro_id"),
        inverseJoinColumns = @JoinColumn(name = "quyen_id")
    )
    private Set<Quyen> danhSachQuyen = new HashSet<>();
}
