
package com.sunnysyed.avant.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("reset_password_key")
    @Expose
    private String resetPasswordKey;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("registration_key")
    @Expose
    private String registrationKey;

    /**
     * 
     * @return
     *     The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     *     The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return
     *     The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName
     *     The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 
     * @return
     *     The resetPasswordKey
     */
    public String getResetPasswordKey() {
        return resetPasswordKey;
    }

    /**
     * 
     * @param resetPasswordKey
     *     The reset_password_key
     */
    public void setResetPasswordKey(String resetPasswordKey) {
        this.resetPasswordKey = resetPasswordKey;
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
     *     The registrationId
     */
    public String getRegistrationId() {
        return registrationId;
    }

    /**
     * 
     * @param registrationId
     *     The registration_id
     */
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 
     * @return
     *     The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return
     *     The registrationKey
     */
    public String getRegistrationKey() {
        return registrationKey;
    }

    /**
     * 
     * @param registrationKey
     *     The registration_key
     */
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

}
