/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;


/**
 *
 * @author User
 */
public class Wumpus extends GameObject {
    
    public Wumpus(int x, int y) {
        super(x, y, "wumpus");
    }
    
    @Override
    public void interact(Player player) {
        System.out.println("Player encountered the Wumpus!");
    }
    
    @Override
    public String getSensation() {
        return "You smell a terrible stench.";
    }
}
