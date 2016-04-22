
package com.sunnysyed.avant.api.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("loan_applications")
    @Expose
    private List<LoanApplication> loanApplications = new ArrayList<LoanApplication>();

    /**
     * 
     * @return
     *     The profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * 
     * @param profile
     *     The profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * 
     * @return
     *     The loanApplications
     */
    public List<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    /**
     * 
     * @param loanApplications
     *     The loan_applications
     */
    public void setLoanApplications(List<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
    }

}
