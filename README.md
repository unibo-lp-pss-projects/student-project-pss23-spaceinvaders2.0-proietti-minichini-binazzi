# SpaceInvaders-2.0
## Analisi 

Il progetto consiste nella realizzazione di un software basato su Java che consente ai giocatori di divertirsi con Space Invaders, uscito per la prima volta in Giappone nel 1978, in uno stile moderno, riuscendo allo stesso tempo a conservare quello originale. 

Il software Space Invaders dovrà permettere all’utente di giocare una partita in cui l’obiettivo è quello di distruggere un’orda di alieni che, partendo dalla parte alta dello schermo, scenderanno progressivamente verso il basso. Per poter vincere, il giocatore avrà ha disposizione una navicella spaziale con cui sparare ai nemici. Una volta eliminati gli alieni, verrà generata una entità chiamata Boss, la quale, come un alieno, sarà in grado di spostarsi verso destra e sinistra dello schermo, ma in più avrà la capacità di sparare dei proiettili, in modo da rendere più difficile il completamento del gioco. 

## Requisiti 

L’applicazione avrà dei requisiti fondamentali: 

- La presenza di una schermata detta Menù, che verrà visualizzata all’avvio del software. In questa finestra saranno presenti 3 opzioni tra cui scegliere: i comandi di gioco, ossia le informazioni principali su come giocare, la possibilità di decidere se cominciare il gioco vero e proprio o se chiudere il software 

- Se selezionata l’opzione di inizio gioco, una musica di sottofondo verrà riprodotta all’infinito, dunque in loop, per poter creare un’atmosfera coinvolgente 

- Saranno  fin da subito visibili l’orda di alieni e la navicella spaziale del giocatore. 

- Per poter iniziare, l’utente dovrà premere un tasto qualunque della tastiera 

- Una volta cominciata la partita, gli alieni saranno in grado di muoversi verso destra e sinistra dello schermo, cominciando a scendere gradualmente verso la parte bassa di esso, ossia verso il giocatore 

- L’utente dovrà pilotare la navicella spaziale posta nella parte bassa dello schermo e sarà in grado di muoversi verso destra e sinistra di esso. Inoltre, premendo un apposito tasto, l’utente avrà l’abilità di sparare proiettili per colpire i nemici  

- Ogni qual volta l’utente separerà un proiettile, sarà possibile sentirne il suono, per poter ulteriormente calare il giocatore nel clima di gioco 

- Una volta distrutta l’orda di alieni, l’entità Boss apparirà nella parte alta dello schermo. Anche quest'ultima avrà la capacità di muoversi verso destra e sinistra dello schermo, con la differenza che non sarà in grado di scendere verso il giocatore. Inoltre, come abilità aggiuntiva, il Boss potrà sparare proiettili, esattamente come la navicella spaziale del giocatore. Perciò, la sfida dell’utente sarà quella di colpire il Boss ed evitare a sua volta di essere colpito 

- Se il giocatore riuscirà a completare l’obiettivo finale, verrà visualizzato un messaggio che comunicherà la vittoria. Al contrario, se il giocatore verrà colpito o non riuscirà ad eliminare tutti gli alieni prima che essi lo raggiungano, sarà reso visibile un messaggio per annunciare la sconfitta

## Analisi e modello del dominio 
[Grafico 1]
Space Invaders permetterà al giocatore di pilotare una navicella spaziale con lo scopo di distruggere prima un’orda di alieni, poi un Boss, ossia un singolo alieno. Per fare ciò, avrà l’abilità di sparare proiettili, ma non sarà l’unico. Infatti, anche il Boss avrà la stessa capacità. L’orda di alieni viene identificata come un alieno singolo moltiplicato varie volte, dunque prende il nome di “AlienEntity”. Il giocatore, rappresentato con la navicella spaziale, viene definito come “ShipEntity”. I proiettili, anch’essi riconosciuti come una singola entità generata ogni qual volta il giocatore prema il relativo tasto sulla tastiera, prendono il nome di “ShotEntity”. Le entità di gioco presentano ciascuna un’immagine che le identifica, che viene detta “Sprite”. Più precisamente, per distinguere lo sparo del giocatore da quello del boss, sono state scelte due immagini differenti e ben distinguibili. Similmente, l'immagine identificativa del boss differisce da quella usata per rappresentare l'orde di alieni iniziale. La Sprite è infatti conservata all’interno di “Entity”, che rappresenta qualsiasi elemento presente nel software, in modo da poter essere utilizzata dalle varie entità. 

