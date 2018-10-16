/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;


/**
 *
 * @author jmoreno
 */
public class PageRank {

    public int path[][];
    public double pagerank[];
    public int totalNodes;

    
    public PageRank(int totalNodes){
        this.path = new int[totalNodes][totalNodes];
        this.pagerank = new double[totalNodes];
        this.totalNodes = totalNodes;
    }
    
    public void calc() {

        double InitialPageRank;
        double OutgoingLinks = 0;
        double DampingFactor = 0.85;
        double TempPageRank[] = new double[10];

        int ExternalNodeNumber;
        int InternalNodeNumber;
        int k; 
        int iter = 1;

        InitialPageRank = 1 / (float)totalNodes;
        System.out.printf(" Total Number of Nodes :" + totalNodes + "\t Initial PageRank  of All Nodes :" + InitialPageRank + "\n");

        //init 
        for (k = 0; k < totalNodes; k++) {
            this.pagerank[k] = InitialPageRank;
        }

        System.out.printf("\n Initial PageRank Values , 0th Step \n");
        for (k = 0; k < totalNodes; k++) {
            System.out.printf(" Page Rank of " + k + " is :\t" + this.pagerank[k] + "\n");
        }

        while (iter <= 2) // Iterations
        {
            // Store the PageRank for All Nodes in Temporary Array 
            for (k = 0; k < totalNodes; k++) {
                TempPageRank[k] = this.pagerank[k];
                this.pagerank[k] = 0;
            }

            for (InternalNodeNumber = 0; InternalNodeNumber < totalNodes; InternalNodeNumber++) {
                for (ExternalNodeNumber = 0; ExternalNodeNumber < totalNodes; ExternalNodeNumber++) {
                    if (this.path[ExternalNodeNumber][InternalNodeNumber] == 1) {
                        k = 0;
                        OutgoingLinks = 0;  // Count the Number of Outgoing Links for each ExternalNodeNumber
                        while (k < totalNodes) {
                            if (this.path[ExternalNodeNumber][k] == 1) {
                                OutgoingLinks = OutgoingLinks + 1; // Counter for Outgoing Links
                            }
                            k = k + 1;
                        }
                        // Calculate PageRank     
                        this.pagerank[InternalNodeNumber] += TempPageRank[ExternalNodeNumber] * (1 / OutgoingLinks);
                    }
                }
            }

            System.out.printf("\n After " + iter + "th Step \n");

            for (k = 0; k < totalNodes; k++) {
                System.out.printf(" Page Rank of " + k + " is :\t" + this.pagerank[k] + "\n");
            }

            iter = iter + 1;
        }

        // Add the Damping Factor to PageRank
        for (k = 0; k < totalNodes; k++) {
            this.pagerank[k] = (1 - DampingFactor) + DampingFactor * this.pagerank[k];
        }

        // Display PageRank
        System.out.printf("\n Final Page Rank : \n");
        for (k = 0; k < totalNodes; k++) {
            System.out.printf(" Page Rank of " + k + " is :\t" + this.pagerank[k] + "\n");
        }

    }

    public static void main(String args[]) {
        System.out.println("Enter the Number of WebPages \n");
        int nodes = 5;//in.nextInt();
        PageRank p = new PageRank(nodes);
        System.out.println("Enter the Adjacency Matrix with 1->PATH & 0->NO PATH Between two WebPages: \n");
        p.path= new int[][] {{0,1,0,0,0},{1,0,0,0,1},{1,1,0,1,1},{0,0,1,0,1},{1,0,0,1,0}};
        p.calc();
    }

}