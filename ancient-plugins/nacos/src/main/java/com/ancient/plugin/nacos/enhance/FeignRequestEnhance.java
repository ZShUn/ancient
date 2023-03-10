package com.ancient.plugin.nacos.enhance;

import com.ancient.agent.core.context.CustomContextAccessor;
import com.ancient.agent.core.enhance.InstanceEnhance;
import com.ancient.agent.core.interceptor.MethodInterceptorResult;
import com.ancient.common.constant.HeaderConstant;
import com.ancient.common.context.RuleContext;
import feign.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;

public class FeignRequestEnhance implements InstanceEnhance {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignRequestEnhance.class);

    private static final String METHOD_NAME = "execute";

    private static final String DECLARED_FIELD_NAME = "headers";

    private static final FeignRequestEnhance INSTANCE = new FeignRequestEnhance();

    public static FeignRequestEnhance getInstance() {
        return INSTANCE;
    }

    @Override
    public void enhance(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result, MethodInterceptorResult methodInterceptorResult) {
        if (method.getName().equals(METHOD_NAME)) {
            if (allArguments.length > 0 && allArguments[0] instanceof Request) {
                Request request = (Request) allArguments[0];
                Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
                List<String> contextCollection = new ArrayList<String>(1);
                contextCollection.add(RuleContext.get());
                headers.put(HeaderConstant.RULE, contextCollection);
                headers.putAll(request.headers());
                try {
                    Field headersField = Request.class.getDeclaredField(DECLARED_FIELD_NAME);
                    headersField.setAccessible(true);
                    headersField.set(request, Collections.unmodifiableMap(headers));
                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }
        }
    }

}
