package org.example.tax;

public interface Taxable {
    double getSimpleTaxRate();
    double getMiddleTaxRate();
    double getUpperTaxRate();
}