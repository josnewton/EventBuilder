/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import java.util.HashMap;

/**
 *
 * @author jnewt018
 */
public class PIDTestMaps {
    
                public static HashMap<Integer,PIDExamination> getElectronTests(DetectorParticle particle) {
                
                HashMap<Integer,PIDExamination>  electrontests = new HashMap<Integer, PIDExamination>();
               
                PIDExamination test1 = new PIDExamination();
                test1.setClosestBeta(true);
                test1.setHTCC(true);
                test1.setCorrectSF(true);
         
                
                PIDExamination test2 = new PIDExamination();
                test2.setClosestBeta(false);
                test2.setHTCC(true);
                test2.setCorrectSF(true);
                
                PIDExamination test3 = new PIDExamination();
                test3.setClosestBeta(true);
                test3.setHTCC(false);
                //test3.setLTCC(false);
                
                PIDExamination test4 = new PIDExamination();
                test4.setClosestBeta(true);
                test4.setHTCC(true);
                test4.setCorrectSF(false);
                test4.setHTCCThreshold(false);
                
                PIDExamination test5 = new PIDExamination();
                test5.setClosestBeta(false);
                test5.setHTCC(true);
                test5.setCorrectSF(false);
                test5.setHTCCThreshold(false);
                
                electrontests.put(0,test1);
                electrontests.put(1,test2);
                electrontests.put(2,test3);
                electrontests.put(3,test4);
                electrontests.put(4,test5);
   
                return electrontests;
            }
    
                public static HashMap<Integer,PIDExamination> getPionTests(DetectorParticle particle) {
                
                HashMap<Integer,PIDExamination>  piontests = new HashMap<Integer, PIDExamination>();
               
                PIDExamination test1 = new PIDExamination();
                test1.setClosestBeta(true);
                test1.setHTCC(false);
                test1.setCorrectSF(false);
              
                
                PIDExamination test2 = new PIDExamination();
                test2.setClosestBeta(true);
                test2.setHTCC(true);
                test2.setCorrectSF(false);
                test2.setHTCCThreshold(true);
                
                PIDExamination test3 = new PIDExamination();
                test3.setClosestBeta(false);
                test3.setHTCC(true);
                test3.setCorrectSF(false);
                test3.setHTCCThreshold(true);
                
//                PIDExamination test4 = new PIDExamination();
//                test4.setClosestBeta(true);
//                test4.setHTCC(false);
//                test4.setLTCC(true);
//                test4.setLTCCThreshold(true);
                
//                PIDExamination test5 = new PIDExamination();
//                test5.setClosestBeta(false);
//                test5.setHTCC(true);
//                test5.setLTCC(false);
//                test5.setLTCCThreshold(false);
                
                piontests.put(0,test1);
                piontests.put(1,test2);
                piontests.put(2,test3);
               // piontests.put(3,test4);
              //  piontests.put(4,test5);

                return piontests;
            }                

                
                public static HashMap<Integer,PIDExamination> getProtonTests(DetectorParticle particle) {
                
                HashMap<Integer,PIDExamination>  protontests = new HashMap<Integer, PIDExamination>();
               
                PIDExamination test1 = new PIDExamination();
           
                test1.setClosestBeta(true);
                test1.setHTCC(false);
               // test1.setLTCC(false);

                protontests.put(0, test1);

                return protontests;
            }
    
                public static HashMap<Integer,PIDExamination> getKaonTests(DetectorParticle particle) {
                
                HashMap<Integer,PIDExamination>  kaontests = new HashMap<Integer, PIDExamination>();
               
                PIDExamination test1 = new PIDExamination();
           
                test1.setClosestBeta(true);
                test1.setHTCC(false);
               // test1.setLTCC(false);

                kaontests.put(0, test1);

                return kaontests;
            }          
                
}
