package com.github.financing.bean;

/********************************************
 * 作者：Administrator
 * 时间：2017/1/10
 * 描述：
 *******************************************/
public class BidInfoBean {
    /**
     * 标的名称
     */
    private String bidName;
    /**
     * 标的类型
     */
    private String bidType;
    /**
     * 标的年利率
     */
    private String bidYearRate;
    /**
     * 标的期限
     */
    private String bidLoanTerm;
    /**
     * 最小转让金额
     */
    private String bidMinimum;
    /**
     * 还款方式
     */
    private String bidRepayment;
    /**
     * 标的金额
     */
    private String TotalAmount;

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getBidYearRate() {
        return bidYearRate;
    }

    public void setBidYearRate(String bidYearRate) {
        this.bidYearRate = bidYearRate;
    }

    public String getBidLoanTerm() {
        return bidLoanTerm;
    }

    public void setBidLoanTerm(String bidLoanTerm) {
        this.bidLoanTerm = bidLoanTerm;
    }

    public String getBidMinimum() {
        return bidMinimum;
    }

    public void setBidMinimum(String bidMinimum) {
        this.bidMinimum = bidMinimum;
    }

    public String getBidRepayment() {
        return bidRepayment;
    }

    public void setBidRepayment(String bidRepayment) {
        this.bidRepayment = bidRepayment;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
