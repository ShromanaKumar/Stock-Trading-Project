import java.util.Objects;
import java.util.Scanner;

public class Task9A {
    public int[][] priceMatrix;
    public Integer[][] sellTable;
    public Integer[][] buyTable;
    public Integer[] stockTable;
    public Integer cooldown;
    public int noOfDays;
    public int noOfStocks;
    
    public void maximumProfit() {
        sellTable = new Integer[noOfStocks + 1][noOfDays + 1];
        buyTable = new Integer[noOfStocks + 1][noOfDays + 1];
        stockTable = new Integer[noOfDays + 1];

        //Day 1
        //initializing the sellTable profit to 0 and buyTable initial profit to the stock price
        for (int i = 0; i <= noOfStocks; i++) {
            sellTable[i][1] = 0;
            buyTable[i][1] = -priceMatrix[i][1];
        }

        //Transaction 1
        //initializing the sellTable and buyTable values to 0 
        for (int i = 0; i <= noOfStocks; i++) {
            sellTable[i][0] = 0;
            buyTable[i][0] = 0;
        }

        //Stock 1
        //initializing the sellTable and buyTable values to 0
        for (int i = 0; i <= noOfDays; i++) {
            sellTable[0][i] = 0;
            buyTable[0][i] = 0;
        }

        //calculate dp sellTable and buyTable
        sell(noOfStocks, noOfDays);

        //backtrack from sellTable to get buy and sell indices
        backTrackSell(noOfStocks, noOfDays);
    }
    
    int bestStock(int dayIdx) {
        if (dayIdx < 0) {
            return 0;
        }
        if (stockTable[dayIdx] == null) {
            int maxValue = Integer.MIN_VALUE;

            for (int i = 1; i < priceMatrix.length; i++) {
                int currPrice = sell(i, dayIdx);
                if (currPrice > maxValue) {
                    maxValue = currPrice;
                    stockTable[dayIdx] = i;
                }
            }
        }
        return sellTable[stockTable[dayIdx]][dayIdx];
    }

    int sell(int stockIdx, int dayIdx) {
        if (sellTable[stockIdx][dayIdx] == null) {
            sellTable[stockIdx][dayIdx] = Math.max(buy(stockIdx, dayIdx - 1) + priceMatrix[stockIdx][dayIdx],
                    Math.max(sell(stockIdx, dayIdx - 1), sell(stockIdx - 1, dayIdx)));
        }
        return sellTable[stockIdx][dayIdx];
    }

    int buy(int stockIdx, int dayIdx) {
        if (buyTable[stockIdx][dayIdx] == null) {
            buyTable[stockIdx][dayIdx] = Math.max(bestStock(dayIdx - cooldown - 1) - priceMatrix[stockIdx][dayIdx], buy(stockIdx, dayIdx - 1));
        }
        return buyTable[stockIdx][dayIdx];
    }

    void backTrackSell(int stockIdx, int dayIdx) {
        if (stockIdx <= 0 || dayIdx <= 0) {
            return;
        }
        if (Objects.equals(sellTable[stockIdx][dayIdx], sellTable[stockIdx - 1][dayIdx])) {
            backTrackSell(stockIdx - 1, dayIdx);
        } else if (Objects.equals(sellTable[stockIdx][dayIdx], sellTable[stockIdx][dayIdx - 1])) {
            backTrackSell(stockIdx, dayIdx - 1);
        }
        else {
            backTrackBuy(stockIdx, dayIdx - 1);
            System.out.println(" " + dayIdx);
        }
    }

    void backTrackBuy(int stockIdx, int dayIdx) {
        if (stockIdx <= 0 || dayIdx <= 0) {
            return;
        }

        if (Objects.equals(buyTable[stockIdx][dayIdx], buyTable[stockIdx][dayIdx - 1])) {
            backTrackBuy(stockIdx, dayIdx - 1);
        } else {
            if(dayIdx-cooldown-1 > 0){
                if (stockTable[dayIdx-cooldown-1] != null) {
                    backTrackSell(stockTable[dayIdx-cooldown-1], dayIdx-cooldown-1);
                }
            }
            System.out.print(stockIdx + " " + dayIdx);
        }
    }
    
    public void getInput() {
		Scanner sc = new Scanner(System.in);
		String tmp = sc.nextLine();
		cooldown = Integer.parseInt(tmp);
		tmp = sc.nextLine();
		String[] tmpArr = tmp.split(" ");
		noOfStocks = Integer.parseInt(tmpArr[0]);
		noOfDays = Integer.parseInt(tmpArr[1]);
		Integer[][] pricesInput = new Integer[noOfStocks][noOfDays];
		
		int stockItr = 0;
		while(stockItr != noOfStocks) {
			tmp = sc.nextLine();
            if(tmp.equals("") || tmp.equals("\n") || tmp.equals(" ")) continue;
			tmpArr = tmp.split(" ");
			for (int i = 0; i < tmpArr.length; i++) {
				pricesInput[stockItr][i] = Integer.parseInt(tmpArr[i]);
			}
			stockItr++;
		}
		
		priceMatrix = new int[noOfStocks + 1][noOfDays + 1];
		for (int i = 1; i <= noOfStocks; i++) {
            for (int j = 1; j <= noOfDays; j++) {
                priceMatrix[i][j] = pricesInput[i - 1][j - 1];
            }
        }
	}
}