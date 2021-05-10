package com.sap.sfsf.reshuffle.simulation.backend.util;

import java.util.List;
import java.util.stream.Collectors;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;

public class FilterUtil {

    /**
     * Generate filter in clause. Exp. $filter=filedName in 'value1', 'value2'
     * @param fieldName
     * @param values
     * @return
     */
    public static FilterExpression generateFilter(String fieldName, List<String> values) {
        String valueList = values.stream().map(s->{
            return "'" + s + "'";
        }).collect(Collectors.joining(","));
        System.out.println(valueList);
        FilterExpression filter = new FilterExpression(fieldName, "in", new ODataType<String>(valueList));
        return filter;
    }
}