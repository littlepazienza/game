package Game;
import java.util.ArrayList;

public class GameBoard
{

	protected char[] files = {'a', 'b', 'c', 'd', 'e','f', 'g','h'};
	public Piece[][] board = new Piece[8][8];
	public ArrayList<Piece> captured;
	public final int BLACK_WIN = 0;
	public final int WHITE_WIN = 1;
	protected final int CONTINUE = 2;
	
	public GameBoard()
	{
		board = new Piece[8][8];
		captured = new ArrayList<>();
	}
	
	public void GameFill()
	{
		//fill white
		board[0][0] = new Rook(0, 0, Piece.Side.WHITE);
		board[0][1] = new Knight(0, 1, Piece.Side.WHITE);
		board[0][2] = new Bishop(0, 2, Piece.Side.WHITE);
		board[0][3] = new Queen(0, 3, Piece.Side.WHITE);
		board[0][4] = new King(0, 4, Piece.Side.WHITE);
		board[0][5] = new Bishop(0, 5, Piece.Side.WHITE);
		board[0][6] = new Knight(0, 6, Piece.Side.WHITE);
		board[0][7] = new Rook(0, 7, Piece.Side.WHITE);
		board[1][0] = new Pawn(1, 0, Piece.Side.WHITE);
		board[1][1] = new Pawn(1, 1, Piece.Side.WHITE);
		board[1][2] = new Pawn(1, 2, Piece.Side.WHITE);
		board[1][3] = new Pawn(1, 3, Piece.Side.WHITE);
		board[1][4] = new Pawn(1, 4, Piece.Side.WHITE);
		board[1][5] = new Pawn(1, 5, Piece.Side.WHITE);
		board[1][6] = new Pawn(1, 6, Piece.Side.WHITE);
		board[1][7] = new Pawn(1, 7, Piece.Side.WHITE);
		
		//fill black
		board[7][7] = new Rook(7, 7, Piece.Side.BLACK);
		board[7][6] = new Knight(7, 6, Piece.Side.BLACK);
		board[7][5] = new Bishop(7, 5, Piece.Side.BLACK);
		board[7][3] = new Queen(7, 3, Piece.Side.BLACK);
		board[7][4] = new King(7, 4, Piece.Side.BLACK);
		board[7][2] = new Bishop(7, 2, Piece.Side.BLACK);
		board[7][1] = new Knight(7, 1, Piece.Side.BLACK);
		board[7][0] = new Rook(7, 0, Piece.Side.BLACK);
		board[6][7] = new Pawn(6, 7, Piece.Side.BLACK);
		board[6][6] = new Pawn(6, 6, Piece.Side.BLACK);
		board[6][5] = new Pawn(6, 5, Piece.Side.BLACK);
		board[6][4] = new Pawn(6, 4, Piece.Side.BLACK);
		board[6][3] = new Pawn(6, 3, Piece.Side.BLACK);
		board[6][2] = new Pawn(6, 2, Piece.Side.BLACK);
		board[6][1] = new Pawn(6, 1, Piece.Side.BLACK);
		board[6][0] = new Pawn(6, 0, Piece.Side.BLACK);

		for(int i = 2; i < 6;i++)
			for(int j = 0; j < 8;j++)
				board[i][j] = null;
	}
	
	public int endGameStatus()
	{
		if(whiteInCheck())
		{
			if(whiteCheckMate())
			{
				return BLACK_WIN;
			}
		}
		else if (blackInCheck())
		{
			if(blackCheckMate())	
			{
				return WHITE_WIN;
			}
		}
		return CONTINUE;
	}

	public boolean makeMove(int fromR, int fromF, int toR, int toF)
	{
		if(board[fromR][fromF] == null)
			return false;

		if(!board[fromR][fromF].validMove(toR, toF, board))
			return false;
		
		if(board[toR][toF] != null)
		{

			if(board[toR][toF].color == board[fromR][fromF].color && board[toR][toF] instanceof Rook && board[fromR][fromF] instanceof King)
			{
				if(inCheckIfMoveMade(fromR, fromF, toR, toF, board[fromR][fromF].color))
				{
					return false;
				}
				else
				{
					board[fromR][fromF].setSelected();
					swapCastle(fromR, fromF, toR, toF, board);
					return true;
				}

			}
			else if(board[toR][toF].color == board[fromR][fromF].color)
			{
				return false;
			}
			else
			{

				if(inCheckIfMoveMade(fromR, fromF, toR, toF, board[fromR][fromF].color))
				{
					return false;
				}
				else
				{
					board[fromR][fromF].setSelected();
					captured.add(board[toR][toF]);
					board[toR][toF] = board[fromR][fromF];
					board[toR][toF].setCoord(toR, toF);
					board[fromR][fromF] = null;
					return true;
				}

			}
		}
		else
		{
			if(inCheckIfMoveMade(fromR, fromF, toR, toF, board[fromR][fromF].color))
				return false;
			else
			{
				board[fromR][fromF].setSelected();
				board[toR][toF] = board[fromR][fromF];
				board[toR][toF].setCoord(toR, toF);
				board[fromR][fromF] = null;
				return true;
			}
		}		
	}
	
