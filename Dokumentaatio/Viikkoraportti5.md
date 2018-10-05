# Viikkoraportti 4

Tällä viikolla toteutettu suurin osa tietorakenteista. Omina toteutuksina on nyt tehty minimikeko, lista, linkitetty lista ja hajautustaulu.
Ensi viikolle jäljelle jää joukko-tietorakenteen toteuttaminen, jonka pitäisi olla yksinkertainen tehtävä. 

Tietorakenteissa Javan rajapintojen toteuttaminen on koodin laadun kannalta hyvä asia, mutta aiheuttaa turhaa työtä tämän projektin kannalta.
java.util.List-rajapintaa toteuttavia luokkia on kaksi, joista kummastakaan ei projektissa käytetä kaikkia toimintoja, mutta kaikki toiminnot on toteutettuna.
Testiraporteissa pyritään kattavaan rivi- ja mutaatiotestikattavuuteen, joten varsinkin testien kirjoittaminen on hyvin aikaa vievää. Tällä hetkellä testauskattavuudessa ei päästä ihan tavoitelukemiin.

Algoritmeista Bellman-Ford algoritmi on toteutettu, joten määrittelyjen mukaisesti jäljellä on vielä haastavin A*. 

Suorituskykytestausten perusteella näyttäisi siltä, että algoritmeissa ja tietorakenteissa päästään tavoiteltuihin aikavaativuuksiin.
Tarkoitus on projektin aikana vielä toteuttaa järkevämpi automatisoitu suorituskykymittaus. Nyt haasteena mittauksissa on se, että osa algoritmeista kaatuu liian suurilla syötteillä tai suoritus kestää niin pitkään, että se pitää lopeuttaa kesken.
Automatisoidussa suorituskykymittauksessa pitää olla vähintään time-out toteutettuna, jos suoritus kestää liian pitkään. Automatiikalla saadaan paremmin vertailtua eri toimintojen aikavaativuuksia ja saadaan parempi näkyvyys siihen, onko suorituskykytavoiteet todellisuudessa saavutettu. 

Itse pelin käyttöliittymän tai logiikan viimeistelyä ei ole edistetty tällä viikolla. 

Aikaa tällä viikolla käytetty noin 20h. 

Toteutusdokumentin kirjoittaamiseen ei vielä löytynyt aikaa, joten se jää ensi viikolle. 

Yhteenvetona todo-listalla tällä hetkellä:
 - Viimeisen Set-tietorakenteen toteuttaminen
 - A* algoritmin toteuttaminen
 - Pelin käyttöliittymän ja logiikan viimeistely
 - Automatiikka suorituskykyraportointiin
 - Toteutusdokumentaatio

[Javadoc](http://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Shortest-Path-Challenge/build/docs/javadoc/index.html)

[Testausdokumentti, jossa linkki uusimpaan pitest-raporttiin](Testaus.md)

[Checkstyle main](https://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Shortest-Path-Challenge/build/reports/checkstyle/main.html)

[Checkstyle test](https://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Shortest-Path-Challenge/build/reports/checkstyle/test.html)
