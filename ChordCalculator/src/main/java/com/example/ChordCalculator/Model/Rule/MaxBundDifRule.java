package com.example.ChordCalculator.Model.Rule;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class MaxBundDifRule extends Rule {

    public MaxBundDifRule(Instrumental inst,int value) {
        instrumental = inst;
        this.value = value;
    }

    public MaxBundDifRule() {
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        int max=1;
        int min=instrumental.getBundNumber();

        for (StringCatch sc:
                stringCatchList) {
            int actualBund = sc.getBund();
            if (actualBund>max) max=actualBund;
            if (actualBund<min && actualBund>0) min = actualBund;
        }
        //System.out.println("max " +max);
        //System.out.println("min " +min);
        //System.out.println(((max-min)+1<=value));
        return (max-min+1<=value);
    }
}
