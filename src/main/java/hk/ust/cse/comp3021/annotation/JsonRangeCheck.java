/*
 * Copyright (c) 2025.
 * Xiang Chen xchenht@connect.ust.hk
 * This project is developed only for HKUST COMP3021 Programming Assignment
 */

package hk.ust.cse.comp3021.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark fields that should within the range during serialization and deserialization.
 * Any field annotated with {@link JsonRangeCheck} will be checked with the min and max.
 */
// TODO: implement this annotation based on the description above, remember to add the min and max variable for three
//  types of fields: int, long and double
