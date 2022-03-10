package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.sdk.datamodel.odata.helper.EntityField;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.sfsf.reshuffle.applicants.backend.config.EnvConfig;

import org.springframework.beans.factory.annotation.Autowired;

public class Util {

    private static final int MAX_FILTER_LENGTH = 1600;
    
    @Autowired
    private EnvConfig envConfig;

    // 一度にリクエストするクエリ長が2000byteを超えないようにするため、
    // fieldListで与えられるリストに対し、一度にリクエストするクエリ長が1600byteを超えないように分割する。
    // ここで計算するクエリを除くURL部分は高々400byteであると仮定している。
    public <EntityT, FieldT> Map<Integer, ExpressionFluentHelper<EntityT>> createFilterMap(
            EntityField<EntityT, FieldT> field, List<FieldT> fieldList) {

        Map<Integer, ExpressionFluentHelper<EntityT>> retMap = new HashMap<>();
        int index = 0;

        for (final FieldT f : fieldList) {
            if (!retMap.containsKey(index)) {
                retMap.put(index, field.eq(f));
            } else {
                ExpressionFluentHelper<EntityT> filter = retMap.get(index).or(field.eq(f));
                retMap.replace(index, filter);
                if (filter.toString().length() > MAX_FILTER_LENGTH)
                    index += 1;
            }
        }

        return retMap;
    }

    public String combineName(String lastName, String firstName) {
        if (lastName.isEmpty())
            return firstName;
        else if (firstName.isEmpty())
            return lastName;
        return lastName + " " + firstName;
    }

    public int calculateTenure(LocalDateTime startDate) {
        return (startDate != null)
                ? (int) ChronoUnit.YEARS.between(startDate, DateTimeUtil.getToday())
                : envConfig.getExceptinalInt();
    }
}
