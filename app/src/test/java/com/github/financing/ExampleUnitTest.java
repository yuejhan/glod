package com.github.financing;

import com.github.financing.utils.SecurityUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public  void test() {
        try {
            System.out.println(new String(SecurityUtils.rsaEncrypt("1000|17300010020|0020|0001000F0096241|11032302065863805666".getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}