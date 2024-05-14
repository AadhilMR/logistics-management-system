package com.aadhil.ejb.interceptor;

import com.aadhil.ejb.impl.CargoServiceBean;
import com.aadhil.ejb.remote.DataFetchService;
import jakarta.ejb.EJB;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class CargoIdGenerationInterceptor {

    @EJB
    private DataFetchService dataFetchService;

    @AroundInvoke
    public Object createCargoId(InvocationContext invocationContext) throws Exception {
        CargoServiceBean cargoServiceBean = (CargoServiceBean) invocationContext.getTarget();

        String newCargoId;
        String lastCargoId = null;

        try {
            lastCargoId = dataFetchService.fetchLastInsertCargo().getCargoId();
        } catch (Exception ignored) {
        } finally {
            if(lastCargoId == null) {
                newCargoId = "C1010";
            } else {
                int cargoIdNumber = Integer.parseInt(lastCargoId.substring(1));
                cargoIdNumber+=10;
                newCargoId = "C" + cargoIdNumber;
            }
        }

        cargoServiceBean.setCargoId(newCargoId);

        return invocationContext.proceed();
    }
}
