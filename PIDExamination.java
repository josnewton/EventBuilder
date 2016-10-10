package org.jlab.clas.ebuilder;




/**
 *
 * @author Joseph Newton
 */
public class PIDExamination {
    
 

    private Integer CandidateID = -1;
    
    private Boolean closestBeta = null;
    private Boolean hasHTCC = null;
    private Boolean hasLTCC = null;
    private Boolean hasFTOF = null;
    private Boolean hasCND = null;
    private Boolean correctSF = null;
    private Boolean HTCCThreshold = null;
    private Boolean LTCCThreshold = null;

    public PIDExamination(){
        
    }
    

    public void   setCandidateID(int x){this.CandidateID = x;}
    public void   setClosestBeta(Boolean beta){this.closestBeta = beta;}
    public void   setHTCC(Boolean htcc){this.hasHTCC = htcc;}
    public void   setLTCC(Boolean ltcc){this.hasLTCC = ltcc;}
    public void   setFTOF(Boolean ftof){this.hasFTOF = ftof;}
    public void   setCorrectSF(Boolean sf){this.correctSF = sf;}
    public void   setHTCCThreshold(Boolean htcc){this.HTCCThreshold = htcc;}
    public void   setLTCCThreshold(Boolean ltcc){this.LTCCThreshold = ltcc;}
    
    public Boolean compareExams(PIDExamination mapexam){
        Boolean truth = true;
        if(mapexam.closestBeta!=null){
            if(this.closestBeta!=mapexam.closestBeta){
                truth = false;
            }
        }
        
        if(mapexam.hasHTCC!=null){
            if(this.hasHTCC!=mapexam.hasHTCC){
                truth = false;
            }
        }
        
        if(mapexam.HTCCThreshold!=null){
            if(mapexam.HTCCThreshold!=this.HTCCThreshold){
                truth = false;
            }
        }
        
        if(mapexam.hasLTCC!=null){
            if(this.hasLTCC!=mapexam.hasLTCC){
                truth = false;
            }
        }
        
        if(mapexam.LTCCThreshold!=null){
            if(this.LTCCThreshold!=mapexam.LTCCThreshold){
                truth = false;
            }
        }
        
        if(mapexam.correctSF!=null){
            if(this.correctSF!=mapexam.correctSF){
                truth = false;
            }
        }

        return truth;
    }


    public Integer getCandidateID(){return this.CandidateID;}
    public Boolean getClosestBeta(){return this.closestBeta;}
    public Boolean getHTCC(){ return this.hasHTCC; }
    public Boolean getLTCC(){ return this.hasLTCC;}
    public Boolean getFTOF(){return this.hasFTOF;}
    public Boolean getCorrectSF() {return this.correctSF;}
    public Boolean getHTCCThreshold() {return this.HTCCThreshold;}
    public Boolean getLTCCThreshold() {return this.LTCCThreshold;}



}