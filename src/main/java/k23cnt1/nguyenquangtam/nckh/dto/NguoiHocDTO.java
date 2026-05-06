package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiHocDTO {
    
    private Long nguoiHocId;
    
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;
    
    @NotNull(message = "Vui lòng chọn giới tính")
    private Boolean gioiTinh; // true: nam, false: nữ
    
    @NotNull(message = "Năm sinh không được để trống")
    private Integer namSinh;
    
    @NotNull(message = "Vui lòng chọn khoa")
    private Long khoaId;
    
    @NotBlank(message = "Ngành học không được để trống")
    private String nganhHoc;
    
    @NotBlank(message = "Khóa học không được để trống")
    private String khoaHoc;
}

