package com.cedarmeadowmeats.orderservice.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFormSelectionEnumTest {

    @Test
    void enumTest() {
        Assertions.assertEquals("eighth-steer", OrderFormSelectionEnum.EIGHTH_STEER.getValue());
        Assertions.assertEquals("quarter-steer", OrderFormSelectionEnum.QUARTER_STEER.getValue());
        Assertions.assertEquals("half-steer", OrderFormSelectionEnum.HALF_STEER.getValue());
        Assertions.assertEquals("whole-steer", OrderFormSelectionEnum.WHOLE_STEER.getValue());
        Assertions.assertEquals("half-hog", OrderFormSelectionEnum.HALF_HOG.getValue());
        Assertions.assertEquals("whole-hog", OrderFormSelectionEnum.WHOLE_HOG.getValue());
    }

}