/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author 1749637
 */
public class EnnemiAerienCercle extends Ennemi {

    private float xFonction = 0, yFonction = 0, base = 0, rayon = 3.0f;//le x utilise dans la fonction qui fait le cerlce
    private double angle = 0.0;

    /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image
     */
    public EnnemiAerienCercle(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 3, 4);
        deltaX = 3.0f;
    }

    /**
     * méthode qui s'occupe du mouvement de cercle et que l'objet respecte les
     * bornes du jeu
     */
    @Override
    public void bouger() {
        if (x < -buffer) {
            this.setDetruire(true);
        }

        yFonction = (float) (rayon * sin(0.5 * (angle)));
        xFonction = (float) (rayon * cos(0.5 * (angle)));

        y = y - yFonction;
        x = x - xFonction - deltaX;
        angle = angle + 0.08;
    }

}
