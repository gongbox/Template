package com.gongbo.common.validate.custom.validator;


import com.gongbo.common.utils.Range;
import com.gongbo.common.validate.custom.annotation.RangeEnd;
import org.springframework.util.comparator.Comparators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeEndCheckValidator implements ConstraintValidator<RangeEnd, Comparable<?>> {

    private boolean required;
    private String rangeGroup;

    @Override
    public void initialize(RangeEnd rangeCheck) {
        this.required = rangeCheck.required();
        this.rangeGroup = rangeCheck.rangeGroup();
    }

    @Override
    public boolean isValid(Comparable<?> value, ConstraintValidatorContext constraintValidatorContext) {
        if (!required) {
            return true;
        }

        Range<Comparable<?>> currentRange = RangeCheckContext.getCurrentRange(rangeGroup);
        currentRange.setEnd(value);

        if (currentRange.getStart() != null && currentRange.getEnd() != null) {
            return Comparators.comparable().compare(currentRange.getStart(), currentRange.getEnd()) <= 0;
        }
        return true;
    }
}
