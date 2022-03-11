package vue;

import controleur.Controleur;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.input.KeyCode;
import modele.Modele;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1767250 par Maxens Destiné et Emulie Chhor
 */
public class Jeu extends BasicGame implements Observer {

    private CopyOnWriteArrayList<Bougeable> listeBougeable = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Entite> listeEntite = new CopyOnWriteArrayList<>();
    private ArrayList<KeyCode> listeKeys = new ArrayList<>();
    private Input input;
    private long tempsEnnemi = 0, tempsBonusTriple = 0, tempsCompteARebours = 0;
    private int nbVies = 3, nbPoints = 0, indexBackGround;
    private Joueur joueur;
    private int LONGUEUR, HAUTEUR, xd = 200, yd = 200, HAUTEUR_SOL = 64, maxArbre = 2, nbArbre = 0;
    private Random random = new Random();
    private SpriteSheet spriteSheetBonus, spriteSheetTiles, spriteSheetPrincesse, spriteSheetMonde,
            spriteSheetCoeur, spriteSheetEnnemi, spriteSheetNuage;

    private boolean finDePartie = false;

    /**
     * buffer espace utilisé lors de la céation d'un objet (zone tampon)
     */
    public static final float buffer = 50;
    private Controleur controleur;
    private Modele modele;

    /**
     * @param LONGUEUR longueur de l'écran de jeu
     * @param HAUTEUR hauteur de l'écran de jeu
     * @param controleur le controleur du jeu
     * @param modele le modele du jeu
     */
    public Jeu(int LONGUEUR, int HAUTEUR, Controleur controleur, Modele modele) {
        super("Game");
        this.controleur = controleur;
        this.modele = modele;
        modele.addObserver(this);
        this.LONGUEUR = LONGUEUR;
        this.HAUTEUR = HAUTEUR;

    }

    /**
     * @param container était présent dans le document word
     */
    @Override
    public void init(GameContainer container) throws SlickException {
        spriteSheetBonus = new SpriteSheet("images/sprites_divers.png", 32, 32);
        spriteSheetNuage = new SpriteSheet("images/tiles.png", 96, 75);
        spriteSheetPrincesse = new SpriteSheet("images/sprites_princess.png", 32, 64);
        spriteSheetMonde = new SpriteSheet("/images/sprites_monde.png", 32, 32);
        spriteSheetCoeur = new SpriteSheet("/images/coeur.png", 32, 32);
        spriteSheetTiles = new SpriteSheet("/images/tiles.png", 32, 32);
        spriteSheetEnnemi = new SpriteSheet("/images/sprites_divers.png", 32, 32);
        // spriteSheetPlancher = new SpriteSheet("/images/sprites_monde.png", 32, 64);

        input = container.getInput();

        tempsEnnemi = System.currentTimeMillis();
        for (int i = 0; i <= LONGUEUR; i += 32) {
            PlancherBas plancherBas = new PlancherBas(i, HAUTEUR - 32, spriteSheetMonde);
            listeEntite.add(plancherBas);
            listeBougeable.add(plancherBas);
        }

        for (int i = 0; i <= LONGUEUR; i += 32) {
            PlancherHaut plancherHaut = new PlancherHaut(i, HAUTEUR - 64, spriteSheetMonde);
            listeEntite.add(plancherHaut);
            listeBougeable.add(plancherHaut);
        }

        for (int f = 0; f < LONGUEUR; f += 32) {
            for (int i = 0; i < HAUTEUR - HAUTEUR_SOL; i += 32) {
                Ciel ciel = new Ciel(f, i, spriteSheetTiles);
                listeEntite.add(ciel);
                listeBougeable.add(ciel);
            }

        }

        int xPos = random.nextInt(400) + 200;

        for (int i = 0; i < maxArbre; i++) {

            Arbre arbre = new Arbre(xPos + i * 35, HAUTEUR - 96, spriteSheetMonde);
            arbre.setHauteur(random.nextInt(9) + 1);
            arbre.setLocation(arbre.getX(), HAUTEUR - 96 - arbre.getHauteur() * 32 - arbre.getHeight());
            listeEntite.add(arbre);
            listeBougeable.add(arbre);

            for (int f = 0; f <= arbre.getHauteur(); f++) {
                Tronc tronc = new Tronc(listeEntite.get(listeEntite.size() - 1).getX(), HAUTEUR - 96 - arbre.getHauteur() * 32 + f * 32, spriteSheetMonde);
                listeEntite.add(tronc);
                listeBougeable.add(tronc);
            }

            nbArbre++;
            //+100 s'assure que les 2 arbres ne sont pas superposés
            xPos += random.nextInt(400) + 100;

        }

        xPos = random.nextInt(100) + 200;
        for (int i = 0; i < 4; i++) {
            /*pas besoin de limite vers le haut pour les nuages, car leur rectangle est plus
            large que l'image elle meme du nuage*/
            Nuage nuage = new Nuage(xPos, random.nextInt(250), spriteSheetNuage);
            listeBougeable.add(nuage);
            listeEntite.add(nuage);
            /*+150 s'assure que les 2 arbres ne sont pas superposés
            et puisque il ne se superpose pas en x, alors on peut mettre n'importe quel y*/
            xPos += random.nextInt(100) + 150;
        }

        indexBackGround = listeEntite.size();

        joueur = new Joueur(64, HAUTEUR - 128, spriteSheetPrincesse);
        listeEntite.add(joueur);

    }

