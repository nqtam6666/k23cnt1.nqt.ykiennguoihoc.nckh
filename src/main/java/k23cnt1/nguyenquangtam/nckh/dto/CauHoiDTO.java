package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CauHoiDTO {
    
    private Long cauHoiId;
    
    @NotNull(message = "Vui lòng chọn khảo sát")
    private Long khaoSatId;
    
    @NotNull(message = "Vui lòng chọn nhóm dịch vụ")
    private Long nhomDichVuId;
    
    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String noiDung;
    
    private String loaiThangDo = "Likert 1-5";
}

