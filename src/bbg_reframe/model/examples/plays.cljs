(ns bbg-reframe.model.examples.plays
  (:require [clojure.tools.reader.edn :refer [read-string]]
            [tubax.core :refer [xml->clj]]))

(def plays-page-1 "<plays username=\"ddmits\" userid=\"119790\" total=\"355\" page=\"1\" termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">
<play id=\"48077780\" date=\"2021-01-01\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Maracaibo\" objecttype=\"thing\" objectid=\"276025\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"142\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"145\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"47761710\" date=\"2020-12-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Maracaibo\" objecttype=\"thing\" objectid=\"276025\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"123\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"178\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"47761709\" date=\"2020-03-15\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Archipelago\" objecttype=\"thing\" objectid=\"105551\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"10\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"10\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"5\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"41582786\" date=\"2020-03-11\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Orléans\" objecttype=\"thing\" objectid=\"164928\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"86\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"169\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"3\" color=\"\" score=\"112\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"4\" color=\"\" score=\"93\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"41582717\" date=\"2020-03-02\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Underwater Cities\" objecttype=\"thing\" objectid=\"247763\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"82\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"114\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"41278004\" date=\"2020-02-26\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Princes of Florence\" objecttype=\"thing\" objectid=\"555\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"56\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"49\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Aimilia\" startposition=\"3\" color=\"\" score=\"40\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Nikos\" startposition=\"4\" color=\"\" score=\"27\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"5\" color=\"\" score=\"40\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"41108629\" date=\"2020-02-20\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Mombasa\" objecttype=\"thing\" objectid=\"172386\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"105\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"122\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"3\" color=\"\" score=\"103\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"4\" color=\"\" score=\"97\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"41108616\" date=\"2020-02-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clank!: A Deck-Building Adventure\" objecttype=\"thing\" objectid=\"201808\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"60\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"57\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"51\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"4\" color=\"\" score=\"48\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"40786014\" date=\"2020-02-05\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clank!: A Deck-Building Adventure\" objecttype=\"thing\" objectid=\"201808\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"60\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"81\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"3\" color=\"\" score=\"81\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"4\" color=\"\" score=\"70\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"40481222\" date=\"2020-01-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"1\" color=\"\" score=\"86\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"2\" color=\"\" score=\"65\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"3\" color=\"\" score=\"62\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"40124725\" date=\"2020-01-08\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Architects of the West Kingdom\" objecttype=\"thing\" objectid=\"236457\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"13\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"26\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"24\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"4\" color=\"\" score=\"28\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"5\" color=\"\" score=\"25\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"40055559\" date=\"2020-01-05\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Architects of the West Kingdom\" objecttype=\"thing\" objectid=\"236457\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"28\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"42\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"43\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"4\" color=\"\" score=\"32\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"5\" color=\"\" score=\"45\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"39945666\" date=\"2020-01-02\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Architects of the West Kingdom\" objecttype=\"thing\" objectid=\"236457\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"33\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"34\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"20\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"4\" color=\"\" score=\"27\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39951166\" date=\"2020-01-02\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Architects of the West Kingdom\" objecttype=\"thing\" objectid=\"236457\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"31\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"45\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"20\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"4\" color=\"\" score=\"35\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"5\" color=\"\" score=\"27\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39945508\" date=\"2020-01-01\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Architects of the West Kingdom\" objecttype=\"thing\" objectid=\"236457\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"22\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"25\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"39945510\" date=\"2020-01-01\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Architects of the West Kingdom\" objecttype=\"thing\" objectid=\"236457\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"30\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"38\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Bot-architect\" startposition=\"3\" color=\"\" score=\"29\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39791048\" date=\"2019-12-29\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"64\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"81\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"3\" color=\"\" score=\"63\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39606903\" date=\"2019-12-23\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Russian Railroads\" objecttype=\"thing\" objectid=\"144733\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"254\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"364\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"3\" color=\"\" score=\"281\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"4\" color=\"\" score=\"271\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39505592\" date=\"2019-12-18\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Russian Railroads\" objecttype=\"thing\" objectid=\"144733\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"279\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"387\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"3\" color=\"\" score=\"244\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39381366\" date=\"2019-12-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"65\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"76\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"3\" color=\"\" score=\"41\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39254331\" date=\"2019-12-04\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Concordia\" objecttype=\"thing\" objectid=\"124361\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"66\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"116\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"58\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Eugenios\" startposition=\"4\" color=\"\" score=\"59\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Michalis\" startposition=\"5\" color=\"\" score=\"65\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"39254321\" date=\"2019-11-14\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"52\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"70\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Eugenios\" startposition=\"3\" color=\"\" score=\"59\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Kyriakos\" startposition=\"4\" color=\"\" score=\"57\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"38276440\" date=\"2019-10-15\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban: Driver's Edition\" objecttype=\"thing\" objectid=\"109276\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"106\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"135\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"119\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Tasos\" startposition=\"4\" color=\"\" score=\"102\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"38179682\" date=\"2019-10-09\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Dungeon Lords\" objecttype=\"thing\" objectid=\"45315\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"\" color=\"\" score=\"17\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"\" color=\"\" score=\"5\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Eugenios\" startposition=\"\" color=\"\" score=\"-10\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"\" color=\"\" score=\"22\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"38059327\" date=\"2019-10-02\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Dungeon Lords\" objecttype=\"thing\" objectid=\"45315\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"\" color=\"\" score=\"9\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"\" color=\"\" score=\"34\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"\" color=\"\" score=\"7\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"37943532\" date=\"2019-09-25\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Dungeon Lords\" objecttype=\"thing\" objectid=\"45315\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"\" color=\"\" score=\"24\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"\" color=\"\" score=\"18\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"\" color=\"\" score=\"5\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"37714247\" date=\"2019-09-11\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Bohnanza\" objecttype=\"thing\" objectid=\"11\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"17\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Aimilia\" startposition=\"2\" color=\"\" score=\"18\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"3\" color=\"\" score=\"20\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Nikos\" startposition=\"4\" color=\"\" score=\"17\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"37714230\" date=\"2019-09-04\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Lancaster\" objecttype=\"thing\" objectid=\"96913\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"49\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"48\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Tasos\" startposition=\"3\" color=\"\" score=\"29\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Spyros\" startposition=\"4\" color=\"\" score=\"39\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"37444315\" date=\"2019-08-27\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Sheriff of Nottingham\" objecttype=\"thing\" objectid=\"157969\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"175\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"210\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Nikos\" startposition=\"3\" color=\"\" score=\"183\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"4\" color=\"\" score=\"198\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Aimilia\" startposition=\"5\" color=\"\" score=\"132\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"37345652\" date=\"2019-08-22\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Teotihuacan: City of Gods\" objecttype=\"thing\" objectid=\"229853\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"179\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"174\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Nikos\" startposition=\"3\" color=\"\" score=\"131\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"37345650\" date=\"2019-08-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"67\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"66\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"41\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Nikos\" startposition=\"4\" color=\"\" score=\"76\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"37039915\" date=\"2019-08-06\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"La Granja\" objecttype=\"thing\" objectid=\"146886\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"51\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"57\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"58\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"37000636\" date=\"2019-08-04\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Teotihuacan: City of Gods\" objecttype=\"thing\" objectid=\"229853\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"1\" color=\"\" score=\"172\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"2\" color=\"\" score=\"151\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"3\" color=\"\" score=\"161\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36623125\" date=\"2019-07-15\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban: Driver's Edition\" objecttype=\"thing\" objectid=\"109276\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"116\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"112\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"126\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"36620891\" date=\"2019-07-15\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Viticulture\" objecttype=\"thing\" objectid=\"128621\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"29\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"27\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"20\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36602408\" date=\"2019-07-14\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Gaia Project\" objecttype=\"thing\" objectid=\"220308\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"123\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"149\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"3\" color=\"\" score=\"85\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36620880\" date=\"2019-07-14\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban: Driver's Edition\" objecttype=\"thing\" objectid=\"109276\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"108\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"121\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"3\" color=\"\" score=\"93\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36527428\" date=\"2019-07-10\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Dominant Species\" objecttype=\"thing\" objectid=\"62219\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"144\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"133\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"112\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"4\" color=\"\" score=\"102\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36497513\" date=\"2019-07-08\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Mythotopia\" objecttype=\"thing\" objectid=\"133632\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"48\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"52\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"46\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36410040\" date=\"2019-07-05\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Castles of Mad King Ludwig\" objecttype=\"thing\" objectid=\"155426\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"88\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"136\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"84\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"4\" color=\"\" score=\"76\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36407875\" date=\"2019-07-04\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban: Driver's Edition\" objecttype=\"thing\" objectid=\"109276\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"98\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"88\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"87\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"4\" color=\"\" score=\"73\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36260790\" date=\"2019-06-26\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Castles of Mad King Ludwig\" objecttype=\"thing\" objectid=\"155426\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"101\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"117\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"89\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36259451\" date=\"2019-06-26\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Santa Maria\" objecttype=\"thing\" objectid=\"229220\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"101\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"97\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"68\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Lambros\" startposition=\"4\" color=\"\" score=\"56\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"36016145\" date=\"2019-06-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Teotihuacan: City of Gods\" objecttype=\"thing\" objectid=\"229853\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"202\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"160\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"113\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35911664\" date=\"2019-06-06\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban Menu\" objecttype=\"thing\" objectid=\"277018\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"1\" color=\"\" score=\"119\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"2\" color=\"\" score=\"95\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Lambros\" startposition=\"3\" color=\"\" score=\"82\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Siromist\" userid=\"378135\" name=\"Mpampis\" startposition=\"4\" color=\"\" score=\"94\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"5\" color=\"\" score=\"119\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"35799151\" date=\"2019-05-31\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban Menu\" objecttype=\"thing\" objectid=\"277018\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"146\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"110\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"134\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Lambros\" startposition=\"4\" color=\"\" score=\"82\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35799150\" date=\"2019-05-23\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Teotihuacan: City of Gods\" objecttype=\"thing\" objectid=\"229853\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"121\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"162\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Tasos\" startposition=\"3\" color=\"\" score=\"115\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35528779\" date=\"2019-05-16\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Through the Ages: A Story of Civilization\" objecttype=\"thing\" objectid=\"25613\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"138\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"2\" color=\"\" score=\"133\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"148\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"35303647\" date=\"2019-05-04\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Tzolk'in: The Mayan Calendar\" objecttype=\"thing\" objectid=\"126163\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"54\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"62\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"54\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35301083\" date=\"2019-05-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Food Chain Magnate\" objecttype=\"thing\" objectid=\"175914\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"60\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"572\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"473\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35266999\" date=\"2019-05-01\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Food Chain Magnate\" objecttype=\"thing\" objectid=\"175914\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"245\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"345\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Siromist\" userid=\"378135\" name=\"Mpampis\" startposition=\"3\" color=\"\" score=\"0\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35266979\" date=\"2019-04-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Kanban Menu\" objecttype=\"thing\" objectid=\"277018\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"137\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"117\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35024562\" date=\"2019-04-19\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Belfort\" objecttype=\"thing\" objectid=\"50750\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"28\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"42\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"30\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"4\" color=\"\" score=\"36\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"35021314\" date=\"2019-04-18\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Abyss\" objecttype=\"thing\" objectid=\"155987\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"39\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"78\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"58\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Escrow\" userid=\"1386373\" name=\"Stavros\" startposition=\"4\" color=\"\" score=\"57\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34934118\" date=\"2019-04-13\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Grand Austria Hotel\" objecttype=\"thing\" objectid=\"182874\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"48\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"142\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"34900702\" date=\"2019-04-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Cthulhu Wars\" objecttype=\"thing\" objectid=\"139976\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"29\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"24\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"23\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"4\" color=\"\" score=\"25\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34897410\" date=\"2019-04-11\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Quadropolis\" objecttype=\"thing\" objectid=\"176396\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"104\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"116\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"84\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34875050\" date=\"2019-04-10\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Food Chain Magnate\" objecttype=\"thing\" objectid=\"175914\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"166\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"327\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"324\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34831738\" date=\"2019-04-07\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Food Chain Magnate\" objecttype=\"thing\" objectid=\"175914\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"1\" color=\"\" score=\"616\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"2\" color=\"\" score=\"94\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Tasos\" startposition=\"3\" color=\"\" score=\"77\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34656226\" date=\"2019-03-29\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Mission: Red Planet (Second Edition)\" objecttype=\"thing\" objectid=\"176920\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"45\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"31\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"23\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"fingerk4\" userid=\"361844\" name=\"Ivo\" startposition=\"4\" color=\"\" score=\"54\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"34656186\" date=\"2019-03-28\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"La Granja\" objecttype=\"thing\" objectid=\"146886\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"1\" color=\"\" score=\"71\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"2\" color=\"\" score=\"55\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Mpampis\" startposition=\"3\" color=\"\" score=\"40\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34586919\" date=\"2019-03-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Yokohama\" objecttype=\"thing\" objectid=\"196340\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"136\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"131\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"140\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"34532821\" date=\"2019-03-21\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Shogun\" objecttype=\"thing\" objectid=\"20551\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"27\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"60\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"51\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Tasos\" startposition=\"4\" color=\"\" score=\"53\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"5\" color=\"\" score=\"33\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34405348\" date=\"2019-03-15\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Mea Culpa\" objecttype=\"thing\" objectid=\"203624\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"6\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"8\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"0\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34402651\" date=\"2019-03-14\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Santa Maria\" objecttype=\"thing\" objectid=\"229220\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"87\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"85\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"82\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Katerina\" startposition=\"4\" color=\"\" score=\"72\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34290591\" date=\"2019-03-09\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Prodigals Club\" objecttype=\"thing\" objectid=\"181796\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"21\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"11\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"34287849\" date=\"2019-03-08\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Terraforming Mars\" objecttype=\"thing\" objectid=\"167791\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"66\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"72\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"49\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"4\" color=\"\" score=\"42\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34206863\" date=\"2019-03-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Prodigals Club\" objecttype=\"thing\" objectid=\"181796\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"18\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"9\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"24\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"34209641\" date=\"2019-03-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Quadropolis\" objecttype=\"thing\" objectid=\"176396\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"113\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"112\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"85\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34203561\" date=\"2019-03-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Santa Maria\" objecttype=\"thing\" objectid=\"229220\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"92\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"82\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"3\" color=\"\" score=\"64\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"34137806\" date=\"2019-02-28\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Prodigals Club\" objecttype=\"thing\" objectid=\"181796\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"17\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"7\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"33993573\" date=\"2019-02-21\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Terraforming Mars\" objecttype=\"thing\" objectid=\"167791\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"94\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"56\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"85\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33990930\" date=\"2019-02-20\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Santa Maria\" objecttype=\"thing\" objectid=\"229220\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"71\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"115\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"74\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33885607\" date=\"2019-02-16\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clans of Caledonia\" objecttype=\"thing\" objectid=\"216132\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"118\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"164\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"123\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33887294\" date=\"2019-02-16\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Quadropolis\" objecttype=\"thing\" objectid=\"176396\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"114\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"111\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"59\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33882890\" date=\"2019-02-15\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Santa Maria\" objecttype=\"thing\" objectid=\"229220\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"72\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"96\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"64\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Tasos\" startposition=\"4\" color=\"\" score=\"66\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33869055\" date=\"2019-02-14\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Quadropolis\" objecttype=\"thing\" objectid=\"176396\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"86\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"104\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kostas Doios\" startposition=\"3\" color=\"\" score=\"91\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33855424\" date=\"2019-02-13\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clans of Caledonia\" objecttype=\"thing\" objectid=\"216132\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"147\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"164\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"123\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33869054\" date=\"2019-02-13\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Quadropolis\" objecttype=\"thing\" objectid=\"176396\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"47\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"49\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"54\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Kostas Doios\" startposition=\"4\" color=\"\" score=\"51\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33722651\" date=\"2019-02-06\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clans of Caledonia\" objecttype=\"thing\" objectid=\"216132\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"129\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"165\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"117\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Κωνσταντίνος\" startposition=\"4\" color=\"\" score=\"134\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33724164\" date=\"2019-02-06\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Quadropolis\" objecttype=\"thing\" objectid=\"176396\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"54\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"55\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"43\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33663752\" date=\"2019-02-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clans of Caledonia\" objecttype=\"thing\" objectid=\"216132\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"68\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"178\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"135\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33642142\" date=\"2019-02-02\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Clans of Caledonia\" objecttype=\"thing\" objectid=\"216132\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"102\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"158\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"33588797\" date=\"2019-01-30\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Concordia\" objecttype=\"thing\" objectid=\"124361\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"100\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"104\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"86\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Κωνσταντίνος\" startposition=\"4\" color=\"\" score=\"95\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"5\" color=\"\" score=\"106\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"33588749\" date=\"2019-01-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Great Western Trail\" objecttype=\"thing\" objectid=\"193738\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"98\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"100\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"55\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33447064\" date=\"2019-01-23\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Concordia\" objecttype=\"thing\" objectid=\"124361\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"106\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"148\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"102\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Κωνσταντίνος\" startposition=\"4\" color=\"\" score=\"88\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33299149\" date=\"2019-01-16\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Great Western Trail\" objecttype=\"thing\" objectid=\"193738\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"44\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"89\" new=\"0\" rating=\"0\" win=\"1\"/>
</players>
</play>
<play id=\"33298405\" date=\"2019-01-16\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Imperial Settlers\" objecttype=\"thing\" objectid=\"154203\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"\" color=\"\" score=\"127\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"\" color=\"\" score=\"75\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33192032\" date=\"2019-01-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Scythe\" objecttype=\"thing\" objectid=\"169786\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"1\" color=\"\" score=\"68\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"2\" color=\"\" score=\"56\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"3\" color=\"\" score=\"38\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"4\" color=\"\" score=\"65\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33188617\" date=\"2019-01-11\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Great Western Trail\" objecttype=\"thing\" objectid=\"193738\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"71\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"76\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"39\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"Aion\" userid=\"797858\" name=\"Andreas Alexiou\" startposition=\"4\" color=\"\" score=\"48\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33014918\" date=\"2019-01-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Trajan\" objecttype=\"thing\" objectid=\"102680\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"129\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"171\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"79\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"4\" color=\"\" score=\"98\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"33018114\" date=\"2019-01-03\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"68\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"79\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"49\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"4\" color=\"\" score=\"42\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32790304\" date=\"2018-12-27\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Railways of the World\" objecttype=\"thing\" objectid=\"17133\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"46\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"67\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"45\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"4\" color=\"\" score=\"35\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Jorge\" startposition=\"5\" color=\"\" score=\"17\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32707539\" date=\"2018-12-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Citadels\" objecttype=\"thing\" objectid=\"478\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"17\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"34\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"3\" color=\"\" score=\"18\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32706196\" date=\"2018-12-24\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Terraforming Mars\" objecttype=\"thing\" objectid=\"167791\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"65\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"80\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"50\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"4\" color=\"\" score=\"50\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32678997\" date=\"2018-12-23\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Castles of Burgundy\" objecttype=\"thing\" objectid=\"84876\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"197\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"2\" color=\"\" score=\"226\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"3\" color=\"\" score=\"183\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32615502\" date=\"2018-12-19\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Scythe\" objecttype=\"thing\" objectid=\"169786\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameintegration\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"77\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"92\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"80\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"kostasloizidis\" userid=\"474669\" name=\"Bob\" startposition=\"4\" color=\"\" score=\"55\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32507517\" date=\"2018-12-12\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Great Western Trail\" objecttype=\"thing\" objectid=\"193738\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"116\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"101\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Αντωνης\" startposition=\"3\" color=\"\" score=\"53\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32394245\" date=\"2018-12-05\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"Lords of Xidit\" objecttype=\"thing\" objectid=\"156566\">
<subtypes>
<subtype value=\"boardgame\"/>
<subtype value=\"boardgameimplementation\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"2\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"4\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Katerina\" startposition=\"3\" color=\"\" score=\"5\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"\" userid=\"0\" name=\"Adelina\" startposition=\"4\" color=\"\" score=\"3\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"\" userid=\"0\" name=\"Κωνσταντίνος\" startposition=\"5\" color=\"\" score=\"1\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
<play id=\"32281691\" date=\"2018-11-28\" quantity=\"1\" length=\"0\" incomplete=\"0\" nowinstats=\"0\" location=\"\">
<item name=\"The Voyages of Marco Polo\" objecttype=\"thing\" objectid=\"171623\">
<subtypes>
<subtype value=\"boardgame\"/>
</subtypes>
</item>
<players>
<player username=\"ddmits\" userid=\"119790\" name=\"Dimitris Dranidis\" startposition=\"1\" color=\"\" score=\"50\" new=\"0\" rating=\"0\" win=\"0\"/>
<player username=\"adranidis\" userid=\"162973\" name=\"Andreas\" startposition=\"2\" color=\"\" score=\"72\" new=\"0\" rating=\"0\" win=\"1\"/>
<player username=\"dimopoulosk\" userid=\"452288\" name=\"Kostas\" startposition=\"3\" color=\"\" score=\"63\" new=\"0\" rating=\"0\" win=\"0\"/>
</players>
</play>
</plays>")
