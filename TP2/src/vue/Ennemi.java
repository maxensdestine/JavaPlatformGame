/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author emuli
 */
public abstract class Ennemi extends Entite implements Bougeable,Collisionnable {

    /**
     *vitesse en X
     */
    protected float deltaX = 2.8f,

    /**
     *vitesse en Y
     */
    deltaY = 0.3f;
    
    /**
     *gère la vitesse d'animation
     */
    protected int tempsAnimation=0;



         /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image 
     * @param ligne ligne dans le spriteSheet où se trouve l'image
     * @param colonne colonne dans le spriteSheet où se trouve l'image 
     */
    public Ennemi(float x, float y, SpriteSheet spriteSheet, int ligne, int colonne) {
        super(x, y, spriteSheet, ligne, colonne);
    }

    /**
     *
     */
    @Override
    public void bouger() {
        if (x < -buffer) {
            this.setDetruire(true);
        }

    }
     
    
    
}
