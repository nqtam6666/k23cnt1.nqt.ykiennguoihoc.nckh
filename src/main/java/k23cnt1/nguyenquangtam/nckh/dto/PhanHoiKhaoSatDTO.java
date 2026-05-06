package k23cnt1.nguyenquangtam.nckh.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanHoiKhaoSatDTO {
    
    @NotNull(message = "Vui lòng chọn khảo sát")
    private Long khaoSatId;
    
    // Map: cauHoiId -> diemDanhGia
    @NotNull(message = "Vui lòng trả lời tất cả câu hỏi")
    private Map<Long, Integer> traLoi; // Key: cauHoiId, Value: diemDanhGia (1-5)
    
    // Map: cauHoiId -> yKienKhac
    private Map<Long, String> yKienKhac; // Key: cauHoiId, Value: ý kiến khác
}

