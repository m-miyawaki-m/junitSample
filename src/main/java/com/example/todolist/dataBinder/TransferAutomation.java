package com.example.todolist.dataBinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import oracle.stellent.ridc.*;
import oracle.stellent.ridc.common.log.*;
import oracle.stellent.ridc.model.*;
import oracle.stellent.ridc.model.serialize.*;
import oracle.stellent.ridc.protocol.*;
import oracle.stellent.ridc.protocol.intradoc.*;

/*
 * @author Sterin- Oracle Inc
 * 
 * This is a class used to test the basic functionality
 * of submitting a search to Content Server using RIDC.  
 * The response is then used to retrieve metadata about
 * the content items.  
 */

public class TransferAutomation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IdcClientManager manager = new IdcClientManager ();
      	Properties prop = new Properties();
        InputStream input = null;

                    
		try{
			input = new FileInputStream("config.properties");
			prop.load(input);
			
			// Create a new IdcClient Connection using idc protocol (i.e. socket connection to Content Server)
			IdcClient idcClient = manager.createClient (prop.getProperty("url"));
			IdcContext userContext = new IdcContext (prop.getProperty("user"),prop.getProperty("password"));
			
			// Create an HdaBinderSerializer; this is not necessary, but it allows us to serialize the request and response data binders
			HdaBinderSerializer serializer = new HdaBinderSerializer ("UTF-8", idcClient.getDataFactory ());
			
			// Create a new binder for submitting a search
			DataBinder dataBinder = idcClient.createBinder();
			   
	        dataBinder.putLocal("IdcService", "TRANSFER_ARCHIVE");
			dataBinder.putLocal("IDC_Name",prop.getProperty("IDC_Name"));
			dataBinder.putLocal("aArchiveName",prop.getProperty("aArchiveName"));
			dataBinder.putLocal("aTargetArchive",prop.getProperty("aTargetArchive"));
			   
			// Write the data binder for the request to stdout
		    serializer.serializeBinder (System.out, dataBinder);
			   
		    // Send the request to Content Server
			ServiceResponse response = idcClient.sendRequest(userContext,dataBinder);
			
			// Get the data binder for the response from Content Server
			DataBinder responseData = response.getResponseAsBinder();
			// Write the response data binder to stdout
			serializer.serializeBinder (System.out, responseData);
			// Retrieve the SearchResults ResultSet from the response
           			
		} catch (IdcClientException ice){
			ice.printStackTrace();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public String createDatabinder(IdcClient idcClient, Properties prop) {
		DataBinder dataBinder = idcClient.createBinder();
		   
        dataBinder.putLocal("IdcService", "TRANSFER_ARCHIVE");
		dataBinder.putLocal("IDC_Name",prop.getProperty("IDC_Name"));
		dataBinder.putLocal("aArchiveName",prop.getProperty("aArchiveName"));
		dataBinder.putLocal("aTargetArchive",prop.getProperty("aTargetArchive"));
		
		Optional<String> opt = Optional.empty();
		if (dataBinder.getLocal("IdcService") != null) {
			opt = Optional.of(dataBinder.getLocal("IdcService"));
		}
		
		if(opt.isPresent()) {
			dataBinder.getLocal("IDC_Name");
		}
		
		return opt.get();
	}

}