	public boolean fakeMove(int fromR, int fromF, int toR, int toF, Piece[][] P)
	{
		if(P[fromR][fromF] == null)
			return false;

		if(!P[fromR][fromF].validMove(toR, toF, P))
			return false;
		
		if(P[toR][toF] != null)
		{

			if(P[toR][toF].color == P[fromR][fromF].color && P[toR][toF] instanceof Rook && P[fromR][fromF] instanceof King)
			{
				if(inCheckIfMoveMade(fromR, fromF, toR, toF, P[fromR][fromF].color))
				{
					return false;
				}
				else
				{
					swapCastle(fromR, fromF, toR, toF, P);
					return true;
				}

			}
			else if(P[toR][toF].color == P[fromR][fromF].color)
			{
				return false;
			}
			else
			{

				if(inCheckIfMoveMade(fromR, fromF, toR, toF, P[fromR][fromF].color))
				{
					return false;
				}
				else
				{
					P[toR][toF] = P[fromR][fromF];
					P[toR][toF].setCoord(toR, toF);
					P[fromR][fromF] = null;
					return true;
				}

			}
		}
		else
		{
			if(inCheckIfMoveMade(fromR, fromF, toR, toF, P[fromR][fromF].color))
				return false;
			else
			{
				P[toR][toF] = P[fromR][fromF];
				P[toR][toF].setCoord(toR, toF);
				P[fromR][fromF] = null;
				return true;
			}
		}		
	}
	
	private void swapCastle(int fR, int fF, int tR, int tF, Piece[][] P)
	{
		if(fR == 0)
		{
			if(tF == 0)
			{
				Piece rook = P[tR][tF];
				Piece king = P[fR][fF];
				P[tR][tF] = null;
				P[fR][fF] = null;
				P[0][2] = king;
				P[0][3] = rook;
				king.setCoord(0, 2);
				rook.setCoord(0, 3);
			}
			else
			{
				Piece rook = P[tR][tF];
				Piece king = P[fR][fF];
				P[tR][tF] = null;
				P[fR][fF] = null;
				P[0][6] = king;
				P[0][5] = rook;
				king.setCoord(0, 6);
				rook.setCoord(0, 5);
			}
		}
		else if(fR == 7)
		{
			if(tF == 0)
			{
				Piece rook = P[tR][tF];
				Piece king = P[fR][fF];
				P[tR][tF] = null;
				P[fR][fF] = null;
				P[7][2] = king;
				P[7][3] = rook;
				king.setCoord(7, 2);
				rook.setCoord(7, 3);
			}
			else
			{
				Piece rook = P[tR][tF];
				Piece king = P[fR][fF];
				P[tR][tF] = null;
				P[fR][fF] = null;
				P[0][6] = king;
				P[0][5] = rook;
				king.setCoord(7, 6);
				rook.setCoord(7, 5);
			}
		}
	}
	
	public Piece[][] getTempOfBoard()
	{
		Piece[][] temp = new Piece[8][8];
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				if(board[i][j] == null)
					temp[i][j] = board[i][j];
				if(board[i][j] instanceof Pawn)
					temp[i][j] = new Pawn(board[i][j].row, board[i][j].file, board[i][j].color);
				if(board[i][j] instanceof Bishop)
					temp[i][j] = new Bishop(board[i][j].row, board[i][j].file, board[i][j].color);
				if(board[i][j] instanceof Knight)
					temp[i][j] = new Knight(board[i][j].row, board[i][j].file, board[i][j].color);
				if(board[i][j] instanceof Rook)
					temp[i][j] = new Rook(board[i][j].row, board[i][j].file, board[i][j].color);
				if(board[i][j] instanceof Queen)
					temp[i][j] = new Queen(board[i][j].row, board[i][j].file, board[i][j].color);
				if(board[i][j] instanceof King)
					temp[i][j] = new King(board[i][j].row, board[i][j].file, board[i][j].color);
			}
				