Oltre a queste entità principali, abbiamo anche identificato “Game”, che funge da mediatore centrale per la logica di gioco e la gestione dell'interfaccia utente. Esso coordina il ciclo di gioco, il rendering grafico, la gestione degli input utente e il controllo degli eventi di gioco. Inoltre, è presente una funzionalità interna a Game detta “KeyInputHandler”, che consente al giocatore di avviare il gioco e muovere la navicella attraverso i tasti della tastiera, in modo da raggiungere il suo obiettivo. 

[Grafico 1]

```mermaid
classDiagram
    Entity <|-- AlienEntity
    Entity <|-- ShipEntity
    Entity <|-- ShotEntity
    Entity <|-- BossEntity
    Game --* Entity
    Entity --> Sprite
    Game -- KeyInputHandler : inner class
    
    class Entity {
        - x: double
        - y: double
        - sprite: Sprite
        - dx: double
        - dy: double
        + move(delta: long): void
        + draw(g: Graphics): void
        + collidesWith(other: Entity): boolean
        + collidedWith(other: Entity): void
    }

    class Sprite {
        - image: Image
        + Sprite(image: Image)
        + getWidth(): int
        + getHeight(): int
        + draw(g: Graphics, x: int, y: int): void
    }
    class AlienEntity {
        - moveSpeed: double
        - game: Game
        + AlienEntity(game: Game, ref: String, x: int, y: int)
        + move(delta: long): void
        + doLogic(): void
        + collidedWith(other: Entity): void
    }
    class ShipEntity {
        - game: Game
        + ShipEntity(game: Game, ref: String, x: int, y: int)
        + move(delta: long): void
        + collidedWith(other: Entity): void
    }
    class ShotEntity {
        - moveSpeed: double
        - game: Game
        - used: boolean
        + ShotEntity(game: Game, sprite: String, x: int, y: int)
        + move(delta: long): void
        + collidedWith(other: Entity): void
    }
    class BossEntity {
        + BossEntity(game: Game, sprite: String, x: int, y: int)
        + move(delta: long): void
        + fire(): void
        + collidedWith(other: Entity): void
        + draw(g: Graphics2D): void
    }

    class Game {
        - BufferStrategy strategy
        - List<Entity> entities
        - Entity ship
        - BossEntity boss
        - boolean gameRunning
        - boolean waitingForKeyPress
        + showNotificationPanel()
        + Game()
        + startGame()
        + initEntities()
        + initBoss()
        + gameLoop()
    }

    class KeyInputHandler {
        - pressCount: int
        + keyPressed(e: KeyEvent): void
        + keyReleased(e: KeyEvent): void
        + keyTyped(e: KeyEvent): void
    }

```

[Grafico 2] 
L'elemento che si apre una volta avviato il software è il Menù, che viene definito "MenuPage". Esso ha 3 opzioni, ognuna contenente una funzione; una di queste mostra i comandi di gioco, un'altra chiude la finestra del menù mentre l'ultima apre la schermata di gioco, richiamando un metodo in. Per quanto riguarda la terza opzione, il Menù è dunque collegato a Game, in modo tale che venga avviato il gioco correttamente. 
Inoltre, sia Game che MenuPage, contengono un riferimento ad "AudioStore" e "AudioClip", che gestiscono gli effetti sonori presenti all'interno del software. Il primo si occupa di gestire le risorse audio memorizzando gli AudioClip caricati per evitare di doverli ricaricare molteplici volte. Il secondo, invece, rappresenta un clip audio, ossia un suono caricato dall'AudioStore, utilizzato per creare un miglior ambiente di gioco. Allo stesso modo, è presente “SpriteStore”, che funge da amministratore delle risorse grafiche del gioco, fornendo un’interfaccia per ottenere le immagini necessarie, ossia le Sprite.

