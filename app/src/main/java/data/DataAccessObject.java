package data;

import android.content.Context;

import com.echo_usa.echo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyuki on 6/1/2016.
 */
public class DataAccessObject {
    public static final int ERROR = -1;

    private List<Card> homeCardList;
    private List<Card> guideCardList;
    private List<Card> docsCardList;
    private List<Card> specsCardList;
    private List<Card> maintCardList;
    private List<Model> modelList;
    private List<ModelFiles> modelFiles;

    private List<Specs> specsList;

    public DataAccessObject(Context context) {
        homeCardList = new ArrayList<>();
        guideCardList = new ArrayList<>();
        docsCardList = new ArrayList<>();
        specsCardList = new ArrayList<>();
        maintCardList = new ArrayList<>();

        specsList = new ArrayList<>();

        buildList(context);
    }

    public List<Card> getCards(int cardType) {
        switch(cardType) {
            case Card.CARD_TYPE_HOME: return homeCardList;
            case Card.CARD_TYPE_GUIDE: return guideCardList;
            case Card.CARD_TYPE_DOC: return docsCardList;
            case Card.CARD_TYPE_SPECS: return specsCardList;
            case Card.CARD_TYPE_MAINT: return maintCardList;
            case ERROR: default:
                //TODO: ERROR OUT, notify user?
                return new ArrayList<>();
        }
    }

    public List<ModelFiles> getFiles() {return modelFiles;}

    public List<Model> getModelList() {
        return modelList;
    }

    public List<Specs> getSpecsList() {
        return specsList;
    }

    private void buildList(Context context) {
        //TODO: clean up!
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
                homeCardList.add(
                        new Card(Card.CARD_TYPE_HOME, Card.CARD_SIZE_LARGE, R.drawable.ads_placeholder, titleList[i], subtitleList[i])
                );
            }
        }

        {
            String[] titleList = new String[] {
                    "Choosing the Right Chain Saw",
                    "Choosing the Right Blower",
                    "Choosing the Right Trimmer",
                    "Running Your Equipment With the Right Fuel",
                    "Take steps to keep yourself safe!"
            };

            String[] subtitleList = new String[] {
                    "ECHO has a wide selection of chainsaws, each tuned for different jobs",
                    "The right blower for the job",
                    "Bigger displacement engines for tougher jobs",
                    "Fuel can make your equipment run as smooth as butter or make it fall flat on its face. Choose wisely!",
                    "We want healthy, returning customers. Please don't be lazy and protect yourself"
            };

            for(int i = 0; i < titleList.length; i++) {
                guideCardList.add(
                        new Card(Card.CARD_TYPE_GUIDE, Card.CARD_SIZE_LARGE, R.drawable.ads_placeholder, titleList[i], subtitleList[i])
                );
            }
        }

        {
            String[] titleList = new String[] {
                    "Quick Start Guide",
                    "User Manual",
                    "Safety Manual",
                    "Parts Catalog",
                    "Warranty Statement"
            };

            for(int i = 0; i < titleList.length; i++) {
                docsCardList.add(
                        new Card(Card.CARD_TYPE_DOC, Card.CARD_SIZE_SMALL, R.drawable.ic_vector_small_card_manuals, titleList[i])
                );
            }
        }


        {
            String[] titleList = new String[] {
                    "Dimensions",
                    "Engine",
                    "Eng. Peripherals",
                    "Drive System"
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

            int[] drawableList = new int[] {
                    R.drawable.ic_vector_dimensions,
                    R.drawable.ic_vector_engine,
                    R.drawable.ic_vector_small_card_peripherals,
                    R.drawable.ic_vector_drive_system
            };

            for(int i = 0; i < titleList.length; i++) {
                specsCardList.add(
                        new Card(Card.CARD_TYPE_SPECS, Card.CARD_SIZE_SMALL, drawableList[i], titleList[i])
                );
            }

            if(specValues.length == titleList.length) {
                for(int i = 0; i < titleList.length; i++) {
                    specsList.add(new Specs(titleList[i], specValues[i]));
                }
            }
        }

        {
            String[] titleList = new String[] {
                    "You-Can Kit",
                    "Service Parts",
                    "Step-By-Step"
            };

            int[] drawableList = new int[] {
                    R.drawable.ic_vector_youcan_kit,
                    R.drawable.ic_vector_small_card_service_parts,
                    R.drawable.ic_vector_small_card_service_parts
            };

            for(int i = 0; i < titleList.length; i++) {
                maintCardList.add(
                        new Card(Card.CARD_TYPE_MAINT, Card.CARD_SIZE_SMALL, drawableList[i], titleList[i])
                );
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

            modelList = new ArrayList<>();

            for(int i = 0; i < garageUnitList.length; i++) {
                int offset = i + 1;
                String serialSuffix = offset < 10 ? "0" + offset : String.valueOf(offset);
                modelList.add(new Model(garageUnitList[i], "S999140010" + serialSuffix, modelResource[i]));
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

            modelFiles = new ArrayList<>();

            for(int i = 0; i < titleList.length; i++) {
                modelFiles.add(new ModelFiles(titleList[i], "subtext", R.drawable.ic_vector_docs_dark));
            }
        }
    }
}
