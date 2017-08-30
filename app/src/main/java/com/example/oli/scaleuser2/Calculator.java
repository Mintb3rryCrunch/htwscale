package com.example.oli.scaleuser2;

import android.view.animation.AnimationUtils;

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

    public static String BMR_Calculcator(String weight, String height, long age, String gender)
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
}
