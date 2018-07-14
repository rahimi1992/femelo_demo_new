package com.femelo.femelo_demo;

import java.io.Serializable;

class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mId;
    private String mUsername;
    private String mFirst_name;
    private String mLast_name;
    private String mEmail;
    private Billing mBilling;
    private String mRole;


    public Customer(int id, String username, String first_name, String last_name, String email, Billing billing, String role) {
        mId = id;
        mUsername = username;
        mFirst_name = first_name;
        mLast_name = last_name;
        mEmail = email;
        mBilling = billing;
        mRole = role;
    }

    public void setFirst_name(String first_name) {
        mFirst_name = first_name;
    }

    public void setLast_name(String last_name) {
        mLast_name = last_name;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setBilling(Billing billing) {
        mBilling = billing;
    }

    public void setRole(String role) {
        mRole = role;
    }

    public int getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getFirst_name() {
        return mFirst_name;
    }

    public String getLast_name() {
        return mLast_name;
    }

    public String getEmail() {
        return mEmail;
    }

    public Billing getBilling() {
        return mBilling;
    }

    public String getRole() {
        return mRole;
    }

    static class Billing{
        private String mFirst_name;
        private String mLast_name;
        private String mAddress_1;
        private String mCity;
        private String mState;
        private String mPostcode;
        private String mEmail;
        private String mPhone;

        public Billing(String first_name, String last_name, String address_1, String city, String state, String postcode, String email, String phone) {
            mFirst_name = first_name;
            mLast_name = last_name;
            mAddress_1 = address_1;
            mCity = city;
            mState = state;
            mPostcode = postcode;
            mEmail = email;
            mPhone = phone;
        }

        public void setFirst_name(String first_name) {
            mFirst_name = first_name;
        }

        public void setLast_name(String last_name) {
            mLast_name = last_name;
        }

        public void setAddress_1(String address_1) {
            mAddress_1 = address_1;
        }

        public void setCity(String city) {
            mCity = city;
        }

        public void setState(String state) {
            mState = state;
        }

        public void setPostcode(String postcode) {
            mPostcode = postcode;
        }

        public void setEmail(String email) {
            mEmail = email;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }

        public String getFirst_name() {
            return mFirst_name;
        }

        public String getLast_name() {
            return mLast_name;
        }

        public String getAddress_1() {
            return mAddress_1;
        }

        public String getCity() {
            return mCity;
        }

        public String getState() {
            return mState;
        }

        public String getPostcode() {
            return mPostcode;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getPhone() {
            return mPhone;
        }

        @Override
        public String toString() {
            return "Billing{" +
                    "mFirstname='" + mFirst_name + '\'' +
                    ", mLastname='" + mLast_name + '\'' +
                    ", mAddress_1='" + mAddress_1 + '\'' +
                    ", mCity='" + mCity + '\'' +
                    ", mState='" + mState + '\'' +
                    ", mPostcode='" + mPostcode + '\'' +
                    ", mEmail='" + mEmail + '\'' +
                    ", mPhone='" + mPhone + '\'' +
                    '}';
        }
    }
}
