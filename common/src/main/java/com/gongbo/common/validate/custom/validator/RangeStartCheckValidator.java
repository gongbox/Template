package com.gongbo.common.validate.custom.validator;


import com.gongbo.common.validate.custom.annotation.RangeStart;
import org.springframework.util.comparator.Comparators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeStartCheckValidator implements ConstraintValidator<RangeStart, Comparable<?>> {

    private boolean required;
    private String rangeGroup;

    @Override
    public void initialize(RangeStart rangeCheck) {
        this.required = rangeCheck.required();
        this.rangeGroup = rangeCheck.rangeGroup();
    }

    @Override
    public boolean isValid(Comparable<?> value, ConstraintValidatorContext constraintValidatorContext) {
        if (!required) {
            return true;
        }
        Comparable<?>[] currents = RangeCheckContext.currentOf(rangeGroup);
        currents[0] = value;

        if (currents[0] != null && currents[1] != null) {
            return Comparators.comparable().compare(currents[0], currents[1]) <= 0;
        }
        return true;
    }

}
