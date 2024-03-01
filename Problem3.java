import java.util.*;

class ReturnValueBF3 {
    public int profit;
    public ArrayList<ArrayList<Integer>> txnList;

    public ReturnValueBF3(int profit, ArrayList<ArrayList<Integer>> txnList) {
        this.profit = profit;
        this.txnList = txnList;
    }
}

public class Problem3 {

	public Integer[][] priceMatrix;
	public int cooldown;
	public int noOfStocks;
	public int noOfDays;
	public ArrayList<ArrayList<Integer>> finalTxnList;

    	public ReturnValueBF3 bruteForce(Integer stockIdx, Integer buyIdx, int dayIdx, boolean canSell, int oldProfit, ArrayList<ArrayList<Integer>> txnList) {
        int maxProfit = 0;
        ArrayList<ArrayList<Integer>> finalOutputList = new ArrayList<ArrayList<Integer>>();
        
        if(dayIdx >= noOfDays) {
            return new ReturnValueBF3(oldProfit, txnList);
        }
        
        if(canSell) { // We have a stock

            // Hold the stock
            ReturnValueBF3 tmpRes1 = bruteForce(stockIdx, buyIdx, dayIdx+1, canSell, oldProfit, (ArrayList<ArrayList<Integer>>) txnList.clone());
            if(tmpRes1.profit > maxProfit){
                maxProfit = tmpRes1.profit;
                finalOutputList = tmpRes1.txnList;
            }
            // Sell the stock
            ArrayList<ArrayList<Integer>> modifiedOutput = (ArrayList<ArrayList<Integer>>) txnList.clone();
            modifiedOutput.add(new ArrayList<Integer>(Arrays.asList(stockIdx,buyIdx, dayIdx)));
            int currentProfit = oldProfit + (priceMatrix[stockIdx][dayIdx] - priceMatrix[stockIdx][buyIdx]);

            ReturnValueBF3 tmpRes2 = bruteForce(null, null, dayIdx + cooldown + 1, false, currentProfit, modifiedOutput);
            if(tmpRes2.profit > maxProfit){
                maxProfit = tmpRes2.profit;
                finalOutputList = tmpRes2.txnList;
            }
        } else { // We don't have a stock
            // Skip the day
            ReturnValueBF3 tmpRes1 = bruteForce(null, null, dayIdx+1, false, oldProfit, (ArrayList<ArrayList<Integer>>) txnList.clone());

            if(tmpRes1.profit > maxProfit) {
                maxProfit = tmpRes1.profit;
                finalOutputList = tmpRes1.txnList;
            }
            
            // Buy stock of one of the companies
            for (int i = 0; i < noOfStocks; i++) {
                ReturnValueBF3 tmpRes2 = bruteForce(i, dayIdx, dayIdx+1, true, oldProfit, (ArrayList<ArrayList<Integer>>) txnList.clone());
                if(tmpRes2.profit > maxProfit){
                    maxProfit = tmpRes2.profit;
                    finalOutputList = tmpRes2.txnList;
                }
            }
        }
        return new ReturnValueBF3(maxProfit, finalOutputList);
    }

