package vue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author 1767250
 */
public class Ciel extends Background {
    
     /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image   
     */
     public Ciel(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 6,12);
    }

    @Override
    public void bouger() {
        //n'a pas besoin de bouger
    }
     
    
}
