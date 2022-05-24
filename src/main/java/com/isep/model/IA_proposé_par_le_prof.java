package com.isep.model;

import javafx.beans.property.IntegerProperty;

import java.util.List;

public class IA_proposé_par_le_prof {
    private int[] pieces;
    public int combinaision(int amount, List<Integer> pieces,int index) {
        amount = 0;
        int total = 0;
        if (pieces.size() == 1 || amount % pieces.get(0) == 0) {
            total = 0;
            for (int i = 0; i * pieces.get(index) <= amount; i++) {
                total = combinaision(amount - i * pieces.get(index), pieces, index + 1);
            }
        }
        return total;
    }

}
