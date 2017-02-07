package data;

import android.content.Context;
import android.content.res.Resources;

import com.echo_usa.echo.R;

import java.util.ArrayList;
import java.util.List;

import data.card_content.CardContent;
import data.card_content.CardCat;
import data.card_content.CardInfo;
import data.frag_list.Contact;
import data.frag_list.FragListItem;
import data.frag_list.ModelDocs;
//import data.card_content.CardCat;
import data.navigation.Model;
import data.navigation.NavItem;
import util.FragSpec;

/**
 * Created by zyuki on 6/1/2016.
 */
public class DataAccessObject {
    private List<CardContent> mHomeCardList, mGuideHomeList, mCatalogHomeList, mLocatorList;
    private List<NavItem> mModelList;
    private List<FragListItem> mModelDocsList, mContactList;

    public DataAccessObject(Context context) {
        mHomeCardList = new ArrayList<>();
        mGuideHomeList = new ArrayList<>();
        mCatalogHomeList = new ArrayList<>();

        mLocatorList = new ArrayList<>();
        mModelList = new ArrayList<>();

        mModelDocsList = new ArrayList<>();
        mContactList = new ArrayList<>();

        buildList(context);
    }

    public List<CardContent> getCards(FragSpec fragSpec) {
        switch(fragSpec) {
            case HOME: return mHomeCardList;
            case GUIDE: return mGuideHomeList;
            case CATALOG: return mCatalogHomeList;
            case LOCATOR: return mLocatorList;
        }

        return null;
    }

    public List<NavItem> getNavList(FragSpec fragSpec) {
        switch(fragSpec) {
//            case LOCATOR:
//                return mLocatorList;
            case MODEL_DOCS:
                return mModelList;
        }

        return null;
    }

    public List<FragListItem> getFragList(FragSpec fragSpec) {
        switch(fragSpec) {
            case MODEL_DOCS:
                return mModelDocsList;
            case CONTACT:
                return mContactList;
        }

        return null;
    }

