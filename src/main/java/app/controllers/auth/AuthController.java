package app.controllers.auth;

import app.auth.AuthService;
import app.entities.User;
import jakarta.persistence.GeneratedValue;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class AuthController {

    private AuthService authService;


    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping(path = "/auth/login")
    public String get(){
        return "admin/login";
    }


    @PostMapping("/login")
    public String login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = authService.login(username,password);

        if(user == null){
            // Tambahkan pesan error
            redirectAttributes.addFlashAttribute("error", "Username atau password salah");
            return "redirect:/auth/login";
        }

        session.setAttribute("USER_LOGIN",user);
        return "redirect:/admin/dashboard";
    }

}
