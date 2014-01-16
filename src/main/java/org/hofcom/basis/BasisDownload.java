package org.hofcom.basis;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Simple utility that downloads your basis data from the basis web app 
 * 
 * @author hof
 */
public class BasisDownload {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
            throws IOException, ExecutionException, InterruptedException {
        
        System.out.println("Basis Data Download 0.2");
        System.out.println("Copyright (C) 2014 Erik van het Hof (hof@hofcom.nl @erikvanhethof)\n");
        
        if (args.length != 3) { 
            System.out.println(("\nUsage: java -jar basis-download-0.2-jar-with-dependencies.jar <username> <password> <day>"));
            System.out.println("\nExample: java -jar basis-download-0.2-jar-with-dependencies.jar hof@hofcom.nl password 2014-01-14"); 
            return; 
        }

        BasisApi basisApi = new BasisApi(); 
        basisApi.authenticate(args[0], args[1]);
        basisApi.getMe(); 
        basisApi.getData(args[2], "0", "0");
        basisApi.getActivities(args[2]); 
        basisApi.getHttpClient().close(); 
        
    }    
}
