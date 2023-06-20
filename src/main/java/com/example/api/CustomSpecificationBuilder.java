package com.example.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

public class CustomSpecificationBuilder {

    static <T> Specification<T> build(Map<String, Map<FilterOperation, String>> params) {
        Specification<T> result = Specification.where(null);

        for (Map.Entry<String,Map<FilterOperation, String>> entry : params.entrySet()) {
            result = Specification.where(result).and(buildSpec(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    private static <T> Specification<T> buildSpec(String k, Map<FilterOperation, String> v) {
        Map.Entry<FilterOperation, String> entry = v.entrySet().iterator().next();
        switch (entry.getKey()) {
            case EQUALITY:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(k),
                        castToRequiredType(root.get(k).getJavaType(), entry.getValue()));
            case NEGATION:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(k),
                        castToRequiredType(root.get(k).getJavaType(), entry.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(k),
                        (Number) castToRequiredType(root.get(k).getJavaType(), entry.getValue()));
            case LESS_THAN:
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.lt(root.get(k),
                    (Number) castToRequiredType(root.get(k).getJavaType(), entry.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(k), entry.getValue().toString());
            case STARTS_WITH:
                return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(k), entry.getValue()+"%");
            case ENDS_WITH:
                return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(k), "%"+entry.getValue());
            case CONTAINS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(k), "%"+entry.getValue()+"%");
            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(k))
                        .value(castToRequiredType(root.get(k).getJavaType(), new ArrayList<String>(Arrays.asList(entry.getValue().split(",")))));
            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private static <T> Object castToRequiredType(Class<T> fieldType, String value) {
        if(fieldType.isAssignableFrom(Double.class)){
            return Double.valueOf(value);
        }else if(fieldType.isAssignableFrom(Integer.class)){
            return Integer.valueOf(value);
        // }else if(Enum.class.isAssignableFrom(fieldType)){
        //     return Enum.valueOf(fieldType, value);
        } else if (Boolean.class.isAssignableFrom(fieldType)) {
            return Boolean.valueOf(value);
        }
        return null;
    }

    private static <T> Object castToRequiredType(Class<T> fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}
