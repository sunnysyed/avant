
package com.sunnysyed.avant.api.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanApplication {

    @SerializedName("loan_application_type")
    @Expose
    private String loanApplicationType;
    @SerializedName("loan_application_attachments")
    @Expose
    private List<LoanApplicationAttachment> loanApplicationAttachments = new ArrayList<LoanApplicationAttachment>();
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("application_status")
    @Expose
    private String applicationStatus;

    /**
     * 
     * @return
     *     The loanApplicationType
     */
    public String getLoanApplicationType() {
        return loanApplicationType;
    }

    /**
     * 
     * @param loanApplicationType
     *     The loan_application_type
     */
    public void setLoanApplicationType(String loanApplicationType) {
        this.loanApplicationType = loanApplicationType;
    }

    /**
     * 
     * @return
     *     The loanApplicationAttachments
     */
    public List<LoanApplicationAttachment> getLoanApplicationAttachments() {
        return loanApplicationAttachments;
    }

    /**
     * 
     * @param loanApplicationAttachments
     *     The loan_application_attachments
     */
    public void setLoanApplicationAttachments(List<LoanApplicationAttachment> loanApplicationAttachments) {
        this.loanApplicationAttachments = loanApplicationAttachments;
    }

    /**
     * 
     * @return
     *     The customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * 
     * @param customerId
     *     The customer_id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The applicationStatus
     */
    public String getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * 
     * @param applicationStatus
     *     The application_status
     */
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

}
