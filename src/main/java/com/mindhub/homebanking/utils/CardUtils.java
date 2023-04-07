package com.mindhub.homebanking.utils;


import org.junit.Test;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public final class CardUtils {

    @Test
    public void testRandomString() {
        String randomString = Utilitis.randomString();
        assertThat(randomString, is(not(emptyOrNullString())));
    }

    @Test
    public void returnCvvNumber_generatesValidCvv() {
        // Verifica que el número de CVV generado esté entre 100 y 999
        int cvv = Utilitis.returnCvvNumber();
        assertTrue(cvv >= 100 && cvv <= 999);
    }

    @Test
    public void returnCvvNumber_generatesDifferentCvv() {
        // Verifica que se generen números de CVV diferentes al llamar varias veces al método
        int cvv1 = Utilitis.returnCvvNumber();
        int cvv2 = Utilitis.returnCvvNumber();
        assertNotEquals(cvv1, cvv2);
    }

    @Test
    public void returnCvvNumber_returnsValidValues() {
        // Verifica que se generen números de CVV válidos
        for (int i = 0; i < 100; i++) {
            int cvv = Utilitis.returnCvvNumber();
            assertTrue(cvv >= 100 && cvv <= 999);
        }
    }
}
