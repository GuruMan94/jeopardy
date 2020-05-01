package ge.tsotne.jeopardy;

import ge.tsotne.jeopardy.configuration.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }
}
