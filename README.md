SmartCaptcha
==========

## What is it?
 SmartCaptcha is a open source Java library, which used for generation of image or/and audio captcha at the page.
 
## Requirements
* Java 6 and later.
* Servlet Container or Application server with Servlets 2.4 and later.

## How to 
To include captcha into your page you should to define servlet(s) and filter to your web.xml
  ``` xml
   <servlet>
        <servlet-name>ImageCaptchaServlet</servlet-name>
        <servlet-class>org.em.miron.captcha.servlet.CaptchaServlet</servlet-class>
    </servlet>
    <servlet>
        <servlet-name>AudioCaptchaServlet</servlet-name>
        <servlet-class>org.em.miron.captcha.servlet.AudioCaptchaServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>AudioCaptchaServlet</servlet-name>
        <url-pattern>/audioCaptcha</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>ImageCaptchaServlet</servlet-name>
        <url-pattern>/captcha</url-pattern>
    </servlet-mapping>

    <filter>
        <filter-name>RefreshCaptchaFilter</filter-name>
        <filter-class>org.em.miron.captcha.servlet.RefreshCaptchaFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>RefreshCaptchaFilter</filter-name>
        <url-pattern>/captcha.xhtml</url-pattern>
    </filter-mapping>
  ```
Replace "/captcha.xhtml" by path to your page with captcha.

Add 
``` html
    <img src="captcha" alt="Captcha"></img>
    <input type="button" onclick="playSound('audioCaptcha')" value="Play"/>
```
to your page.
"captcha" is path to your ImageCaptchaServlet and "audioCaptcha" is path to your AudioCaptchaServlet. 
In resources of library you will find audio.js, which play the audio at the page. 
If you want to use your own solution, just add "audioCaptcha" to src of HTML object, embed or audio.
The order to get the answer for captcha you need get session attribute with name "CAPTCHA" or you can use contant CaptchaServletUtil.CAPTCHA_ATTRIBUTE, which already contains this name
``` java
String answer = request.getSession().getAttribute(CaptchaServletUtil.CAPTCHA_ATTRIBUTE);
``` 
## Restriction
Don't use GET request to check the answer. Because of included RefreshCaptchaFilter we clean a previous 
value from session attribute. GET request serves for this purpose.
