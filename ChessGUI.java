import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

//TODO FLOW:     MVC refactor progress ---> legal move verification --> checkmate detection ---->  UI FEATURES (score displayed from capture mechanic, main menu frame ideally to prepare for networking additions) --> networking (localhost socket programming) --> onwards and upwards
public class ChessGUI extends JFrame implements MouseListener, MouseMotionListener {
	private ChessController ctrl;
	private JPanel board;
	private JLayeredPane layeredPane;
	private JLabel chessPiece;
	private int xAdjustment;
	private int yAdjustment;
	private Dimension boardSize;
	private int heldGridPosition = -1;
	final String[] whitePieces = { "whiteking.png", "whitepawn.png", "whitebishop.png", "whiteknight.png", "whiterook.png", "whitequeen.png"};
	final String[] blackPieces = { "blackking.png", "blackpawn.png", "blackbishop.png", "blackknight.png", "blackrook.png", "blackqueen.png"};
	//private ChessPiece lastHeld;

	public ChessGUI(ChessController ctrl) {
		this.ctrl = ctrl;
		board = new JPanel();
		this.boardSize = new Dimension(600, 600);
		layeredPane = new JLayeredPane();
		this.getContentPane().add(layeredPane);
		layeredPane.setPreferredSize(boardSize);
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);
		layeredPane.add(board, JLayeredPane.DEFAULT_LAYER);
		board.setLayout(new GridLayout(8,8));
		board.setPreferredSize(boardSize);
		board.setBounds(0,0,boardSize.width, boardSize.height);

		for (int i = 0; i < 64; i++) {
			JPanel square = new JPanel(new BorderLayout());
			board.add(square);
			int row = i/8;
			row %= 2;
			if(row == 0) square.setBackground(i%2 == 0 ? Color.white : Color.gray);
			else square.setBackground(i%2 == 0 ? Color.gray : Color.white);
	
		}
	
		//pces
		
		this.initializeBoard(board);
	}
	
	public void removePiece(int pos) {
		System.out.printf("ChessGUI: removePiece(%d)\n", pos);
		
		/*if(c instanceof JLabel) {
			Container parent = c.getParent();
			parent.remove(0);
			return true;
		}
		Container parent = (Container) c;
		parent.remove(0);
		return true;*/
		
		Component c = board.getComponent(pos);
		Container cn = (Container) c;
		cn.remove(0);
		board.repaint();
	}
	
	private void initializeBoard(JPanel board) {
		
			for(int i = 0; i < 64; i++) {
				JPanel panel = (JPanel) board.getComponent(i);
				ChessPiece piece = this.ctrl.fetchPiece(i);
				JLabel piece_label;
				int piw = 0;
				int pib = 0;
				if(piece != null) {
					if(piece.getColor() == 0) {
						piece_label = new JLabel(new ImageIcon(whitePieces[piece.getType()]));
					} else {
						piece_label = new JLabel(new ImageIcon(blackPieces[piece.getType()]));
					}
					panel.add(piece_label);
				}
			}
		}	

	public void mousePressed(MouseEvent e) {
		//this.gameState._printGameState();
		chessPiece = null;
		Component c = board.findComponentAt(e.getX(), e.getY());
		
		if(c instanceof JPanel)
			return;
		
		Point parentLocation = c.getParent().getLocation();
		int gridX = (int) (parentLocation.x/(boardSize.getWidth()/8));
		int gridY = (int) (parentLocation.y/(boardSize.getHeight()/8));
		int gridPosition = ChessController.joinGridPos(gridX, gridY);
		System.out.printf("mousePressed: gridPosition = %d\n", gridPosition);
		
		ChessPiece currentPieceInGame = this.ctrl.fetchPiece(gridPosition);
		//System.out.println(currentPieceInGame);
		if(currentPieceInGame != null) {
			System.out.printf("mousePressed: currentPieceInGame = %s\n", ChessGame.nameFromType[currentPieceInGame.getType()]);
			int targetColor = currentPieceInGame.getColor();
			if((this.ctrl.whiteToMove() && targetColor == 0) || (!this.ctrl.whiteToMove() && targetColor == 1)) {
				xAdjustment = parentLocation.x - e.getX();
				yAdjustment = parentLocation.y - e.getY();
				chessPiece = (JLabel) c;
				chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
				chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
				layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
				heldGridPosition = gridPosition;
				//lastHeld = this.ctrl.fetchPiece(gridPosition);
				//System.out.println(lastHeld);
			} 
		}
		
	}
	
	public void updatePiece(int pos) {
		System.out.printf("ChessGUI: updatePiece(%d)\n", pos);
		chessPiece.setVisible(false);
		JPanel sq = (JPanel) board.getComponent(pos);
		
		ChessPiece updated = this.ctrl.fetchPiece(pos);
		
		sq.add(chessPiece);
		chessPiece.setVisible(true);
		/*chessPiece.setVisible(false);
		JPanel sq = (JPanel) board.getComponent(pos);
		sq.add(chessPiece);
		chessPiece.setVisible(true);*/
		
	}
	
	public void gameOver(int endType, int colorWinner) { //colorWinner == -1 for endType == 1 (stalemate)
		System.out.println("GAME OVER!!!\n");
		if(endType == 0) {//checkmate 
			System.out.printf("Checkmate! %s wins!", (colorWinner == 0) ? "White" : "Black");
		} else if(endType == 1) {//stalemate
			System.out.println("Stalemate! You both lose.");
		} else {
			System.out.println("ChessGUI: gameOver: something went wrong. ***");
		}
		
	}
	public void mouseDragged(MouseEvent e) {
		if(chessPiece == null) return;
		chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
	}
	
	public void mouseReleased(MouseEvent e) {
		if(chessPiece == null) return;
		//Check legal move status according to game state information and event location.
		
		Component c = board.findComponentAt(e.getX(), e.getY());
		if(c == null) {
			chessPiece.setVisible(false);
			JPanel panel = (JPanel) board.getComponent(heldGridPosition);
			panel.add(chessPiece);
			chessPiece.setVisible(true);
			return;
		}
		//System.out.println(c);
		int newGridX = (int) (e.getX()/(boardSize.getWidth()/8));
		int newGridY = (int) (e.getY()/(boardSize.getHeight()/8));
		int newGridPos = ChessController.joinGridPos(newGridX, newGridY);
		if(newGridPos == this.heldGridPosition) {
			this.updatePiece(this.heldGridPosition);
			return;
		}
		//System.out.println(newGridPos);
		boolean check = this.ctrl.attemptMove(heldGridPosition, newGridPos);
		if(!check) {
			System.out.printf("ChessGUI: mouseReleased: attemptMove from %d to %d failed\n", heldGridPosition, newGridPos);
		}
		
		
	}
	

	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseMoved(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
	
	}
	
}
