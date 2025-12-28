package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanHoiDTO {
    
    private Long id;
    
    @NotBlank(message = "Mã sinh viên không được để trống")
    private String maSinhVien;
    
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String soDienThoai;
    
    @NotNull(message = "Vui lòng chọn dịch vụ đào tạo")
    private Long dichVuDaoTaoId;
    
    @NotNull(message = "Vui lòng chọn loại phản hồi")
    private Long loaiPhanHoiId;
    
    @NotBlank(message = "Nội dung phản hồi không được để trống")
    @Size(min = 10, message = "Nội dung phản hồi phải có ít nhất 10 ký tự")
    private String noiDung;
    
    @Min(value = 1, message = "Điểm đánh giá phải từ 1 đến 5")
    @Max(value = 5, message = "Điểm đánh giá phải từ 1 đến 5")
    private Integer diemDanhGia;
}