    /**
     * @param container était présent dans le document word
     * @param delta était présent dans le document word
     */
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (!finDePartie) {
            if (System.currentTimeMillis() > tempsEnnemi + 2500) {
                declencherWaveEnnemis();
                tempsEnnemi = System.currentTimeMillis();
            }

            for (Bougeable bougeable : listeBougeable) {
                bougeable.bouger();
            }

            deplacerBackground();
            enleverEntitesExterieurEcran();

            getKeys();
            traiterKeys();

            gererCollisionJoueurEnnemi();
            gererCollisionBulletEnnemi();
            gererCollisionJoueurBonus();
            gererArbre();
            tireEnnemi();
        } else {
            verifierCompteARebours();
        }

    }

    /**
     *
     * @param container était présent dans le document word
     * @param g était présent dans le document word
     * @throws SlickException était présent dans le document word
     */
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        listeEntite.remove(joueur);
        for (Entite entite : listeEntite) {
            g.drawImage(entite.getImage(), entite.getX(), entite.getY());
        }
        listeEntite.add(joueur);
        g.drawImage(joueur.getImage(), joueur.getX(), joueur.getY());
        g.drawString("Points: " + nbPoints, LONGUEUR - buffer * 5, 10);

        int posXCoeur = 16, posYCoeur = 32;
        for (int i = 0; i < nbVies; i++) {
            g.drawImage(spriteSheetCoeur, posXCoeur, posYCoeur);
            posXCoeur += 32;
        }

        if (finDePartie) {
            g.drawString("GAME OVER", LONGUEUR / 2 - 10, HAUTEUR / 2);

        }

    }

    private void getKeys() {
        if (input.isKeyDown(Input.KEY_SPACE)) {
            if (!listeKeys.contains(KeyCode.SPACE)) {
                listeKeys.add(KeyCode.SPACE);
            }
        } else {
            listeKeys.remove(KeyCode.SPACE);
        }

        if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
            if (!listeKeys.contains(KeyCode.RIGHT)) {
                listeKeys.add(KeyCode.RIGHT);
            }
        } else {
            listeKeys.remove(KeyCode.RIGHT);
        }

        if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
            if (!listeKeys.contains(KeyCode.LEFT)) {
                listeKeys.add(KeyCode.LEFT);
            }
        } else {
            listeKeys.remove(KeyCode.LEFT);
        }

        if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
            if (!listeKeys.contains(KeyCode.UP)) {
                listeKeys.add(KeyCode.UP);
            }
        } else {
            listeKeys.remove(KeyCode.UP);
        }

        if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
            if (!listeKeys.contains(KeyCode.DOWN)) {
                listeKeys.add(KeyCode.DOWN);
            }
        } else {
            listeKeys.remove(KeyCode.DOWN);
        }

        if (input.isKeyDown(Input.KEY_A)) {
            if (!listeKeys.contains(KeyCode.A)) {
                listeKeys.add(KeyCode.A);
            }
        } else {
            listeKeys.remove(KeyCode.A);
        }

        if (input.isKeyDown(Input.KEY_S)) {
            if (!listeKeys.contains(KeyCode.S)) {
                listeKeys.add(KeyCode.S);
            }
        } else {
            listeKeys.remove(KeyCode.S);
        }
        if (input.isKeyDown(Input.KEY_D)) {
            if (!listeKeys.contains(KeyCode.D)) {
                listeKeys.add(KeyCode.D);
            }
        } else {
            listeKeys.remove(KeyCode.D);
        }
        if (input.isKeyDown(Input.KEY_W)) {
            if (!listeKeys.contains(KeyCode.W)) {
                listeKeys.add(KeyCode.W);
            }
        } else {
            listeKeys.remove(KeyCode.W);
        }
    }

    private void traiterKeys() throws SlickException {
        joueur.bouger(listeKeys);
        if (listeKeys.contains(KeyCode.SPACE) && joueur.peutAttaquer()) {
            tirerBalle();
        }
    }

    /**
     *
     * @return true si le temps du triple tir n'est pas écoulé false en cas
     * contraire
     */
    public boolean isBonusTriple() {
        if (tempsBonusTriple + 10000 < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    private void tirerBalle() throws SlickException {
        if (!isBonusTriple()) {

            Bullet bullet = new Bullet(joueur.getX() + joueur.getWidth(), joueur.getY() + (joueur.getHeight() / 4));
            listeBougeable.add(bullet);
            listeEntite.add(bullet);
        } else {
            Bullet bullet = new Bullet(joueur.getX() + joueur.getWidth(), joueur.getY() + (joueur.getHeight() / 4));
            listeBougeable.add(bullet);
            listeEntite.add(bullet);
            Bullet bullet1 = new Bullet(joueur.getX() + joueur.getWidth(), joueur.getY() + (joueur.getHeight() / 4));
            bullet.setDeltaY(12);
            listeBougeable.add(bullet1);
            listeEntite.add(bullet1);
            Bullet bullet2 = new Bullet(joueur.getX() + joueur.getWidth(), joueur.getY() + (joueur.getHeight() / 4));
            bullet2.setDeltaY(-12);
            listeBougeable.add(bullet2);
            listeEntite.add(bullet2);
        }
    }

    private void gererCollisionJoueurEnnemi() {
        ArrayList<Entite> listeTemp = new ArrayList();

        for (Bougeable bougeable : listeBougeable) {
            if (bougeable instanceof Ennemi || bougeable instanceof BulletEnnemi) {
                if (joueur.getRectangle().intersects(bougeable.getRectangle())) {
                    listeTemp.add((Entite) bougeable);
                    controleur.perdVie();
                }
            }
        }

        listeEntite.removeAll(listeTemp);
        listeBougeable.removeAll(listeTemp);

    }

    private void gererCollisionJoueurBonus() {
        ArrayList<Entite> listeTemp = new ArrayList();
        Bonus bonus = null;

        for (Bougeable bougeable : listeBougeable) {
            if (bougeable instanceof Bonus) {
                if (joueur.getRectangle().intersects(bougeable.getRectangle())) {
                    listeTemp.add((Entite) bougeable);

                    controleur.ramasseBonus();
                    bonus = (Bonus) bougeable;

                }
            }
        }

        if (!listeTemp.isEmpty()) {
            activerPouvoirBonus(bonus);

        }

        listeBougeable.removeAll(listeTemp);
        listeEntite.removeAll(listeTemp);

    }

    private void gererCollisionBulletEnnemi() throws SlickException {
        ArrayList<Entite> listeTemp = new ArrayList();
        for (Bougeable b1 : listeBougeable) {

            for (Bougeable b2 : listeBougeable) {
                if ((b1 instanceof Ennemi && b2 instanceof Bullet)) {

                    if (b1.getRectangle().intersects(b2.getRectangle())) {

                        listeTemp.add((Entite) b1);
                        listeTemp.add((Entite) b2);

                        controleur.elimineEnnemis();
                    }
                }
            }
        }

        if (!listeTemp.isEmpty()) {
            int r = random.nextInt(10);
            if (r == 1) {
                spondBonus(listeTemp.get(0).getRectangle().getX(), listeTemp.get(0).getRectangle().getY());
            }
        }

        listeEntite.removeAll(listeTemp);
        listeBougeable.removeAll(listeTemp);
    }

    private void reset() {
        ArrayList<Entite> listeTemp = new ArrayList();
        for (Entite entite : listeEntite) {
            if (entite instanceof Collisionnable) {
                listeTemp.add(entite);
            }
        }
        listeEntite.removeAll(listeTemp);
        listeBougeable.removeAll(listeTemp);

        controleur.finDePartie();
        joueur.setLocation(64, HAUTEUR - 128);
    }

    @Override
    public void update(Observable o, Object arg) {
        finDePartie = modele.isFinDePartie();
        if (finDePartie) {
            tempsCompteARebours = System.currentTimeMillis() + 2500;
        }
        nbVies = modele.getHealthPoints();
        nbPoints = modele.getPoints();
    }

    private void enleverEntitesExterieurEcran() {
        ArrayList<Entite> listeTemp = new ArrayList();

        for (Entite entite : listeEntite) {
            if (entite.getDetruire()) {
                listeTemp.add(entite);
            }
        }
        listeEntite.removeAll(listeTemp);
        listeBougeable.remove(listeTemp);
    }

    private void gererArbre() throws SlickException {
        Arbre arbre1 = null, arbre2 = null;
        float ancienX;
        for (Entite entite : listeEntite) {
            if (entite instanceof Arbre && arbre1 == null) {
                arbre1 = (Arbre) entite;
            } else if (entite instanceof Arbre) {
                arbre2 = (Arbre) entite;
            }
        }

        hauteurArbre(arbre1);
        hauteurArbre(arbre2);

        if (arbre2.getX() + arbre2.getWidth() - 5 < 0) {
            if (random.nextInt(2) == 1) {
                ancienX = arbre2.getX();
                arbre2.setLocation(arbre1.getX() + random.nextInt(500) + 70 + LONGUEUR, arbre2.getY());
                for (Entite entite : listeEntite) {
                    if (entite instanceof Tronc && entite.getX() == ancienX) {
                        entite.setX(arbre2.getX());
                    }
                }

            }
        }

    }

    private void hauteurArbre(Arbre arbre) {
        int hauteur, difference;
        ArrayList<Tronc> listeTronc = new ArrayList();
        if (arbre.getX() + arbre.getWidth() - 5 < 0) {
            hauteur = random.nextInt(9) + 1;
            difference = hauteur - arbre.getHauteur();
            arbre.setHauteur(hauteur);
            arbre.setLocation(arbre.getX(), arbre.getY() - difference * 32);
            if (difference > 0) {

                for (int i = 0; i < difference; i++) {
                    Tronc tronc = new Tronc(arbre.getX(), (arbre.getY()) + i * 32 + 32, spriteSheetMonde);
                    listeTronc.add(tronc);
                }

                listeEntite.addAll(listeTronc);
                listeBougeable.addAll(listeTronc);

            } else if (difference < 0) {

                for (Entite entite : listeEntite) {
                    if (entite instanceof Tronc && entite.getX() == arbre.getX() && entite.getY() <= arbre.getY()) {

                        listeTronc.add((Tronc) entite);
                    }
                }
                listeEntite.removeAll(listeTronc);
                listeBougeable.removeAll(listeTronc);
            }

        }

    }

    private void deplacerBackground() {
        for (Entite entite : listeEntite) {
            if (entite instanceof Background) {
                if (entite.getX() + entite.getWidth() <= 0) {
                    entite.setX(LONGUEUR);
                }
            }
        }
    }

    private void declencherWaveEnnemis() throws SlickException {
        int choixTypeEnnemi = random.nextInt(7);

        int nbEnnemis;
        do {
            nbEnnemis = random.nextInt(6) * 2;//pour que ca soit pair
        } while (nbEnnemis == 0);

        switch (choixTypeEnnemi) {
            case 0:
                creerEnnemiSol(nbEnnemis);
                break;
            case 1:

                creerEnnemiAerienVertical(nbEnnemis);
                break;

            case 2:

                creerEnnemiAerienCercle(nbEnnemis);
                break;

            case 3:
                creerEnnemisBulles(nbEnnemis);
                break;

            case 4:
                creerEnnemiAerienZigZag(nbEnnemis);
                break;

            case 5:
                creerEnnemiAerienVertical(nbEnnemis);

                break;

            case 6:
                creerEnnemiAerienRoue();
                break;
            default:

        }
    }

    /**
     *
     * @param x position en x du bonus
     * @param y position en y du bonus
     * @throws SlickException
     */
    private void spondBonus(float x, float y) throws SlickException {
        int choixBonus = random.nextInt(3);

        Bonus bonus = null;
        switch (choixBonus) {
            case 0:
                bonus = new BoostEnergie(x, y, spriteSheetBonus);
                break;
            case 1:
                bonus = new BombeMega(x, y, spriteSheetBonus);

                break;
            case 2:
                bonus = new ArmeABalles(x, y, spriteSheetBonus);
                break;
            //  default:
        }
        listeEntite.add(indexBackGround, bonus);
        listeBougeable.add((Bougeable) bonus);
    }

    private void tireEnnemi() {
        ArrayList<BulletEnnemi> listeBullet = new ArrayList();
        for (Entite entite : listeEntite) {
            if (entite instanceof EnnemiSol) {
                if (((EnnemiSol) (entite)).tireEnCours()) {
                    BulletEnnemi bulletEnnemi = new BulletEnnemi(entite.getX(), entite.getY() - 32, spriteSheetBonus);
                    listeBullet.add(bulletEnnemi);

                }

            }
        }

        listeEntite.addAll(listeBullet);
        listeBougeable.addAll(listeBullet);
    }

    /**
     * @param nbEnnemis le nombre d'ennemi à créer
     */
    private void creerEnnemiSol(int nbEnnemis) {
        if (nbEnnemis > 4) {
            nbEnnemis = random.nextInt(5) + 1;

        }
        for (int i = 0; i < nbEnnemis; i++) {
            EnnemiSol ennemiSol = new EnnemiSol(LONGUEUR + buffer + i * 32, HAUTEUR - HAUTEUR_SOL - 32, spriteSheetEnnemi);
            listeEntite.add(ennemiSol);
            listeBougeable.add(ennemiSol);
        }

    }

    /**
     * @param nbEnnemis le nombre d'ennemi à créer
     */
    private void creerEnnemiAerienVertical(int nbEnnemis) {

        int posY = random.nextInt(HAUTEUR / 16) + 20;
        for (int i = 0; i < nbEnnemis; i++) {
            EnnemiAerien ennemiAerien = new EnnemiAerien(LONGUEUR + buffer, posY + i * 32, spriteSheetEnnemi);
            listeEntite.add(ennemiAerien);
            listeBougeable.add(ennemiAerien);
            posY += 15;
        }
    }

    /**
     * @param nbEnnemis le nombre d'ennemi à créer
     */
    private void creerEnnemiAerienZigZag(int nbEnnemis) {
        int posY = random.nextInt(200) + 100;
        for (int i = 0; i < nbEnnemis; i++) {
            EnnemiAerienZigZag ennemiAerienZigZag = new EnnemiAerienZigZag(LONGUEUR + buffer + i * 42, posY, spriteSheetEnnemi);
            listeEntite.add(ennemiAerienZigZag);
            listeBougeable.add(ennemiAerienZigZag);
        }

    }

    /**
     * @param nbEnnemis le nombre d'ennemi à créer
     */
    private void creerEnnemiAerienCercle(int nbEnnemis) {

        int posY = random.nextInt(200) + 350;
        for (int i = 0; i < nbEnnemis; i++) {
            EnnemiAerienCercle ennemiAerienCercle = new EnnemiAerienCercle(LONGUEUR + buffer + i * 38, posY, spriteSheetEnnemi);
            listeEntite.add(ennemiAerienCercle);
            listeBougeable.add(ennemiAerienCercle);
        }

    }

    private void creerEnnemiAerienRoue() {
        int posY = random.nextInt(250) + 300;
        float[][] positionRoue = modele.getPositionRoue();
        for (int i = 0; i < positionRoue[0].length; i++) {

            Ennemi ennemiAerienRoue = new EnnemiAerienRoue(LONGUEUR + buffer + positionRoue[0][i], posY - positionRoue[1][i],
                    spriteSheetEnnemi, positionRoue[2][i], posY);

            listeEntite.add(ennemiAerienRoue);
            listeBougeable.add(ennemiAerienRoue);
        }
    }

    /**
     * @param bonus le bonus qui a été attrapé
     */
    private void activerPouvoirBonus(Bonus bonus) {
        ArrayList<Entite> listeTemp = new ArrayList();

        if (bonus instanceof BombeMega) {
            for (Bougeable bougeable : listeBougeable) {
                if (bougeable instanceof Ennemi && ((Entite) bougeable).getX() < LONGUEUR) {
                    listeTemp.add((Entite) bougeable);
                }
            }
            Pow pow = new Pow(LONGUEUR / 2, HAUTEUR / 2, spriteSheetMonde);
            listeEntite.add(pow);
            listeBougeable.add(pow);

            int nbEnnemisTues = listeTemp.size();
            controleur.ramasseBombeMega(nbEnnemisTues);

            listeEntite.removeAll(listeTemp);
            listeBougeable.removeAll(listeTemp);
        }
        if (bonus instanceof BoostEnergie) {
            controleur.ramasseBoostEnergie();
        }
        if (bonus instanceof ArmeABalles) {
            tempsBonusTriple = System.currentTimeMillis();

        }

    }

    private void verifierCompteARebours() {
        if (System.currentTimeMillis() > tempsCompteARebours) {
            reset();
        }
    }

    /**
     * @param nbEnnemis nombre d'ennemis à créer
     */
    private void creerEnnemisBulles(int nbEnnemis) {
        int decalagae = random.nextInt(HAUTEUR/2 - HAUTEUR_SOL)+128;
        for (int i = 0; i < nbEnnemis; i++) {
            EnnemiBulle ennemiBulle = new EnnemiBulle(LONGUEUR + buffer + i * 16, HAUTEUR - decalagae, spriteSheetEnnemi);
            listeEntite.add(ennemiBulle);
            listeBougeable.add(ennemiBulle);
        }
    }

}
