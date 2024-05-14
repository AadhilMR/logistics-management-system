package com.aadhil.ejb.interceptor;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.impl.RouteServiceBean;
import com.aadhil.ejb.remote.DataFetchService;
import jakarta.ejb.EJB;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class FetchTerminalsInterceptor {
    private static final Pattern PATTERN = Pattern.compile("\\s*,\\s*");

    @EJB
    private DataFetchService dataFetchService;

    @AroundInvoke
    public Object fetch(InvocationContext invocationContext) throws Exception {
        RouteServiceBean routeServiceBean = (RouteServiceBean) invocationContext.getTarget();

        Object[] parameters = invocationContext.getParameters();
        String terminals = (String) parameters[1];

        String[] terminalArray = PATTERN.split(terminals);
        List<String> terminalNameList = Arrays.asList(terminalArray);
        List<Terminal> terminalList = dataFetchService.fetchTerminals(terminalNameList);

        routeServiceBean.setTerminalList(terminalList);

        return invocationContext.proceed();
    }
}
