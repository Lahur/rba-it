package hr.rba.rba_it.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OibValidator implements ConstraintValidator<ValidOib, String> {

    private static final int OIB_LENGTH = 11;

    @Override
    public boolean isValid(String oib, ConstraintValidatorContext context) {
        if (oib == null) {
            return false;
        }
        if (!oib.matches("\\d{" + OIB_LENGTH + "}")) {
            return false;
        }
        return hasValidCheckDigit(oib);
    }

    private boolean hasValidCheckDigit(String oib) {
        int a = 10;
        for (int i = 0; i < OIB_LENGTH - 1; i++) {
            int digit = Character.getNumericValue(oib.charAt(i));
            a = (a + digit) % 10;
            if (a == 0) {
                a = 10;
            }
            a = (a * 2) % 11;
        }
        int controlDigit = (11 - a) % 10;
        int lastDigit = Character.getNumericValue(oib.charAt(OIB_LENGTH - 1));
        return controlDigit == lastDigit;
    }
}