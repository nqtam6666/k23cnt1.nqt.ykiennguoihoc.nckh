package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;
}

