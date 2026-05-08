package k23cnt1.nguyenquangtam.nckh.config;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhapAndTrangThai(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản hoặc tài khoản bị khóa: " + username));
        
        // Eager load roles & permissions if not loaded (though we used EAGER fetch in entity, this is just to be safe)
        nguoiDung.getDanhSachVaiTro().size();
        
        return new CustomUserDetails(nguoiDung);
    }
}