    public ArrayList<ArrayList<Integer>> task9b() {     

        //The profit 2D array will hold the maximum profit for any day
        int[] profit = new int[noOfDays];

        //The stock 2D array holds the stock index where maximum profit is made for each day
        int[] stock = new int[noOfDays];

        //The buy 2D array holds the buy date where maximum profit is made for each day
        int[] buy = new int[noOfDays];

        //The prevDiffWithProfit 1D array holds the max(profit[j - cooldown -1] – price[j]) for all j in range [0, day -2], for each stock
        int[] prevDiffWithProfit = new int[noOfStocks];

        //The buyIndex 1D array holds the value of j for which we would get max(profit[j - cooldown -1] – price[j]) 
        //for all j in range [0, day -2], for each stock
        int[] buyIndex = new int[noOfStocks];

        //We cannot have any profit if we sell on the 1st day
        profit[0] = 0;
        stock[0] = 0;
        buy[0] = 0;

        //Initializing the prevDiffWithProfit and buyIndex for all stocks and days to min_value
        for (int stockloop = 0; stockloop < noOfStocks; stockloop++){
            prevDiffWithProfit[stockloop] = Integer.MIN_VALUE;
            buyIndex[stockloop] = 0;
        }
                
        for (int dayloop = 1; dayloop < noOfDays; dayloop++) {  
                    
            //Calculating previousDay based on the cooldown period
            int previousDay = ((dayloop - 1 - cooldown - 1) < 0? 0 : (dayloop - 1 - cooldown -1));
            
            //Calculating profit for previousDay
            int yesterdaysProfit = profit[previousDay];

            for(int stockloop = 0; stockloop < noOfStocks; stockloop++){               
                       
                //Calculating the prevDiffWithProfit for (dayloop -1) as the prevDiffWithProfit array only has data upto (dayloop -2)
                int yesterdaysDiffwithProfit = yesterdaysProfit - priceMatrix[stockloop][dayloop -1];

                //We would buy the stock on the day with the max between prevDiffWithProfit and yesterdaysDiffwithProfit
                buyIndex[stockloop] = (prevDiffWithProfit[stockloop] > yesterdaysDiffwithProfit? buyIndex[stockloop] : (dayloop -1));

                //Update the value of prevDiffWithProfit to have max value between prevDiffWithProfit and yesterdaysDiffwithProfit
                prevDiffWithProfit[stockloop] = Math.max(prevDiffWithProfit[stockloop], yesterdaysDiffwithProfit);

                //Taking the maximum between profit at dayloop vs profit at (dayloop -1) and current stockloop
                int tempMax = Math.max(profit[dayloop - 1], priceMatrix[stockloop][dayloop] + prevDiffWithProfit[stockloop]);

                //Update the profit, stock and buy 2D array if the max profit in this stockloop for this dayloop is greater than 
                //the max profit in all previous stocks for the same day.
                stock[dayloop] = (tempMax > profit[dayloop]? (stockloop +1) : stock[dayloop]);
                buy[dayloop] = (tempMax > profit[dayloop]? buyIndex[stockloop] : buy[dayloop]);
                profit[dayloop] = Math.max(tempMax, profit[dayloop]);
            }       
        }

        // finalTxnList 2D list will store the profit, stock, buyDay and sellDay
        finalTxnList = new ArrayList<ArrayList<Integer>>();
            
        //Here the finalAns 2D array will be calculated using backtracking from the profit, stock and buy 2D arrays
        for (int dayloop = noOfDays-1; dayloop >0; dayloop--){ 

            //If profit for day dayloop and (dayloop -1) is the same then we should check for same day (dayloop -1),
            //as the profit of day dayloop would have been passed on from day (dayloop -1) 
            if (profit[dayloop] == profit[dayloop -1]) {
                continue;
            }

            //Storing the values stock, buyDay, sellDay
            ArrayList<Integer> tradeData = new ArrayList<Integer>();
            tradeData.add(stock[dayloop]);
            tradeData.add(buy[dayloop]+1);
            tradeData.add(dayloop+1);
            finalTxnList.add(tradeData);
                    
            //Change the dayloop to be equal to the (buyday - (cooldown +1) +1), so that next profit should be considered from buyday's index
            dayloop = buy[dayloop] - (cooldown +1) +1;
        }
        return finalTxnList;
    }

