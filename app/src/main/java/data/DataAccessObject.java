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
    public static final int CARDS_FOR_HOME = 0;
    public static final int CARDS_FOR_DOCS = 1;

    private List<Card> homeCardsList;
    private List<Card> docsCardsList;
    private Map<FragName, Fragment> fragMap;

    public DataAccessObject() {
        homeCardsList = new ArrayList<>();
        docsCardsList = new ArrayList<>();

        buildList();
        buildFragmentList();
    }

    public List<Card> getCards(int cardsForPage) {
        if (cardsForPage == CARDS_FOR_DOCS) {
            return docsCardsList;
        } else if (cardsForPage == CARDS_FOR_HOME) {
            return homeCardsList;
        } else return null;
    }

    public Integer[] getAdsForHeader() {
        return new Integer[] {
                R.drawable.header,
                R.drawable.header,
                R.drawable.header,
                R.drawable.header
        };
    }

    public Fragment getThisFrag(FragName key) {return fragMap.get(key);}

    public boolean isFragVisible(FragName fragName) {
        return fragMap.get(fragName).isVisible();
    }

    public boolean isFragVisible(int menuId) {
        FragName fragName = FragName.getNameById(menuId);

        return isFragVisible(fragName);
    }

    private void buildList() {
        {
            String[] titleList = new String[] {
                    "Get the specs of this unit",
                    "Locate the nearest dealer",
                    "Ongoing sale!",
                    "How-to videos",
                    "Get this fuel!",
                    "Which unit should I buy?"
            };

            Integer[] drawableList = new Integer[] {
                    R.drawable.specs,
                    R.drawable.locator,
                    R.drawable.sale,
                    R.drawable.videos,
                    R.drawable.can,
                    R.drawable.suggestions
            };

            for(int i = 0; i < titleList.length; i++) {
                homeCardsList.add(
                        new Card(drawableList[i], titleList[i])
                );
            }
        }

        {
            String[] titleList = new String[] {
                    "user manual",
                    "spec sheet",
                    "warranty",
                    "quick start"
            };

            Integer[] drawableList = new Integer[] {
                    R.drawable.placeholder,
                    R.drawable.placeholder,
                    R.drawable.placeholder,
                    R.drawable.placeholder
            };

            for(int i = 0; i < titleList.length; i++) {
                docsCardsList.add(
                        new Card(drawableList[i], titleList[i])
                );
            }
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
