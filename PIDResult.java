package org.jlab.service.eb;




/**
 *
 * @author jnewton
 */
public class PIDResult {
    
   private int finalid=-1;
   private PIDExamination pidexam = new PIDExamination();


    
    public PIDResult(){
        
    }
    
    public void   setFinalID(int id){ this.finalid = id;}
    public void   setPIDExamination(PIDExamination exam){this.pidexam = exam;}

    public int getFinalID(){ return this.finalid;}
    public PIDExamination getPIDExamination(){return this.pidexam;}


}