[Grafico 2]
```mermaid
classDiagram
    MenuPage --> Game
    AudioStore --> AudioClip
    SpriteStore --> Sprite
    Game --> AudioStore
    Game --> AudioClip
    MenuPage --> AudioStore
    MenuPage --> AudioClip

    class MenuPage {
        - AudioStore audioStore
        - AudioClip menuMusic
        + MenuPage()
    }

    class Game {
        - BufferStrategy strategy
        - List<Entity> entities
        - Entity ship
        - BossEntity boss
        - boolean gameRunning
        - boolean waitingForKeyPress
        + showNotificationPanel()
        + Game()
        + startGame()
        + initEntities()
        + initBoss()
        + gameLoop()
    }

    class AudioStore {
        - AudioStore SINGLE_STORE
        - HashMap<String, AudioClip> audioClips
        + AudioStore()
        + get(): AudioStore
        + getAudio(ref: String): AudioClip
        + fail(message: String): void
    }

    class AudioClip {
        - Clip clip
        + AudioClip()
        + getClip()
        + play(): void
        + loop(): void
        + stop(): void
    }

    class Sprite {
        - image: Image
        + Sprite(image: Image)
        + getWidth(): int
        + getHeight(): int
        + draw(g: Graphics, x: int, y: int): void
    }

    class SpriteStore {
        - SpriteStore SINGLE_STORE
        - HashMap<String, Sprite> sprites
        + get(): SpriteStore
        + SpriteStore()
        + getSprite(ref: String): Sprite
        + fail(message: String): void
    }
```

## Design  
### Architettura  

Il design di Space Invaders è un'icona intramontabile nel mondo dei videogiochi, per cui abbiamo mantenuto una grafica classica, che rende il gioco semplice ed efficace. Le entità vengono visualizzate tramite un file gif mantenendo la tipica forma “di pixel” e gestite dalla classe “SpriteStore”, la quale funge da amministratore delle risorse grafiche del gioco, fornendo un’interfaccia per ottenere gli sprite necessari. Più precisamente, SpriteStore utilizza una mappa di hash per memorizzare gli sprite caricati al fine di evitare di ricaricarli più volte. Tale classe, dunque, lavora in maniera coordinata con la classe “Sprite”, la quale rappresenta un singolo sprite contenente solo l’immagine senza alcuna informazione sul suo stato, oltre ad altezza e larghezza. Perciò, quando SpriteStore viene richiamato per ottenere uno sprite, utilizza l’oggetto Sprite per creare un nuovo sprite se non è già presente nella sua cache. Di seguito lo schema UML per dare maggiore chiarezza: 
```mermaid
classDiagram
    class Sprite {
        - Image image
        + Sprite(image: Image)
        + getWidth(): int
        + getHeight(): int
        + draw(g: Graphics, x: int, y: int): void
    }

    class SpriteStore {
        - static final SINGLE_STORE: SpriteStore
        - sprites: HashMap<String, Sprite>
        + SpriteStore()
        + get(): SpriteStore
        + getSprite(ref: String): Sprite
        - fail(message: String): void
    }

    SpriteStore --|> Sprite

```

