/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author 1749637
 */
public class Pow extends Entite implements Bougeable {

    private ArrayList<Image> listeAnimation = new ArrayList<>();
    private int tempsAnimation = 0;

    /**
     * @param x position en x
     * @param y position en y
     * @param spriteSheet spriteSheet ou se trouve son image
     */
    public Pow(float x, float y, SpriteSheet spriteSheet) {
        super(x, y, spriteSheet, 22, 7);
        listeAnimation.add(spriteSheet.getSubImage(7, 22));
        listeAnimation.add(spriteSheet.getSubImage(6, 22));
        listeAnimation.add(spriteSheet.getSubImage(5, 22));
        listeAnimation.add(spriteSheet.getSubImage(4, 22));
        listeAnimation.add(spriteSheet.getSubImage(3, 22));
        listeAnimation.add(spriteSheet.getSubImage(2, 22));
        listeAnimation.add(spriteSheet.getSubImage(1, 22));
        listeAnimation.add(spriteSheet.getSubImage(0, 22));
    }

    /**
     * anime l'objet
     */
    @Override
    public void bouger() {
        changerAnimation();
    }

    private void changerAnimation() {
        if (tempsAnimation == 0) {
            image = listeAnimation.get(0);
        } else if (tempsAnimation == 5) {
            image = listeAnimation.get(1);
        } else if (tempsAnimation == 10) {
            image = listeAnimation.get(2);
        } else if (tempsAnimation == 15) {
            image = listeAnimation.get(3);
        } else if (tempsAnimation == 20) {
            image = listeAnimation.get(4);
        } else if (tempsAnimation == 25) {
            image = listeAnimation.get(5);
        } else if (tempsAnimation == 30) {
            image = listeAnimation.get(6);
        } else if (tempsAnimation == 35) {
            image = listeAnimation.get(7);
        } else if (tempsAnimation == 40) {

            //  tempsAnimation = -1;
            setDetruire(true);
        }
        tempsAnimation++;

    }
}
