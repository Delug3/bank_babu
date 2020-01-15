package com.bankbabu.balance.utils;


public class CalculatorHelper {

  /**
   * P x R x (1+R)^N = EMI * ((1+R)^N - 1)
   */
  public static double calculateAmountLoan(double emi, int period, double interestRate) {
    double r = (interestRate / 12) / 100;

    return Math.round(((emi * (Math.pow((1 + r), period) - 1)) / (r * Math.pow((1 + r), period))));
  }

  /**
   * EMI = [P x R x (1+R)^N]/[(1+R)^N-1]
   */
  public static double calculateMonthlyEmi(double amountBorrowed, int period, double interestRate) {
    double r = (interestRate / 12) / 100;

    return Math
        .round((amountBorrowed * r * Math.pow((1 + r), period)) / (Math.pow((1 + r), period) - 1));
  }

  /**
   * Period
   */
  public static int calculatePeriod(double amountBorrowed, double emi, double interestRate) {
    double r = (interestRate / 12) / 100;

    return (int) Math.round(logb((emi / (emi - (amountBorrowed * r))), 1 + r));
  }

  private static double logb(double a, double b) {
    return Math.log(a) / Math.log(b);
  }

  /**
   * EMI = [P x R x (1+R)^N]/[(1+R)^N-1]
   */
  public static double calculateInterest(double amountBorrowed, int period,
      double emi) {
    return Math.round((calculateRate(period, emi, -amountBorrowed, 0, 0, 0.1) * 1200));
  }

  private static double calculateRate(double nper, double pmt, double pv, double fv, double type,
      double guess) {
    //FROM MS http://office.microsoft.com/en-us/excel-help/rate-HP005209232.aspx
    int FINANCIAL_MAX_ITERATIONS = 128;//Bet accuracy with 128
    double FINANCIAL_PRECISION = 0.0000001;//1.0e-8

    double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
    double rate = guess;
    if (Math.abs(rate) < FINANCIAL_PRECISION) {
      y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
    } else {
      f = Math.exp(nper * Math.log(1 + rate));
      y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
    }
    y0 = pv + pmt * nper + fv;
    y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

    // find root by Newton secant method
    i = x0 = 0.0;
    x1 = rate;
    while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS)) {
      rate = (y1 * x0 - y0 * x1) / (y1 - y0);
      x0 = x1;
      x1 = rate;

      if (Math.abs(rate) < FINANCIAL_PRECISION) {
        y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
      } else {
        f = Math.exp(nper * Math.log(1 + rate));
        y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
      }

      y0 = y1;
      y1 = y;
      ++i;
    }
    return rate;
  }

  /**
   * Total interest.
   */
  public static double calculateTotalInterest(double totalPayment, double amountBorrowed) {
    return totalPayment - amountBorrowed;
  }

  /**
   * Total payment.
   */
  public static double calculateTotalPayment(double monthlyEmi, int period) {
    return monthlyEmi * period;
  }

  /**
   * GST(amount).
   */
  public static double calculateAmount(double initialAmount, int gstRate, boolean addGst) {
    if (addGst) {
      return (initialAmount * gstRate) / 100;
    } else {
      return initialAmount - (initialAmount * (100.0 / (100.0 + gstRate)));
    }
  }

  /**
   * CGST.
   */
  public static double calculateCgst(double amount) {
    return amount / 2;
  }

  /**
   * SGST
   */
  public static double calculateSgst(double amount) {
    return amount / 2;
  }

  /**
   * Total calculation.
   */
  public static double calculateTotalCalculation(double initialAmount, double amount,
      boolean addGst) {
    if (addGst) {
      return initialAmount + amount;
    } else {
      return initialAmount - amount;
    }
  }

  /**
   * Maturity amount.
   */
  public static double calculateMaturityAmount(long sipAmount, int period, double rate,
      boolean isMonths) {
    period = isMonths ? period : period * 12;
    double i = (rate / 100) / 12;

    return sipAmount * ((Math.pow((1 + i), period) - 1) / i) * (1 + i);
  }

  /**
   * Sip Amount.
   */
  public static double calculateSipAmount(long maturityAmount, int period, double rate,
      boolean isMonths) {
    period = isMonths ? period : period * 12;
    double i = (rate / 100) / 12;

    return Math.round(maturityAmount / (((Math.pow((1 + i), period) - 1) / i) * (1 + i)));
  }

  /**
   * Investment period.
   */
  public static int calculateInvestmentPeriod(long sipAmount, long maturityAmount, double rate) {
    double i = (rate / 100) / 12;

    return (int) Math.round(logb((((maturityAmount * i) / ((i + 1) * sipAmount)) + 1),
        (i + 1)));
  }
}
