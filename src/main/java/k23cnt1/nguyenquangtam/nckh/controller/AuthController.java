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

