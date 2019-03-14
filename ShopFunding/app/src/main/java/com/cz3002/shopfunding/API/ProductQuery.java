package com.cz3002.shopfunding.API;

import android.content.Context;
import com.cz3002.shopfunding.Model.Carousel;
import com.cz3002.shopfunding.Model.Product;
import com.cz3002.shopfunding.Model.ProductModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductQuery {

    public ArrayList<Carousel> getShopeeCarousel(final Context context, String url, int numberOfItems){
        var requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONArray json_response = requestManager.getRequest_JSONArray(
                context, ENDPOINTS.PRODUCT_QUERY + ENDPOINTS.GET_SHOPEE_CAROUSEL + "?" + "nbrOfItems=" + numberOfItems);

        if (json_response != null) {
            var carouselArrayList  = new ArrayList<Carousel>();

            try {
                    var jsonArrayList = json_response.getJsonArray("items");
                    for (var item: jsonArrayList) {
                        var carousel = new Carousel();
                        carousel.setItemId(item.getJsonObject("itemId"));
                        carousel.setShopId(item.getJsonObject("shopId"));
                        carousel.setImage(item.getJsonObject("image"));
                        carouselArrayList.add(carousel)
                    }
                } catch (JSONException e) {
                e.printStackTrace();
            }

            return carouselArrayList;
        }

        return null;
    }

    public Product getShopeeProduct(final Context context, String url, int itemId, int shopId) {
        var requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONArray json_response = requestManager.getRequest_JSONArray(
                context, ENDPOINTS.PRODUCT_QUERY + ENDPOINTS.GET_SHOPEE_PRODUCT + "?" + "itemId=" + itemId + "&" +
                        "shopId=" + shopId);

        if (json_response != null) {
            try {
                var product = new Product();
                product.setName(json_response.getJsonObject("name"));
                product.setItemId(json_response.getJsonObject("itemId"));
                product.setShopId(json_response.getJsonObject("shopId"));
                product.setImageLinks(json_response.getJsonArray("imageLinks"));
                var models = json_response.getJsonArray("models");
                var productModels = new ArrayList<ProductModel>();

                for (var model: models) {
                    var productModel = new ProductModel();
                    productModel.setItemId(model.getJsonObject("itemid"));
                    productModel.setName(model.getJsonObject("name"));
                    productModel.setPromotionId(model.getJsonObject("promotionid"));
                    productModel.setPrice(model.getJsonObject("price"));
                    productModel.setCurrency(model.getJsonObject("currency"));
                    productModel.setModelId(model.getJsonObject("modelid"));
                    productModel.setSold(model.getJsonObject("sold"));
                    productModel.setStock(model.getJsonObject("stock"));
                    productModels.add(productModel);
                }

                if (productModels.size() != 0) {
                    product.setModels(productModels);
                } else {
                    product.setModels(null);
                }

                product.setMinPrice(json_response.getJsonObject("minPrice"));
                product.setShippingFee(json_response.getJsonObject("shippingFee"));
                product.setDescription(json_response.getJsonObject("description"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return;
    }
}