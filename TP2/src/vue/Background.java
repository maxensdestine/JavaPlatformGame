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
public class Background extends Entite implements Bougeable{
    
    /**
     *
     */
    public static float deplacementEcranX=1.6f;
    //maxY;
    
     /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image
     * @param xSprite position de la ligne dans le spriteSheet
     * @param ySprite position de la colonne dans le spriteSheet
     */
    public Background(float x, float y, SpriteSheet spriteSheet,int xSprite,int ySprite) {
        super(x, y, spriteSheet, xSprite, ySprite);
    }

    /**
     *méthode qui déplace le décors vers la gauche
     */
    @Override
    public void bouger() {
        x=x-deplacementEcranX;
    }

   
}
