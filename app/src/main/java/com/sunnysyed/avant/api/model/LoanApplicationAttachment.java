
package com.sunnysyed.avant.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanApplicationAttachment {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("loan_application_id")
    @Expose
    private Integer loanApplicationId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("attachment_type")
    @Expose
    private String attachmentType;

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The loanApplicationId
     */
    public Integer getLoanApplicationId() {
        return loanApplicationId;
    }

    /**
     * 
     * @param loanApplicationId
     *     The loan_application_id
     */
    public void setLoanApplicationId(Integer loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    /**
     * 
     * @return
     *     The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 
     * @param imageUrl
     *     The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getAttachmentType() {
        return attachmentType;
    }

}
