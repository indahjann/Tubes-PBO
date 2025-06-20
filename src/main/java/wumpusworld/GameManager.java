/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

import java.util.Random;
import javax.swing.SwingUtilities;

/**
 *
 * @author Haidar Ali
 */
public class GameManager {
    private GameGrid grid;
    private Player player;
    private int initialPlayerX;
    private int initialPlayerY;
    private boolean allGoldCollected;
    private boolean isGameOver;
    private boolean isGameWon;
    private WumpusMain mainFrame;
    private Random random;
    
    public GameManager(WumpusMain mainFrame) {
        this.mainFrame = mainFrame;
        this.random = new Random();
        this.initialPlayerX = 0;
        this.initialPlayerY = 0;
        this.allGoldCollected = false;
        this.isGameOver = false;
        this.isGameWon = false;
    }
    
    public void initializeGame() {
        // Create a new game grid (e.g., 10x10)
        grid = new GameGrid(10, 10);
        
        // Create and place player
        player = new Player(initialPlayerX, initialPlayerY);
        grid.placeObject(player, initialPlayerX, initialPlayerY);
        
        // Ubah floor texture di koordinat (0,0) - tambah baris ini
        grid.getCell(0, 0).setFloorTexture("Enterance");
        
        // Reveal starting cell and adjacent cells
        grid.getCell(initialPlayerX, initialPlayerY).setVisible(true);
        revealCellsAroundPlayer();
        
        // Place Gold objects
        placeRandomObjects(Gold.class, 3);
        
        // Place Wumpus
        placeRandomObjects(Wumpus.class, 3);
        
        // Place Pits
        placeRandomObjects(Pit.class, 5);
        
        // Update UI
        mainFrame.updateStatus(player.getScore(), player.getGoldCollected(), allGoldCollected, initialPlayerX, initialPlayerY);
        mainFrame.redrawGrid();
    }
    
    private void placeRandomObjects(Class<?> objectType, int count) {
        int placed = 0;
        int maxAttempts = 100; // Prevent infinite loop
        int attempts = 0;
        
        while (placed < count && attempts < maxAttempts) {
            int r = random.nextInt(grid.getRows());
            int c = random.nextInt(grid.getCols());
            
            // Skip initial player position
            if (r == initialPlayerX && c == initialPlayerY) {
                attempts++;
                continue;
            }
            
            Cell cell = grid.getCell(r, c);
            if (cell.isEmpty()) {
                try {
                    GameObject obj = null;
                    
                    if (objectType == Gold.class) {
                        obj = new Gold(r, c);
                    } else if (objectType == Wumpus.class) {
                        obj = new Wumpus(r, c);
                    } else if (objectType == Pit.class) {
                        obj = new Pit(r, c);
                    }
                    
                    if (obj != null) {
                        grid.placeObject(obj, r, c);
                        placed++;
                    }
                } catch (Exception e) {
                    System.err.println("Error creating object: " + e.getMessage());
                }
            }
            attempts++;
        }
    }
    
    public void resetGame() {
        this.allGoldCollected = false;
        this.isGameOver = false;
        this.isGameWon = false;
        initializeGame();
    }
   

    public void handlePlayerMove(int dx, int dy) {
    if (isGameOver || isGameWon) {
        return;
    }

    int newX = player.getX() + dx;
    int newY = player.getY() + dy;

    if (!grid.isValidPosition(newX, newY)) {
        return; // di luar grid
    }

    // 1. Simpan isi sel tujuan (bisa null, Gold, Pit, atau Wumpus)
    Cell destCell = grid.getCell(newX, newY);
    GameObject target = destCell.getContent();

    // 2. Pindahkan pemain:
    int oldX = player.getX();
    int oldY = player.getY();

    player.move(dx, dy);
    grid.getCell(oldX, oldY).setContent(null);
    grid.placeObject(player, newX, newY);

    // 3. Kurangi skor pergerakan
    player.setScore(player.getScore() - 10);

    // 4. Reveal kotak tujuan, lalu repaint dan update label
    destCell.setVisible(true);
    revealCellsAroundPlayer();

    mainFrame.redrawGrid();
    mainFrame.updateStatus(player.getScore(), player.getGoldCollected(), allGoldCollected, 0, 0);

    // 5. Setelah repaint selesai, cek apakah kotak tadi Pit atau Wumpus
    SwingUtilities.invokeLater(() -> {
        if (target instanceof Pit) {
            isGameOver = true;
            mainFrame.showGameOverMessage("Player fell into a pit! Game Over.");
        }
        else if (target instanceof Wumpus) {
            isGameOver = true;
            mainFrame.showGameOverMessage("Player encountered the Wumpus! Game Over.");
        }
        else {
            if (!isGameOver) {
                checkGameStatus();  
            }
        }
    });
    }

    
    public void attemptPickupGold() {
        if (isGameOver || isGameWon) return;

        int x = player.getX();
        int y = player.getY();

        Gold found = null;
        for (Gold g : grid.getGoldList()) {
            if (g.getX() == x && g.getY() == y) {
                found = g;
                break;
            }
        }

        if (found != null) {
            System.out.println("Gold ditemukan di sel (" + x + "," + y + "). Mengambil...");
            // 1. Tambah goldCollected dan skor
            player.pickupGold();
            player.setScore(player.getScore() + 1000);

            // 2. Hapus Gold itu dari list dan dari grid
            grid.getGoldList().remove(found);
            Cell currentCell = grid.getCell(x, y);
            currentCell.setContent(null);
            currentCell.setFloorTexture("floor"); // <-- Tambahkan baris ini!
            grid.placeObject(player, x, y);

            allGoldCollected = grid.getGoldList().isEmpty();

            if (allGoldCollected && !(player.getX() == initialPlayerX && player.getY() == initialPlayerY)) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mainFrame.showInfoMessage("All gold has been collected!\nReturn to the Cave Enterance to win!!.");
                });
            }

            // 3. Update UI
            mainFrame.updateStatus(player.getScore(), player.getGoldCollected(), allGoldCollected, 0, 0);
            mainFrame.redrawGrid();

            // 4. Cek kemenangan (semua Gold terkumpul & kembali ke (0,0))
            if (allGoldCollected && player.getX() == initialPlayerX && player.getY() == initialPlayerY) {
                isGameWon = true;
                mainFrame.showGameWinMessage(
                    "Congratulations! You won with a score of " + player.getScore() + "!"
                );
            }
        } else {
            System.out.println("Tidak ada Gold di sel ini.");
        }

        
    }


    
    public void checkGameStatus() {
        // Periksa jika menang (semua Gold diambil dan kembali ke (0,0))
        if (allGoldCollected && player.getX() == initialPlayerX && player.getY() == initialPlayerY) {
            isGameWon = true;
            mainFrame.showGameWinMessage("Congratulations! You won with a score of " + player.getScore() + "!");
        }
    }
    
    public void revealCellsAroundPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();
        
        // Reveal current cell
        grid.getCell(playerX, playerY).setVisible(true);
        
    }
    
    // Getters
    public GameGrid getGrid() {
        return grid;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public boolean isGameWon() {
        return isGameWon;
    }
    
    public int getScore() {
        return player.getScore();
    }
    
    public int getRemainingGold() {
        return grid.getGoldList().size();
    }
    
    public boolean hasAllGoldCollected() {
        return allGoldCollected;
    }
}
