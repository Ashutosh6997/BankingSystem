package com.vasistha.bankingsystem.ModelClasses;

/**
 * Created by Ashutosh Singh on 08-Mar-18.
 */

public class FixedDepositData
{
    String startDate;
    int fixedDepositAmount;
    String endDate;
    int maturityAmount;

    public FixedDepositData()
    {
    }

    public FixedDepositData(String startDate, int fixedDepositAmount, String endDate, int maturityAmount)
    {
        this.startDate = startDate;
        this.fixedDepositAmount = fixedDepositAmount;
        this.endDate = endDate;
        this.maturityAmount = maturityAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getFixedDepositAmount() {
        return fixedDepositAmount;
    }

    public void setFixedDepositAmount(int fixedDepositAmount) {
        this.fixedDepositAmount = fixedDepositAmount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(int maturityAmount) {
        this.maturityAmount = maturityAmount;
    }
}
