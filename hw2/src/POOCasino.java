import java.util.*;
//each card has string color,number,rank and colnum for color and number
//String s is for find each card's rank by indexOf
class Card {
	private static String S = "A2345678910JQK";
	public String color;
	public String number;
	public String colnum;
	public int rank;
	public Card(String c, String n) {
		this.color = c;
		this.number = n;
		colnum = c + n;
		rank = S.indexOf(n);
	}
}
//use setsize(int N) to initialize and int getNext() to get next number
class RandomIndex{
    //DATA:
    public int[] index;
    public int count = 0;

    //ACTIONS:
    public void setSize(int N){
	if (index == null || N != index.length){
	    index = new int[N];
	    initializeIndex();
	    permuteIndex();
	}
}
    public void initializeIndex(){
	for(int i=0;i<index.length;i++)
	    index[i] = i;
    }

    public void permuteIndex(){
	java.util.Random rnd = new java.util.Random();
	for(int i=index.length-1;i>=0;i--){
	    int j = rnd.nextInt(i+1);
	    int tmp = index[j];
	    index[j] = index[i];
	    index[i] = tmp;
	}
    }

    public int getNext(){
	int res = index[count];
	count++;
	if (count == index.length){
	    permuteIndex();
	    count = 0;
	}
	return res;
    }
}
/*use void suffle() to suffle cards;
Card dealcard() to get next card;
condtructor will make a new index with 52 cards and suffle at first*/
class Shuffler {
	private String[] color = {"C","D","H","S"};
	private String[] number = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
	private RandomIndex newIndex = new RandomIndex();
	private Card[] cards = new Card[52];
	public Shuffler() {
		int k = 0;
		newIndex.setSize(52);
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++){
				cards[k] = new Card(color[i],number[j]);
				k++;
			}
		}
	}
	public void shuffle() {
		newIndex.permuteIndex();
		newIndex.count = 0;
	}
	public Card dealcard() {
		int i = newIndex.getNext();
		Card tem = new Card(cards[i].color,cards[i].number);
		return tem;
	}
}
/*void start to start() the game
boolean nextRound() to start nextRound and return false if player want to exit;
void dealfirst() to dealcard at the first time
void drop() to ask Which card to drop and deal new cards
void dealsecond() to print newcards in hand
void classify() to check the type player get
void result() to print the result*/
class Computer {
	private Player playone;
	private int bet, round;
	private static String[] abc = {" (a) "," (b) "," (c) "," (d) "," (e) "};
	private static String abcd = "abcde";
	private static String straight = "A2345678910JQKA10JQK";
	private Shuffler dealer = new Shuffler();
	private Scanner scanner = new Scanner(System.in);
	public void start(){
		System.out.print("POOCasino Jacks or better, written by b02705043 Hairy\nPlease enter your name:");
		playone = new Player(scanner.nextLine());
		round = 0;
		System.out.println("Welcome, "+playone.getname()+".");
	}
	public Boolean nextRound(){
		round ++;
		System.out.println("You have "+playone.getmoney()+" P-dollars now.");
		System.out.print("Please enter your P-dollar bet for round "+round+" (1-5 or anyother number for quitting the game): ");
		bet = Integer.valueOf(scanner.next());
		if (bet > 5 || bet < 1) {
			System.out.println("Good bye, "+playone.getname()+". You played for "+(round-1)+" round and have "+playone.getmoney()+" P-dollars now.");
			return false;
		}
		else {
			playone.usemoney(-1 * bet);
			dealer.shuffle();
			playone.clearall();
			return true;
		}
	}
	public void dealfirst() {
		for(int i = 0; i < 5; i++){
			playone.takeCard(dealer.dealcard());
		}
		Vector<Card> temp = playone.gethand();
		System.out.print("Your cards are");
		for (int i = 0; i < 5; i++) {
			System.out.print(abc[i]+temp.get(i).colnum);
		}
	}
	public void drop() {
		System.out.print("\nWhich cards do you want to keep?(or 'n' to drop all) ");
		Vector<Integer> dindex = new Vector<Integer>(0);
		String kword = scanner.next();
		String dword = "abcde";
		for (int i = 0; i < kword.length() ; i++) {
			dword = dword.replace(kword.substring(i,i+1),"");
		}
		for (int i = 0; i < dword.length() ; i++) {
			dindex.add(abcd.indexOf(dword.substring(i,i+1)));
		}
		Vector<Card> dcard = playone.dropcard(dindex);
		System.out.print("Okay. I will discard");
		for (int i = 0; i < dword.length() ; i++) {
			playone.takeCard(dealer.dealcard());
			System.out.print(abc[dindex.get(i)] + dcard.get(i).colnum);
		}
	}
	public void dealsecond() {
		System.out.print(".\nYour new cards are");
		Vector<Card> temp = playone.gethand();
		for (int i = 0; i < 5 ; i++) {
			System.out.print(" "+temp.get(i).colnum);
		}
	}
	private int classify() {
		Vector<Card> temp = playone.gethand();
		Boolean flush = true;
		String tempNum = "";
		int[] pair = new int[14];
		for (int i = 0; i < 5; i++) {
			tempNum = tempNum + temp.get(i).number;
			pair[temp.get(i).rank] ++;
		}
		int straightn = straight.indexOf(tempNum);
		for (int i = 0; i < 4 ; i++) {
			if(!temp.get(i).color.equals(temp.get(i+1).color)) {
				flush = false;
				break;
			}
		}
		if (flush){
			switch (straightn) {
				case -1: return 5;
				case 14: return 1;
				default: return 2;
			}
		}
		else {
			if (straightn != -1) {
				return 6;
			}
			else {
				int four = 0, three = 0, two = 0;
				int pnum = 0;
				for (int i = 0; i < 14; i++) {
					switch (pair[i]) {
						case 4: four++; break;
						case 3: three++; break;
						case 2: two++; pnum = i; break;
					}
				}
				if (three == 1 && two == 1){return 4;}
				else if (four == 1){return 3;}
				else if (three == 1 && two != 1){return 7;}
				else if (two == 2) {return 8;}
				else if (two == 1 && (pnum > 10 || pnum == 0)) {return 9;}
				else {return 10;}
			}
		}
	}
	public void result(){
		int classi = classify();
		String kind = "";
		int win = 0;
		switch(classi) {
			case 1:
				kind = "a royal flush";
				if (bet == 5){win = 4000;}
				else{win = bet * 250;}
				break;
			case 2:
				kind = "a straight flush";
				win = bet * 50;
				break;
			case 3:
				kind = "a four of a kind";
				win = bet * 25;
				break;
			case 4:
				kind = "a full House";
				win = bet * 9;
				break;
			case 5:
				kind = "a flush";
				win = bet * 6;
				break;
			case 6:
				kind = "a straight";
				win = bet * 4;
				break;
			case 7:
				kind = "a three of a kind";
				win = bet * 3;
				break;
			case 8:
				kind = "a twopair";
				win = bet * 2;
				break;
			case 9:
				kind = "an Jacks or better";
				win = bet;
				break;
			case 10: kind = "an others"; break;
		}
		System.out.println("\nYou get "+kind+". The payoff is "+win+".");
		playone.usemoney(win);
	}
}
/*sting getname() to get player's name
Player(string n) to set player's name and money
void usemoney(int n) to modify player's money
void takeCard(Card card) for player to get a newcard
void dropcard(Vector<int> v) to drop cards by the numbers in the vector
Card gethand() to get player's cards in hand
void clearall() to drop all cards from player*/
class Player {
	private int money;
	private String name;
	private Vector<Card> hand = new Vector<Card>(0);
	public Player(String n) {
		money = 1000;
		name = n;
	}
	public String getname(){return name;}
	public void usemoney(int n) {money += n;}
	public int getmoney() {return money;}
	public void takeCard(Card card) {
		Card temp = new Card(card.color,card.number);
		if (hand.size() == 0){
			hand.add(temp);
		}
		else {
			for(int i = 0; i < hand.size(); i++){
				if (temp.rank <= hand.get(i).rank) {hand.add(i,temp); break;}
				else if (temp.rank >= hand.get(hand.size()-1).rank) {hand.add(temp);break;}
			}
		}
	}
	public Vector<Card> dropcard(Vector<Integer> v) {
		Vector<Card> temp = new Vector<Card>(0);
		for(int i = 0; i < v.size(); i++) {
			temp.add(hand.get(v.get(i)-i));
			hand.remove(v.get(i)-i);
		}
		return temp;
	}
	public Vector<Card> gethand() {
		Vector<Card> temp = new Vector<Card>(0);
		for (int i = 0; i < hand.size(); i++) {
			temp.add(hand.get(i));
		}
		return temp;
	}
	public void clearall(){hand.clear();}
}

public class POOCasino {
	public static void main(String[] args) {
		Computer c = new Computer();
		c.start();
		while (c.nextRound()) {
			c.dealfirst();
			c.drop();
			c.dealsecond();
			c.result();
		}
	}
}
