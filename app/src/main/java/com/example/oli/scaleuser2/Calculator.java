package com.example.oli.scaleuser2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oli on 08.08.2017.
 */

public class Calculator {

    public static long age_Calculator(String string_birthday)
    {

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dtBirthday = df.parse(string_birthday);
            Date now = new Date();
            df.format(now);



            return (now.getTime() - dtBirthday.getTime()) / (24 * 60 * 60 * 1000) / 365;


        }catch(ParseException e){
            e.printStackTrace();
        }
        return 0;

    }

    public static String BMI_Calculator(String weight, String height)
    {
        float calc_weight = Float.parseFloat(weight);
        float calc_height = Float.parseFloat(height) /100;

        float BMI = calc_weight / (calc_height*calc_height);

        BMI = (float)Math.round(BMI * 10) / 10;

        return Float.toString(BMI);
    }

    public static String BMR_Calculator(String weight, String height, long age, String gender)
    {
        double BMR;

        float calc_weight = Float.parseFloat(weight);
        float calc_height = Float.parseFloat(height);

        if(calc_weight == 0.0)
        {
            return Double.toString(0.0);
        }
        else if (gender.contains("Female"))
        {
            BMR = 655 + (9.6 * calc_weight) + (1.8 * calc_height) - (4.7 * age);
            BMR = Math.round(BMR * 100) / 100;
            return Double.toString(BMR);
        }
        else
        {
            BMR = 66 + (13.7 * calc_weight) + (5 * calc_height) - (6.8 * age);
            BMR = Math.round(BMR * 100) / 100;
            return Double.toString(BMR);
        }


    }

    public static String idealWeight(String gender, String height)
    {
        double iW = 0.0;
        float calc_height = Float.parseFloat(height);
        float normalWeight = calc_height;
        normalWeight = normalWeight - 100.0f;
        if(gender.contains(("Female")))
        {

            iW = normalWeight * 0.85;
            return Double.toString(iW);
        }
        else
        {
            iW = normalWeight * 0.9;
            return Double.toString(iW);
        }

    }

    //Bedeutung der BMI Tabelle
    public static String bmiTable(String gender, String bmi, long age)
    {
        String res = "";
        float parse_bmi = Float.parseFloat(bmi);
        if(gender.contains("Female"))
        {
            //float parse_bmi = Float.parseFloat(bmi);
            if(age>=18 && age<=24)

            {

                if(parse_bmi < 19.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=19.0 && parse_bmi<24.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=24.0 && parse_bmi<29.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=29.0 && parse_bmi<39.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 39.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=25 && age<=34)

            {

                if(parse_bmi < 20.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=20.0 && parse_bmi<25.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=25.0 && parse_bmi<30.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=30.0 && parse_bmi<40.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 40.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=35 && age<=44)

            {

                if(parse_bmi < 21.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=21.0 && parse_bmi<26.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=26.0 && parse_bmi<31.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=31.0 && parse_bmi<41.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 41.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=45 && age<=54)

            {

                if(parse_bmi < 22.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=22.0 && parse_bmi<27.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=27.0 && parse_bmi<32.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=32.0 && parse_bmi<42.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 42.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=55 && age<=64)

            {

                if(parse_bmi < 23.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=23.0 && parse_bmi<28.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=28.0 && parse_bmi<33.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=33.0 && parse_bmi<43.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 43.0)
                {
                    res = "Obesity";
                }

            }

            else if(age>=65)

            {

                if(parse_bmi < 24.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=24.0 && parse_bmi<29.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=29.0 && parse_bmi<34.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=34.0 && parse_bmi<44.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 44.0)
                {
                    res = "Obesity";
                }

            }

        }

        else
        {
            //float parse_bmi = Float.parseFloat(bmi);
            if(age>=18 && age<=24)

            {

                if(parse_bmi < 20.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=20.0 && parse_bmi<25.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=25.0 && parse_bmi<30.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=30.0 && parse_bmi<40.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 40.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=25 && age<=34)

            {

                if(parse_bmi < 21.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=21.0 && parse_bmi<26.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=26.0 && parse_bmi<31.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=31.0 && parse_bmi<41.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 41.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=35 && age<=44)

            {

                if(parse_bmi < 22.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=22.0 && parse_bmi<27.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=27.0 && parse_bmi<32.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=32.0 && parse_bmi<42.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 42.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=45 && age<=54)

            {

                if(parse_bmi < 23.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=23.0 && parse_bmi<28.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=28.0 && parse_bmi<33.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=33.0 && parse_bmi<43.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 43.0)
                {
                    res = "Obesity";
                }

            }

            if(age>=55 && age<=64)

            {

                if(parse_bmi < 24.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=24.0 && parse_bmi<29.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=29.0 && parse_bmi<34.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=34.0 && parse_bmi<44.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 44.0)
                {
                    res = "Obesity";
                }

            }

            else if(age>=65)

            {

                if(parse_bmi < 25.0)
                {
                    res = "Underweight";
                }
                else if(parse_bmi>=25.0 && parse_bmi<30.0)
                {
                    res = "Normal weight";
                }
                else if(parse_bmi>=30.0 && parse_bmi<35.0)
                {
                    res = "slightly Overweight";
                }
                else if(parse_bmi>=35.0 && parse_bmi<45.0)
                {
                    res = "Overweight";
                }
                else if(parse_bmi >= 45.0)
                {
                    res = "Obesity";
                }

            }

        }

        return res;

    }

}
