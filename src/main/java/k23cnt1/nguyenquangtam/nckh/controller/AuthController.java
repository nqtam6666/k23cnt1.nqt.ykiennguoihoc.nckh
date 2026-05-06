package k23cnt1.nguyenquangtam.nckh.controller;

import k23cnt1.nguyenquangtam.nckh.dto.LoginDTO;
import k23cnt1.nguyenquangtam.nckh.dto.RegisterDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@Valid LoginDTO loginDTO, 
                       BindingResult result, 
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/login";
        }
        
        Optional<NguoiDung> nguoiDung = authService.authenticate(loginDTO.getTenDangNhap(), loginDTO.getMatKhau());
        
        if (nguoiDung.isPresent()) {
            session.setAttribute("nguoiDung", nguoiDung.get());
            session.setAttribute("vaiTro", nguoiDung.get().getVaiTro());
            
            // Redirect theo vai trò
            String vaiTro = nguoiDung.get().getVaiTro();
            if ("admin".equals(vaiTro)) {
                return "redirect:/admin/dashboard";
            } else if ("giang_vien".equals(vaiTro)) {
                return "redirect:/giang-vien/dashboard";
            } else {
                return "redirect:/nguoi-hoc/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "redirect:/auth/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
    
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@Valid RegisterDTO registerDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        // Kiểm tra mật khẩu xác nhận
        if (!registerDTO.getMatKhau().equals(registerDTO.getXacNhanMatKhau())) {
            result.rejectValue("xacNhanMatKhau", "error.registerDTO", "Mật khẩu xác nhận không khớp!");
            return "auth/register";
        }
        
        try {
            // Đăng ký với vai trò mặc định là nguoi_hoc
            authService.dangKy(registerDTO.getTenDangNhap(), registerDTO.getMatKhau());
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
}

