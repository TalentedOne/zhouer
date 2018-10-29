package com.mobile.liguanjian.liguanjian.util;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ListFilter {

    public static <T> List<T> filterList(boolean isRegex, String rule,
                                         List<T> sourceList, ToStringCallback<T> callback) {
        if (isObjectToStringTrimEmpty(rule))
            return sourceList;
        else if (isRegex)
            return filterListByRegex(rule, sourceList, callback, null);
        else
            return filterListByStartAndContain(rule, sourceList, callback, null);
    }

    private static boolean isObjectToStringTrimEmpty(String rule) {
        return rule == null || rule.trim().isEmpty();
    }

    /**
     * Regex allows letter,digit,chinese only
     */
    public static <T> List<T> filterList(boolean isRegex, String rule,
                                         List<T> sourceList, ToStringListCallback<T> callback) {
        if (isObjectToStringTrimEmpty(rule))
            return sourceList;
        else if (isRegex)
            return filterListByRegex(rule, sourceList, null, callback);
        else
            return filterListByStartAndContain(rule, sourceList, null, callback);
    }

    private static <T> List<T> filterListByStartAndContain(String rule, List<T> sourceList,
                                                           ToStringCallback<T> toStringCallback, ToStringListCallback<T> toStringListCallback) {
        List<T> resultList = new ArrayList<T>();
        List<T> startList = new ArrayList<T>();
        List<T> containList = new ArrayList<T>();
        String ruleString = rule.toUpperCase(Locale.ENGLISH);

        for (T element : sourceList)
            if (toStringCallback != null)
                judgeToAddElement(startList, containList, ruleString, element, toStringCallback.getFilterText(element).toUpperCase(Locale.ENGLISH));
            else
                for (String str : toStringListCallback.getStringList(element))
                    if (judgeToAddElement(startList, containList, ruleString, element, str))
                        break;

        resultList.addAll(startList);
        resultList.addAll(containList);
        return resultList;
    }

    private static <T> boolean judgeToAddElement(List<T> startList, List<T> containList, String ruleString, T element, String itemString) {
        if (itemString.toUpperCase().startsWith(ruleString))
            startList.add(element);
        else if (itemString.toUpperCase().contains(ruleString))
            containList.add(element);
        else
            return false;
        return true;
    }

    /**
     * Regex allows letter,digit,chinese only
     */
    private static <T> List<T> filterListByRegex(String rule, List<T> sourceList,
                                                 ToStringCallback<T> toStringCallback, ToStringListCallback<T> toStringListCallback) {
        rule = rule.toUpperCase();
        List<T> resultList = new ArrayList<T>();

        Pattern pattern = getPattern(false, rule);

        if (pattern == null)
            return filterListByStartAndContain(rule, sourceList, toStringCallback, null);
        else {
            for (T listItem : sourceList)
                if (toStringCallback != null)
                    judgeToAddElement(resultList, pattern, listItem, toStringCallback.getFilterText(listItem));
                else
                    for (String str : toStringListCallback.getStringList(listItem))
                        if (judgeToAddElement(resultList, pattern, listItem, str))
                            break;
            return resultList;
        }
    }

    private static <T> boolean judgeToAddElement(List<T> resultList, Pattern pattern, T listItem, String str) {
        str = str.toUpperCase();
        boolean isMatchPattern = pattern.matcher(str).lookingAt();

        if (isMatchPattern)
            resultList.add(listItem);
        else
            return false;
        return true;
    }

    private static Pattern getPattern(boolean isCaseSensitive, String rule) {
        try {
            if (isCaseSensitive)
                return Pattern.compile(rule);
            else
                return Pattern.compile(rule,
                        Pattern.CASE_INSENSITIVE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ToStringCallback<T> {
        String getFilterText(T param);
    }

    public interface ToStringListCallback<T> {
        @NonNull
        List<String> getStringList(T param);
    }
}