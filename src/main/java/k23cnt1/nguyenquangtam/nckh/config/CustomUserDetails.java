package k23cnt1.nguyenquangtam.nckh.config;

import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.entity.Quyen;
import k23cnt1.nguyenquangtam.nckh.entity.VaiTro;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final NguoiDung nguoiDung;

    public CustomUserDetails(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        for (VaiTro vaiTro : nguoiDung.getDanhSachVaiTro()) {
            authorities.add(new SimpleGrantedAuthority(vaiTro.getTenVaiTro()));
            for (Quyen quyen : vaiTro.getDanhSachQuyen()) {
                authorities.add(new SimpleGrantedAuthority(quyen.getMaQuyen()));
            }
        }
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return nguoiDung.getMatKhau();
    }

    @Override
    public String getUsername() {
        return nguoiDung.getTenDangNhap();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return nguoiDung.getTrangThai();
    }
    
    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }
}
