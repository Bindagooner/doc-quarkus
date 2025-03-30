package com.example.auth;

import com.example.model.Document;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InterceptorBinding;
import jakarta.interceptor.InvocationContext;

import java.util.Arrays;

@TenantSecured
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class TenantSecurityInterceptor {

    @Inject
    SecurityContext securityContext;

    @AroundInvoke
    public Object validateTenant(InvocationContext context) throws Exception {
        // Get the document from the method parameters
        Document document = Arrays.stream(context.getParameters())
                .filter(param -> param instanceof Document)
                .map(param -> (Document) param)
                .findFirst()
                .orElse(null);

        if (document != null && !securityContext.hasAccessToTenant(document.getTenantId())) {
            throw new SecurityException("Access denied: Invalid tenant");
        }

        return context.proceed();
    }
}
