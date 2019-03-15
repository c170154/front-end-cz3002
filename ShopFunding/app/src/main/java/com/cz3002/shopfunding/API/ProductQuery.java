package com.cz3002.shopfunding.API;

import android.content.Context;
import com.cz3002.shopfunding.Model.Carousel;
import com.cz3002.shopfunding.Model.Product;
import com.cz3002.shopfunding.Model.ProductModel;
import com.cz3002.shopfunding.Model.SearchResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ProductQuery {

    public ArrayList<Carousel> getShopeeCarousel(final Context context, String url, int numberOfItems) {
        HashMap<String, String> queryItems = new HashMap<>();
        queryItems.put("nbrOfItems", Integer.toString(numberOfItems));
        String queryUrl = getUrlBuilder(ENDPOINTS.PRODUCT_QUERY + ENDPOINTS.GET_SHOPEE_CAROUSEL, queryItems);

        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject json_response = requestManager.getRequest(context, queryUrl);

        if (json_response != null) {
            ArrayList<Carousel> carouselArrayList = new ArrayList<>();

            try {
                JSONArray jsonArrayList = json_response.getJSONArray("items");
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    Carousel carousel = new Carousel();
                    carousel.setItemId(jsonArrayList.getJSONObject(i).getInt("itemId"));
                    carousel.setShopId(jsonArrayList.getJSONObject(i).getInt("shopId"));
                    carousel.setImage(jsonArrayList.getJSONObject(i).getString("image"));
                    carouselArrayList.add(carousel);
                }
                return carouselArrayList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Product getShopeeProduct(final Context context, String url, int itemId, int shopId) {
        HashMap<String, String> queryItems = new HashMap<>();
        queryItems.put("itemId", Integer.toString(itemId));
        queryItems.put("shopId", Integer.toString(shopId));
        String queryUrl = getUrlBuilder(ENDPOINTS.PRODUCT_QUERY + ENDPOINTS.GET_SHOPEE_PRODUCT, queryItems);

        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject json_response = requestManager.getRequest(context, queryUrl);

        if (json_response != null) {
            try {
                Product product = new Product();
                product.setName(json_response.getString("name"));
                product.setItemId(json_response.getInt("itemId"));
                product.setShopId(json_response.getInt("shopId"));

                JSONArray jsonImageLinks = json_response.getJSONArray("imageLinks");
                ArrayList<String> imageLinks = new ArrayList<>();
                for (int i = 0; i < jsonImageLinks.length(); i++) {
                    imageLinks.add(jsonImageLinks.getString(i));
                }
                product.setImageLinks(imageLinks);

                JSONArray models = json_response.getJSONArray("models");
                ArrayList<ProductModel> productModels = new ArrayList<>();
                for (int i = 0; i < models.length(); i++) {
                    ProductModel productModel = new ProductModel();
                    productModel.setItemId(models.getJSONObject(i).getInt("itemid"));
                    productModel.setName(models.getJSONObject(i).getString("name"));
                    productModel.setPromotionId(models.getJSONObject(i).getInt("promotionid"));
                    productModel.setPrice(models.getJSONObject(i).getDouble("price"));
                    productModel.setCurrency(models.getJSONObject(i).getString("currency"));
                    productModel.setModelId(models.getJSONObject(i).getInt("modelid"));
                    productModel.setSold(models.getJSONObject(i).getInt("sold"));
                    productModel.setStock(models.getJSONObject(i).getInt("stock"));
                    productModels.add(productModel);
                }

                if (productModels.size() != 0) {
                    product.setModels(productModels);
                } else {
                    product.setModels(null);
                }

                product.setMinPrice(json_response.getDouble("minPrice"));
                product.setShippingFee(json_response.getDouble("shippingFee"));
                product.setDescription(json_response.getString("description"));

                return product;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<SearchResult> getSearchResults(final Context context, String url, String keyword) {
        HashMap<String, String> queryItems = new HashMap<>();
        queryItems.put("keyword", keyword);
        String queryUrl = getUrlBuilder(ENDPOINTS.PRODUCT_QUERY + ENDPOINTS.GET_SHOPEE_SEARCH_RESULTS, queryItems);

        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject json_response = requestManager.getRequest(context, queryUrl);

        if (json_response != null) {
            try {
                ArrayList<SearchResult> searchResults = new ArrayList<>();
                JSONArray items = json_response.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    SearchResult searchResult = new SearchResult();
                    searchResult.setItemId(items.getJSONObject(i).getInt("itemId"));
                    searchResult.setShopId(items.getJSONObject(i).getInt("shopId"));
                    searchResult.setName(items.getJSONObject(i).getString("name"));

                    ArrayList<String> imageLinks = new ArrayList<>();
                    JSONArray imagesJsonArray = items.getJSONArray(i);
                    for (int j = 0; j < imagesJsonArray.length(); j++) {
                        imageLinks.add(imagesJsonArray.getString(j));
                    }
                    searchResult.setImageLinks(imageLinks);

                    searchResults.add(searchResult);
                }
                return searchResults;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getUrlBuilder (String url, HashMap < String, String > params){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);

        Set<String> keySet = params.keySet();
        if (keySet.size() > 0) {
            stringBuilder.append("?");
            for (String key : keySet) {
                String value = params.get(key);
                // Check. Seems like having an extra & after the ? does not matter.
                stringBuilder.append("&");
                stringBuilder.append(key);
                stringBuilder.append("=");
                stringBuilder.append(value);
            }
        }
        return stringBuilder.toString();
    }
}
