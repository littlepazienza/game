import javax.swing.ImageIcon;

public abstract class Piece
{
	protected int file, row;
	protected Side color;
	
	public Piece(int f, int r, Side c)
	{
		this.file = f;
		this.row = r;
		this.color = c;
	}

	public abstract boolean validMove(int r, int f, Piece[][] A);

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f] != null && A[r][f].color != color;
	}
	
	public abstract ImageIcon getImage(boolean onWhite);

	public void setCoord(int r, int f)
	{
		this.row = r;
		this.file = f;
	}
	
	public abstract int value();
	
	public enum Side
	{
		WHITE, BLACK
	}
}
