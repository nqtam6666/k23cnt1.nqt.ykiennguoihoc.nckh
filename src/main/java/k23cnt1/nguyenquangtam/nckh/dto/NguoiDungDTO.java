package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungDTO {
    
    private Long nguoiDungId;
    
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;
    
    private String matKhau; // Optional khi sửa
    
    @NotBlank(message = "Vai trò không được để trống")
    private String vaiTro; // admin, giang_vien, nguoi_hoc
    
    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai = true;
}

