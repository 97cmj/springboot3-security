package com.cmj.api.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class IpAddressRequestMatcher implements RequestMatcher {

    private final IpAddressMatcher ipAddressMatcher;

    public IpAddressRequestMatcher(String allowedIpAddress) {
        this.ipAddressMatcher = new IpAddressMatcher(allowedIpAddress);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return ipAddressMatcher.matches(request);
    }
}