In maniera simile vengono gestiti gli effetti sonori del gioco attraverso le classi “AudioClip” e “AudioStore”: AudioStore funge da gestore delle risorse audio, fornendo un'interfaccia per ottenere gli AudioClip necessari e, similarmente a SpriteStore, utilizza una mappa di hash per memorizzare gli AudioClip caricati al fine di evitare di ricaricarli più volte, mentre AudioClip rappresenta un clip audio nel gioco e fornisce metodi per il controllo della riproduzione; Inoltre utilizza la classe Clip dell'API Java Sound per la riproduzione audio. Quindi AudioClip e AudioStore sono direttamente collegati con la classe Game, nella quale viene gestita l’intera logica del gioco, e con MenuPage dato che, all’inizio di ogni partita e mentre viene visualizzata la finestra iniziale di gioco, viene riprodotta in loop una musica di background (una relativa al menù e una al gioco vero e proprio). 

Il componente architetturale fondamentale di Spase Invaders è la classe “MenuPage”. Essa, infatti, è la prima finestra visibile al giocatore all’avvio del gioco ed è composta da tre bottoni che gestiscono rispettivamente: un piccolo menù illustrativo contenente i comandi principali, la chiusura della finestra di menù e l’avvio del gioco. A pari importanza vi è la classe Game il cui ruolo principale è quello della gestione della logica di gioco e che dipende da MenuPage in quanto, senza di essa, il gioco non potrebbe iniziare. Game quindi:  

- istanzia le varie entità presenti nel gioco come quella del boss, dell’orda aliena nemica e della navicella del giocatore con i relativi effetti sonori
- gestisce eventi come la pressione di un tasto della tastiera per permettere, per esempio, il movimento della navicella o lo sparo da parte del giocatore 

Ultimo componente architetturale, ma non per importanza, utilizzato dal software è la classe “Entity”, la quale rappresenta qualsiasi elemento che appare nel gioco. Uno dei suoi ruoli principali è quello della gestione risoluzione delle collisioni e del movimento implementando vari metodi. La classe Entity, infine, viene estesa in altre quattro classi per poter gestire in maniera indipendente le diverse entità presenti nel gioco riutilizzando i metodi “base” in essa implementati e aggiungendone dei nuovi, ove necessario. 

### Design dettagliato

– BossEntity (Martina) 

Problema: Nel contesto del gioco, ci si è trovati di fronte alla sfida di gestire il movimento del boss all'interno dello schermo e la sua capacità di sparare proiettili verso il giocatore, assicurando allo stesso tempo che il boss rimanga all'interno dei limiti dello schermo e che i proiettili vengano generati e sparati correttamente nella direzione desiderata. 

Soluzione: Per risolvere questo problema, si è fatto uso di due pattern di progettazione: il pattern Strategy per gestire il comportamento di sparare del boss e il pattern Template Method per definire il movimento del boss all'interno dello schermo. Più nello specifico, 

- Strategy Pattern: Il comportamento di sparare del boss è stato gestito tramite il metodo fire(), il quale crea e lancia un proiettile nella direzione desiderata e utilizzando un'interfaccia “FireStrategy” che rappresenta la strategia di sparare. L'interfaccia definisce il contratto per tutte le strategie di sparare implementate dalle classi concrete. Il metodo ha incapsulato il comportamento di sparare, consentendo una facile sostituzione dell'implementazione senza dover modificare il codice del boss stesso. L'utilizzo di questo pattern ha favorito la modularità e l'estensibilità del codice, permettendo di modificare il comportamento di sparare senza impattare sul resto del sistema di gioco. 
- Template Method Pattern: Il movimento del boss all'interno dello schermo è stato gestito tramite il metodo template “move()” nella classe “BossEntity”, che rappresenta un esempio di Template Method. Questo metodo ha fornito uno scheletro per il movimento del boss, lasciando alle sottoclassi il compito di implementare i dettagli specifici del movimento. Il Template Method Pattern ha permesso una maggiore flessibilità nel gestire il movimento del boss, facilitando eventuali modifiche future senza influire sul comportamento di altre entità nel gioco. 