    public ArrayList<ArrayList<Integer>> task8(){
        //The profit 2D array will hold the maximum profit for all day
        int[] profit = new int[noOfDays];

        //The stock 2D array holds the stock index where maximum profit is made for each day
        int[] stock = new int[noOfDays];

        //The buy 2D array holds the buy date where maximum profit is made for each day
        int[] buy = new int[noOfDays];

        //We cannot have any profit if we sell on the 1st day
        profit[0] = 0;
        stock[0] = 0;
        buy[0] = 0;
        
        for (int dayloop = 1; dayloop < noOfDays; dayloop++) {
            int maxTotalCurrProfit = 0;
            int buyIndex = Integer.MIN_VALUE;
            int buyStockIndex = 0;

            for (int buyloop = 0; buyloop < dayloop; buyloop++) {
                    
                //Calculating previousSellDay based on the cooldown period
                int previousSellDay = ((buyloop - cooldown - 1) < 0? 0 : (buyloop - cooldown - 1));

                //prevProfit holds the profit at previousSellDay
                int prevProfit = profit[previousSellDay];
                int maxcurrProfit = 0;
                int stockIndex = 0;

                for(int stockloop = 0; stockloop < noOfStocks; stockloop++){                                                       

                    //currProfit holds the profit for selling day (dayloop), stock (stockloop) and buy day (buyloop)
                    int currProfit = priceMatrix[stockloop][dayloop] - priceMatrix[stockloop][buyloop];

                    //stockIndex holds the index of the stock which has max profit for a particular selling day (dayloop) and buy day (buyloop)
                    stockIndex = (currProfit < maxcurrProfit? stockIndex : stockloop);

                    //maxcurrProfit holds the max profit for a particular selling day (dayloop) and buy day (buyloop)
                    maxcurrProfit = Math.max(currProfit, maxcurrProfit);

                }

                //profitHere adds the max profit for a particular selling day (dayloop) to the profit at previous txn for buy day (buyloop)
                int profitHere = maxcurrProfit + prevProfit;

                //buyIndex holds the buy day which yields max profit for a particular selling day (dayloop)
                buyIndex = (maxTotalCurrProfit < profitHere? buyloop : buyIndex);

                //buyStockIndex holds the index of the stock which has max profit for a particular selling day (dayloop)
                buyStockIndex = (maxTotalCurrProfit < profitHere? stockIndex : buyStockIndex);

                //maxTotalCurrProfit holds the max profit for a particular selling day (dayloop)
                maxTotalCurrProfit = Math.max(maxTotalCurrProfit, profitHere);

                //Update the profit , stock and buy 2D array if the max profit in this stockloop and buyloop for this dayloop is greater than 
                //the max profit in all previous stocks and buy days for the same selling day dayloop.
                stock[dayloop] = (profit[dayloop - 1] < maxTotalCurrProfit? (buyStockIndex +1) : stock[dayloop -1]);
                buy[dayloop] = (profit[dayloop - 1] < maxTotalCurrProfit? buyIndex : buy[dayloop -1]);
                profit[dayloop] = Math.max(profit[dayloop - 1], maxTotalCurrProfit);
            }
        }

	     // finalTxnList 2D list will store the profit, stock, buyDay and sellDay for each txn
	    finalTxnList = new ArrayList<ArrayList<Integer>>();
            
        //Here the finalAns 2D array will be calculated using backtracking from the profit, stock and buy 2D arrays
        for (int dayloop = noOfDays-1; dayloop >0; dayloop--){ 

            //If profit for day dayloop and (dayloop -1) is the same then we should check for same day (dayloop -1),
            //as the profit of day dayloop would have been passed on from day (dayloop -1) 
            if (profit[dayloop] == profit[dayloop -1]) {
                continue;
            }
            
            // Storing the values stock, buyDay, sellDay for the txn txnloop
            ArrayList<Integer> tradeData = new ArrayList<Integer>();
            tradeData.add(stock[dayloop]);
            tradeData.add(buy[dayloop]+1);
            tradeData.add(dayloop+1);
            finalTxnList.add(tradeData);

             //Change the dayloop to be equal to the (buyday - (cooldown +1) +1), so that next profit should be considered from buyday's index
             dayloop = buy[dayloop] - (cooldown +1) +1;
        }
        
        return finalTxnList;
    }
    
    public void printOutput(ArrayList<ArrayList<Integer>> finalTxnList, boolean convertTo1Based) {
    	for (ArrayList<Integer> tradeData : finalTxnList) {
			for (Integer i : tradeData) {
				if(convertTo1Based) {
					System.out.print(i+1 + " ");
				} else {
					System.out.print(i + " ");
				}
			}
			
			System.out.println();
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
		priceMatrix = new Integer[noOfStocks][noOfDays];
		
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
}