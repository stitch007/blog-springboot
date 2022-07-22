package run.stitch.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class IpUtil {
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null, unknown = "unknown", separator = ",";
        int maxLength = 15;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("解析ip失败", e);
        }

        if (StringUtils.isEmpty(ip) && ip.length() > maxLength) {
            int idx = ip.indexOf(separator);
            if (idx > 0) {
                ip = ip.substring(0, idx);
            }
        }

        return ip;
    }

    public static String getIpAddr() {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        return getIpAddr(request);
    }
}
