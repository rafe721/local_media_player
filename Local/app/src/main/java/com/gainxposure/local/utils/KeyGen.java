package com.gainxposure.local.utils;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 22/12/2016.
 */

public class KeyGen {
    private String HexToDecimal(char paramChar) {
        return HexToDecimal(String.valueOf(paramChar));
    }

    private String HexToDecimal(String paramString)
    {
        switch (paramString) {
            case "a":
            case "A":
                return "10";
            case "b":
            case "B":
                return "11";
            case "c":
            case "C":
                return "12";
            case "d":
            case "D":
                return "13";
            case "e":
            case "E":
                return "14";
            case "f":
            case "F":
                return "15";
            default:
                return paramString;
        }
    }
    private String[] splitBy(String paramString, int paramInt)
    {
        List<String> stringList = new ArrayList();
        int j = paramString.length();
        int i = 0;
        while (i < j)
        {
            stringList.add(paramString.substring(i, Math.min(j, i + paramInt)));
            i += paramInt;
        }
        return stringList.toArray(new String[(stringList).size()]);
    }

    private int sumOfDigits(int paramInt)
    {
        int j = 0;
        int i = paramInt;
        for (;;) {
            if (i <= 0) { break; }
            j += i % 10;
            i /= 10;
        }
        return j;
    }

    public String encode(Context paramContext)
    {
        String androidId = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
        StringBuilder genCode = new StringBuilder();
        String splitIDs[] = splitBy(androidId, 2);
        int k = splitIDs.length;
        int i = 0;
        while (i < k)
        {
            String splitId = splitIDs[i];
            int m = Integer.parseInt(HexToDecimal((splitId).charAt(0)));
            int n = Integer.parseInt(HexToDecimal((splitId).charAt(1)));
            int j = 0;
            for (j = m + n; (j < 1) || (j > 26); j = sumOfDigits(m + n)) {/* do nothing */}
            genCode.append((char)(j + 64));
            i += 1; // updating the
        }
        return genCode.toString();
    }
}
