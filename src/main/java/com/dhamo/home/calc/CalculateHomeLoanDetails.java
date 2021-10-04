package com.dhamo.home.calc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculateHomeLoanDetails {
    double salaryIncPerYear = 10;
    double propertyIncPerYear = 8;
    double initialSalaryPerYear = 1200000.00;
    double rentIncPerYear = 5;
    double savingPercentagePerYear = 40;
    double yearlyExpenditurePercentage = 60;
    double investmentIncRate = 12;
    double ownHouseRentPerYear = 96000.0;
    double loanInterestRate = 7;
    double houseInterestAmountTaxBenTotal;
    List<Double> savingsPerYearList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#");
    double noOfYears = 20;
    double yearlyExpenditure;
    double yearlyRemaining;


    double emiPerYear = 0; //https://www.hdfc.com/home-loan-emi-calculator
    double houseInterestAmountTotal = 0; //https://www.hdfc.com/home-loan-emi-calculator


    double hraAmountTaxBenefitPerYear; //varies as rent changes
    double loanAmount; //varies as initial payment, interestrate, number of years, propertyValue changes

    public CalculateHomeLoanDetails() {
        houseInterestAmountTaxBenTotal = houseInterestAmountTotal * 0.3;
        yearlyExpenditure = (initialSalaryPerYear / 100) * yearlyExpenditurePercentage;
        yearlyRemaining = initialSalaryPerYear - yearlyExpenditure;
        df.setMaximumFractionDigits(2);
    }

    public void calculate() {
        System.out.println(
                " Property Value: "
                        + " Init Amount: "
                        + " Number of Years: "
                        + " Property Value: "
                        + " Cash Savings: "
                        + " Total Amount: "
                        + " Property %: "
                        + " Cash %: "
                        + " Loan Interest Amount: "
                        + " Absolute Percentage of interest : "
        );
        for(double propertyVal = 1000000.00; propertyVal <= 4500000; propertyVal += 100000) {
            for(double initAmount = 500000; initAmount <= 1500000 && initAmount <= propertyVal; initAmount += 100000) {
                for(int j = 7; j <= noOfYears; j++) {
                    loanAmount = propertyVal - initAmount;
                    double emiPerMonth = calculateMonthlyEMI(loanAmount, j, loanInterestRate);
                    emiPerYear = calculateMonthlyEMI(loanAmount, j, loanInterestRate) * 12;
                    houseInterestAmountTotal = calculateTotalInterestAmount(loanAmount, emiPerMonth, j);
                    double savingsPerYear = 0;
                    double savingsAfterYears = 0;
                    List<Double> savingsAfterYearsList = new ArrayList<>();
                    double salaryPerYear = 0;
                    double currPropertyVal = 0;
                    for (int i = 0; i < j; i++) {
                        salaryPerYear = calculateCompoundInterest(initialSalaryPerYear, i, salaryIncPerYear);
                        yearlyExpenditure = (salaryPerYear / 100) * yearlyExpenditurePercentage;
                        yearlyRemaining = salaryPerYear - yearlyExpenditure;
                        currPropertyVal = calculateCompoundInterest(propertyVal, i, propertyIncPerYear);
                        savingsPerYear = yearlyRemaining - emiPerYear;
                        savingsAfterYears = calculateCompoundInterest(savingsPerYear, j - i, investmentIncRate);
                        savingsAfterYearsList.add(savingsAfterYears);
                    }

                    if(savingsPerYear <= 0) {
                        continue;
                    }

                    Double cashSavings = savingsAfterYearsList.stream().reduce(0.0, Double::sum);

                    double totalAssetValue = currPropertyVal + cashSavings;
                    System.out.println(
                            df.format(propertyVal)
                            + " : " + df.format(initAmount)
                            + " : " + j
                            + " : " + df.format(currPropertyVal)
                            + " : " + df.format(cashSavings)
                            + " : " + df.format(totalAssetValue)
                            + " : " + df.format((currPropertyVal / totalAssetValue) * 100)
                            + " : " + df.format((cashSavings / totalAssetValue) * 100)
                            + " : " + df.format(houseInterestAmountTotal)
                            + " : " + df.format((houseInterestAmountTotal/loanAmount) * 100)
                    );
                }
            }
        }
    }

    public double calculateCompoundInterest(double amount, double noOfYears, double interestRate) {
        if(noOfYears == 0) {
            return amount;
        }
        return (amount * Math.pow(1 + interestRate/100, noOfYears));
    }

    public double calculateMonthlyEMI(double amount, double noOfYears, double interestRate) {
        if(noOfYears == 0) {
            return amount;
        }
        interestRate = interestRate / 12 / 100;
        double noOfMonths = noOfYears * 12;
        double pow = Math.pow(1 + interestRate, noOfMonths);
        return (amount * interestRate * pow/(pow - 1));
    }

    public double calculateTotalInterestAmount(double amount, double monthlyEMI, double noOfYears) {
        if(noOfYears == 0) {
            return amount;
        }
        double totalEMIPayable = monthlyEMI * 12 * noOfYears;
        return totalEMIPayable - amount;
    }

    public double calculateTotalAmountPayable(double monthlyEMI, double noOfYears) {
        return monthlyEMI * 12 * noOfYears;
    }
}
