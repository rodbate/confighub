package com.iworker.bigdata.entity;


/**
 *
 * 目标费用实体类
 */
public class TargetFee {


    private long id;

    private long companyId;

    private long typeId;

    private int dateRangeType;

    private int year;

    private int month;

    private long userId;

    private double amount;

    private int storeId;

    //费用类型     1，不分费用类型，2分费用类型
    private int categoryId;

    private long orgId;

    private int feeTypeId;

    //to deprecate
    private double amountUsed;


    public TargetFee() {
    }

    public TargetFee(double amountUsed, long id, long companyId, long typeId, int dateRangeType, int year,
                     int month, long userId, double amount, int storeId, int categoryId, long orgId, int feeTypeId) {
        this.amountUsed = amountUsed;
        this.id = id;
        this.companyId = companyId;
        this.typeId = typeId;
        this.dateRangeType = dateRangeType;
        this.year = year;
        this.month = month;
        this.userId = userId;
        this.amount = amount;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.orgId = orgId;
        this.feeTypeId = feeTypeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public int getDateRangeType() {
        return dateRangeType;
    }

    public void setDateRangeType(int dateRangeType) {
        this.dateRangeType = dateRangeType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public double getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(double amountUsed) {
        this.amountUsed = amountUsed;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
