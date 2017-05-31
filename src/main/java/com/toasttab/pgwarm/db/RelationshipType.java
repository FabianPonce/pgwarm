package com.toasttab.pgwarm.db;

public enum RelationshipType {
    TABLE,
    INDEX,
    SEQUENCE,
    VIEW,
    MATERIALIZED_VIEW,
    COMPOSITE_TYPE,
    TOAST_TABLE,
    FOREIGN_TABLE;

    public static RelationshipType fromRelKind(String relKind) {
        switch(relKind.charAt(0)) {
            case 'r':
                return TABLE;
            case 'i':
                return INDEX;
            case 'v':
                return VIEW;
            case 'm':
                return MATERIALIZED_VIEW;
            case 'c':
                return COMPOSITE_TYPE;
            case 't':
                return TOAST_TABLE;
            case 'f':
                return FOREIGN_TABLE;
        }

        throw new IllegalArgumentException("relKind " + relKind + " is not known.");
    }

    public char toRelKind() {
        switch(this) {
            case TABLE:
                return 'r';
            case INDEX:
                return 'i';
            case VIEW:
                return 'v';
            case MATERIALIZED_VIEW:
                return 'm';
            case COMPOSITE_TYPE:
                return 'c';
            case TOAST_TABLE:
                return 't';
            case FOREIGN_TABLE:
                return 'f';
        }

        throw new IllegalStateException();
    }
}
