package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportNguoiDungDTO {
    
    @NotBlank(message = "Vui lòng nhập danh sách tài khoản")
    private String danhSachTaiKhoan; // Mỗi dòng một tài khoản
    
    @NotBlank(message = "Vui lòng nhập mật khẩu chung")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhauChung;
    
    private String vaiTro = "nguoi_hoc"; // Mặc định là sinh viên
}

