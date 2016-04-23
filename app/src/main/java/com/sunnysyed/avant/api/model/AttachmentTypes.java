
package com.sunnysyed.avant.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AttachmentTypes {

    @SerializedName("attachment_types")
    @Expose
    private List<String> attachmentTypes = new ArrayList<String>();

    /**
     * 
     * @return
     *     The loanTypes
     */
    public List<String> getAttachmentTypes() {
        return attachmentTypes;
    }

}
