package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.ResponseModel;

import java.util.List;

public class RebateResponse {

    private long ErrorCode;
    private String ErrorDetail;

    private List<FinesseCalculatorResponseModel> finesseCalculator;
   // private FinesseCalculatorResponseModel finesseCalculator;

    public long getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(long errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorDetail() {
        return ErrorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        ErrorDetail = errorDetail;
    }

    public List<FinesseCalculatorResponseModel> getFinesseCalculator() {
        return finesseCalculator;
    }

    public void setFinesseCalculator(List<FinesseCalculatorResponseModel> finesseCalculator) {
        this.finesseCalculator = finesseCalculator;
    }
}
