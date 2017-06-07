package com.pruebascongit.pau.tabs.ContentProvider;

import com.pruebascongit.pau.tabs.Pojos.Summary;
import nl.littlerobots.cupboard.tools.provider.CupboardContentProvider;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class OcrContentProvider extends CupboardContentProvider{

    public static final String AUTHORITY = "com.pruebascongit.pau.tabs.provider";


    static {
        cupboard().register(Summary.class);
    }

    public OcrContentProvider(){
        super(AUTHORITY,1);
    }
}
