package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.WireLinePackageModel;

import java.util.List;

public class PackagesWirelineResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<WireLinePackageModel> PACKAGES;
    /*private List<WireLinePackageModel>  PACKAGENAMES;
    private List<WireLinePackageModel>  PRICEPLANIDS;*/

    public PackagesWirelineResponse() {
    }

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

    public List<WireLinePackageModel> getPACKAGES() {
        return PACKAGES;
    }

    public void setPACKAGES(List<WireLinePackageModel> PACKAGES) {
        this.PACKAGES = PACKAGES;
    }

    /*  public PackagesWirelineResponse(long errorCode, String errorDetail, List<WireLinePackageModel> PACKAGENAMES, List<WireLinePackageModel> PRICEPLANIDS) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.PACKAGENAMES = PACKAGENAMES;
        this.PRICEPLANIDS = PRICEPLANIDS;
    }


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

    public List<WireLinePackageModel> getPACKAGENAMES() {
        return PACKAGENAMES;
    }

    public void setPACKAGENAMES(List<WireLinePackageModel> PACKAGENAMES) {
        this.PACKAGENAMES = PACKAGENAMES;
    }

    public List<WireLinePackageModel> getPRICEPLANIDS() {
        return PRICEPLANIDS;
    }

    public void setPRICEPLANIDS(List<WireLinePackageModel> PRICEPLANIDS) {
        this.PRICEPLANIDS = PRICEPLANIDS;
    }*/
}
