package com.femelo.femelo_demo;

import java.io.Serializable;
import java.util.List;

class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mId;
    private String mPayment_method;
    private String mPayment_method_title;
    private boolean mSet_paid;
    private int mCustomer_id;
    private String mStatus;
    private String Currency;
    private List<OrderItem> mLine_items;
    private Customer.Billing mBilling;
    private List<Shipping_line> mShipping_lines;

    public Order() {
    }

    public Order(int id, String payment_method, String payment_method_title, boolean set_paid, int customer_id, String status, List<OrderItem> line_items, Customer.Billing billing, List<Shipping_line> shipping_lines) {
        mId = id;
        mPayment_method = payment_method;
        mPayment_method_title = payment_method_title;
        mSet_paid = set_paid;
        mCustomer_id = customer_id;
        mStatus = status;
        mLine_items = line_items;
        mBilling = billing;
        mShipping_lines = shipping_lines;
        Currency = "IRT";
    }

    public int getCustomer_Id() {
        return mCustomer_id;
    }

    public void setCustomer_Id(int customer_Id) {
        mCustomer_id = customer_Id;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPayment_method() {
        return mPayment_method;
    }

    public void setPayment_method(String payment_method) {
        mPayment_method = payment_method;
    }

    public String getPayment_method_title() {
        return mPayment_method_title;
    }

    public void setPayment_method_title(String payment_method_title) {
        mPayment_method_title = payment_method_title;
    }

    public boolean isSet_paid() {
        return mSet_paid;
    }

    public void setSet_paid(boolean set_paid) {
        mSet_paid = set_paid;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public List<OrderItem> getLine_items() {
        return mLine_items;
    }

    public void setLine_items(List<OrderItem> line_items) {
        mLine_items = line_items;
    }

    public Customer.Billing getBilling() {
        return mBilling;
    }

    public void setBilling(Customer.Billing billing) {
        mBilling = billing;
    }

    public List<Shipping_line> getShipping_lines() {
        return mShipping_lines;
    }

    public void setShipping_lines(List<Shipping_line> shipping_lines) {
        mShipping_lines = shipping_lines;
    }



    static class OrderItem {
        //private int mId;
        private String mName;
        private int mProductPos;
        private int mProduct_id;
        private int mQuantity;
        private String mTotal;
        private String mImage;

        public OrderItem(String name,int productPos, int product_id, int quantity, String total, String image) {
            //mId = id;
            mName = name;
            mProductPos = productPos;
            mProduct_id = product_id;
            mQuantity = quantity;
            mTotal = total;
            mImage = image;

        }


        public String getName() {
            return mName;
        }

        public int getProductPos() {
            return mProductPos;
        }

        public int getProduct_id() {
            return mProduct_id;
        }

        public int getQuantity() {
            return mQuantity;
        }

        public String getTotal() {
            return mTotal;
        }

        public String getImage() {
            return mImage;
        }



        public void setName(String name) {
            mName = name;
        }

        public void setProductPos(int productPos) {
            mProductPos = productPos;
        }

        public void setProduct_id(int product_id) {
            mProduct_id = product_id;
        }

        public void setQuantity(int quantity) {
            mQuantity = quantity;
        }

        public void setTotal(String total) {
            mTotal = total;
        }

        public void setImage(String image) {
            mImage = image;
        }

        public void plusQuantity() {
            mQuantity++;
        }

        public void minusQuantity() {
            if (mQuantity > 1)
                mQuantity--;
        }

        @Override
        public String toString() {
            return "{\"OrderItem\":{"
                    + ", \"mName\":\"" + mName + "\""
                    + ", \"mProductPos\":\"" + mProductPos + "\""
                    + ", \"mProduct_id\":\"" + mProduct_id + "\""
                    + ", \"mQuantity\":\"" + mQuantity + "\""
                    + ", \"mTotal\":\"" + mTotal + "\""
                    + ", \"mImage\":\"" + mImage + "\""
                    + "}}";
        }
    }

    static class Shipping_line{
        private String mMethod_id;
        private String mMethod_title;
        private String mTotal;

        public Shipping_line(String method_id, String method_title, String total) {
            mMethod_id = method_id;
            mMethod_title = method_title;
            mTotal = total;
        }

        public String getMethod_id() {
            return mMethod_id;
        }

        public void setMethod_id(String method_id) {
            mMethod_id = method_id;
        }

        public String getMethod_title() {
            return mMethod_title;
        }

        public void setMethod_title(String method_title) {
            mMethod_title = method_title;
        }

        public String getTotal() {
            return mTotal;
        }

        public void setTotal(String total) {
            mTotal = total;
        }
    }

}
