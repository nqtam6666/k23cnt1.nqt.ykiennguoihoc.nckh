package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    
    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    private String tenDangNhap;
    
    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhau;
    
    @NotBlank(message = "Vui lòng xác nhận mật khẩu")
    private String xacNhanMatKhau;
}

