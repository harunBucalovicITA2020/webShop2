package com.web.way.miniwebshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Harun
 */
public class ShoppingCartItem implements Serializable{
    
   private int quantity;
   private Product product;

    public ShoppingCartItem() {
        super();
    }

    public ShoppingCartItem(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
   
   public BigDecimal getTotalPrice(){
       BigDecimal total = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
       total = total.setScale(2,RoundingMode.HALF_UP);
       return total;
   }
    
}
