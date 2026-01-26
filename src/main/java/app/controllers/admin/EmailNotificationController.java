package app.controllers.admin;

import app.notification.SendEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmailNotificationController {

    private SendEmailService sendEmailService;

    public EmailNotificationController(SendEmailService sendEmailService){
        this.sendEmailService = sendEmailService;
    }

    @PostMapping(path = "/admin/email-notification")
    public String addEmail(String email){
        sendEmailService.addEmail(email);
        return "redirect:/admin/email-notification";
    }

    @GetMapping(path = "/admin/email-notification")
    public String getAllEmails(Model model){
        model.addAttribute("listEmails",sendEmailService.getAll());
        return "/admin/email-notifikasi";
    }

    @PutMapping(path = "/admin/email-notification/{id}")
    public String editEmail(String email,@PathVariable(name = "id") Integer id){
        sendEmailService.editEmail(email,id);
        return "redirect:/admin/email-notification";
    }

    @GetMapping(path = "/admin/email-notification/{id}")
    public String addEmail(@PathVariable(name = "id") Integer id){
        sendEmailService.deleteEmail(id);
        return "redirect:/admin/email-notification";
    }


}
