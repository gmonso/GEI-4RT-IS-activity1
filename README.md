# Activitat 1

## Importació del projecte

Per carregar el projecte al vscode, primer executar la següent comanda dins del directori arrel del projecte:

```bash
scala-cli setup-ide .
```

Una vegada executada, ja podrem obrir el projecte al vscode fent, per exemple:

```bash
code .
```

O, si volem usar l'IntelliJ, obrim el directori del projecte.

Generar scaladocs:
```bash
scala-cli doc ./src/main/scala/cat/udl/eps/is/* -d ./ -f
```

## Estructura de l'activitat

L'activitat consta de diferents fitxers que tenen exercicis "paral·lels" als que hem fet, especialment del tema 4 (els temes anteriors són de conceptes inicials o presentació del llenguatge de programació)

1. Tipus llista (amb la definció que hem fet amb un enum)
2. Tipus llista de la llibreria estàndard
3. Arbres, en aquest cas, arbres binaris de cerca

Per a cada escun dels apartats, hi ha un fitxer que conté el codi bàsic, que s'ha de completar, i un esquelet de joc de proves, que prova alguna de les funcions que us demanem (joc que proves que, òbviament, podeu ampliar)

NOTA: Per evitar problemes d'indentació, he usat la notació d'scala que usa claus.

### List

#### partition

Donada una llista i un predicat, retorna un parell de llistes, la primera de les quals conté els elements que compleixen el predicat, i la segona, els que no el compleixen.

Podeu fer vàries versions (que podeu denominar: partition, partition2, partition3, etc):

* recursivitat directa
* recursivitat final
* foldRight
* foldLeft

L'objectiu és "jugar" amb totes le possibilitats que hi ha per entendre millor com lligen les recursivitats, els folds, els ordres dels elements a les llistes, etc, etc.

NOTA: Penseu, p.e. en les funcions que ja teniu definides com `reverse`. 

#### digitsToNum

Donada una llista amb els dígits d'un número retornar el valor numèric (com enter, d'aquest número).

En aquesta primeta versió suposarem que la llista només conté dígots, és a dir, que tots els valor que conté són enters al rang [0, 9].

Feu dues versions:

* recursiva (ja decidireu si directa o amb alguna funció auxiliar per a que sigui recursiva final)
* fold (ja decidireu de quina mena)

NOTA: Passar a String i després fer un toInt no és la solució que busquem, encara que pot servir d'inspiració.

#### insertion sort

Ordenar la llista per inserció. La funció que passem com a paràmetre indica si dos elements estàn ordenats.

PISTA: Haureu de definir una funció auxiliar per inserir de forma ordenada un element en una llista (que ja està ordenada).

#### merge sorted

Fusiona dues llistes ordenades en una de sola (que també ho estarà). També passem com a aparàmetre el criteri d'ordenació.

Per aquesta funció no hi ha cap funció de test, per tant, cal que l'escriviu vosaltres.

NOTA: Suposarem que és cert que les dues llistes estan ordenades per aquest criteri d'ordenació.

#### check sorted

Donada una llista i una funció que indica el criteri d'ordenació, comprovar que realment la llista està ordenat per aquest criteri.

#### find

Donada una llista i un predicat, retornes el primer element de la llista que compleix el predicat. Com podria ser que no hi hagués cap, utilitzem el tipus Some.

NOTA: Aquest exercici requereix el tema 5

#### partition map

Semblant al particion, però en comptes de tenir un booleà que m'indica a quin lloc van a parar els elements originals, tinc una funció que els transforma en dues variants (representades pel tipus Either)

NOTA: Aquest exercici requereix el tema 5

#### digits to num (amb errors via Option)

Ara haurem de controlar que els elements de la llista siguien realment dígits. Per això usarem el resultat dins d'un Option de manera que si algun element no ho és, retornarem None.

NOTA: Aquest exercici requereix el tema 5

#### digits to num (amb errors via Either)

Com abans però ara amb un resultat Either[Int, Int], on el tipus de l'esquerra indicarà l'element de la llista que no era un dígit.

NOTA: Aquest exercici requereix el tema 5

### StdList

Aquests exercicis també van sobre llistes, però usen les predefinides en scala.

