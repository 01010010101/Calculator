package calcRA;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) throws CalcException {
            while (true){
            System.out.println("Программа принимает арабские и римские числа от 1 до 10 и от I до X включительно. Введите выражение. Для выхода введите \"q\"");

            Scanner in = new Scanner(System.in);
            String str = in.nextLine();
            if(str.equals("q"))
                break;
            str = str.replaceAll(" ", "");
            Pattern patternArabicNumbers = Pattern.compile("^(\\d+)([+-/*])(\\d+)$");
            Pattern patternRomanNumbers = Pattern.compile("^([IVX]+)([+-/*])([IVX]+)$");

            Matcher mArabic = patternArabicNumbers.matcher(str);
            Matcher mRoman = patternRomanNumbers.matcher(str);

            if (mArabic.find()) {
                System.out.println(calc(str));
            } else if (mRoman.find()) {

                str = Converter.lineRoman2Arabic(str);
                Byte result = calc(str);
                if (result != null && result > 0)
                    System.out.println(Converter.toRoman((int) result));
                else {
                    throw new CalcException("В римской системе результат не может быть меньше единицы");
                }

            } else
                throw new CalcException("Калькулятор работает только с арабскими или только с римскими числами натурального ряда. Не более двух чисел и одного знака.");
            }
        }
    public static Byte calc(String str) throws CalcException {
        String[] numbers;
        byte result = 0;
        numbers = str.split("[+*/-]");
        if(Byte.parseByte(numbers[0]) <= 10 && Byte.parseByte(numbers[1]) <= 10 && Byte.parseByte(numbers[0]) > 0 && Byte.parseByte(numbers[1]) > 0){
            if (str.indexOf('+') > 0) {
                result = (byte) (Byte.parseByte(numbers[0]) + Byte.parseByte(numbers[1]));
            } else if (str.indexOf('-') > 0) {
                result = (byte) (Byte.parseByte(numbers[0]) - Byte.parseByte(numbers[1]));
            } else if (str.indexOf('/') > 0) {
                result = (byte) (Byte.parseByte(numbers[0]) / Byte.parseByte(numbers[1]));
            } else if (str.indexOf('*') > 0) {
                result = (byte) (Byte.parseByte(numbers[0]) * Byte.parseByte(numbers[1]));
            }
            return result;
        }
        else
            throw new CalcException("Число на вводе не может быть меньше 1 и больше 10");

    }
    public static class Converter {

        private static final TreeMap<Integer, String> ARABIC = new TreeMap<>();

        static {

            ARABIC.put(100, "C");
            ARABIC.put(90, "XC");
            ARABIC.put(50, "L");
            ARABIC.put(40, "XL");
            ARABIC.put(10, "X");
            ARABIC.put(9, "IX");
            ARABIC.put(5, "V");
            ARABIC.put(4, "IV");
            ARABIC.put(1, "I");
        }

        private static final Map<String, Integer> ROMAN = new HashMap<>();

        static {
            ROMAN.put("I", 1);
            ROMAN.put("II", 2);
            ROMAN.put("III", 3);
            ROMAN.put("IV", 4);
            ROMAN.put("V", 5);
            ROMAN.put("VI", 6);
            ROMAN.put("VII", 7);
            ROMAN.put("VIII", 8);
            ROMAN.put("IX", 9);
            ROMAN.put("X", 10);

        }

        public static String toRoman(int number) {
            int n = ARABIC.floorKey(number);
            if(number == n){
                return ARABIC.get(number);

            }
            else{
                return ARABIC.get(n) + toRoman(number - n);
            }

        }

        public static int toArabic(String number) {
            return ROMAN.get(number);
        }

        public static String lineRoman2Arabic(String romanLine){
            String arabicLine = romanLine;
            Pattern patternRomanNumbers = Pattern.compile("([IVX]+)([+-/*])([IVX]+)");
            Matcher mRoman = patternRomanNumbers.matcher(arabicLine);
            mRoman.find();
            arabicLine = arabicLine.replaceFirst("[IVX]+",String.valueOf(toArabic(mRoman.group(1))));
            arabicLine = arabicLine.replaceFirst("[IVX]+",String.valueOf(toArabic(mRoman.group(3))));
            return arabicLine;
        }
        }
        public static class CalcException extends Exception {
            public CalcException(String description) {
                super(description);
            }
        }

}
