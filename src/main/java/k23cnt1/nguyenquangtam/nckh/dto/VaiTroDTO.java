package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaiTroDTO {
    
    private Long vaiTroId;
    
    @NotBlank(message = "Tên vai trò (Mã Role) không được để trống")
    private String tenVaiTro;
    
    @NotBlank(message = "Tên hiển thị không được để trống")
    private String moTa;
    
    private Boolean macDinh = false;
    
    private List<Long> quyenIds = new ArrayList<>();
}
