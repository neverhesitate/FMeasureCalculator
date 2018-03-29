/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fmeasurecalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * FMeasureCalculator
 * @author funlu
 */
public class FMeasureCalculator {
    private boolean fn4Each = true;
    private int fn4EachValue = 0;
    private final List<ClusterInstance> instances;

    public FMeasureCalculator(List<ClusterInstance> instances) {
        this.instances = instances;
    }

    /**
     * False Negative (FN) Assignment: when similar members are assigned to different communities. This is an incorrect decision
     * @return 
     */
    public int calculateFN(){
       this.fn4EachValue = 0;
       this.fn4Each = true; 
       for(ClusterInstance instance : instances){
            List<ClusterInstance> operationalClusters = new ArrayList<ClusterInstance>(); 
            for(ClusterInstance tobeChecked : instances){
                if(!instance.getClusterName().equals(tobeChecked.getClusterName())){
                    //for(ClusterMember member : instance.getMembers()){
                        if(tobeChecked.hasMember(instance.getClusterMember())){
                            //falseNegativeValue = falseNegativeValue + (tobeChecked.getMember(instance.getClusterMember()).getMemberCount() * instance.getClusterMember().getMemberCount()); 
                            operationalClusters.add(tobeChecked);
                        }  
                    //}
                }
            }
            calculateFN4EachCluster(instance,operationalClusters);
            this.fn4Each = true;   
        }  
                
        return this.fn4EachValue;
    }
    
    /**
     * calculateFN4EachCluster
     * @param instance
     * @param operationalClusters
     * @return 
     */
    private int calculateFN4EachCluster(ClusterInstance instance,List<ClusterInstance> operationalClusters){
        if(operationalClusters.size() >= 1){
            if(fn4Each){
                for(ClusterInstance tobeChecked : operationalClusters){
                  fn4EachValue = fn4EachValue + (tobeChecked.getMember(instance.getClusterMember()).getMemberCount() * instance.getClusterMember().getMemberCount()); 
                }
                fn4Each = false;
            }else{
                ClusterInstance temp = operationalClusters.get(0);
                for(ClusterInstance tobeChecked : operationalClusters){
                    if(!temp.getClusterName().equals(tobeChecked.getClusterName()))
                        fn4EachValue = fn4EachValue + (tobeChecked.getMember(instance.getClusterMember()).getMemberCount() * temp.getMember(instance.getClusterMember()).getMemberCount()); 
                }
                operationalClusters.remove(temp);
            }
            return calculateFN4EachCluster(instance,operationalClusters);
        }
        return fn4EachValue;
    }
    
    /**
     * False Positive (FP) Assignment: when dissimilar members are assigned to the same community. This is an incorrect decision
     * @return 
     */
    public int calculateFP(){
        int falsePositiveValue = 0;
        for(ClusterInstance instance : instances){
            //Check if cluster instance contains more than 1 member, otherwise not added to calculation
            if(instance.getMembers().size() > 1){           
                for (int i = 0; i < instance.getMembers().size(); i++) {
                    for (int j = i + 1; j < instance.getMembers().size(); j++) {
                        falsePositiveValue = falsePositiveValue + (instance.getMembers().get(i).getMemberCount() * instance.getMembers().get(j).getMemberCount());
                        boolean foundExtra = false;
                        for (int k = 0; k < instance.getMembers().size() && !foundExtra; k++){
                            if (k != j && k != i){
                                for (int l = 0; l < instance.getMembers().size(); l++){
                                    if (l != k && l != j && l != i){
                                        foundExtra = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }                           
            }
        }    
        return falsePositiveValue;
    }
    
    /**
     * calculateTPandFP
     * @return 
     */
    public int calculateTPandFP(){
        //total N(N−1)/2 for each class
        int toReturn = 0;
        for(ClusterInstance instance : instances){
           toReturn += (instance.getTotalInstances()*(instance.getTotalInstances()-1))/2;
        }
        return toReturn;
    }
    /**
     * calculateTotalPairs
     * @return 
     */
    public int calculateTotalPairs(){
        //N(N−1)/2
        int totalClusterMembers = 0;
        for(ClusterInstance instance : instances){
            totalClusterMembers += instance.getTotalInstances();
        }
        return (totalClusterMembers*(totalClusterMembers-1))/2;
    }
    /**
     * True Positive (TP) Assignment: when similar members are assigned to the same community. This is a correct decision.
     * easier version
     * @return 
     */
    public int calculateTPv1(){
        //TP=totalPositives - FP
        return calculateTPandFP() - calculateFP();
    }
    /**
     * True Negative (TN) Assignment: when dissimilar members are assigned to different communities. This is a correct decision.
     * @return 
     */
    public int calculateTNv1(){
        //totalNegatives=N−totalPositives
        //TN=totalNegatives-FN
        this.fn4EachValue = 0;
        this.fn4Each = true;
        int totalNegatives = calculateTotalPairs()- calculateTPandFP();
        return totalNegatives - calculateFN();
    }
    
    /**
     * calculateRecall
     * @return 
     */
    public double calculateRecall(){
        // R = TP/TP + FN
       return (double) calculateTPv1() / (double)(calculateTPv1() + calculateFN());
    }
    
    /**
     * calculatePrecision
     * @return 
     */
    public double calculatePrecision(){
        //P = TP /(TP + FP)
        return (double)calculateTPv1() / (double)(calculateTPv1() + calculateFP());
    }
    
    /**
     * calculateFMeasure
     * @return 
     */
    public double calculateFMeasure(){
        //F = 2*((P*R)/(P+R));
        return 2*((calculatePrecision()*calculateRecall())/(calculatePrecision()+calculateRecall()));
    }
}