Per la documentació sobre les llistes podeu consultar la [documentació de la llibreria estàndard de scala](https://www.scala-lang.org/api/3.3.3/)

Al principi del fitxer StdLib, teniu una llista de mètodes amb els que és important que us familieritzeu abans de fer els exercicis. 

Fixeu-vos, que hi ha mètodes que només existeixen per algunes instanciacions dels tipus genèrics. Per exemple, el mètode sum, està definit a les llistes de enters (i retorna un enter), però no podem fer un sum d'una llista d'strings (i falla a nivell de compilació).

Com sempre, **proveu de fer vàries versions**: l'objectiu és ser fluïts a l'hora de pensar solucions.

Abans de fer-les, implementeu jocs de prova per a provar-les.

#### count lengths

Ha de retornar un mapa (Map) en el que, per a cada longitud, hi hagi el nombre de paraules amb aquesta longitud.

#### sum first powers of b

Fa la suma b^0 + b^1 + b^2 + ... + b^(n-1)

No feu servir math.pow doncs retorna un double i això provoca problemes d'arrodoniment.

### BST

Aquest exercici presenta un ADT que representa els arbres binaris de cerca.

Per simplificar algunes coses, i no haver d'introduir més elements de Scala com les typeclasses i els paràmetres implícits, farem que els mètodes que ho requereixin, rebin la funció que indica l'ordre sobre els elements de l'arbre com a paràmetre.

#### insert

Fixeu-vos que és un mètode (l'arbre sobre el que s'aplica és this que és de tipus BST[A]) i rep l'element que es vol inserir (que és de tipus B, amb R restringit als supertipus de A) i es rep la funció que indica si dos elements de tipus B estan ben ordenats, per a moure's dins de l'arbre.  El resultat és un nou BST[B] que, òbviament, compartirà parts dels nodes amb l'arbre de partida. SI l'element ja es trobava a l'arbre, aquest queda igual.

Tal com s'explica al document que us adjunto (Laboratori 4 d'estructures de dades del curs 2022/2023) a l'inserir es va fent una còpia del camí des del node arrel fins al punt d'inserció. No cal que optimitzeu el cas en què, en un punt del camí detecteu que l'element hi és i realment no calia copiar res. Això es comenta a l'apartat de tasques complementàries de la pràctica.

#### find

Indica si l'element buscat es troba a l'arbre.

#### fold

Versió del fold per aquest ADT.

#### fromList

Retorna un arbre construït per la inserció de tots els elements de la llista.

Com a llistes, feu servir les de la biblioteca stàndard de scala, amb les que us heu familiaritzat a l'apartat anterior.

#### inorder

Recorregut en inordre de l'arbre.

Com a llistes, feu servir les de la biblioteca stàndard de scala, amb les que us heu familiaritzat a l'apartat anterior.

### D'Hondt

Finalment, us presento un problema complex que resoldrem funcionalment. Una mica com a les pràctiques de primer, us presentaré una possible descomposició del problema en subproblema, i cadascun d'aquests apartats serà la tasca a resoldre.

Com el nom de l'apartat indica, el problema a resoldre és el de repartir els escons en una circumscripció, donats els vots a cada partit. Per simplificar, no considerarem ni els vots en blanc ni els nuls, i ja harem filtrat les candidatures que no arriben al mínim percentatge de vots.

La descripció de cada mètode la trobareu al fitxer de partida i, al test, els resultats que va haver-hi a la circumscripció de Lleida i el repartiment resultant.

A l'enllaç [Procediment Electoral](https://www.parlament.cat/pcat/parlament/que-es-el-parlament/procediment-electoral/), concretament a la secció **El resultat electoral** en trobareu informació

A l'apartat anterior teniu una llista de mètodes que poden ser interessants.

NOTA: Altres descomposicions són possibles i podeu fer dissenys alternatius. Com més coses proves, més aprendreu.

## Consells de realització

L'ordre suggerit de resolució és el següent és el suggerit al readme (exepte per les parts que requereixen del tema 5).

No cal que us preocupeu inicialment d'aspectes d'eficiència o de si la solució es *stack-safe* o no, però es valorarà que resoleu un mateix problema de diferents maneres amb diferents característiques.

En alguns casos indico que la solució que busquem es usant p.e. un `foldRight`. Si no trobeu aquesta solució, podeu fer-ne una diferent.

Com hem fet a classe, el procediment a seguir pot ser:

* solució evident/intuïtiva (possiblement via pattern-matching i/o recursivitat explícita)
* solució usant altres combinadors
* anàlisi de la/les solució/ns anteriors en termes d'eficiència, stack-safeness, etc.
* proposta d'altres solucions

Per cada fitxer he creat una petita classe de proves perquè podeu afegir tests que comprovin el funcionament del vostre codi.

També podeu usar worksheets per a fer-ho interactivament (teniu un exemple de worksheet al directori `worksheets`)

Si voleu saber una mica més les possibilitats de la biblioteca de tests, podeu consultar la seva documentació a [munit homepage](https://scalameta.org/munit/)

## Què heu d'entregar

* Projecte amb la resolució dels exercicis, amb els tests o workflows que heu afegit.

  * Per poder tenir diverses solucions del mateix problema podeu donar-los noms numerats (p. ex. `partition`, `partition2`, `partition3`, etc.)

* Informe, en PDF, explicant el procés de resolució de cada problema
