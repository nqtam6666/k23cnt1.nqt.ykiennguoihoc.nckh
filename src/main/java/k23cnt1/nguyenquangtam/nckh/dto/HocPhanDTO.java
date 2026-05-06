package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocPhanDTO {
    
    private Long hocPhanId;
    
    @NotBlank(message = "Tên học phần không được để trống")
    private String tenHocPhan;
    
    @NotNull(message = "Số tín chỉ không được để trống")
    @Min(value = 1, message = "Số tín chỉ phải lớn hơn 0")
    private Integer soTinChi;
    
    @NotNull(message = "Vui lòng chọn khoa")
    private Long khoaId;
}

