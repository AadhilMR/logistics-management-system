package com.aadhil.ejb.interceptor;

import com.aadhil.ejb.exception.CargoTransactionException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class TransactionParameterValidationInterceptor {
    @AroundInvoke
    public Object validateParameters(InvocationContext invocationContext) throws Exception {
        Object[] parameters = invocationContext.getParameters();

        String cargoId = (String) parameters[0];
        int originId = (int) parameters[1];
        int destinationId = (int) parameters[2];

        if(cargoId.isEmpty() || cargoId.equals("0")) {
            throw new CargoTransactionException("Parameters are not valid.");
        }
        if(originId == 0 || destinationId == 0) {
            throw new CargoTransactionException("Parameters are not valid.");
        }

        return invocationContext.proceed();
    }
}
