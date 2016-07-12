package data;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import com.echo_usa.echo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragment.FragmentCardDisplay;
import fragment.FragmentContact;
import fragment.FragmentDocuments;
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
    private List<Specs> specsList;
    private Map<FragName, Fragment> fragMap;

    private ArrayAdapter<String> garageUnitAdapter;

    public DataAccessObject(Context context) {
        homeCardsList = new ArrayList<>();
        docsCardsList = new ArrayList<>();
        specsList = new ArrayList<>();

        buildList(context);
        buildFragmentList();
    }

    public List<Card> getCards(int cardsForPage) {
        if (cardsForPage == CARDS_FOR_DOCS) {
            return docsCardsList;
        } else if (cardsForPage == CARDS_FOR_HOME) {
            return homeCardsList;
        } else return null;
    }

    public ArrayAdapter<String> getGarageAdapter() {
        return garageUnitAdapter;
    }

    public List<Specs> getSpecsList() {
        return specsList;
    }

    private void buildList(Context context) {
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
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder
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

        {
            String[] garageUnitList = new String[] {
                    "Android List View",
                    "Adapter implementation",
                    "Simple List View In Android",
                    "Create List View Android",
                    "Android Example",
                    "List View Source Code",
                    "List View Array Adapter",
                    "Adapter implementation",
                    "Simple List View In Android",
                    "Create List View Android",
                    "Android Example",
                    "List View Source Code",
                    "List View Array Adapter",
                    "Android Example List View"
            };

            garageUnitAdapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_list_item_1,
                    garageUnitList
            );
        }

        {
            String[] specLabels = new String[] {
                    "Engine Displacement (cc)",
                    "Carburetor",
                    "Fuel Capacity (fl. oz.)",
                    "Shaft Length (in)",
                    "Shaft Type",
                    "Starting System",
                    "Cutting Head",
                    "Cutting Swath (in)",
                    "Shield",
                    "Drive Shaft",
                    "Nylon Line (in dia.)",
                    "Dry Weight (lbs)",
                    "Dry Weight (lbs)",
                    "Consumer Warranty",
                    "Commercial Warranty",
                    "Rental Warranty"
            };

            String[] specValues = new String[] {
                    "21.2",
                    "Rotary",
                    "14.9",
                    "48",
                    "Curved",
                    "i-30",
                    "Rapid-Loader",
                    "16",
                    "Stnd. Shield",
                    "4-Layer Cable",
                    "0.080 Cross-Fire",
                    "10.1",
                    "9.6",
                    "5 years",
                    "2 years",
                    "90 days"
            };

            if(specValues.length == specLabels.length) {
                for(int i = 0; i < specLabels.length; i++) {
                    specsList.add(new Specs(specLabels[i], specValues[i]));
                }
            }
        }
    }

    private Map<FragName, Fragment> buildFragmentList() {
        fragMap = new HashMap<>();

        fragMap.put(HOME, FragmentHome.newInstance());
        fragMap.put(CONTACT, FragmentContact.newInstance());
        fragMap.put(LOCATOR, FragmentLocator.newInstance());
        fragMap.put(MAINT, FragmentMaintenance.newInstance());
        fragMap.put(SETTINGS, FragmentSettings.newInstance());
        fragMap.put(SPECS, FragmentSpecifications.newInstance());
        fragMap.put(DOCS, FragmentDocuments.newInstance());
        fragMap.put(CARD_DISP, FragmentCardDisplay.newInstance());

        return fragMap;
    }
}
