package org.hofcom.basis;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author hof
 */
public class BasisDownload {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
            throws IOException, ExecutionException, InterruptedException {
        
        System.out.println("Basis Data Download 0.1.");
        System.out.println("Erik van het Hof (hof@hofcom.nl) Twitter: @erikvanhethof");
        
        if (args.length != 5) { 
            System.out.println(("\nUsage: BasisDownload <username> <password> <day> <start_offset> <end_offset>"));
            System.out.println("\nExample: BasisDownload hof@hofcom.nl password 2014-01-14 0 0"); 
            return; 
        }

        BasisApi basisApi = new BasisApi(); 
        basisApi.authenticate(args[0], args[1]);
        basisApi.getMe(); 
        basisApi.getData(args[2], args[3], args[4]);
        basisApi.getActivities(args[2]); 
        basisApi.getHttpClient().close(); 
        
    }    
}
