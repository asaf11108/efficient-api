package com.example.api;

public enum FilterOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, STARTS_WITH_I, ENDS_WITH, CONTAINS, IN;

    public static final String[] SIMPLE_OPERATION_SET = { "$eq", "$ne", "$gt", "$lt", "$like", "$startsWith", "$startsWithi", "$endsWith", "$contains", "$in" };

    public static FilterOperation getSimpleOperation(String input) {
        switch (input) {
        case "$eq":
            return EQUALITY;
        case "$ne":
            return NEGATION;
        case "$gt":
            return GREATER_THAN;
        case "$lt":
            return LESS_THAN;
        case "$like":
            return LIKE;
        case "$startsWith":
            return STARTS_WITH;
        case "$startsWithi":
            return STARTS_WITH_I;
        case "$endsWith":
            return ENDS_WITH;
        case "$contains":
            return CONTAINS;
        case "$in":
            return IN;
        default:
            return null;
        }
    }
}
