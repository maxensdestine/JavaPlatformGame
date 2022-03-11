/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import static java.lang.Math.sin;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author 1749637
 */
public class EnnemiAerienZigZag extends Ennemi {

    private float baseY;

    /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image
     */
    public EnnemiAerienZigZag(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 2, 6);
        baseY = y;
        deltaX = 4.0f;

    }

    /**
     * fait bouger l'objet
     */
    @Override
    public void bouger() {
        super.bouger();
        x = x - deltaX;
        /* if (x < -buffer) {
            this.setDetruire(true);
        }*/

        y = (float) (90 * sin((0.009) * x) + baseY);

    }

}
