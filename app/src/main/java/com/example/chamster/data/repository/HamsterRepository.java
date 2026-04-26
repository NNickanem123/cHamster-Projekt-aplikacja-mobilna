package com.example.chamster.data;

import com.example.chamster.data.model.HamsterItem;
import java.util.ArrayList;
import java.util.List;

public class HamsterRepository {

    public static List<HamsterItem> getItems() {
        List<HamsterItem> list = new ArrayList<>();

        list.add(new HamsterItem(
                "Chomik podstawowy",
                "Domyślny wygląd Twojego chomika.",
                "hamster/chomik.png"
        ));

        list.add(new HamsterItem(
                "Czarny chomik",
                "Mroczna wersja chomika.",
                "hamster/czarny_chomik.png"
        ));

        list.add(new HamsterItem(
                "Czerwony chomik",
                "Chomik w ognistym stylu.",
                "hamster/czerwony_chomik.png"
        ));

        list.add(new HamsterItem(
                "Niebieski chomik",
                "Chomik w chłodnym, niebieskim kolorze.",
                "hamster/niebieski_chomik.png"
        ));

        list.add(new HamsterItem(
                "Zielony chomik",
                "Chomik w zielonym odcieniu.",
                "hamster/zielony_chomik.png"
        ));

        list.add(new HamsterItem(
                "Czapka",
                "Mała czapeczka dla Twojego chomika.",
                "hamster/czapka.png"
        ));

        list.add(new HamsterItem(
                "Okulary",
                "Stylowe okulary dla chomika.",
                "hamster/okulary.png"
        ));

        list.add(new HamsterItem(
                "Korona",
                "Król chomików — idealne dla VIPa.",
                "hamster/korona.png"
        ));

        list.add(new HamsterItem(
                "Aureola",
                "Anielska aureola dla grzecznego chomika.",
                "hamster/aureola.png"
        ));

        list.add(new HamsterItem(
                "Peleryna",
                "Peleryna superbohatera.",
                "hamster/peleryna.png"
        ));

        list.add(new HamsterItem(
                "Skrzydła",
                "Chomik-aniołek.",
                "hamster/skrzydla.png"
        ));

        return list;
    }
}