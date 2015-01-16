package net.miron.captcha.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static net.miron.captcha.util.CaptchaServletUtil.CAPTCHA_ATTRIBUTE;

/**
 * Created by Evgeny Mironenko on 16.01.2015.
 */
public class RefreshCaptchaFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletRequest)request).getSession().removeAttribute(CAPTCHA_ATTRIBUTE);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