L'utilizzo dell'interfaccia FireStrategy per il pattern Strategy e del metodo template move() per il pattern Template Method ha consentito di separare le responsabilità legate al comportamento di sparare e al movimento del boss, migliorando la manutenibilità e l'estensibilità del codice. In particolare, l'incapsulamento del comportamento di sparare tramite l'interfaccia FireStrategy ha reso più semplice l'aggiunta di nuovi tipi di attacchi del boss senza dover modificare direttamente la classe del boss. Allo stesso modo, l'utilizzo del metodo template move() ha permesso di adattare facilmente il comportamento del boss a eventuali cambiamenti nel design del gioco.  

Oltre all'utilizzo dei pattern di progettazione, è stato sfruttato il riuso del codice presente nella classe Entity. Quest'ultima fornisce funzionalità di base per tutte le entità del gioco, come il movimento, il disegno e la gestione delle collisioni. Il riuso del codice ha contribuito a ridurre la duplicazione e a migliorare la manutenibilità del sistema. L'implementazione dei pattern Strategy e Template Method, insieme al riuso del codice presente nella classe Entity, ha permesso dunque di gestire in modo efficace il comportamento di sparare e il movimento del boss nel contesto del gioco. Questo approccio ha favorito la modularità, l'estensibilità e la manutenibilità del codice, consentendo di adattare facilmente il comportamento del boss e di mantenere una buona struttura del sistema di gioco nel suo complesso. Di seguito la rappresentazione UML dell’applicazione dei due pattern. 

```mermaid
classDiagram
    class Entity {
        +move(delta: long) : void
        +draw(g: Graphics2D) : void
        +collidedWith(other: Entity) : void
        +doLogic() : void
    }
    
    class BossEntity {
        +BossEntity(game: Game, sprite: String, x: int, y: int)
        +move(delta: long) : void
        +fire() : void
        +draw(g: Graphics2D) : void
        +collidedWith(other: Entity) : void
        +doLogic() : void
    }

    class ShotEntity {
        -moveSpeed: double
        +ShotEntity(game: Game, sprite: String, x: int, y: int)
        +move(delta: long) : void
        +collidedWith(other: Entity) : void
    }

    Entity <|-- BossEntity
    Entity <|-- ShotEntity

```
Il diagramma UML mostra la classe BossEntity che gestisce il comportamento del boss nel gioco. La classe ha un'associazione con la classe Game e contiene i metodi per il movimento, l'attacco e la gestione delle collisioni del boss. 

– MenuPage (Michela) 

Problema: Nel progettare la schermata di menù, si sono riscontrate diverse difficoltà. Innanzitutto, capire il corretto funzionamento e implementazione degli elementi che compongono il menù, ovvero i pulsanti. In secondo luogo, passare dal menù alla finestra di gioco attraverso l’uso di uno dei bottoni in modo regolare.

Dato che ciascun pulsante, se premuto, esegue una certa azione, far sì che quest’ultime vengano correttamente effettuate è stata la sfida principale. Di fatto, le difficoltà maggiori sono state riscontrate precisamente nel primo e nel terzo pulsante.  
Il primo è responsabile di far comparire sotto di esso una striga di testo che funge da guida per il giocatore, spiegandogli quali tasti della tastiera devono essere premuti per giocare. Il terzo, invece, svolge il compito più importante. Esso si occupa di avviare la schermata di gioco per poter consentire all’utente di iniziare la partita. Ciò rappresentò la sfida più complessa, poiché nei vari tentativi effettuati, il collegamento tra MenuPage e Game non era sempre eccellente. 
Più precisamente, i problemi furono: 
- Il terzo pulsante del menù apre la schermata di gioco ma, né suono né parte grafica, vengono correttamente visualizzate. La finestra di gioco appare completamente bianca. 
- Il terzo pulsante apre il gioco consentendo all’utente di visualizzarne gli elementi, ma i tasti della sua tastiera non sono funzionanti, ovvero non vengono letti correttamente come input per poter consentire al giocatore di muovere la navicella e sparare. Ciò non permette nemmeno agli alieni di muoversi nello schermo

