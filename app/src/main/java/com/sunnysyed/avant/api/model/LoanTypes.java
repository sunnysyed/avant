
package com.sunnysyed.avant.api.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanTypes {

    @SerializedName("loan_types")
    @Expose
    private List<String> loanTypes = new ArrayList<String>();

    /**
     * 
     * @return
     *     The loanTypes
     */
    public List<String> getLoanTypes() {
        return loanTypes;
    }

    /**
     * 
     * @param loanTypes
     *     The loan_types
     */
    public void setLoanTypes(List<String> loanTypes) {
        this.loanTypes = loanTypes;
    }

}
