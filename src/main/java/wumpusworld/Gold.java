/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;


/**
 *
 * @author User
 */
public class Gold extends GameObject {
    
    public Gold(int x, int y) {
        super(x, y, "gold");
    }
    
    
    @Override
    public void interact(Player player) {
        System.out.println("You found gold!");
        player.pickupGold();
    }
    
    @Override
    public String getSensation() {
        return "You see a glimmer.";
    }
}
