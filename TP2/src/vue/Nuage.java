/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author 1767250
 */
public class Nuage extends Background {

    /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image
     */
    public Nuage(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 3, 0);
    }

}