		return temp;
	}

	private boolean inCheckIfMoveMade(int fromR, int fromF, int toR, int toF, Piece.Side s)
	{
		if(fromR == toR && fromF == toF)
			return false;
		
		Piece[][] P = getTempOfBoard();
		P[toR][toF] = P[fromR][fromF];
		P[toR][toF].setCoord(toR, toF);
		P[fromR][fromF] = null;

		return attackingKing(P, s);
	}	
	
	private boolean attackingKing(Piece[][] A, Piece.Side s)
	{
		int r = rowOfKing(s, A);
		int f = fileOfKing(s, A);
		
		for(int i = 0; i < 8;i++)
			for(int j =0;j<8;j++)
				if(A[i][j]!= null && A[i][j].attacking(r, f, A))
					return true;
		return false;
	}
	
	private boolean whiteInCheck()
	{
		return attackingKing(board, Piece.Side.WHITE);
	}
	
	private boolean blackInCheck()
	{
		return attackingKing(board, Piece.Side.BLACK);
	}
	
	private int rowOfKing(Piece.Side c, Piece[][] A)
	{
		for(int i = 0; i < 8;i++)
			for(int j = 0; j < 8;j++)
				if(A[i][j] instanceof King && A[i][j].color == c)
					return i;
		
		return -1;
	}

	private int fileOfKing(Piece.Side c, Piece[][] A)
	{
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8;j++)
				if(A[i][j] instanceof King && A[i][j].color == c)
					return j;

		return -1;
	}

	public Pawn pawnPromotion()
	{
		for(int i = 0;  i < 8;i++)
		{
			if(board[7][i] instanceof Pawn)
				return (Pawn) board[7][i];
			else if(board[0][i] instanceof Pawn)
				return (Pawn) board[0][i];
		}
		return null;
	}
	
	public boolean whiteCheckMate()
	{	
		for(int i=1;i<8;i++)
			for(int j=0;j<8;j++)
				for(int k =0; k < 8;k++)
					for(int z =0;z<8;z++)
						if(board[k][z] != null && board[k][z].color == Piece.Side.WHITE && fakeMove(k, z, i, j, getTempOfBoard()))
							return false;
		return true;		
	}
	
	public boolean blackCheckMate()
	{			
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				for(int k=0;k<8;k++)
					for(int z=0;z<8;z++)
						if(board[k][z] != null && board[k][z].color == Piece.Side.BLACK && fakeMove(k, z, i, j, getTempOfBoard()))
							return false;
		return true;		
	}
	
	public int whiteSum()
	{
		int sum = 0;
		for(int i = 0; i < 8;i++)
			for(int j = 0; j < 8;j++)
			{
				if(board[i][j] != null && board[i][j].color == Piece.Side.WHITE)
					sum+= board[i][j].value();
			}
		
		return sum;
	}
	
	public int blackSum()
	{
		int sum = 0;
		for(int i = 0; i < 8;i++)
			for(int j = 0; j < 8;j++)
			{
				if(board[i][j] != null && board[i][j].color == Piece.Side.BLACK)
					sum+= board[i][j].value();
			}
		
		return sum;
	}
	
	public String moveNotation(int fromR, int fromF, int toR, int toF, Piece[][] P)
	{
		Piece from = P[fromR][fromF];
		String O = "";
		
		if(from instanceof Pawn)
			if(P[toR][toF] != null)
				O+= files[fromF] + "x" + files[toF] + toR;
			else
				O+= "" + files[toF] +  (toR + 1);
		if(from instanceof Bishop)
			if(P[toR][toF] != null)
				O+= "Bx" + files[toF] +  (toR + 1);
			else
				O+= "B" + files[toF] +  (toR + 1);
		if(from instanceof Rook)
			if(P[toR][toF] != null)
				O+= "Rx" + files[toF] +  (toR + 1);
			else
				O+= "R" + files[toF] +  (toR + 1);
		if(from instanceof Queen)
			if(P[toR][toF] != null)
				O+= "Qx" + files[toF] + (toR + 1);
			else
				O+= "Q" + files[toF] + (toR + 1);
		if(from instanceof King)
			if(P[toR][toF] != null)
				O+= "Kx" + files[toF] +  (toR + 1);
			else
				O+= "K" + files[toF] +  (toR + 1);
		if(from instanceof Knight)
			if(P[toR][toF] != null)
				O+= "Nx" + files[toF] + (toR + 1);
			else
				O+= "N" + files[toF] + (toR + 1);
		else
			O+= "";
		
		
		if(whiteCheckMate())
			return O + "#";
		else
		{
			
			P[toR][toF] = P[fromR][fromF];
			P[fromR][fromF] = null;
			
			if(attackingKing(P, (P[toR][toF].color == Piece.Side.WHITE ? Piece.Side.BLACK:Piece.Side.WHITE)))
				return O + "+";
			else
				return O;
		}
		
	}
}