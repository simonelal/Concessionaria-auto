package com.concessionaria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.concessionaria.model.OptionBundle;
import com.concessionaria.model.OptionLeaf;

public class OptionBundleTest {

    @Test
    void bundleCalculatesTotalPrice() {
        OptionBundle bundle = new OptionBundle("Test Pack");
        bundle.add(new OptionLeaf("A", 100));
        bundle.add(new OptionLeaf("B", 200));

        assertEquals(300.0, bundle.getPrice());
    }
}
