package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiHocCapNhatDTO {
    
    private Long nguoiHocId;
    
    private String matKhau; // Optional - chỉ cập nhật nếu có
    
    @NotNull(message = "Vui lòng chọn giới tính")
    private Boolean gioiTinh; // true: nam, false: nữ
    
    @NotNull(message = "Năm sinh không được để trống")
    private Integer namSinh;
    
    @NotNull(message = "Vui lòng chọn khoa")
    private Long khoaId;
    
    private String nganhHoc;
    
    private String khoaHoc;
}

