/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;


/**
 *
 * @author Haidar Ali
 */
public class GamePanel extends JPanel implements KeyListener {
    // Attributes
    private GameManager gameManager;
    private JTextField jTextFieldSensation;  
    private Timer animationTimer;
    
    // Constructor
    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.setBackground(Color.WHITE);

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow(); 

        //ini buat animasi gif nya biar gerak kang!!!
        animationTimer = new Timer(100, e -> repaint()); // Repaint setiap 100ms
        animationTimer.start();
    }

    
    // Constructor pakai jTextFieldSensation
    public GamePanel(GameManager gameManager, JTextField jTextFieldSensation) {
        this.gameManager = gameManager;
        this.jTextFieldSensation = jTextFieldSensation;
        this.setBackground(Color.WHITE);
        
        // Add key listener
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        
        //ini buat animasi gif nya biar gerak kang!!!
        animationTimer = new Timer(100, e -> repaint()); // Repaint setiap 100ms
        animationTimer.start();
    }
    
    // Setter jTextFieldSensation
    public void setTextField(JTextField jTextFieldSensation) {
        this.jTextFieldSensation = jTextFieldSensation;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (gameManager == null || gameManager.getGrid() == null) {
            return;
        }
        
        GameGrid grid = gameManager.getGrid();
        int rows = grid.getRows();
        int cols = grid.getCols();
        
        // Calculate tile size 
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int tileWidth = panelWidth / cols;
        int tileHeight = panelHeight / rows;
        
        // Draw the grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = c * tileWidth;
                int y = r * tileHeight;
                
                Cell cell = grid.getCell(r, c);
                
                if (cell.isVisible()) {
                    // Gambar floor texture 
                    Image floorTexture = TextureManager.getInstance().getTexture(cell.getFloorTexture());
                    if (floorTexture != null) {
                        g.drawImage(floorTexture, x, y, tileWidth, tileHeight, null);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y, tileWidth, tileHeight);
                    }

                    GameObject content = cell.getContent();

                    for (Gold gold : grid.getGoldList()) {
                        if (gold.getX() == r && gold.getY() == c) {
                            gold.draw(g, x, y, tileWidth, tileHeight);
                        }
                    }

                    for (Wumpus wumpus : grid.getWumpusList()) {
                        if (wumpus.getX() == r && wumpus.getY() == c) {
                            wumpus.draw(g, x, y, tileWidth, tileHeight);
                        }
                    }
                    for (Pit pit : grid.getPitList()) { 
                        if (pit.getX() == r && pit.getY() == c) {
                            pit.draw(g, x, y, tileWidth, tileHeight);
                        }
                    }

                    if (content instanceof Player) {
                        content.draw(g, x, y, tileWidth, tileHeight);
                    } else if (content != null) {
                        content.draw(g, x, y, tileWidth, tileHeight);
                    }
                } else {
                    Image wallTexture = TextureManager.getInstance().getTexture("wall");
                    if (wallTexture != null) {
                        g.drawImage(wallTexture, x, y, tileWidth, tileHeight, null);
                    } else {
                        g.setColor(Color.GRAY);
                        g.fillRect(x, y, tileWidth, tileHeight);
                    }
                }
                
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileWidth, tileHeight);
            }
        }
        
        updateSensationTextField();
    }
    
    private void updateSensationTextField() {
        if (gameManager == null || gameManager.getPlayer() == null || jTextFieldSensation == null) {
            return;
        }
        
        Player player = gameManager.getPlayer();
        int playerX = player.getX();
        int playerY = player.getY();
        
        GameGrid grid = gameManager.getGrid();
        ArrayList<String> sensations = new ArrayList<>();
        
        // Periksa sensasi di seluruh arah
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int newX = playerX + dir[0];
            int newY = playerY + dir[1];
            
            if (grid.isValidPosition(newX, newY)) {
                GameObject obj = grid.getCell(newX, newY).getContent();
                
                if (obj != null && !(obj instanceof Player)) {
                    String sensation = obj.getSensation();
                    if (!sensation.isEmpty() && !sensations.contains(sensation)) {
                        sensations.add(sensation);
                    }
                }
            }
        }
        
        StringBuilder sensorText = new StringBuilder();
        for (String sensation : sensations) {
            if (sensorText.length() > 0) {
                sensorText.append(" | ");
            }
            sensorText.append(sensation);
        }
        
        // Update text field dengan sensasi yang terdeteksi
        jTextFieldSensation.setText(sensorText.toString());
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                System.out.println("Tombol UP ditekan");
                gameManager.handlePlayerMove(-1, 0);
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("Tombol DOWN ditekan");
                gameManager.handlePlayerMove(1, 0);
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("Tombol LEFT ditekan");
                gameManager.handlePlayerMove(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("Tombol RIGHT ditekan");
                gameManager.handlePlayerMove(0, 1);
                break;
            case KeyEvent.VK_G:
                System.out.println("Tombol G ditekan → attemptPickupGold()");
                gameManager.attemptPickupGold();
                break;
        }
        requestFocusInWindow();
    }



    
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
}
