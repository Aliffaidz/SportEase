package app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.logging.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private FilterAuth filterAuth;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(filterAuth)
                .addPathPatterns("/admin/**");
    }
}
