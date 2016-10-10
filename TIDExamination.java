package org.jlab.clas.ebuilder;




/**
 *
 * @author Joseph Newton
 */
public class TIDExamination {
    
 

    private Integer CandidateID = -1;
    

    private Boolean hasHTCC = false;
    private Boolean hasLTCC = false;
    private Boolean hasFTOF = false;
    private Boolean correctSF = false;

    public TIDExamination(){
        
    }
    

    public void   setCandidateID(int x){this.CandidateID = x;}
    public void   setHTCC(Boolean htcc){this.hasHTCC = htcc;}
    public void   setLTCC(Boolean ltcc){this.hasLTCC = ltcc;}
    public void   setFTOF(Boolean ftof){this.hasFTOF = ftof;}
    public void   setCorrectSF(Boolean sf){this.correctSF = sf;}

    public Integer getTriggerScore(){
        Integer score = 0;
        if(this.hasHTCC==true){
            score = score + 1000;
        }
        if(this.correctSF==true){
            score = score + 100;
        }
        if(this.hasFTOF==true){
            score = score + 10;
        }
        if(this.hasLTCC==true){
            System.out.println("WTF");
            score = score + 1;
        }
        return score;
    }
       

    public Integer getCandidateID(){return this.CandidateID;}
    public Boolean getHTCC(){ return this.hasHTCC; }
    public Boolean getLTCC(){ return this.hasLTCC;}
    public Boolean getFTOF(){return this.hasFTOF;}
    public Boolean getCorrectSF() {return this.correctSF;}



}