Soluzione: Gli approcci iniziali al problema furono diversi. Per quanto riguarda il corretto funzionamento dei primi pulsanti e della loro generale implementazione, che comprende dimensione, colore e posizione, la soluzione fu semplice. Bastò semplicemente documentarsi online sull’uso di metodi come ad esempio “.setFont”, “.setForeground” e “.setLayout” e scegliere i più efficaci.  

Per far funzionare correttamente l’ultimo pulsante, invece, come approccio iniziale era stata aggiunta la schermata di menù all’interno di Game, per fare in modo che si aprisse come primo elemento per poi essere chiusa e lasciare il posto alla finestra di gioco. Dopo diversi tentativi, il menù fu creato come classe a sé stante, dunque al di fuori di classi già esistenti. In questo modo, non solo veniva aperto come primo elemento in modo regolare, ma fu più facile gestirne l’aspetto e le funzionalità. Inoltre, fu sfruttata JFrame, una classe di Java Swing che rappresenta un frame, ossia una finestra, che contiene diversi elementi. MenuPage ne estende le caratteristiche, dunque diventa un tipo di schermata che può essere visualizzata e avere le funzionalità fornite da JFrame.  

Per poter risolvere invece il problema principale, vale a dire il collegare menù e schermata di gioco, la soluzione fu l’uso di una sequenza di operazioni. Per prima cosa, fu utilizzato il metodo dispose() per poter chiudere la finestra del menù. Poi, la musica di background fu interrotta, per poter lasciare spazio a quella della schermata di gioco. Venne istanziata in seguito una nuova istanza della classe Game, per poter successivamente richiamare il metodo startGame(), appartenente a Game, che inizializza l’ambiente di gioco. Infine, fu utilizzato il seguente codice: “new Thread (() -> { game.gameLoop() }).start() “, che si occupa di avviare un nuovo thread per l’esecuzione del loop di gioco. Ciò viene eseguito per evitare che il ciclo di gioco blocchi l’event dispatch thread (EDT) della GUI (Graphical User Interface). Il loop di gioco, detto gameLoop, è un metodo della classe Game ed è responsabile dell'aggiornamento dello stato del gioco e del rendering della grafica. Trattandosi di un task di lunga durata, creando un nuovo thread specifico per questo metodo, esso viene eseguito in concomitanza con l’EDT, assicurando che l’interfaccia grafica rimanga reattiva mentre la logica di gioco viene eseguita in background. 
Grazie a questo tipo di approccio, il problema fu risolto correttamente, e la schermata di gioco è ora perfettamente collegata a quella del menù, permettendo all’utente di passare da quest’ultimo, che funge da presentazione a Space Invaders, al gioco vero e proprio.  

Per riassumere, utilizziamo un grafico UML per rappresentare i vari componenti presi in esame. 

```mermaid
classDiagram 
    MenuPage --> Game 
    MenuPage --|> JFrame 

    class Game {
        - BufferStrategy strategy 
        - List<Entity> entities 
        - Entity ship 
        - BossEntity boss 
        - boolean gameRunning 
        - boolean waitingForKeyPress 
        + showNotificationPanel() 
        + Game() 
        + startGame() 
        + initEntities() 
        + initBoss() 
        + gameLoop() 
    } 

    class MenuPage {
        - AudioStore audioStore 
        - AudioClip menuMusic 
        + MenuPage() 
    }

    class JFrame {
        + title: String 
        + contentPane: Container 
        + visible: boolean 
        + setContentPane(contentPane: Container): void 
        + setTitle(title: String): void 
        + pack(): void 
        + setVisible(visible: boolean): void 
        + add(component: Component): void 
        + remove(component: Component): void 
        + setSize(width: int, height: int): void 
        + getContentPane(): Container 
        + setLocationRelativeTo(component: Component): void 
        + addWindowListener(listener: WindowListener): void
        + dispose(): void 
    }

```

