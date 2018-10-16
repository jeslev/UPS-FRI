/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 *
 * @author jmoreno
 */
public class PageRank3 {

    public int path[][];
    public double pagerank[];
    public int totalNodes;

    
    public PageRank3(int totalNodes){
        this.path = new int[totalNodes][totalNodes];
        this.pagerank = new double[totalNodes];
        this.totalNodes = totalNodes;
    }
    
    public void calc() {

        double InitialPageRank;
        double OutgoingLinks = 0;
        double DampingFactor = 0.85;
        double TempPageRank[] = new double[this.totalNodes];

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

        while (iter <= 10	) // Iterations
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
        int nodes = 599330;//in.nextInt();
        
        String nameFileXML = "/home/jeslev/masterUPS/M1/RI/TP/simpleAdjacencyMatrix/AdjacencyMatrix.tsv";
        
                
        //PageRank3 p = new PageRank3(nodes);
        //System.out.println("Enter the Adjacency Matrix with 1->PATH & 0->NO PATH Between two WebPages: \n");
        
        Multimap<Long, Long> map = HashMultimap.create();
        
        
        try {
            CSVParser csvFileParser = CSVFormat.TDF.parse(new FileReader(new File(nameFileXML)));
            for (CSVRecord csvRecord : csvFileParser) {
            	Long x = new Long(csvRecord.get(0)).longValue();
            	Long y = new Long(csvRecord.get(1)).longValue();
                map.put(x,y);
                //p.path[x][y]= 1;
            	//System.out.println(csvRecord.get(0) + " / "+ csvRecord.get(1));
            }
        } catch (IOException e) {
            Logger.getLogger(IndexCollection.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        //p.calc();
        
        try {
			PrintWriter writer = new PrintWriter("wikigraph.graph-txt", "UTF-8");
			writer.println(nodes);
			
			
			for(long i=0;i<=nodes;i++) {
				Collection<Long> cc = map.get(i);
				Iterator<Long> it = cc.iterator();
				boolean fstart = true;
				while(it.hasNext()) {
					if(fstart) {
						writer.print(it.next());
						fstart=false;
					}else {
						writer.print(" "+it.next());	
					}
					
					
				}
				writer.println();
			}
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
		}
        
    }

}