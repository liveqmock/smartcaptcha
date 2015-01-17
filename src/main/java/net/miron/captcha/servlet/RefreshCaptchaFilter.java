package net.miron.captcha.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static net.miron.captcha.util.CaptchaServletUtil.CAPTCHA_ATTRIBUTE;

/**
 * Removes the captcha attribute from session, thereby resets captcha.
 */
public class RefreshCaptchaFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletRequest) request).getSession().removeAttribute(CAPTCHA_ATTRIBUTE);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
