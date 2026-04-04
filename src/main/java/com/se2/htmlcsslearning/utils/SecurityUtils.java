package com.se2.htmlcsslearning.utils;

import com.se2.htmlcsslearning.config.CustomUserDetails;
import com.se2.htmlcsslearning.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SecurityUtils {
    private SecurityUtils() {}

    public static void refreshAuthentication(User updatedUser) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth == null) return;

        Object principal = currentAuth.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) return;

        CustomUserDetails currentDetails = (CustomUserDetails) principal;
        if (!currentDetails.getUser().getId().equals(updatedUser.getId())) return;

        CustomUserDetails newDetails = new CustomUserDetails(updatedUser);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                newDetails,
                currentAuth.getCredentials(),
                newDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }


}