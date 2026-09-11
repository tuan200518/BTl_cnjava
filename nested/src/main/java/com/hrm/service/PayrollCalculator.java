package com.hrm.service;

/** Công thức phiếu lương: bảo hiểm bắt buộc, giảm trừ gia cảnh, PIT và thực lĩnh. */
public final class PayrollCalculator {
    public static final double INSURANCE_RATE = 0.105; // BHXH 8% + BHYT 1.5% + BHTN 1%
    public static final double PERSONAL_DEDUCTION_2026 = 15_500_000d;
    public static final double DEPENDENT_DEDUCTION_2026 = 6_200_000d;
    public static final double PERSONAL_DEDUCTION_OLD = 11_000_000d;
    public static final double DEPENDENT_DEDUCTION_OLD = 4_400_000d;

    private PayrollCalculator() {}

    public record Result(double gross, double insuranceBase, double insurance,
                         double personalDeduction, double dependentDeduction,
                         double nonTaxable, double totalDeductionBeforeTax,
                         double taxableIncome, double pit, double penalty, double net) {}

    public static Result calculate(int year, double base, double overtime, double bonus, double penalty, int dependents) {
        double gross = Math.max(0, base + overtime + bonus);
        double insuranceBase = Math.max(0, base);
        double insurance = insurance(insuranceBase);
        double personal = year >= 2026 ? PERSONAL_DEDUCTION_2026 : PERSONAL_DEDUCTION_OLD;
        double dependentUnit = year >= 2026 ? DEPENDENT_DEDUCTION_2026 : DEPENDENT_DEDUCTION_OLD;
        double dependent = Math.max(0, dependents) * dependentUnit;
        double nonTaxable = 0;
        double totalBeforeTax = insurance + personal + dependent + nonTaxable;
        double taxable = Math.max(0, gross - insurance - personal - dependent - nonTaxable);
        double pit = pit(taxable, year);
        double net = net(gross, insurance, pit, penalty);
        return new Result(gross, insuranceBase, insurance, personal, dependent, nonTaxable,
                totalBeforeTax, taxable, pit, Math.max(0, penalty), net);
    }

    public static double insurance(double insuranceSalary) {
        return Math.max(0, insuranceSalary) * INSURANCE_RATE;
    }

    public static double taxableIncome(double gross, double insurance, int dependents) {
        return Math.max(0, gross - insurance - PERSONAL_DEDUCTION_2026 - Math.max(0, dependents) * DEPENDENT_DEDUCTION_2026);
    }

    public static double pit(double taxable) { return pit(taxable, 2026); }

    public static double pit(double taxable, int year) {
        double x = Math.max(0, taxable);
        if (year >= 2026) {
            if (x <= 10_000_000) return x * 0.05;
            if (x <= 30_000_000) return 500_000 + (x - 10_000_000) * 0.10;
            if (x <= 60_000_000) return 2_500_000 + (x - 30_000_000) * 0.20;
            if (x <= 100_000_000) return 8_500_000 + (x - 60_000_000) * 0.30;
            return 20_500_000 + (x - 100_000_000) * 0.35;
        }
        // Biểu 7 bậc áp dụng cho kỳ 2022-2025.
        double[] bands = {5_000_000, 5_000_000, 8_000_000, 14_000_000, 20_000_000, 28_000_000};
        double[] rates = {0.05, 0.10, 0.15, 0.20, 0.25, 0.30};
        double tax = 0;
        for (int i = 0; i < bands.length; i++) {
            double part = Math.min(x, bands[i]);
            tax += part * rates[i];
            x -= part;
            if (x <= 0) return tax;
        }
        return tax + x * 0.35;
    }

    public static double net(double gross, double insurance, double pit, double discipline) {
        return Math.max(0, gross - insurance - pit - Math.max(0, discipline));
    }
}
