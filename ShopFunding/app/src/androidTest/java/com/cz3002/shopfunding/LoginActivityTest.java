package com.cz3002.shopfunding;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import org.junit.Test;

import static com.cz3002.shopfunding.API.User.getJWTToken;
import static org.junit.Assert.assertNull;

public class LoginActivityTest {

    @Test
    public void onCreate() {
        Context context = InstrumentationRegistry.getTargetContext();
        assertNull(getJWTToken(context), null);
    }
}