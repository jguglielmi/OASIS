package com.example;

public class MathExampleFixture {

	private double numerator, denominator;
	private double multiplicand, multiplier;

	public static void main(String[] args) {
        System.out.println("Fixture is working");
    }
	
	public MathExampleFixture () { //class constructor
	}
	
	public MathExampleFixture (double firstNumber) {  //class constructor for script table
		numerator = firstNumber;
		multiplicand = firstNumber;
	}
	
	public boolean confirmDivisionWithNumeratorAndDenominatorAndAnswer(double numerator, double denominator, double answer) {
		return (numerator / denominator) == answer;
	}
	
	public void setNumerator(double numerator) {
		this.numerator = numerator;
	}
	public void setDenominator(double denominator) {
		this.denominator = denominator;
	}
	
	public void setMultiplicand(double multiplicand) {
		this.multiplicand = multiplicand;
	}
	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
	
	public double quotient() {
		return numerator / denominator;
	}
	
	public double multiply() {
		return this.multiplicand * this.multiplier;
	}
	
}
