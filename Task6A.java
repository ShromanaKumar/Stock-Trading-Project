import java.util.*;

public class Task6A {
    Integer[][][] buyTable;
    Integer[][][] sellTable;
    int[][] priceMatrix;
    Integer[][] stockTable;
    int noOfStocks;
    int noOfDays;
    int noOfTxns;

    int bestStock(int dayIdx, int txnIdx) {
        
        if (stockTable[txnIdx][dayIdx] == null) {
            int tmpMax = Integer.MIN_VALUE;

            for (int stockItr = 1; stockItr < priceMatrix.length; stockItr++) {
                int sellRes = sell(stockItr, txnIdx, dayIdx);
                if (sellRes > tmpMax) {
                    tmpMax = sellRes;
                    stockTable[txnIdx][dayIdx] = stockItr;
                }
            }
        }

        return sellTable[stockTable[txnIdx][dayIdx]][txnIdx][dayIdx];
    }
    
    int sell(int stockIdx, int txnIdx, int dayIdx) {
        if (sellTable[stockIdx][txnIdx][dayIdx] == null) {
            sellTable[stockIdx][txnIdx][dayIdx] = Math.max(buy(stockIdx, txnIdx, dayIdx - 1) + priceMatrix[stockIdx][dayIdx], Math.max(sell(stockIdx, txnIdx, dayIdx - 1), sell(stockIdx - 1, txnIdx, dayIdx)));
        }

        return sellTable[stockIdx][txnIdx][dayIdx];
    }

    int buy(int stockIdx, int txnIdx, int dayIdx) {
        if (buyTable[stockIdx][txnIdx][dayIdx] == null) {
            buyTable[stockIdx][txnIdx][dayIdx] = Math.max(bestStock(dayIdx, txnIdx - 1) - priceMatrix[stockIdx][dayIdx], buy(stockIdx, txnIdx, dayIdx - 1));
        }

        return buyTable[stockIdx][txnIdx][dayIdx];
    }

    void backTrackSell(int stockIdx, int txnIdx, int dayIdx) {
        if (stockIdx <= 0 || txnIdx <= 0 || dayIdx <= 0) {
            return;
        }

        if (Objects.equals(sellTable[stockIdx][txnIdx][dayIdx], sellTable[stockIdx - 1][txnIdx][dayIdx])) {
            backTrackSell(stockIdx - 1, txnIdx, dayIdx);
        } else if (Objects.equals(sellTable[stockIdx][txnIdx][dayIdx], sellTable[stockIdx][txnIdx][dayIdx - 1])) {
            backTrackSell(stockIdx, txnIdx, dayIdx - 1);
        } else if (Objects.equals(sellTable[stockIdx][txnIdx][dayIdx], sellTable[stockIdx][txnIdx - 1][dayIdx])) {
            backTrackSell(stockIdx, txnIdx - 1, dayIdx);
        } else {
            backTrackBuy(stockIdx, txnIdx, dayIdx - 1);
            System.out.println(" " + dayIdx);
        }
    }

    void backTrackBuy(int stockIdx, int txnIdx, int dayIdx) {
        if (stockIdx <= 0 || txnIdx <= 0 || dayIdx <= 0) {
            return;
        }

        if (Objects.equals(buyTable[stockIdx][txnIdx][dayIdx], buyTable[stockIdx][txnIdx][dayIdx - 1])) {
            backTrackBuy(stockIdx, txnIdx, dayIdx - 1);
        } else {
            if (stockTable[txnIdx - 1][dayIdx] != null) {
                backTrackSell(stockTable[txnIdx - 1][dayIdx], txnIdx - 1, dayIdx);
            }
            System.out.print(stockIdx + " " + dayIdx);
        }
    }
    
    public void maximumProfit() {
        sellTable = new Integer[noOfStocks + 1][noOfTxns + 1][noOfDays + 1];
        buyTable = new Integer[noOfStocks + 1][noOfTxns + 1][noOfDays + 1];
        stockTable = new Integer[noOfTxns + 1][noOfDays + 1];
        
        //Stock 1
        //initializing the sellTable and buyTable values to 0 
         for (int i = 0; i <= noOfTxns; i++) {
             for (int j = 0; j <= noOfDays; j++) {
                 sellTable[0][i][j] = 0;
                 buyTable[0][i][j] = 0;
             }
         }
        

        //Transaction 1
        //initializing the sellTable and buyTable values to 0 
        for (int i = 0; i <= noOfDays; i++) {
            for (int j = 0; j <= noOfStocks; j++) {
                sellTable[j][0][i] = 0;
                buyTable[j][0][i] = 0;
            }
        }
        
      //Day 1
        //initializing the sellTable profit to 0 and buyTable initial profit to the stock price
        for (int i = 0; i <= noOfTxns; i++) {
            for (int j = 0; j <= noOfStocks; j++) {
                sellTable[j][i][1] = 0;
                buyTable[j][i][1] = -priceMatrix[j][1];
            }
        }

        //calculate dp sellTable and buyTable
        sell(noOfStocks, noOfTxns, noOfDays);

        //backtrack from sellTable to get buy and sell indices
        backTrackSell(noOfStocks, noOfTxns, noOfDays);
    }

    public void getInput() {
		Scanner sc = new Scanner(System.in);
		String tmp = sc.nextLine();
		noOfTxns = Integer.parseInt(tmp);
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
