package com.web.way.miniwebshop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Harun
 */
public class ShoppingCart {

    private final List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            if (shoppingCartItem.getProduct().getId() == product.getId()) {
                shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + quantity);
               return; 
            }
        }
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(quantity, product);
        shoppingCartItems.add(shoppingCartItem);
    }

    public void deleteItem(Product product, int quantity) {
        Iterator<ShoppingCartItem> iterator = shoppingCartItems.iterator();
        while (iterator.hasNext()) {
            ShoppingCartItem item = iterator.next();
            if (item.getProduct().getId() == product.getId()) {
                if (quantity >= item.getQuantity()) {
                    iterator.remove();
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                }
                return;
            }
        }
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

}
