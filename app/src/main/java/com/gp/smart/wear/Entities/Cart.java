package com.gp.smart.wear.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by basse on 20-Jun-17.
 */

public class Cart implements Serializable {

    public String price;
    public List<String> cartWatches_IDs;

    public Cart() {
        this.price = "0$" ;
        this.cartWatches_IDs = new ArrayList<>();
    }
}
