# Testausdokumentti

## Yksikkötestaus
Sovelluksen yksikkötestausta koskeva ajantasainen raportti löytyy täältä: [Pitest](https://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Dokumentaatio/pitest/201810052224/index.html)

## Suorituskykytestaus
Tavoitteena on päästä algoritemeissa ja tietorakenteissa optimaalisiin O-aikavaatimuksiin.
Näitä ei ole järkevää automatisoida yksikkötestauksilla, vaan sitä varten sovellukseen on tehty erillinen pääohjelma com.edii.spc.perf.PerformanceTest, jonka voi ajaa muuttamalla mainClass-parametrin build.gradle -tiedostoon. 

### Tilanne 28.9.2018:
Omia tietorakenteita (minimikeko, joukko, hajautustaulu, lista) ei ole vielä toteutettu, vaan luokat perivät Javan valmiit toteutukset sellaisenaan, joten suorituskykylukuja ei kannata tähän vielä laittaa. 

Lyhimmän polun löytävistä algoritmeista on toteutettu Dijkstran algoritmi, jonka aikavaativuus määritelmän mukaan on O(V log V), jossa V on verkon solmujen määrä.
Alla on taulukoitu yhdestä suorituskykytestin ajokerrasta saatuja lukuja. Uudella rivillä pelikentän sivun koko tuplaantuu, eli solmujen määrä nelinkertaistuu koska pelikenttä on neliön muotoinen. 
Kentän koolla 6400 reilut 1034 sekuntia (reilut 17 minuuttia) ja lopulta kaatui.
*Huom.* näissä tuloksissa Dijkstran algoritmissa oleva käsiteltävien solmujen tietorakenne on vielä toteuttamatta, ja taustalla käytetään Javan ArrayListiä, joka joudutaan sorttaamaan usein. 
Paremmat testitulokset saadaan kun tuo tietorakenne on toteutettu optimaalisena minimikekona. 

Kentän koko | Solmujen määrä | Kesto    | Keston kerroin
------------|----------------|----------|---------------
100         | 10000          | 0.106s   | n/a
200         | 40000          | 0.276s   | 2.604
400         | 160000         | 1.007s   | 3.649
800         | 640000         | 3.621s   | 3.596
1600        | 2560000        | 16.406s  | 4.531
3200        | 10240000       | 107.923s | 6.578
6400        | 40960000       | xxx      | xxx

### Tilanne 3.10.2018
Dijkstran algoritmia nopeutti huomattavasti, kun käsiteltävien solmujen tietorakenne toteutettiin tehokkaammalla MinHeap-tietorakenteella. 
Eron huomaa kentän koolla 3200, jonka ratkaiseminen Dijkstran algoritmilla nopeutui vajaasta kahdesta minuutista alle sekuntiin.
6400 kokoisen kentän ratkaisu ei edelleenkään onnistu. Tarkempi tilanteen analysointi Netbeansin profilerilla paljastaa että näissä määrissä suurin osa prosessoriajasta kuluu Javan garbage collectoriin. 

Kentän koko | Solmujen määrä | Kesto    | Keston kerroin
------------|----------------|----------|---------------
100         | 10000          | 0.040s   | n/a
200         | 40000          | 0.029s   | 0.725
400         | 160000         | 0.038s   | 1.310
800         | 640000         | 0.068s   | 1.789
1600        | 2560000        | 0.192s   | 2.824
3200        | 10240000       | 0.705s   | 3.672
6400        | 40960000       | xxx      | xxx

Oman MinHeap-tietorakenteen toteutuksen suorituskykyä mitattaessa Javan garbage collector tulee vastaan, eikä sen takia mittaustuloksia ole järkevää taulukoida.
Suorituskykytestiä ajettaessa insert-toiminnon kesto on alle 0 minuuttia jokaisella kerralla, paitsi silloin kun garbage collector käynnistyy, jolloin kesto alkaa kasvaa yli kymmenen sekunnin, kun tietorakenteen koko on kymmeniä miljoonia.
Pienemmissä data-määrissä mittausten perusteella voidaan kuitenkin todeta, että suorituskyky on kohdallaan. Jos toteutuksessa olisi jotain perusteellista laatua olevaa ongelmaa, alkaisi insert-toiminnon kesto kasvamaan myös niissä iteraatioissa, milloin garbage collectoria ei ajeta.

Garbage collectorin tiheyttä pyrin rajoittamaan allokoimalla jo testin alkuun tarpeeksi suuret taulukot.
Tässä vaiheessa käytössä oli vielä Javan standardit ArrayListit ja HashMapit, joiden konstruktoreille voi antaa initialCapacity-parametrin tietorakenteen allokoimista varten.
Tästä ei kuitenkaan ollut apuja.
Myöhemmin voi vielä kokeilla auttaako, jos ohjelmaa käynnistäessä antaa JVM-parametreja, joilla Javan muistinkäyttöä voi hallita. 

### Tilanne 5.10.2018

Bellman-Ford algoritmi on nyt toteutettu. Testituloksista huomataan, että sen suorituskyky on huomattavasti heikompi kuin Dijkstran algoritmin tässä tapauksessa, kun haetaan polkua yhdestä alkupisteestä yhteen loppupisteeseen.
Alla taulukoituna yhden testikierroksen tuloksia

Kentän koko | Solmujen määrä | Kesto   | Keston kerroin
------------|----------------|---------|---------------
100         | 10000          | 1.787   | n/a
200         | 40000          | 8.226   | 4.603
400         | 160000         | 39.924  | 4.853
800         | 640000         | 203.841 | 5.106
1600        | 2560000        | 993.671 | 4.875
3200        | 10240000       | xxx     | xxx

Tätä pidemmälle ei ollut mielekästä mittaroida tuloksia. Jos sama kaava olisi jatkunut, olisi kesto ollut noin puolitoista tuntia, todennäköisesti pidempään. 

Bellman-Ford algoritmin aikavaativuuden pitäisi olla O(V*E), jossa V on solmujen määrä ja E on kaarien määrä, kun taas Dijkstran algoritmin aikavaativuudessa päästään O(V log V). 
Tämän kyseisen pelin tapauksessa V ja E ovat suoraan laskettavista pelikentän koosta. Jos pelikentän kokoa merkitään kirjaimella s, niin V ja E arvota saadaan seuraavasti:

V = s^2
E = 4 * s * (s - 1) = 4*s^2 - 4*s

Eli sekä V, että E ovat korvattavissa merkinnällä O(s^2)

Jos Dijkstran ja Bellman-Ford algoritmien aikavaativuuden muuttaa muotoihin, jotka riippuvat pelikentän koosta s, 

Dijkstra: O(S^2 * log (S^2)) => O(S^3)
Bellman-Ford: O(S^2 * S^2) => O(S^4)

Tähän muotoon muutetuista aikavaativuuksista on selvää, että Bellman-Fordin suoritus kestää pidempään. 

### Tilanne 12.10.2018

Kuluneen viikon aikana A* toteutuksen myötä toteutettiin ristiinvertailutesti, jolla oli tarkoitus varmistaa että kaikki algoritmit palauttavat saman tuloksen (tarkemmin saman painoisen tuloksen), ja voidaan varmistua siitä että kaikki algoritmit todella löytävät lyhimmän reitin, eikä vain jotain reittiä.  Ristiinvertailun tuloksena löytyi hankalasti löydettävä bugi, joka liittyi pelikentän solmun .equals()-metodin oikeellisuuteen. Kun pelikentän koko oli noin 24, equals() alkoi antamaan "false positive" tuloksia, jonka seurauksena algoritmit eivät ole aiemmin palauttaneet täysin oikeita tuloksia. Vian korjauksen jälkeen algoritmien kesto pidentyi, joten ylläolevat aiempien viikkojen mittaustulokset eivät ole oikeellisia. 

Suoritukseen ja tietorakenteisiin liittyvä koodi on valmista ja kattavasti testattua. Lisäksi kuluvan viikon aikana on tehty mekanismi tarkempien mittaustulosten saamiseen, joka vaatii vielä pieniä hiomisia. Ensi viikona aikana testausdokumentti päivittyy lopulliseen muotoonsa.
