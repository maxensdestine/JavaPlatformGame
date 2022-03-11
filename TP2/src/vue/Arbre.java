package vue;
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
public class Arbre extends Background {

    private SpriteSheet spriteSheet;
    private int hauteur = 4;

    /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image   
     */
    public Arbre(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 1, 5);
        this.spriteSheet = spriteSheet;     
   
    }


    
    protected int getHauteur() {
        return hauteur;
    }

    protected void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }


}
