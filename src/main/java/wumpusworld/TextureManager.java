package wumpusworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class TextureManager {
    private static TextureManager instance;
    private HashMap<String, Image> textures;
    
    private TextureManager() {
        textures = new HashMap<>();
        loadTextures();
    }
    
    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }
    
    private void loadTextures() {
        loadTexture("floor", "floor.png");
        loadTexture("wall", "wall.png");
        loadTexture("player", "player.gif");
        loadTexture("wumpus", "wumpus.gif");
        loadTexture("pit", "pit.png");
        loadTexture("gold", "gold.png");
        loadTexture("Enterance", "Enterance.png");
    }
    
    private void loadTexture(String name, String fileName) {
        try {
            System.out.println("Trying to load texture: " + fileName);
            
            File imageFile = new File("src/main/java/resources/textures/" + fileName);
            if (imageFile.exists()) {
                System.out.println("Found file at: " + imageFile.getAbsolutePath());
                Image img = new ImageIcon(imageFile.getAbsolutePath()).getImage();
                textures.put(name, img);
                System.out.println("Successfully loaded texture: " + name);
                return;
            }
            
            String[] paths = {
                "resources/textures/" + fileName,
                "./resources/textures/" + fileName,
                "./src/main/resources/textures/" + fileName,
                "../resources/textures/" + fileName
            };
            
            for (String path : paths) {
                imageFile = new File(path);
                if (imageFile.exists()) {
                    System.out.println("Found file at: " + imageFile.getAbsolutePath());
                    Image img = new ImageIcon(imageFile.getAbsolutePath()).getImage();
                    textures.put(name, img);
                    System.out.println("Successfully loaded texture: " + name);
                    return;
                }
            }
            
            System.out.println("Failed to find file: " + fileName + " in any location");
            textures.put(name, createPlaceholderImage(name));
            
        } catch (Exception e) {
            System.out.println("Exception loading texture: " + fileName);
            e.printStackTrace();
            textures.put(name, createPlaceholderImage(name));
        }
    }
    
    // gambar placeholder
    private Image createPlaceholderImage(String name) {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        // Warna berbeda untuk placeholder berbeda
        if (name.equals("player")) {
            g.setColor(Color.BLUE);
        } else if (name.equals("wumpus")) {
            g.setColor(Color.RED);
        } else if (name.equals("gold")) {
            g.setColor(Color.YELLOW);
        } else if (name.equals("pit")) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.GRAY);
        }
        
        g.fillRect(0, 0, 32, 32);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, 31, 31);
        g.drawString(name.substring(0, Math.min(5, name.length())), 2, 20);
        g.dispose();
        return img;
    }
    
    public Image getTexture(String name) {
        Image img = textures.get(name);
        if (img == null) {
            System.out.println("ERROR: Texture '" + name + "' not found in texture map");
            return null;
        } else if (img.getWidth(null) <= 0) {
            System.out.println("ERROR: Texture '" + name + "' was loaded but has invalid dimensions");
            return null;
        }
        return img;
    }
}
