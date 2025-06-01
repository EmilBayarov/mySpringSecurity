package org.example.myspringsecurity.securityLib.core.context;

import java.util.function.Supplier;

public interface SecurityContextHolderStrategy {
    void clearContext();
    SecurityContext getContext();
    default Supplier<SecurityContext> getDeferredContext() {
        return this::getContext;
    }
    void setContext(SecurityContext context);
    default void setDeferredContext(Supplier<SecurityContext> context) {
        setContext(context.get());
    }
    SecurityContext createEmptyContext();
}
