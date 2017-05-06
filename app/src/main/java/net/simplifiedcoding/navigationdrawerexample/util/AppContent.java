package net.simplifiedcoding.navigationdrawerexample.util;

import net.simplifiedcoding.navigationdrawerexample.Model.Contribution;

import java.util.ArrayList;

/**
 * Created by Dell on 17-04-2017.
 */

public class AppContent {

    public static ArrayList<Contribution> getMinuteDropDown() {

        ArrayList<Contribution> arrayListDropDown = new ArrayList<>();
        Contribution contribution = new Contribution();
        contribution.setTag("It technology");
        contribution.setCount("Utar Predesh");
        arrayListDropDown.add(contribution);

        Contribution contribution1 = new Contribution();
        contribution1.setTag("It technology");
        contribution1.setCount("Utar Predesh");
        arrayListDropDown.add(contribution1);

        Contribution contribution2 = new Contribution();
        contribution2.setTag("It technology");
        contribution2.setCount("Utar Predesh");
        arrayListDropDown.add(contribution2);

        Contribution contribution3 = new Contribution();
        contribution3.setTag("It technology");
        contribution3.setCount("Utar Predesh");
        arrayListDropDown.add(contribution3);


        return arrayListDropDown;
    }
}
