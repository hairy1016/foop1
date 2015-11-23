import java.util.*;
class Computer {
	public Player[] players = new Player[4];
	private Vector<Integer> loser = new Vector<Integer>(0);
	public Random who = new Random();
	public String[] color = {"C","D","H","S"};
	public String[] number = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
	private int turnTo, turnFr;
	public void Computer() {}
	public void initialize() {
		loser = new Vector<Integer>(0);
		for(int i = 0; i < 4; i++){
			players[i] = new Player();
			loser.add(i);
		}
	}
	public void dealcard() {
		int [] max = {14,14,13,13};
		System.out.println("Deal cards");
		int whos = who.nextInt(4);
		players[whos].takeCard("R0");
		whos = who.nextInt(4);
		players[whos].takeCard("B0");
		for(int n = 0; n < 13; n++){
			for (int c = 0; c < 4; c++){
				whos = who.nextInt(4);
				if (players[whos].getNum() == max[whos]){
					while (players[whos].getNum() == max[whos]){
						whos = (whos + 1) % 4;
					}
				}
				players[whos].takeCard(color[c]+number[n]);
			}
		}
		for(int i = 0; i < 4; i++){
			System.out.print("Player"+i+":");
			players[i].print();
		}
	}
	public void dropcard() {
		System.out.println("Drop cards");
		for(int i = 0; i < 4; i++){
			System.out.print("Player"+i+":");
			players[i].drop();
			players[i].print();
		}
	}
	private void onechange(int to, int from) {
		String drawcard;
		drawcard=players[from].draw(who.nextInt(players[from].getNum()));
		System.out.println("Player"+to+" draws a card form Player"+from+" "+drawcard);
		players[to].insert(drawcard);
		System.out.print("Player"+to+":");
		players[to].print();
		System.out.print("Player"+from+":");
		players[from].print();
	}
	private void win(int to, int from){
		int tem;
		if (players[loser.get(to)].getNum()==0 && players[loser.get(from)].getNum()==0) {
			System.out.print("Player"+Math.min(loser.get(to),loser.get(from))+" and Player"+Math.max(loser.get(to),loser.get(from))+" win\n");
			if (to > from) {
				loser.remove(to);
				loser.remove(from);
				tem = from;
			}
			else {
				loser.remove(to);
				loser.remove(to);
				tem = to % loser.size();
			}
		}
		else if(players[loser.get(to)].getNum()==0){
			System.out.println("Player" + loser.get(to) + " wins");
			loser.remove(to);
			tem = to % loser.size();
		}
		else {
			System.out.println("Player" + loser.get(from) + " wins");
			loser.remove(from);
			tem = from % loser.size();
		}
		turnTo = tem;
		turnFr = (turnTo + 1) % loser.size();
	}
	public void basicgame() {
		turnTo = 0;
		turnFr = 1;
		System.out.println("Game start");
		String drawcard;
		while(true) {
			onechange(loser.get(turnTo),loser.get(turnFr));
			if (players[loser.get(turnFr)].getNum()!=0 && players[loser.get(turnTo)].getNum()!=0){
				turnTo = (turnTo + 1) % loser.size();
				turnFr = (turnFr + 1) % loser.size();
			}
			else {break;}
		}
		win(turnTo,turnFr);
		System.out.println("Basic game over\nContinue");
	}
	public void bonusgame() {
		while(turnTo != turnFr) {
			onechange(loser.get(turnTo),loser.get(turnFr));
			if (players[loser.get(turnFr)].getNum()!=0 && players[loser.get(turnTo)].getNum()!=0){
				turnTo = (turnTo + 1) % loser.size();
				turnFr = (turnFr + 1) % loser.size();
			}
			else {win(turnTo,turnFr);}
		}
		System.out.println("Bonus game over.");
	}
}
class ungguy extends Computer{
	private int playNum;
	public void dealcard() {
		int [] max = {13,13,13,13};
		System.out.println("Deal cards");
		int no = who.nextInt(13);
		int co = who.nextInt(4);
		int whos = 0;
		for(int n = 0; n < 13; n++){
			for (int c = 0; c < 4; c++){
				whos = who.nextInt(4);
				if(n == no && c == co){}
				else {
					if (players[whos].getNum() == max[whos]){
						while (players[whos].getNum() == max[whos]){
							whos = (whos + 1) % 4;
						}
					}
					players[whos].takeCard(color[c]+number[n]);
				}
			}
		}
		for(int i = 0; i < 4; i++){
			System.out.print("Player"+i+":");
			players[i].print();
		}
	}
}
class jackless extends Computer {
	public void dealcard() {
		int [] max = {13,13,13,13};
		System.out.println("Deal cards");
		int no = 10;
		int co = who.nextInt(4);
		int whos = 0;
		for(int n = 0; n < 13; n++){
			for (int c = 0; c < 4; c++){
				whos = who.nextInt(4);
				if(n == no && c == co){}
				else {
					if (players[whos].getNum() == max[whos]){
						while (players[whos].getNum() == max[whos]){
							whos = (whos + 1) % 4;
						}
					}
					players[whos].takeCard(color[c]+number[n]);
				}
			}
		}
		for(int i = 0; i < 4; i++){
			System.out.print("Player"+i+":");
			players[i].print();
		}
	}
}
/*public int getNum() to get number of player's card;
public void print() to print all of player's card;
private boolean VS(Sring a,String b) to check whether card b > card a;
private boolean equal(String a, String b) to check whether a,b are pair;
public void takeCard(String n) to take cards at the beginning;
public void insert(String n) to insert card and drop pair;
*/
class Player {
	private class card{
    	public String number;
        public int to;
        public card(){}
    }
    private card[] cards = new card[15];
	private static String table = "02345678910JQKA";
    private int head, last, start, cardNum;
    public int getNum(){return cardNum;}
	public void print(){
		if (cardNum > 0){
			for(int i = head; i != cards[last].to; i = cards[i].to){
				System.out.print(" " + cards[i].number);
			}
		}
		System.out.print("\n");
	}
    private boolean VS(String a, String b){
        int a2 = table.indexOf(a.substring(1));
		int b2 = table.indexOf(b.substring(1));
		if (a2 < b2){return true;}
		else if (a2 > b2) {return false;}
		else {
			char[] a1 = a.toCharArray();
			char[] b1 = b.toCharArray();
			if (a1[0] == 'B'){return false;}
			else {return true;}
		}
    }
    private boolean equal(String a, String b){
		int a2 = table.indexOf(a.substring(1));
		int b2 = table.indexOf(b.substring(1));
		if (a2 == 0 || b2 == 0){return false;}
		else if (a2 == b2){return true;}
		else {return false;}
    }
    public Player(){
        for(int i = 0; i < 14; i++){
			this.cards[i] = new card();
            this.cards[i].to = i + 1;
        }
		this.cards[14] = new card();
		cardNum = 0;
        head = 0;
        start = 0;
    }
    public void takeCard(String n){
        this.cards[start].number = n;
        last = start;
        start++;
		cardNum++;
    }
	public String draw(int n){
		int pre = head;
		int cur = head;
		cardNum--;
		if(n == 0){
			head = cards[head].to;
			cards[cur].to = cards[last].to;
			cards[last].to = cur;
			return cards[cur].number;
		}
		else{
			for(int i = 1; i <= n; i++){
				pre = cur;
				cur = cards[cur].to;
			}
			if (cur == last){
				last = pre;
				return cards[cur].number;
			}
			else {
				cards[pre].to = cards[cur].to;
				cards[cur].to = cards[last].to;
				cards[last].to = cur;
				return cards[cur].number;
			}
		}
	}
    public void insert(String n){
        int cur = head;
		int pre = head;
        while(true){
			if (cur == cards[last].to){
				cards[cards[last].to].number = n;
				last = cards[last].to;
				cardNum++;
				break;
			}
            if (equal(this.cards[cur].number,n)){
                if (cur == head){
					head = cards[cur].to;
					cards[cur].to = cards[last].to;
					cards[last].to = cur;
                }
				else if(cur == last) {last = pre;}
				else {
					cards[pre].to = cards[cur].to;
					cards[cur].to = cards[last].to;
					cards[last].to = cur;
			 	}
				cardNum--;
				break;
            }
            else if (VS(cards[cur].number,n)){
				pre = cur;
                cur = cards[cur].to;
            }
            else {
                 if (cur == head){
					cards[cards[last].to].number = n;
					head = cards[last].to;
					cards[last].to = cards[head].to;
					cards[head].to = cur;
				}
				else {
					cur = cards[last].to;
					cards[cur].number = n;
					cards[last].to = cards[cards[last].to].to;
					cards[cur].to = cards[pre].to;
					cards[pre].to = cur;
				}
				cardNum++;
				break;
            }
        }
    }
	public void drop(){
		int pre = head;
		int pree = head;
		for(int i = cards[head].to; i != cards[last].to; i = cards[i].to){
			if (equal(cards[pre].number,cards[i].number)){
				if (pre == head){
					head = cards[i].to;
					cards[last].to = pre;
					i = head;
					pre = head;
					pree = head;
					cardNum-=2;
				}
				else if (i == last){
					last = pree;
					i = last;
					cardNum-=2;
				}
				else {
					cards[pree].to = cards[i].to;
					cards[i].to = cards[last].to;
					cards[last].to = pre;
					i = cards[pree].to;
					pre = i;
					cardNum-=2;
				}
			}
			else {
				pree = pre;
				pre = i;
			}
		}
	}
}

public class newgame {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String in = scanner.next();
		System.out.println(in);
		if (in.equals("A")) {
			Computer c = new Computer();
			c.initialize();
			c.dealcard();
			c.dropcard();
			c.basicgame();
			c.bonusgame();
		}
		else if (in.equals("B")) {
			ungguy c = new ungguy();
			c.initialize();
			c.dealcard();
			c.dropcard();
			c.basicgame();
			c.bonusgame();
		}
		else if (in.equals("C")) {
			jackless c = new jackless ();
			c.initialize();
			c.dealcard();
			c.dropcard();
			c.basicgame();
			c.bonusgame();
		}
	}
}
