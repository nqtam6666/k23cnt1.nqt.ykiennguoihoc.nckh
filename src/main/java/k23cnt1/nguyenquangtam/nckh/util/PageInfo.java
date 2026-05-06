package k23cnt1.nguyenquangtam.nckh.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public static <T> PageInfo<T> of(List<T> content, int page, int size, long total) {
        int totalPages = (int) Math.ceil((double) total / size);
        return new PageInfo<>(
            content,
            page,
            totalPages,
            total,
            size,
            page < totalPages - 1,
            page > 0
        );
    }
}

