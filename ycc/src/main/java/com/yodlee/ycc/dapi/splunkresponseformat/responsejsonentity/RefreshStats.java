package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public  final class RefreshStats {
    public  String groupBy;
    public  String refreshType;
    public  String orderBy;
    public  List<Info> info;
  

    @JsonCreator
    public RefreshStats(@JsonProperty("groupBy") String groupBy, @JsonProperty("refreshType") String refreshType, @JsonProperty("orderBy") String orderBy, @JsonProperty("info") List<Info> info){
        this.groupBy = groupBy;
        this.refreshType = refreshType;
        this.orderBy = orderBy;
        this.info = info;
    }
}