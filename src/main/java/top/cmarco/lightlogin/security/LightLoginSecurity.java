package top.cmarco.lightlogin.security;

import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.security.Permission;
import java.util.Objects;

@SuppressWarnings("removal")
public final class LightLoginSecurity extends SecurityManager {

    private LightLoginPlugin plugin;
    public LightLoginSecurity(@NotNull final LightLoginPlugin plugin) {
        if (plugin == null) {
            throw new RuntimeException("Cannot implement security from null plugin!");
        }

        this.plugin = plugin;
    }

    private static final String ALLOWED_PACKAGE_PREFIX = "top.cmarco.lightlogin";

    @Override
    public void checkPermission(@NotNull final Permission perm) {
        // You can implement more checks here if needed
        if (perm.getName().equals("suppressAccessChecks")) {
            checkPackageAccess();
        }
    }

    private void checkPackageAccess() {
        Class<?>[] classContext = getClassContext();
        for (final Class<?> clazz : classContext) {
            if (clazz.getName().startsWith(ALLOWED_PACKAGE_PREFIX)) {
                // System.out.println("allowed access from " + clazz.getName());
                return;  // Allow access if calling class is within the allowed package
            }
        }

        throw new SecurityException("Access denied to methods outside of " + ALLOWED_PACKAGE_PREFIX);
    }
}
