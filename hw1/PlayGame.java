import java.util.Random;
import java.util.Scanner;
class remain {
	public int id;
	public int to;	
	public remain(){}
}
class player {
	private class card{
    	public String number;
        public int to;
        public card(){}    
    }
    private card[] cards = new card[15];
	private String table;
    private int head, last, start, cardNum;
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
		if (a2 < b2){
			return true;	
		}    
		else if (a2 > b2) {
			return false;	
		}
		else {
			char[] a1 = a.toCharArray();
			char[] b1 = b.toCharArray();
			if (a1[0] == 'B'){
				return false;	
			}	
			else {
				return true;
			}
		}
    }
    private boolean equal(String a, String b){
		int a2 = table.indexOf(a.substring(1));
		int b2 = table.indexOf(b.substring(1));
		if (a2 == 0 || b2 == 0){
			return false;	
		}
		else if (a2 == b2){
			return true;	
		}
		else {
			return false;	
		}
    }
	public int getNum(){return cardNum;}
    public player(){
        for(int i = 0; i < 14; i++){
			this.cards[i] = new card();
            this.cards[i].to = i + 1;
        }
		this.cards[14] = new card();
		cardNum = 0;
		table = "02345678910JQKA";
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
				else if(cur == last){
					last = pre;
				}
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
public class PlayGame {
	public static void main(String[] args) { 
		String[] color = {"C","D","H","S"};
		String[] number = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
		player[] players = new player [4];
		for(int i = 0; i < 4; i++){
			players[i] = new player();	
		}
		int [] max = {14,14,13,13};
		System.out.println("Deal cards");
		Random who = new Random();
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
		System.out.println("Drop cards");
		for(int i = 0; i < 4; i++){
			System.out.print("Player"+i+":");
			players[i].drop();
			players[i].print();
		}
		System.out.println("Game start");
		int drawfrom = 1,drawto = 0;
		String drawcard;
		while(players[drawfrom].getNum()!=0 && players[drawto].getNum()!=0) {
			drawcard=players[drawfrom].draw(who.nextInt(players[drawfrom].getNum()));
			System.out.println("Player"+drawto+" draws a card form Player"+drawfrom+" "+drawcard);
			players[drawto].insert(drawcard);
			System.out.print("Player"+drawto+":");
			players[drawto].print();
			System.out.print("Player"+drawfrom+":");
			players[drawfrom].print();
			drawfrom = (drawfrom + 1) % 4;
			drawto = (drawto + 1) % 4;
		}
		remain[] remains = new remain[4];
		for(int i = 0; i < 4; i++) {
			remains[i] = new remain();
			remains[i].to = i + 1;	
		}
		remains[3].to = 0;
		
	Scanner scanner = new Scanner(System.in);

		if(players[drawfrom].getNum()==0 && players[drawto].getNum()==0) {
			System.out.print("Player"+Math.min(drawfrom,drawto)+" and Player"+Math.max(drawto,drawfrom)+" win\n");
			remains[(drawfrom + 2) % 4].to = remains[drawfrom].to;
			drawfrom = (drawfrom + 2) % 4;
			drawto = (drawto + 2) % 4;	
			int num = scanner.nextInt();	
		}
		else if (players[drawfrom].getNum() == 0){
			System.out.print("Player"+drawfrom+" wins\n");
			remains[drawto].to = remains[drawfrom].to;
			drawfrom = (drawfrom + 2) % 4;
			drawto = remains[drawto].to;	
		}
		else {
			System.out.print("Player"+drawto+" wins\n");
			remains[(drawfrom + 2) % 4].to = drawfrom;
			drawfrom = (drawfrom + 1) % 4;
			drawto = (drawto + 1) % 4;	
		}
    	System.out.println("Basic game over\nContinue");
		while (drawfrom != drawto){
			drawcard=players[drawfrom].draw(who.nextInt(players[drawfrom].getNum()));
			System.out.println("Player"+drawto+" draws a card form Player"+drawfrom+" "+drawcard);
			players[drawto].insert(drawcard);
			System.out.print("Player"+drawto+":");
			players[drawto].print();
			System.out.print("Player"+drawfrom+":");
			players[drawfrom].print();
		
			if (players[drawfrom].getNum()==0 && players[drawto].getNum()==0) {
				System.out.print("Player"+Math.min(drawfrom,drawto)+" and Player"+Math.max(drawto,drawfrom)+" win\n");
				drawfrom = drawto;
			}
			else if (players[drawfrom].getNum() == 0){
				System.out.print("Player"+drawfrom+" wins\n");
				remains[drawto].to = remains[drawfrom].to;
				drawfrom = remains[remains[drawfrom].to].to;
				drawto = remains[drawto].to;
			}
			else if(players[drawto].getNum() == 0){
				System.out.print("Player"+drawto+" wins\n");
				int k = drawfrom;
				while (remains[k].to != drawto){
					k = remains[k].to;	
				}
				remains[k].to = drawfrom;
				drawto = drawfrom;
				drawfrom = remains[drawfrom].to;
			}
			else {
				drawfrom = remains[drawfrom].to;
				drawto = remains[drawto].to;
			}
		}
		System.out.println("Bonus game over");
	}
}

