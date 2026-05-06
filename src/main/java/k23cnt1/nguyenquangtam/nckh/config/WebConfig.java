package k23cnt1.nguyenquangtam.nckh.config;

import k23cnt1.nguyenquangtam.nckh.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/giang-vien/**", "/nguoi-hoc/**")
                .excludePathPatterns("/auth/**", "/css/**", "/js/**", "/images/**");
    }
}

