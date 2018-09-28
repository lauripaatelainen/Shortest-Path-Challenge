# Testausdokumentti

## Yksikkötestaus
Sovelluksen yksikkötestausta koskeva ajantasainen raportti löytyy täältä: *linkki*

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
100         | 10000          | 0.106s   | n/a
200         | 40000          | 0.276s   | 2.604
400         | 160000         | 1.007s   | 3.649
800         | 640000         | 3.621s   | 3.596
1600        | 2560000        | 16.406s  | 4.531
3200        | 10240000       | 107.923s | 6.578
6400        | 40960000       | xxx      | xxx
