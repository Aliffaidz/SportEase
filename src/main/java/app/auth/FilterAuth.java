package app.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
@Slf4j
public class FilterAuth implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        log.info("filer =============");
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("USER_LOGIN") == null){
            response.sendRedirect("/auth/login");
            return false;
        }
        log.info("filer berhasil========");
        return true;
    }


}
