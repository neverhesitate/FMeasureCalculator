/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fmeasurecalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * ClusterInstance
 * @author funlu
 */
public class ClusterInstance {
    public String clusterName;
    public int totalInstances;
    public List<ClusterMember> members = new ArrayList<ClusterMember>();

    public ClusterInstance(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getTotalInstances() {
        return totalInstances;
    }

    public void setTotalInstances(int totalInstances) {
        this.totalInstances = totalInstances;
    }

    public List<ClusterMember> getMembers() {
        return members;
    }

    public void setMembers(List<ClusterMember> members) {
        this.members = members;
    }
    
    public void addMember(ClusterMember member){
        this.members.add(member);
    }
  
    public boolean hasMember(ClusterMember memberToBeChecked){
        boolean toReturn = false;
        for(ClusterMember member: this.members){
            if(member.getMemberName().equals(memberToBeChecked.getMemberName()))
                toReturn = true;
        }
        return toReturn;
    }
    
    public ClusterMember getMember(ClusterMember memberToBeChecked){
        for(ClusterMember member: this.members){
            if(member.getMemberName().equals(memberToBeChecked.getMemberName()))
                return member;
        }
        return null;
    }
    
    public ClusterMember getClusterMember(){
        for(ClusterMember member: this.members){
            if(member.getMemberName().equals(this.getClusterName()))
                return member;
        }
        return null;
    }
  
}
