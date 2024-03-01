import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.time.*;

public class Problem1 {

	public Integer[][] M;	// Memoization matrix
	public Integer[] maxPerStockTopDown;
	public int noOfStocks;
	public int noOfDays;
	public int[][] priceMatrix;
	public int[] maxBuyIndices;
	public int[] maxSellIndices;
	
	public int[] bruteForce() {
		
		int maxProfit = Integer.MIN_VALUE;
		int maxProfitStockIdx = -1;
		int maxBuyIdx = -1;
		int maxSellIdx = -1;
		
		for (int stockIdx = 0; stockIdx < noOfStocks; stockIdx++) {
			for (int buyDayIdx = 0; buyDayIdx < noOfDays-1; buyDayIdx++) {
				for (int sellDayIdx = buyDayIdx+1; sellDayIdx < noOfDays; sellDayIdx++) {
					int tempDiff = priceMatrix[stockIdx][sellDayIdx] - priceMatrix[stockIdx][buyDayIdx];
					if(tempDiff > maxProfit) {
						maxProfit = tempDiff;
						maxProfitStockIdx = stockIdx;
						maxBuyIdx = buyDayIdx;
						maxSellIdx = sellDayIdx;
					}
				}
			}
		}
		
		return new int[] {maxProfitStockIdx+1, maxBuyIdx+1, maxSellIdx+1};
	}
	
	// Find the transaction giving maximum profit for each stock and then find the max profit transaction among these to 
	// get the overall max.
	public int[] dpBottomUp() {
		int[][] OPT = new int[noOfStocks][noOfDays];
		for (int i = 0; i < OPT.length; i++) {
			OPT[i][0] = 0;
		}
		
		Integer[] maxPerStock = new Integer[noOfStocks];
		Arrays.fill(maxPerStock, Integer.MIN_VALUE);

		// Arrays for maintaining the buy and sell indices of transactions which give max profit for each stock
		int[] maxBuyIndices = new int[noOfStocks];
		int[] maxSellIndices = new int[noOfStocks];
		
		for (int stockIdx = 0; stockIdx < OPT.length; stockIdx++) {
			for (int dayIdx = 1; dayIdx < OPT[stockIdx].length; dayIdx++) {

				/* There can be two cases: 
				*  1. We buy the stock today so today's profit is 0.
				*  2. We have already bought the stock and we sell it today. The profit here is the difference between today's price and 
				*     yesterday's price summed with the profit we had yesterday.
				* The case where we just hold the stock without buying or selling it also gets covered here.
				*/
				OPT[stockIdx][dayIdx] = Math.max(0, OPT[stockIdx][dayIdx-1] + (priceMatrix[stockIdx][dayIdx] - priceMatrix[stockIdx][dayIdx-1]));

				if(OPT[stockIdx][dayIdx] == 0) {
					maxBuyIndices[stockIdx] = dayIdx;
				}

				if(OPT[stockIdx][dayIdx] > maxPerStock[stockIdx]) {
					maxPerStock[stockIdx] = OPT[stockIdx][dayIdx];
					maxSellIndices[stockIdx] = dayIdx;
				}
			}
		}
		
		List<Integer> maxPerStockList = Arrays.asList(maxPerStock);
		int maxProfit = Collections.max(maxPerStockList);
		int maxProfitIndex = maxPerStockList.indexOf(maxProfit);
		return new int[] {maxProfitIndex+1, maxBuyIndices[maxProfitIndex]+1, maxSellIndices[maxProfitIndex]+1};
	}

	// Find the transaction giving maximum profit for each stock and then find the max profit transaction among these to 
	// get the overall max.
	public int[] dpTopDownDriver() {

		M = new Integer[noOfStocks][noOfDays];
		for (int i = 0; i < M.length; i++) {
			M[i][0] = 0;
		}

		// Array for maintaining max profit for each stock
		maxPerStockTopDown = new Integer[noOfStocks];
		Arrays.fill(maxPerStockTopDown, Integer.MIN_VALUE);

		// Arrays for maintaining the buy and sell indices of transactions which give max profit for each stock
		maxBuyIndices = new int[noOfStocks];
		maxSellIndices = new int[noOfStocks];

		for (int i = 0; i < maxPerStockTopDown.length; i++) {
			dpTopDown(i, noOfDays-1);
		}

		List<Integer> maxPerStockList = Arrays.asList(maxPerStockTopDown);
		int maxProfit = Collections.max(maxPerStockList);
		int maxProfitIndex = maxPerStockList.indexOf(maxProfit);
		return new int[] {maxProfitIndex+1, maxBuyIndices[maxProfitIndex]+1, maxSellIndices[maxProfitIndex]+1};
	}

