# Testausdokumentti
Dokumentin vanhat versiot löytyvät [täälä](Testaustilanne.md)

Sovellus on testattu kattavasti yksikkö- ja mutaatiotestauksilla. Testausraportti löytyy täälä: [Pitest](https://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Dokumentaatio/pitest/201810052224/index.html)

Sovelluksen suorituskyvyn osalta on analysoitu reitinhakualgoritmeja.
Yksittäisten tietorakenteiden (dynaaminen lista, linkitetty lista, joukko, hajautustaulu, minimikeko) tarkempi analysointi ei ollut mielekästä,
koska käytännössä operaatiot suoriutuivat niin nopeasti myös suurilla tietorakenteilla että Java kaatui OutOfMemoryErroriin ennen kuin tuloksia olisi voinut vertailla.

## Algoritmien suorituskyky.
Algoritmien suorituskyky on testattu automatisoidulla mittauksella, jonka voi suorittaa muuttamalla `build.gradle`-tiedostoon parametrin `ext.mainClass = 'com.edii.spc.perf.PerformanceTest'`.

Alla yhden suorituskykymittauksen tuloste.Suorituskykymittauksessa suoritetaan jokainen algoritmi siten että ensin pelikentän koko on väliltä 2-10, ja kokoa kasvatetaan yhdellä testien välissä.
Seuraavaksi testataan pelikentän koot välillä 10-100, ja kokoa kasvatetaan kymmenellä testien välissä.
Seuraavaksi testataan pelikentän koot välillä 100-1000, ja kokoa kasvatetaan sadalla testien välissä.
Lopuksi testataan pelikentän koot välillä 1000-5000, ja kokoa kasvatetaan tuhannella testien välissä. 
Yli 5000 kokoisilla pelikentillä pelkästään pelikentän muodostaminen kestää niin pitkään, ettei testejä ollut järkevää suorittaa. 

## Suorituskykymittauksen tuloste
Näytetään testin 'Ratkaistaan pelikenttä algoritmilla AStarSolver, pelikentän ko'oilla 2-10, askeleen koko 1' tulokset.

Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
------------------- | ------------------- | ------------------- | ------------------- 
10                  | 100                 | 0.00040             | n/a
20                  | 400                 | 0.00148             | 3.66286
30                  | 900                 | 0.00402             | 2.72012
40                  | 1600                | 0.00667             | 1.65686
50                  | 2500                | 0.02035             | 3.05381
60                  | 3600                | 0.01850             | 0.90896
70                  | 4900                | 0.02786             | 1.50565
80                  | 6400                | 0.08182             | 2.93730
90                  | 8100                | 0.07181             | 0.87757



| Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      |
| ------------------- | ------------------- | ------------------- | ------------------- |
| 2                   | 4                   | 0.00532             | n/a                 |
| 3                   | 9                   | 0.00150             | 0.28274             |
| 4                   | 16                  | 0.00145             | 0.96318             |
| 5                   | 25                  | 0.00175             | 1.20844             |
| 6                   | 36                  | 0.00148             | 0.84628             |
| 7                   | 49                  | 0.00183             | 1.23418             |
| 8                   | 64                  | 0.00177             | 0.96868             |
| 9                   | 81                  | 0.00122             | 0.69151             |

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla DijkstraSolver, pelikentän ko'oilla 2-10, askeleen koko 1' tulokset.
|Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      |
|--------------------|---------------------|---------------------|---------------------|
|2                   | 4                   | 0.00023             | n/a                 |
|3                   | 9                   | 0.00017             | 0.71783             |
|4                   | 16                  | 0.00026             | 1.54923             |
|5                   | 25                  | 0.00106             | 4.06093             |
|6                   | 36                  | 0.00053             | 0.49947             |
|7                   | 49                  | 0.00063             | 1.19480             |
|8                   | 64                  | 0.00385             | 6.10094             |
|9                   | 81                  | 0.00085             | 0.22206             |

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla BellmanFordSolver, pelikentän ko'oilla 2-10, askeleen koko 1' tulokset.
Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
--------------------|---------------------|---------------------|---------------------
2                   | 4                   | 0.00017             | n/a
3                   | 9                   | 0.00011             | 0.65031
4                   | 16                  | 0.00496             | 46.21342
5                   | 25                  | 0.00037             | 0.07408
6                   | 36                  | 0.00336             | 9.14608
7                   | 49                  | 0.00159             | 0.47344
8                   | 64                  | 0.00255             | 1.60124
9                   | 81                  | 0.00403             | 1.58273

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla AStarSolver, pelikentän ko'oilla 10-100, askeleen koko 10' tulokset.
Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
--------------------|---------------------|---------------------|---------------------
10                  | 100                 | 0.00701             | n/a
20                  | 400                 | 0.00343             | 0.48928
30                  | 900                 | 0.00762             | 2.22180
40                  | 1600                | 0.01422             | 1.86674
50                  | 2500                | 0.02270             | 1.59596
60                  | 3600                | 0.02187             | 0.96313
70                  | 4900                | 0.04653             | 2.12803
80                  | 6400                | 0.04873             | 1.04730
90                  | 8100                | 0.06039             | 1.23922

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla DijkstraSolver, pelikentän ko'oilla 10-100, askeleen koko 10' tulokset.
Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
--------------------|---------------------|---------------------|---------------------
10                  | 100                 | 0.00047             | n/a
20                  | 400                 | 0.00220             | 4.67406
30                  | 900                 | 0.00594             | 2.70109
40                  | 1600                | 0.00847             | 1.42596
50                  | 2500                | 0.01721             | 2.03118
60                  | 3600                | 0.01755             | 1.02024
70                  | 4900                | 0.02411             | 1.37359
80                  | 6400                | 0.04187             | 1.73656
90                  | 8100                | 0.03772             | 0.90090

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla BellmanFordSolver, pelikentän ko'oilla 10-100, askeleen koko 10' tulokset.
Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
--------------------|---------------------|---------------------|---------------------
10                  | 100                 | 0.00590             | n/a
20                  | 400                 | 0.05646             | 9.56917
30                  | 900                 | 0.16505             | 2.92345
40                  | 1600                | 0.52040             | 3.15293
50                  | 2500                | 1.47100             | 2.82670
60                  | 3600                | 3.16256             | 2.14994
70                  | 4900                | 6.27267             | 1.98342
80                  | 6400                | 10.87772            | 1.73414
90                  | 8100                | 18.38631            | 1.69027

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla AStarSolver, pelikentän ko'oilla 100-1000, askeleen koko 100' tulokset.
Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
--------------------|---------------------|---------------------|---------------------
100                 | 10000               | 0.04309             | n/a
200                 | 40000               | 0.20954             | 4.86323
300                 | 90000               | 0.58008             | 2.76827
400                 | 160000              | 1.17031             | 2.01751
500                 | 250000              | 2.20187             | 1.88144
600                 | 360000              | 3.89910             | 1.77081
700                 | 490000              | 5.11517             | 1.31189
800                 | 640000              | 8.23738             | 1.61038
900                 | 810000              | 9.99976             | 1.21395

Näytetään testin 'Ratkaistaan pelikenttä algoritmilla DijkstraSolver, pelikentän ko'oilla 100-1000, askeleen koko 100' tulokset.
Kentän koko         | Solmujen määrä      | Kesto               | Keston kerroin      
--------------------|---------------------|---------------------|---------------------
100                 | 10000               | 0.04755             | n/a
200                 | 40000               | 0.23301             | 4.90025
300                 | 90000               | 2.00539             | 8.60636
400                 | 160000              | 1.53145             | 0.76367
500                 | 250000              | 2.96299             | 1.93476
600                 | 360000              | 4.99599             | 1.68613
700                 | 490000              | 7.53912             | 1.50903
800                 | 640000              | 11.56973            | 1.53463
900                 | 810000              | 15.99401            | 1.38240


## Suorituskykymittauksen graafinen esitys
