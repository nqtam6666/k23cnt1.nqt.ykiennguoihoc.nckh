package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanCongDTO {
    
    private Long phanCongId;
    
    @NotNull(message = "Vui lòng chọn giảng viên")
    private Long giangVienId;
    
    @NotNull(message = "Vui lòng chọn học phần")
    private Long hocPhanId;
    
    private String hocKy;
    
    private String namHoc;
}

