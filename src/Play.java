import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Play extends JFrame implements ActionListener
{
	protected ImageIcon[] options = {new ImageIcon("res/white_queen_on_white.png"), new ImageIcon("res/white_knight_on_white.png"), new ImageIcon("res/white_bishop_on_white.png"), new ImageIcon("res/white_rook_on_white.png")};
	char[] files = {'a', 'b', 'c', 'd', 'e','f', 'g','h'};
	protected JButton[][] buttons;
	protected GameBoard g;
	boolean whiteTurn;
	protected int selectedR, selectedF;
	JPanel frame = new JPanel();
	static String p1, p2;
	
	
	public Play(String p, String q)
	{	
		g = new GameBoard();
		g.GameFill();
		whiteTurn = true;
		selectedR = -1;
		selectedF = -1;
		buttons = new tileButton[8][8];
		
		
		update(p, q);
		
		frame.setLayout(null);
		frame.setBackground(new Color(135, 67, 67));
		getContentPane().setBackground(new Color(30, 206, 219));
		getContentPane().add(frame, 0);
		frame.setVisible(true);
		
	}
		
	public static void main(String[] args)
	{
		Play p = new Play(args[0], args[1]);
		p.setVisible(true);
		p.setSize(1000, 1000);
		p1 = args[0];
		p2 = args[1];
	}

	public void actionPerformed(ActionEvent e) {
		tileButton b = (tileButton) e.getSource();
		
		if(selectedR == -1)
		{
			if(whiteTurn && g.board[b.row][b.file].color != Piece.Side.WHITE)
			{
				JOptionPane.showMessageDialog(null, "White's turn");
			}
			else if(!whiteTurn && g.board[b.row][b.file].color != Piece.Side.BLACK)
			{
				JOptionPane.showMessageDialog(null, "Black's turn");
			}else
			{
				selectedR = b.row;
				selectedF = b.file;
			}
		}
		else
		{
			if(g.makeMove(selectedR, selectedF, b.row, b.file))
			{
				selectedR = -1;
				if(whiteTurn)
					whiteTurn = false;
				else
					whiteTurn = true;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid Move");
				selectedR = -1;
			}
			
		}
		update(p1, p2);
	}
	
	private void update(String p, String q)
	{
		frame.removeAll();
		Pawn pwn = g.pawnPromotion();
		if(pwn != null)
		{
			char f = files[pwn.file];
			int n = JOptionPane.showOptionDialog(this, "Choose which piece to upgrade the " + f + " pawn to...", f + " Pawn Promotion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if(n == 0 || JOptionPane.CANCEL_OPTION == n)
				g.board[pwn.row][pwn.file] = new Queen(pwn.row, pwn.file, pwn.color);
			else if(n == 1)
				g.board[pwn.row][pwn.file] = new Knight(pwn.row, pwn.file, pwn.color);
			else if(n == 2)
				g.board[pwn.row][pwn.file] = new Bishop(pwn.row, pwn.file, pwn.color);
			else if(n == 3)
				g.board[pwn.row][pwn.file] = new Rook(pwn.row, pwn.file, pwn.color);
		}
		
		boolean white = true;
		for(int i = 0 ; i < 8;i++)
		{	
			if(white)
				white = false;
			else
				white = true;
			
			for(int j = 0; j < 8;j++)
			{				
				JButton button;
				if(g.board[i][j] != null)
					button = new tileButton(g.board[i][j].getImage(white), i, j);
				else
				{
					if(white)
						button = new tileButton(new ImageIcon("res/white.png"), i, j);
					else
						button = new tileButton(new ImageIcon("res/black.png"), i, j);
				}
				button.setBounds((180 + 75*j), (670 - 75*i), 75, 75);
				button.setIcon(resizeIcon(button.getIcon(), button.getWidth(), button.getHeight()));
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				button.addActionListener(this);
				frame.add(button);	
				
				buttons[i][j] = button;
				
				if(white)
					white =false;
				else
					white = true;
			}	
		}
		
		//whose turn is it
		JLabel turn = new JLabel((whiteTurn?p + "'s Turn":q + "'s Turn"));
		turn.setBounds(40, 400, 200, 100);
		turn.setFont(new Font(turn.getName(), Font.PLAIN, 18));
		turn.setForeground(Color.WHITE);
		
		//configure advantage
		int w = g.whiteSum();
		int b = g.blackSum();
		JLabel adv;
		
		if(w > b)
		{
			adv = new JLabel("+" + (w-b));
			adv.setBounds(200, 775, 100, 100);
			adv.setFont(new Font(adv.getName(), Font.PLAIN, 18));
			adv.setForeground(Color.WHITE);
			frame.add(adv);
		}
		else if(w < b)
		{
			adv = new JLabel("+" + (b-w));
			adv.setBounds(200, 25, 100, 100);
			adv.setFont(new Font(adv.getName(), Font.PLAIN, 18));
			adv.setForeground(Color.WHITE);
			frame.add(adv);
		}
		
		//pieces captured
		w = 0;
		b = 0;
		JLabel lbl;
		for(Piece pie : g.captured)
		{
			if(pie.color == Piece.Side.WHITE)
			{
				lbl = new JLabel(pie.getImage(false));
				lbl.setBounds(225 + 25 * w,  50, 50, 50);
				w++;
				frame.add(lbl);
			}
			else
			{
				lbl = new JLabel(pie.getImage(false));
				lbl.setBounds(225 + 25 * b, 800, 50, 50);
				b++;
				frame.add(lbl);
			}
			
		}
		
		//teams
		JButton whiteBtn = new JButton(p);
		whiteBtn.setBackground(new Color(135, 67, 67));
		whiteBtn.setBounds(75, 775, 100, 100);
		whiteBtn.setForeground(Color.BLACK);
		whiteBtn.setBackground(Color.WHITE);
		whiteBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		whiteBtn.setEnabled(false);
		frame.add(whiteBtn);
		
		JButton blackBtn = new JButton(q);
		blackBtn.setBackground(new Color(135, 67, 67));
		blackBtn.setBounds(75, 30, 100, 100);
		blackBtn.setForeground(Color.WHITE);
		blackBtn.setBackground(Color.BLACK);
		blackBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		blackBtn.setEnabled(false);
		frame.add(blackBtn);
		
		frame.add(turn);
		frame.repaint();
	}
	
	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
	    Image img = ((ImageIcon) icon).getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
	
	class tileButton extends JButton
	{
		protected int file, row;
		public tileButton(ImageIcon txt, int r, int f) 
		{
			super(txt);
			this.row = r;
			this.file = f;
		}
		
	}
}
