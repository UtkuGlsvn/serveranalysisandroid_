package com.webischia.serveranalysis.Service;

//Server ile android arasındaki querylerin çağırılacağı metotların belirtilidiği yer
public interface QueryService {

    void doQuery(String query,String token);

}
