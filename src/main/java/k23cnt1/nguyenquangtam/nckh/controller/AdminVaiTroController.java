package k23cnt1.nguyenquangtam.nckh.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt1.nguyenquangtam.nckh.dto.VaiTroDTO;
import k23cnt1.nguyenquangtam.nckh.entity.NguoiDung;
import k23cnt1.nguyenquangtam.nckh.entity.VaiTro;
import k23cnt1.nguyenquangtam.nckh.entity.Quyen;
import k23cnt1.nguyenquangtam.nckh.service.VaiTroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/vai-tro")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminVaiTroController {

    private final VaiTroService vaiTroService;

    @GetMapping
    public String danhSachVaiTro(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("danhSachVaiTro", vaiTroService.layTatCaVaiTro());
        model.addAttribute("pageTitle", "Quản lý Vai trò");
        return "admin/vai-tro/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("vaiTroDTO", new VaiTroDTO());
        Map<String, List<Quyen>> quyenTheoNhom = vaiTroService.layTatCaQuyen().stream()
                .collect(Collectors.groupingBy(Quyen::getNhom));
        model.addAttribute("quyenTheoNhom", quyenTheoNhom);
        model.addAttribute("pageTitle", "Thêm Vai trò mới");
        return "admin/vai-tro/form";
    }

    @PostMapping("/them")
    public String xuLyThemVaiTro(@Valid @ModelAttribute("vaiTroDTO") VaiTroDTO vaiTroDTO,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            Map<String, List<Quyen>> quyenTheoNhom = vaiTroService.layTatCaQuyen().stream()
                    .collect(Collectors.groupingBy(Quyen::getNhom));
            model.addAttribute("quyenTheoNhom", quyenTheoNhom);
            model.addAttribute("pageTitle", "Thêm Vai trò mới");
            return "admin/vai-tro/form";
        }

        try {
            vaiTroService.taoVaiTro(vaiTroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm vai trò thành công!");
            return "redirect:/admin/vai-tro";
        } catch (RuntimeException e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("errorMessage", e.getMessage());
            Map<String, List<Quyen>> quyenTheoNhom = vaiTroService.layTatCaQuyen().stream()
                    .collect(Collectors.groupingBy(Quyen::getNhom));
            model.addAttribute("quyenTheoNhom", quyenTheoNhom);
            model.addAttribute("pageTitle", "Thêm Vai trò mới");
            return "admin/vai-tro/form";
        }
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";

        try {
            VaiTro vaiTro = vaiTroService.layVaiTroTheoId(id);
            VaiTroDTO dto = new VaiTroDTO();
            dto.setVaiTroId(vaiTro.getVaiTroId());
            dto.setTenVaiTro(vaiTro.getTenVaiTro());
            dto.setMoTa(vaiTro.getMoTa());
            dto.setMacDinh(vaiTro.getMacDinh());
            
            List<Long> quyenIds = vaiTro.getDanhSachQuyen().stream()
                    .map(Quyen::getQuyenId)
                    .collect(Collectors.toList());
            dto.setQuyenIds(quyenIds);

            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("vaiTroDTO", dto);
            Map<String, List<Quyen>> quyenTheoNhom = vaiTroService.layTatCaQuyen().stream()
                    .collect(Collectors.groupingBy(Quyen::getNhom));
            model.addAttribute("quyenTheoNhom", quyenTheoNhom);
            model.addAttribute("pageTitle", "Cập nhật Vai trò");
            return "admin/vai-tro/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/vai-tro";
        }
    }

    @PostMapping("/sua/{id}")
    public String xuLySuaVaiTro(@PathVariable Long id,
                                @Valid @ModelAttribute("vaiTroDTO") VaiTroDTO vaiTroDTO,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDung");
        if (nguoiDung == null) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            Map<String, List<Quyen>> quyenTheoNhom = vaiTroService.layTatCaQuyen().stream()
                    .collect(Collectors.groupingBy(Quyen::getNhom));
            model.addAttribute("quyenTheoNhom", quyenTheoNhom);
            model.addAttribute("pageTitle", "Cập nhật Vai trò");
            return "admin/vai-tro/form";
        }

        try {
            vaiTroService.capNhatVaiTro(id, vaiTroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật vai trò thành công!");
            return "redirect:/admin/vai-tro";
        } catch (RuntimeException e) {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("errorMessage", e.getMessage());
            Map<String, List<Quyen>> quyenTheoNhom = vaiTroService.layTatCaQuyen().stream()
                    .collect(Collectors.groupingBy(Quyen::getNhom));
            model.addAttribute("quyenTheoNhom", quyenTheoNhom);
            model.addAttribute("pageTitle", "Cập nhật Vai trò");
            return "admin/vai-tro/form";
        }
    }

    @PostMapping("/xoa/{id}")
    public String xoaVaiTro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vaiTroService.xoaVaiTro(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa vai trò thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/vai-tro";
    }
}