Nell’UML vengono rappresentate le classi MenuPage, ovvero il menù, e Game, ossia il gioco vero e proprio. La relazione che gli elementi presentano è monodirezionale, ovvero il menù richiama il gioco poichè quest’ultimo viene aperto tramite l’uso di un pulsante presente nella schermata di presentazione di Space Invaders. Nello schema è presente anche JFrame, in modo da esplicitare il fatto che MenuPage ne estenda le caratteristiche. Infatti, molti dei metodi elencati nel grafico furono utilizzati per poter implementare correttamente il menù. 

-Collisione (Angelica) 

Problema: la collisione dello sparo è stata una parte complessa dello svolgimento del progetto.  

Soluzione: si è usato il metodo “collidedWith” che gestisce gli eventi di collisione tra il colpo e altre entità. Assicura che venga processato solo un evento di collisione per ogni colpo e procede a rimuovere le entità coinvolte e a notificare il gioco quando il colpo entra in collisione con un'entità di tipo AlienEntity. Questo metodo inoltre utilizza il pattern "Flag" impostando “used” a “true” quando il colpo colpisce un'entità, per evitare che il colpo la colpisca più volte. Il "pattern flag" è un design pattern che viene utilizzato per tenere traccia dello stato di un oggetto mediante l'uso di un flag booleano. Questo flag viene utilizzato per indicare se un particolare evento è già avvenuto o se un'azione è già stata eseguita. Il pattern flag è utile quando si desidera evitare di eseguire un'azione più volte o quando si vuole controllare se un certo evento è già accaduto. 

Viene utilizzato anche Template Method: I metodi “move(long delta)” e “collidedWith(Entity other)” definiscono lo scheletro di un algoritmo con il comportamento generale di movimento e gestione delle collisioni per un'entità colpo (ShotEntity). Questi metodi delegano alcune operazioni specifiche alle sottoclassi attraverso il concetto di ereditarietà. 

In ausilio ai pattern citati viene usato anche Single Responsibility Principle (SRP): Ogni metodo all'interno della classe ShotEntity ha un compito specifico e separato: il metodo “move” si occupa del movimento del colpo, il metodo “collidedWith” gestisce le collisioni e il metodo “createShot” si occupa di creare un nuovo colpo. Questo rispetta il principio SRP, che suggerisce che una classe dovrebbe avere una sola ragione per cambiare.

```mermaid
classDiagram 
    class Entity { 
        <<abstract>> 
        +String sprite 
        +int x 
        +int y 
        +double dx 
        +double dy 
        +Entity(String sprite, int x, int y) 
        +void move(long delta) 
        +void collidedWith(Entity other) 
    } 
    class ShotEntity { 
        -double moveSpeed 
        -Game game 
        -boolean used 
        +ShotEntity(Game game, String sprite, int x, int y) 
        +void move(long delta) 
        +void collidedWith(Entity other) 

    } 
    class Game { 
        +void removeEntity(Entity entity) 
        +void notifyAlienKilled() 
    } 

    Entity <|-- ShotEntity 
    ShotEntity *-- Game
```

## Sviluppo
### Testing automatizzato 

I file di test presenti all’interno del progetto nella cartella “example”, sotto la voce “test”, sono stati sviluppati utilizzando il framework JUnit per verificare il corretto funzionamento di alcune classi. Questo approccio consente di eseguire i test in modo automatico, garantendo che le funzionalità del software siano valide e che eventuali modifiche non introducano regressioni. Infine, uso di suite specifiche come JUnit è vantaggioso, poiché semplifica l'esecuzione e la gestione dei test, contribuendo a una maggiore efficienza nel processo di sviluppo del software. 