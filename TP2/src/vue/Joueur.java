package vue;

import controleur.Controleur;
import static controleur.Controleur.HAUTEUR;
import static controleur.Controleur.HAUTEUR_PLANCHER;
import static controleur.Controleur.LONGUEUR;
import static java.lang.Math.sin;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1767250
 */
public class Joueur extends Entite {
//implements Observer

    private float deltaX = 5.5f, deltaY = 5.5f, hover = 0.0f;
    private boolean envole = true, bonusTriple = false;
    private ArrayList<Image> listeAnimationAuSol = new ArrayList<>();
    private ArrayList<Image> listeAnimationEnVol = new ArrayList<>();
    private int posImage = 0, tempsAnimation = 0;

    private long tempsAttaque = 0;

    /**
     *
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet dans lequel se trouve l'image de l'objet
     */
    public Joueur(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 0, 3);
        listeAnimationEnVol.add(spriteSheet.getSubImage(0, 0));
        listeAnimationEnVol.add(spriteSheet.getSubImage(1, 0));
        listeAnimationEnVol.add(spriteSheet.getSubImage(2, 0));
        listeAnimationAuSol.add(spriteSheet.getSubImage(3, 0));
        listeAnimationAuSol.add(spriteSheet.getSubImage(4, 0));
        listeAnimationAuSol.add(spriteSheet.getSubImage(5, 0));

    }

    /**
     * @param listeKeys liste qui contient les touches appuyées par
     * l'utilisateur
     */
    public void bouger(ArrayList<KeyCode> listeKeys) {
        //mvt horizontal
        if (listeKeys.contains(KeyCode.RIGHT) || listeKeys.contains(KeyCode.D)) {
            x = x + deltaX;
            if (x > (LONGUEUR - width)) {
                x = LONGUEUR - width;
            }
        } else if (listeKeys.contains(KeyCode.LEFT) || listeKeys.contains(KeyCode.A)) {
            x = x - deltaX;
            if (x < 0) {
                x = 0;
            }
        }
        //mvt vertical
        if (listeKeys.contains(KeyCode.UP) || listeKeys.contains(KeyCode.W)) {
            y = y - deltaY;

        } else if (listeKeys.contains(KeyCode.DOWN) || listeKeys.contains(KeyCode.S)) {
            y = y + deltaY;

        }
        envole = estEnvole();

        if (envole && listeKeys.isEmpty()) {
            if ((y - sin(hover) > 0) || y - sin(hover) < HAUTEUR_PLANCHER) {
                y = (float) (y + sin(hover));
                hover += 0.1;
            }

        }

        if (y < 0) {
            y = 0;
        }
        if (y > HAUTEUR - HAUTEUR_PLANCHER - height) {
            y = HAUTEUR - HAUTEUR_PLANCHER - height;
        }
        changerImage();
    }

    /**
     * @return retourne true si le joueur peut attaquer, false en cas contraire
     */
    protected boolean peutAttaquer() {

        if (System.currentTimeMillis() - tempsAttaque >= 500) {
            tempsAttaque = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    private boolean estEnvole() {
        if (y + height == Controleur.HAUTEUR - Controleur.HAUTEUR_PLANCHER) {
            return false;
        }
        return true;
    }

    private void changerImage() {
        if (tempsAnimation == 0) {
            posImage = 0;
        } else if (tempsAnimation == 30) {
            posImage = 1;
        } else if (tempsAnimation == 60) {
            posImage = 2;
        } else if (tempsAnimation == 90) {
            tempsAnimation = -1;
        }

        if (envole) {
            this.image = listeAnimationEnVol.get(posImage);

        } else {
            this.image = listeAnimationAuSol.get(posImage);
        }

        tempsAnimation++;

    }
}
