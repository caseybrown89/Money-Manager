package com.caseynbrown.moneymanager;
import java.text.DecimalFormat;

public class HelperMethods {

	static float roundFloat(float amount){
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Float.valueOf(twoDForm.format(amount));
	}

	static boolean validAmount(String amt){
		String[] splitStrings = amt.split("\\.");

		if (splitStrings.length == 1){
			return true;
		} else {
			System.out.println(splitStrings[1].length());
			if (splitStrings[1].length() != 2){
				return false;
			} else {
				return true;
			}
		}
	}

	static String intToDollar(int i){
		String sign;

		if (i < 0){
			sign = "-";
		} else {
			sign = "+";
		}

		int absValAmount = Math.abs(i);

		int remainder = absValAmount % 100;
		int wholeDollar = absValAmount - remainder;
		String decimal;

		if (remainder > 9){
			decimal = ""+remainder;
		} else {
			decimal = "0"+remainder;
		}

		if (wholeDollar > 0){
			int totalDollars = wholeDollar / 100;

			return sign+totalDollars+"."+decimal;
		} else {
			return sign+"."+decimal;
		}
	}

	static int dollarToInt(String dol){
		String sign = dol.substring(0, 1);
		boolean negative;

		if (sign.equals("+")){
			negative = false;
		} else {
			negative = true;
		}

		String amount = dol.substring(1);

		String[] splitDecimal = amount.split("\\.");
		int finalAmount;

		if (splitDecimal.length == 1){
			finalAmount = new Integer(splitDecimal[0]);
			finalAmount = finalAmount * 100;
		}
		else if (splitDecimal[0].equals("")){
			finalAmount = new Integer(splitDecimal[1]);
		} else {
			int wholeDollar = new Integer(splitDecimal[0]);
			int decimal = new Integer(splitDecimal[1]);
			finalAmount =  (100 *  wholeDollar) + decimal;
		}

		if (negative){
			return -1 * finalAmount;
		} else {
			return finalAmount;
		}
	}


/*	public static void main(String[] args){
		int test1 = 123;
		int test2 = 46;
		int test3 = 1299;
		int test4 = -425;
		int test5 = -500;
		int test6 = 500;

		String amt1 = "4.96";
		String amt2 = "4.9";
		String amt3 = "4.";
		String amt4 = "4";

		if (validAmount(amt1) != true &&
			validAmount(amt2) != false &&
			validAmount(amt3) != true &&
			validAmount(amt4) != true)
		{
			System.out.println("validAmount tests failed");
		} else {
			System.out.println("validAmount tests passed");
		}

		if (dollarToInt("+.46") == 46 &&
			dollarToInt("-5.46") == -546 &&
			dollarToInt("-.06") == -6 &&
			dollarToInt("+10.01") == 1001 &&
			dollarToInt("-5") == -500 &&
			dollarToInt("+5") == 500)

		{
			System.out.println("dollarToInt tests passed");
		} else {
			System.out.println("dollarToInt tests failed");
		}

		if (intToDollar(test1).equals("+1.23") &&
			intToDollar(test2).equals("+.46") &&
			intToDollar(test3).equals("+12.99") &&
			intToDollar(test4).equals("-4.25") &&
			intToDollar(test5).equals("-5.00") &&
			intToDollar(test6).equals("+5.00"))
		{
			System.out.println("intToDollar tests passed");
		} else {
			System.out.println("intToDollar tests failed");
		}

		if (dollarToInt(intToDollar(test1)) == 123 &&
			dollarToInt(intToDollar(test2)) == 46 &&
			dollarToInt(intToDollar(test3)) == 1299)
		{
			System.out.println("Conversion tests passed");
		} else {
			System.out.println("Conversion tests failed");
		}
	}*/
}
