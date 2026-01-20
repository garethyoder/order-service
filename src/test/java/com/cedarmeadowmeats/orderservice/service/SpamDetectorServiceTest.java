package com.cedarmeadowmeats.orderservice.service;

import com.cedarmeadowmeats.orderservice.model.Submission;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpamDetectorServiceTest {

    private String makeString(char c, int count) {
        char[] arr = new char[count];
        Arrays.fill(arr, c);
        return new String(arr);
    }

    @Test
    void isSpam_returnsTrue_whenNameAndCommentsAreAtLeast15AndHaveNoSpaces() {
        Submission s = new Submission();
        s.setName(makeString('a', 15));
        s.setComments(makeString('b', 20));

        assertTrue(SpamDetectorService.isSpam(s));
    }

    @Test
    void isSpam_returnsFalse_whenSubmissionIsNull() {
        assertFalse(SpamDetectorService.isSpam(null));
    }

    @Test
    void isSpam_returnsFalse_whenNameIsNull() {
        Submission s = new Submission();
        s.setName(null);
        s.setComments(makeString('b', 15));

        assertFalse(SpamDetectorService.isSpam(s));
    }

    @Test
    void isSpam_returnsFalse_whenCommentsIsNull() {
        Submission s = new Submission();
        s.setName(makeString('a', 15));
        s.setComments(null);

        assertFalse(SpamDetectorService.isSpam(s));
    }

    @Test
    void isSpam_returnsFalse_whenNameIsTooShort() {
        Submission s = new Submission();
        s.setName(makeString('a', 14));
        s.setComments(makeString('b', 15));

        assertFalse(SpamDetectorService.isSpam(s));
    }

    @Test
    void isSpam_returnsFalse_whenCommentsContainsSpace() {
        Submission s = new Submission();
        s.setName(makeString('a', 16));
        // create a 16-char string that contains a space
        String commentsWithSpace = makeString('c', 7) + " " + makeString('d', 8); // total 16
        s.setComments(commentsWithSpace);

        assertFalse(SpamDetectorService.isSpam(s));
    }

    @Test
    void isSpam_returnsTrue_whenExactly15CharsAndNoSpaces() {
        Submission s = new Submission();
        s.setName(makeString('x', 15));
        s.setComments(makeString('y', 15));

        assertTrue(SpamDetectorService.isSpam(s));
    }
}