package org.em.miron.captcha.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.em.miron.captcha.util.CaptchaServletUtil.CAPTCHA_ATTRIBUTE;

/**
 * Removes the captcha attribute from session, thereby resets captcha.
 *
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public class RefreshCaptchaFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        // We should do this only in case redirect via browser
        if (httpRequest.getMethod().equals("GET")) {
            httpRequest.getSession().removeAttribute(CAPTCHA_ATTRIBUTE);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
