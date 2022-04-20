package cpuschedulingroundrobin;

import java.util.*;

public class CPUSchedulingRoundRobin {
    static Scanner console = new Scanner(System.in);

    static int size = 0;
    
    public static void main(String[] args) {
//        ArrayList<Integer> process = new ArrayList<>(Arrays.asList(1, 2, 3));
//        ArrayList<Double> burst = new ArrayList<>(Arrays.asList(10.0, 5.0, 8.0));
//        ArrayList<Double> waiting = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
//        ArrayList<Double> turn = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
//        ArrayList<Double> arrival = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
//        ArrayList<Double> start = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
//        ArrayList<Double> complete = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
//        ArrayList<Double> remainder = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
//        double quantum = 2;

        ArrayList<Integer> process = new ArrayList<>();
        ArrayList<Double> burst = new ArrayList<>();
        ArrayList<Double> waiting = new ArrayList<>();
        ArrayList<Double> turn = new ArrayList<>();
        ArrayList<Double> arrival = new ArrayList<>();
        ArrayList<Double> start = new ArrayList<>();
        ArrayList<Double> complete = new ArrayList<>();
        ArrayList<Double> remainder = new ArrayList<>();
        
        /**
         * STEP 1 - Just create table
         */
        char cont;
        int iteration = 0;

        System.out.print("\nEnter quantum: ");
        double quantum = Double.parseDouble(console.next());

        do {
            System.out.println("\nProcess " + ++iteration);
            
            System.out.print("Enter burst: ");
            burst.add(Double.parseDouble(console.next()));
            
            process.add(iteration);
            waiting.add(0.0);
            turn.add(0.0);
            arrival.add(0.0);
            start.add(0.0);
            complete.add(0.0);
            remainder.add(0.0);
            
            System.out.print("\nPress Y to continue: ");
            cont = console.next().charAt(0);
        } while (Character.toUpperCase(cont) == 'Y');

        size = process.size();
        
        // Output Step 1
        printOutput(1, process, burst, waiting, turn, arrival, start, complete, remainder);
        
        /**
         * STEP 2 - Calculate Waiting Time
         */
        for (int i = 0; i < process.size(); i++){
            // calc start (prev index complete value but if index 0 then start is 0)
            if (i==0){
                start.set(i, 0.0);
            } else {
                start.set(i, complete.get(i-1));
            }

            // calc waiting
            waiting.set(i, start.get(i) - arrival.get(i));

            // calc completion
            if(burst.get(i) > quantum){
                complete.set(i, start.get(i) + quantum);
            } else {
                complete.set(i, start.get(i) + burst.get(i));
            }

            // calc turn (complete - arrival)
            turn.set(i, 0.0);

            // calc remainder (burst - quantum)
            double rem_val = burst.get(i) - quantum;

            if (rem_val< 0){
                remainder.set(i, 0.0);
            } else {
                remainder.set(i, burst.get(i) - quantum);
            }

            if (rem_val > 0){
                process.add(process.get(i));
                burst.add(remainder.get(i));
                waiting.add(start.get(i) - arrival.get(i));
                turn.add(0.0);
                arrival.add(complete.get(i));
                start.add(0.0);

                if(burst.get(i) > quantum){
                    complete.add(i, start.get(i) + quantum);
                } else {
                    complete.add(i, start.get(i) + burst.get(i));
                }

                rem_val = burst.get(i) - quantum;
                if (rem_val< 0){
                    remainder.add(0.0);
                } else {
                    remainder.add(burst.get(i) - quantum);
                }
            }
        }
        
        // Output Step 2
        printOutput(2, process, burst, waiting, turn, arrival, start, complete, remainder);
        
        
        /**
         * STEP 3 - Calculate Turnaround
         */
        
        for (int i = 0; i < process.size(); i++){
            turn.set(i, complete.get(i) - arrival.get(i));
        }
        // Output Step 3
        printOutput(3, process, burst, waiting, turn, arrival, start, complete, remainder);
    }
    
    public static void printOutput(
            int step,
            ArrayList<Integer> process,
            ArrayList<Double> burst,
            ArrayList<Double> waiting,
            ArrayList<Double> turn,
            ArrayList<Double> arrival,
            ArrayList<Double> start,
            ArrayList<Double> complete,
            ArrayList<Double> remainder
            ) {
        System.out.println("\nSTEP " + step);
        String leftAlignFormat = "| %-11s | %-11s | %-9s | %-6s | %-9s | %-7s | %-10s | %-11s |%n";

        System.out
                .format("+-------------+-------------+-----------+--------+-----------+---------+------------+-------------+%n");
        System.out
                .format("|   Process   |    Burst    |  Waiting  |  Turn  |  Arrival  |  Start  |  Complete  |  Remainder  |%n");
        System.out
                .format("+-------------+-------------+-----------+--------+-----------+---------+------------+-------------+%n");

        for (int i = 0; i < process.size(); i++) {
            System.out.format(leftAlignFormat, 
                    "Process " + process.get(i), 
                    burst.get(i), 
                    waiting.get(i),
                    turn.get(i),
                    arrival.get(i),
                    start.get(i),
                    complete.get(i),
                    remainder.get(i)
                    );
        }

        System.out
                .format("+-------------+-------------+-----------+--------+-----------+---------+------------+-------------+%n");
    }
}
