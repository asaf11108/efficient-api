package com.example.api;

public enum FilterOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS, IN;

    public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~", "i" };

    public static FilterOperation getSimpleOperation(char input) {
        switch (input) {
        case ':':
            return EQUALITY;
        case '!':
            return NEGATION;
        case '>':
            return GREATER_THAN;
        case '<':
            return LESS_THAN;
        case '~':
            return LIKE;
        case 'i':
            return IN;
        default:
            return null;
        }
    }
}
