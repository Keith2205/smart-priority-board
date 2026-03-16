package com.smartboard.model;

public enum Quadrant {

    /** Urgent + Important — Do first */
    Q1,
    /** Not Urgent + Important — Schedule */
    Q2,
    /** Urgent + Not Important — Delegate */
    Q3,
    /** Not Urgent + Not Important — Eliminate */
    Q4;

    public static Quadrant calculate(boolean isImportant, boolean isUrgent) {
        if (isImportant && isUrgent)  return Q1;
        if (isImportant)              return Q2;
        if (isUrgent)                 return Q3;
        return Q4;
    }
}