	public int dpTopDown(int stockNo, int dayNo) {
		/* There can be two cases: 
		*  1. We buy the stock today so today's profit is 0.
		*  2. We have already bought the stock and we sell it today. The profit here is the difference between today's price and 
		*     yesterday's price summed with the profit we had yesterday.
		* The case where we just hold the stock without buying or selling it also gets covered here.
		*/
		
		if(stockNo < 0 || dayNo < 1) {
			return 0;
		}

		if(M[stockNo][dayNo] != null) {
			return M[stockNo][dayNo];
		}

		M[stockNo][dayNo] = Math.max(0, dpTopDown(stockNo, dayNo-1) + (priceMatrix[stockNo][dayNo] - priceMatrix[stockNo][dayNo-1]));
		if(M[stockNo][dayNo] == 0) {
			maxBuyIndices[stockNo] = dayNo;
		}
		
		if(M[stockNo][dayNo] > maxPerStockTopDown[stockNo]) {
			maxPerStockTopDown[stockNo] = M[stockNo][dayNo];
			maxSellIndices[stockNo] = dayNo;
		}

		return M[stockNo][dayNo];
	}

	/* In the greedy approach to this problem, we would try to buy the stocks at the minimum price and sell them at 
	* the maximum price found so far. For any given day, we will buy the stock at the minimum price available till that day, 
	* and measure how much maximum profit (if any) we will be able to get if we sell the stocks on the current day. We would 
	* keep recalculating the maximum profit for every day for each stock. This way we would have found the maximum profit 
	* for each of the m different stocks. After that, we would choose the stocks with the maximum profit.
	*/
	public int[] greedy() {

        int finalStockIndex = 1;
        int finalBuyPrice = priceMatrix[0][0];
        int finalSellPrice = priceMatrix[0][0];
        int finalBuyIndex = 0;
        int finalSellIndex = 0;

        int finalProfit = Integer.MIN_VALUE;

        for(int stockIdx = 0; stockIdx < noOfStocks; stockIdx++) {
            int maxProfit = Integer.MIN_VALUE;
            int minStockCounter = priceMatrix[stockIdx][0];
            int minStockCounterIndex = 0;
            int maxStockValue = priceMatrix[stockIdx][0];
            int minStockValue = priceMatrix[stockIdx][0];
            int maxStockIndex = 0;
            int minStockIndex = 0;
        
            for(int dayIdx = 1; dayIdx < noOfDays; dayIdx++){

                int currProfit = priceMatrix[stockIdx][dayIdx] - minStockCounter;
                maxStockValue = (maxProfit < currProfit? priceMatrix[stockIdx][dayIdx] : maxStockValue);
                minStockValue = (maxProfit < currProfit? minStockCounter : minStockValue);

                maxStockIndex = (maxProfit < currProfit? dayIdx : maxStockIndex);
                minStockIndex = (maxProfit < currProfit? minStockCounterIndex : minStockIndex);

                maxProfit = Math.max(maxProfit,currProfit);
                
                minStockCounterIndex = (minStockCounter < priceMatrix[stockIdx][dayIdx]? minStockCounterIndex : dayIdx);
                minStockCounter = Math.min(minStockCounter, priceMatrix[stockIdx][dayIdx]);
            }
            
			if(finalProfit < maxProfit) {
				finalSellIndex = maxStockIndex+1;
				finalBuyIndex = minStockIndex+1;
				finalSellPrice = maxStockValue;
				finalBuyPrice = minStockValue;
				finalStockIndex = stockIdx+1;
			}

            finalProfit = Math.max(finalProfit, maxProfit);
        }

		return new int[] {finalStockIndex, finalBuyIndex, finalSellIndex};
    }
	
	public void getInput() {
		Scanner sc = new Scanner(System.in);
		String tmp = sc.nextLine();
		String[] tmpArr = tmp.split(" ");
		noOfStocks = Integer.parseInt(tmpArr[0]);
		noOfDays = Integer.parseInt(tmpArr[1]);
		priceMatrix = new int[noOfStocks][noOfDays];
		
		int stockItr = 0;
		while(stockItr != noOfStocks) {
			tmp = sc.nextLine();
			if(tmp.equals("") || tmp.equals("\n") || tmp.equals(" ")) continue;
			tmpArr = tmp.split(" ");
			for (int i = 0; i < tmpArr.length; i++) {
				priceMatrix[stockItr][i] = Integer.parseInt(tmpArr[i]);
			}
			stockItr++;
		}
	}
	
	public void printOutput(int[] res) {
		System.out.print(res[0] + " " + res[1] + " " + res[2]);
	}
}
