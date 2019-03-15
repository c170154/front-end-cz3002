package com.cz3002.shopfunding.API;

import android.content.Context;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductQueryTest {
    private ProductQuery productQuery = new ProductQuery();
    private Context appContext = InstrumentationRegistry.getTargetContext();
    private String baseUrl = "http://172.21.147.36:8002/";

    @Test
    public void getShopeeCarousel() {
        assertNotNull(productQuery.getShopeeCarousel(appContext, baseUrl, 5));
    }

    @Test
    public void getShopeeProduct() {
        assertNotNull(productQuery.getShopeeProduct(appContext, baseUrl, 120981896, 1953044238));
    }

    @Test
    public void getSearchResults() {
        assertNotNull(productQuery.getSearchResults(appContext, baseUrl, "mouse"));
    }
}