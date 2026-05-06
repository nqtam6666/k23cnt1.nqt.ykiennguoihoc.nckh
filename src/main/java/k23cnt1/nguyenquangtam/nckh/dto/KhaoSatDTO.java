package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhaoSatDTO {
    
    private Long khaoSatId;
    
    @NotBlank(message = "Tên khảo sát không được để trống")
    private String tenKhaoSat;
    
    private String hocKy;
    
    private String namHoc;
}

