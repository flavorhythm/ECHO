package data;

import android.support.v4.app.Fragment;

import com.echo_usa.echo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragment.FragmentCardDisplay;
import fragment.FragmentContact;
import fragment.FragmentDocuments;
import fragment.FragmentGuide;
import fragment.FragmentHome;
import fragment.FragmentLocator;
import fragment.FragmentMaintenance;
import fragment.FragmentSettings;
import fragment.FragmentSpecifications;
import util.FragName;

import static util.FragName.*;

/**
 * Created by zyuki on 6/1/2016.
 */
public class DataAccessObject {
    private List<Card> cardsList;
    private Map<FragName, Fragment> fragMap;

    private String[] titleList;
    private Integer[] drawableList;

    public DataAccessObject() {
        cardsList = new ArrayList<>();
        titleList = new String[] {
                "Get the specs of this unit",
                "Locate the nearest dealer",
                "Ongoing sale!",
                "How-to videos",
                "Get this fuel!",
                "Which unit should I buy?"
        };

        drawableList = new Integer[] {
                R.drawable.specs,
                R.drawable.locator,
                R.drawable.sale,
                R.drawable.videos,
                R.drawable.can,
                R.drawable.suggestions
        };

        buildList();
        buildFragmentList();
    }

    public List<Card> getCards() {return cardsList;}

    public Fragment getThisFrag(FragName key) {return fragMap.get(key);}

    public boolean isFragVisible(FragName fragName) {
        return fragMap.get(fragName).isVisible();
    }

    public boolean isFragVisible(int menuId) {
        FragName fragName = FragName.getNameById(menuId);

        return isFragVisible(fragName);
    }

    private void buildList() {
        for(int i = 0; i < titleList.length; i++) {
            cardsList.add(
                    new Card(drawableList[i], titleList[i])
            );
        }
    }

    private Map<FragName, Fragment> buildFragmentList() {
        fragMap = new HashMap<>();

        fragMap.put(HOME, FragmentHome.newInstance());
        fragMap.put(CONTACT, FragmentContact.newInstance());
        fragMap.put(GUIDE, FragmentGuide.newInstance());
        fragMap.put(LOCATOR, FragmentLocator.newInstance());
        fragMap.put(MAINT, FragmentMaintenance.newInstance());
        fragMap.put(SETTINGS, FragmentSettings.newInstance());
        fragMap.put(SPECS, FragmentSpecifications.newInstance());
        fragMap.put(DOCS, FragmentDocuments.newInstance());
        fragMap.put(CARD_DISP, FragmentCardDisplay.newInstance());

        return fragMap;
    }
}
