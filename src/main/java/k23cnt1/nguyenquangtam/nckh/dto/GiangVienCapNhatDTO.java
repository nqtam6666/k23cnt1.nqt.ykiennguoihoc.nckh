package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiangVienCapNhatDTO {
    
    private Long giangVienId;
    
    private String matKhau; // Optional - chỉ cập nhật nếu có
    
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;
    
    private String hocVi;
    
    @NotNull(message = "Vui lòng chọn khoa")
    private Long khoaId;
    
    private Integer soNamKinhNghiem;
    
    private Integer namBatDau; // Năm bắt đầu làm việc
}

