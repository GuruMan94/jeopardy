package ge.tsotne.jeopardy;

import ge.tsotne.jeopardy.configuration.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.constraints.NotNull;

public class Utils {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            return null;
        }
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }

    public static long getCurrentUserIdNotNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new RuntimeException("USER_NOT_FOUND");
        }
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }

    @NotNull
    public static UserPrincipal getCurrentUserNotNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new RuntimeException("USER_NOT_FOUND");
        }
        return (UserPrincipal) auth.getPrincipal();
    }
}