    private void buildList(Context context) {
        {
            String[] titleList = new String[] {
                    "One Day Sale",
                    "Industry-only 5 year Warranty",
                    "Professional-grade Cordless",
                    "ECHO Power Fuel",
                    "Introducing the Pro X-Treme Series",
                    "Fleet Discount Program"
            };

            String[] subtitleList = new String[] {
                    "Learn more about our upcoming sales event",
                    "The one and only 5 year consumer warranty program",
                    "Our 58V product line is here",
                    "Never worry about Ethanol again",
                    "Most lightweight and powerful engine of its class on the market",
                    "Looking for large orders? Bundle multiple units together and get a great discount"
            };

            for(int i = 0; i < titleList.length; i++) {
                //No dividers
                mHomeCardList.add(
                        new CardInfo(R.drawable.ads_placeholder, titleList[i], subtitleList[i])
                );
            }
        }

        {
            final int EMPTY = -1;
            int[] resList = new int[] {
                    EMPTY,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    EMPTY,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder
            };

            String[] titleList = new String[] {
                    "Right tool for the job", //DIVIDER
                    "Choosing the Right Chain Saw",
                    "Choosing the Right Blower",
                    "Choosing the Right Trimmer",
                    "Getting started safely", //DIVIDER
                    "Running Your Equipment With the Right Fuel",
                    "Take steps to keep yourself safe!"
            };

            String[] subtitleList = new String[] {
                    "Before you get started", //DIVIDER
                    "ECHO has a wide selection of chainsaws, each tuned for different jobs",
                    "The right blower for the job",
                    "Bigger displacement engines for tougher jobs",
                    "Lets put that machine to work!", //DIVIDER
                    "Fuel can make your equipment run as smooth as butter or make it fall flat on its face. Choose wisely!",
                    "We want healthy, returning customers. Please don't be lazy and protect yourself"
            };

            for(int i = 0; i < titleList.length; i++) {
                mGuideHomeList.add(
                        resList[i] == EMPTY ?
                                new CardInfo(titleList[i], subtitleList[i]) :
                                new CardInfo(resList[i], titleList[i], subtitleList[i])
                );
            }
        }

        {
            int[] resList = new int[] {
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder,
                    R.drawable.ads_placeholder
            };

            String[] titleList = new String[]{
                    "Trimmers",
                    "Brushcutters",
                    "Blowers",
                    "Chain Saws",
                    "Edgers",
                    "Hedge Trimmers",
                    "Sprayers",
                    "Accessories"
            };

            for(int i = 0; i < titleList.length; i++) {
                mCatalogHomeList.add(new CardCat(resList[i], titleList[i], 0));
            }
        }

        {
            String[] garageUnitList = new String[] {
                    "SRM-225",
                    "PB-770H",
                    "DH232"
            };

            int[] modelResource = new int[] {
                    R.drawable.srm_225,
                    R.drawable.pb_770h,
                    R.drawable.dh232
            };

            for(int i = 0; i < garageUnitList.length; i++) {
                int offset = i + 1;
                String serialSuffix = offset < 10 ? "0" + offset : String.valueOf(offset);
                mModelList.add(new Model(modelResource[i], garageUnitList[i], "S999140010" + serialSuffix, 0));
            }
        }

        {
            int[] iconResList = new int[] {
                    FragListItem.INIT,
                    R.drawable.ic_vector_http,
                    R.drawable.ic_vector_address,
                    R.drawable.ic_vector_phone,
                    R.drawable.ic_vector_chat,
                    R.drawable.ic_vector_email,
                    FragListItem.INIT,
                    R.drawable.ic_vector_http
            };

            Resources resources = context.getResources();
            String[] titleList = new String[] {
                    "Contact us",
                    resources.getString(R.string.label_web),
                    resources.getString(R.string.label_address),
                    resources.getString(R.string.label_phone),
                    resources.getString(R.string.label_chat),
                    resources.getString(R.string.label_app_email),
                    "ECHO Cordless",
                    resources.getString(R.string.label_web)
            };

            String[] subtitleList = new String[] {
                    null,
                    "http://www.echo-usa.com",
                    resources.getString(R.string.echo_address),
                    resources.getString(R.string.echo_phone),
                    null,
                    null,
                    null,
                    resources.getString(R.string.V58_URL)
            };

            for(int i = 0; i < titleList.length; i++) {
                if(iconResList[i] != FragListItem.INIT) {
                    mContactList.add(new Contact(iconResList[i], /*layoutResList[i],*/ titleList[i], subtitleList[i]));
                } else {
                    mContactList.add(new Contact(titleList[i]));
                }
            }
        }

        {
            String[] titleList = new String[] {
                    "Quick Start Guide",
                    "User Manual",
                    "Safety Manual",
                    "Parts Catalog",
                    "Warranty Statement",
                    "You-Can Kit",
                    "Service Parts",
                    "Step-By-Step",
                    "Dimensions",
                    "Engine",
                    "Eng. Peripherals",
                    "Drive System"
            };

            for(int i = 0; i < titleList.length; i++) {
                mModelDocsList.add(new ModelDocs(titleList[i]));
            }
        }

        {
            String[] nameList = new String[] {
                    "Buck Bros Inc",
                    "Ralph Helm Inc",
                    "Arlington Power Equipment Inc",
                    "McHenry Power Equipment",
                    "Russon Elgin",
                    "Blains Farm & Fleet",
                    "Russo Hainesville",
                    "Grower Equipment & Supply Co",
                    "Grayslake Outdoor Power Equip",
                    "Valentines Repairs"
            };

            String[] address = new String[] {
                    "29626 N. Highway 12\nWauconda, IL 60084",
                    "7402 Teckler Road\nCrystal Lake, IL 60014",
                    "20175 N. Rand Road\nPalatine, IL, 60074",
                    "3622 W. Elm Street\nMcHenry, IL, 60050",
                    "1001 N. Randall Road\nElgin, IL 60123",
                    "11501 Route 14\nWoodstock, IL 60098",
                    "39 E. Belvidere Road\nHainesVille, IL 60030",
                    "294 E. Belvidere Road\nHainesville, IL 60030",
                    "81 E. Belvidere Road\nGrayslake, IL 60030",
                    "6417 Keystone Road,\nRichmond, IL 60071"
            };

            String[] phone = new String[] {
                    "(847)487 - 4900",
                    "(815)788 - 1616",
                    "(847)253 - 5727",
                    "(815)344 - 7660",
                    "(224)268 - 3035",
                    "(815)338 - 2549",
                    "(847)752 - 0420",
                    "(847)223 - 3100",
                    "(847)223 - 6333",
                    "(815)653 - 4100"
            };

            boolean[] rws = new boolean[] {
                    false, false, true, true, true, false, true, false, false, false
            };

            for(int i = 0; i < nameList.length; i++) {
//                mLocatorList.add(new Dealer());
            }
        }
    }
}
